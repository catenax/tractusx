#
# Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
#
# See the AUTHORS file(s) distributed with this work for additional
# information regarding authorship.
#
# See the LICENSE file(s) distributed with this work for
# additional information regarding license terms.
#

# Script to set a k8/manifest environment (here: PoC/Speedboat)
# This version contains no secrets (see the commented enties below) which need to be
# added best throuhgh a seperate secrets.sh (ignored in git)

# certificate stuff, here: productive issuer, set to -staging if authority needs not to be strong
export CATENA_ADMIN_MAIL=admin@example.com
# container stuff
export CONTAINER_REGISTRY_SHORT=catenaxdev042acr
export CONTAINER_REGISTRY=${CONTAINER_REGISTRY_SHORT}.azurecr.io
export VERSION=latest
export IMAGE_PULL_POLICY=Always
export NODE_RESOURCE_GROUP=catenax-dev042-node-rg
export K8_RESOURCE_GROUP=catenax-dev042-rg
export K8_RESOURCE_NAME=catenax-dev042-aks-services
# Persistence Layer
export STORAGE_ACCOUNT_NAME=catenaxdev042storage
export POSTGRES_RESOURCE_NAME=catenaxdev042database
# Service Layer
export SERVICE_DOMAIN=catenaxdev042akssrv
export CATENA_SERVICE_URL=${SERVICE_DOMAIN}.germanywestcentral.cloudapp.azure.com
# Portal Layer
export PORTAL_DOMAIN=catenaxdev042aksportal
export CATENA_PORTAL_URL=${PORTAL_DOMAIN}.germanywestcentral.cloudapp.azure.com
export PORTAL_IP=20.79.138.175

###
### Secrets (define in secrets.sh)
###

# build environment
#export HTTP_PROXY_HOST=
#export HTTP_PROXY_PORT=
#export PROXY=http://${HTTP_PROXY_HOST}:${HTTP_PROXY:PORT}

# certificate secrets
#export KEYSTORE_PASSWORD=

# SSO secrets
#export APIID=

# persistence secrets
#export STORAGEACCOUNT_CONNECTIONSTRING=
#export POSTGREPARTSMASTERPASSWORD=
#export POSTGREPARTSMASTERURL="jdbc:postgresql://${DATABASE_RESOURCE_NAME}.postgres.database.azure.com:5432/partsmasterdata?user=${HTTPUSER}@${DATABASE_RESOURCE_NAME}&password=${POSTGREPARTSMASTERPASSWORD}&sslmode=require"

# service & portal layer secrets 
#export HTTPUSER=
#export HTTPPASSWORD=