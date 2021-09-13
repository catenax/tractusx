#
# Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
#
# See the AUTHORS file(s) distributed with this work for additional
# information regarding authorship.
#
# See the LICENSE file(s) distributed with this work for
# additional information regarding license terms.
#

# Example script to deploy a provider/consumer connector pair in kubernetes
# You need to define some environment variables
# source ../infrastructure/manifests/environment.sh
# source ../infrastructure/pipelines/secrets.sh

docker build --build-arg MAVEN_OPTS="-Dhttp.proxyHost=${HTTP_PROXY_HOST} -Dhttp.proxyPort=${HTTP_PROXY_PORT} -Dhttps.proxyHost=${HTTP_PROXY_HOST} -Dhttps.proxyPort=${HTTP_PROXY_PORT}" -t $CONTAINER_REGISTRY/dataspace-connector:$VERSION -f Dockerfile DataspaceConnector
docker push $CONTAINER_REGISTRY/dataspace-connector:$VERSION

cd ..

kubectl delete namespace dataspace-connector

az postgres db delete -g ${K8_RESOURCE_GROUP} -s ${POSTGRES_RESOURCE_NAME} -n connectorprovider
az postgres db delete -g ${K8_RESOURCE_GROUP} -s ${POSTGRES_RESOURCE_NAME} -n connectorconsumer

az postgres db create -g ${K8_RESOURCE_GROUP} -s ${POSTGRES_RESOURCE_NAME} -n connectorprovider
az postgres db create -g ${K8_RESOURCE_GROUP} -s ${POSTGRES_RESOURCE_NAME} -n connectorconsumer

kubectl create namespace dataspace-connector

kubectl create secret generic connector-config -n dataspace-connector --from-file=configprovider.json=./src/main/resources/conf/provider_config.json --from-file=configconsumer.json=./src/main/resources/conf/consumer_config.json --from-file=keystore-localhost.p12=DataspaceConnector/src/main/resources/conf/keystore-localhost.p12 --from-file=truststore.p12=DataspaceConnector/src/main/resources/conf/truststore.p12 --dry-run=client -o yaml \
    | kubectl apply -f -

ROLE=provider bash -c 'cat deployment.yaml | envsubst | kubectl apply -f -'
ROLE=consumer bash -c 'cat deployment.yaml | envsubst | kubectl apply -f -'

ROLE=provider bash -c 'cat deployment-ingress.yaml | envsubst | kubectl apply -f -'
ROLE=consumer bash -c 'cat deployment-ingress.yaml | envsubst | kubectl apply -f -'