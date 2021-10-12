#
# Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
#
# See the AUTHORS file(s) distributed with this work for additional
# information regarding authorship.
#
# See the LICENSE file(s) distributed with this work for
# additional information regarding license terms.
#

#
# Shell script to perform a manual deployment once terraform has done its work
# Should only be run in "homoeopathic" doses
#
# Secrets and environmental informations need to be set via environment variables
##Docker Registry
#export CONTAINER_REGISTRY=catenaxdevacr.azurecr.io
##Image Versions
#export VERSION=latest
##User for service/servers
#export HTTPUSER=
##Password for service/servers
#export HTTPPASSWORD=    
##External Volumes/Storage Connection
#export STORAGEACCOUNT_CONNECTIONSTRING=
##External Database Connection User
#export POSTGREPARTSMASTERPASSWORD=
##External Database Connection Password
#export POSTGREPARTSMASTERURL=
##In case you are living behind a proxy
#export HTTP_PROXY_HOST=yourproxy.corporate.net
#export HTTP_PROXY_PORT=8080
#export PROXY=http://yourproxy.corporate.net:8080
##domain name of the final frontend
#export PORTAL=tsicatenaxdevaksportalsrv
## Application ID as registered in AAD
#export APIID=
#export CLUSTER_ISSUER=letsencrypt-prod
#export CATENA_SERVICE_URL=catenaxdevakssrv.germanywestcentral.cloudapp.azure.com
#export IMAGE_PULL_POLICY=Always

cd ../../coreservices/connectordns
mvn install -DskipTests
docker build -t $CONTAINER_REGISTRY/cdns:$VERSION .
docker push $CONTAINER_REGISTRY/cdns:$VERSION

cd ../../infrastructure/pipelines

kubectl create namespace cdns

kubectl create secret generic cdns-secret -n cdns --from-literal=http_basic_auth_password=${HTTPPASSWORD} --from-literal=storage_connectionstring=${STORAGEACCOUNT_CONNECTIONSTRING} --dry-run=client -o yaml \
    | kubectl apply -f -

cat ../manifests/cdns.yaml | envsubst | kubectl apply -n cdns

cat ../manifests/cdns-ingress.yaml | envsubst | kubectl apply -n cdns

cd ../../coreservices/businesspartners

mvn package -DskipTests
docker build -t $CONTAINER_REGISTRY/businesspartners:$VERSION .
docker push $CONTAINER_REGISTRY/businesspartners:$VERSION

cd ../../infrastructure/pipelines

kubectl create namespace businesspartners

kubectl create secret generic businesspartners-secret -n businesspartners --from-literal=http_basic_auth_password=${HTTPPASSWORD} --from-literal=storage_connectionstring=${STORAGEACCOUNT_CONNECTIONSTRING} --dry-run=client -o yaml \
    | kubectl apply -f -

cat ../manifests/businesspartners.yaml | envsubst | kubectl apply -n businesspartners

cat ../manifests/businesspartners-ingress.yaml | envsubst | kubectl apply -n businesspartners

cd ../../coreservices/partsmasterdata
mvn package -DskipTests
docker build -t $CONTAINER_REGISTRY/partsmasterdata:$VERSION .
docker push $CONTAINER_REGISTRY/partsmasterdata:$VERSION


cd ../../infrastructure/pipelines

kubectl create namespace partsmasterdata

kubectl create secret generic partsmasterdata-secret -n partsmasterdata --from-literal=http_basic_auth_password=${HTTPPASSWORD} --from-literal=postgres_partsmaster_url=${POSTGREPARTSMASTERURL} --from-literal=postgre_partsmaster_user=${HTTPUSER}  --from-literal=postgre_partsmaster_password=${POSTGREPARTSMASTERPASSWORD} --from-literal=postgre_partsmaster_db=partsmaster  --dry-run=client -o yaml \
    | kubectl apply -f -

cat ../manifests/partsmasterdata.yaml | envsubst | kubectl apply -n partsmasterdata

cat ../manifests/partsmasterdata-ingress.yaml | envsubst | kubectl apply -n partsmasterdata

cd ../../coreservices/kmuuploadapp
mvn package -DskipTests
docker build -t $CONTAINER_REGISTRY/uploadappadapter:$VERSION .
docker push $CONTAINER_REGISTRY/uploadappadapter:$VERSION

cd ../../infrastructure/pipelines

kubectl create secret generic uploadappadapter-secret -n partsmasterdata --from-literal=storage_connectionstring=${STORAGEACCOUNT_CONNECTIONSTRING} --from-literal=postgre_upload_user=${HTTPUSER} --from-literal=http_basic_auth_password=${HTTPPASSWORD}  --from-literal=postgres_upload_url=${POSTGREPARTSMASTERURL} --from-literal=postgre_upload_password=${POSTGREPARTSMASTERPASSWORD} --from-literal=postgre_upload_db=upload --from-literal=aad_api_id_uri=${APIID} --from-literal=aad_client_id=${APIID}  --dry-run=client -o yaml \
    | kubectl apply -f -

cat ../manifests/uploadapp.yaml | envsubst | kubectl apply -n partsmasterdata

cat ../manifests/uploadapp-ingress.yaml | envsubst | kubectl apply -n partsmasterdata

cd ../../coreservices/simplescheduler
mvn package -DskipTests
docker build -t $CONTAINER_REGISTRY/simplescheduler:$VERSION .
docker push $CONTAINER_REGISTRY/simplescheduler:$VERSION

cd ../../infrastructure/pipelines

kubectl create namespace centralconnector

kubectl create secret generic simplescheduler-secret -n centralconnector --from-literal=connector_login=${HTTPUSER} --from-literal=connector_password=${HTTPPASSWORD}  --dry-run=client -o yaml \
    | kubectl apply -f -

cat ../manifests/simplescheduler.yaml |envsubst | kubectl apply -n centralconnector

#####################################
# Containerize and Deploy portal
#####################################

cd ../../portal/code/tractus-x-portal

# choose the right environment
cp dev042.env .env

docker build -f Dockerfile.develop --build-arg HTTP_PROXY=$HTTP_PROXY --build-arg HTTPS_PROXY=$HTTPS_PROXY -t $CONTAINER_REGISTRY/frontend/portal${WORKSPACE}:$VERSION .

docker push $CONTAINER_REGISTRY/frontend/portal${WORKSPACE}:$VERSION

cd ../../../infrastructure/pipelines

#kubectl delete namespace portal
#kubectl create namespace portal

cat ../manifests/portal.yaml | envsubst | kubectl apply -n portal -f -

kubectl rollout restart deployment portal -n portal

#####################################
# Containerize and Deploy Semantics
#####################################

cd ../../semantics

docker build --build-arg MAVEN_OPTS="-Dhttp.proxyHost=${HTTP_PROXY_HOST} -Dhttp.proxyPort=${HTTP_PROXY_PORT} -Dhttps.proxyHost=${HTTP_PROXY_HOST} -Dhttps.proxyPort=${HTTP_PROXY_PORT}" -t $CONTAINER_REGISTRY/semantics/services${WORKSPACE}:$VERSION .
docker push $CONTAINER_REGISTRY/semantics/services${WORKSPACE}:$VERSION

cd ../infrastructure/pipelines

#kubectl delete namespace semantics
#kubectl create namespace semantics

kubectl create secret generic semantics-secret -n semantics --from-literal=database_url=jdbc:postgresql://catenax${WORKSPACE}database.postgres.database.azure.com:5432/semantics?sslmode=require \
   --from-literal=database_user=${CATENAX_ADMIN_USER}@catenax${WORKSPACE}database \
   --from-literal=database_password=${CATENAX_ADMIN_PASSWORD} \
   --from-literal=connector_user=${CATENAX_ADMIN_USER} \
   --from-literal=connector_password=${CATENAX_ADMIN_PASSWORD} \
   --from-literal=http_basic_auth_user=${CATENAX_USER} \
   --from-literal=http_basic_auth_password=${CATENAX_PASSWORD} --dry-run=client -o yaml \
   | kubectl apply -f -

cat ../manifests/semantics.yaml | envsubst | kubectl apply -n portal -f -

kubectl rollout restart deployment semantics -n semantics

