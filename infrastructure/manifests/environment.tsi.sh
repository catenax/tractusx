#
# Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
#
# See the AUTHORS file(s) distributed with this work for additional
# information regarding authorship.
#
# See the LICENSE file(s) distributed with this work for
# additional information regarding license terms.
#

# Script to set a k8/manifest environment
# productive issuer, set to -staging if authority needs not to be the productive one
export CLUSTER_ISSUER=-staging
export SERVICE_DOMAIN=tsicatenaxdevakssrv
export CATENA_SERVICE_URL=${SERVICE_DOMAIN}.germanywestcentral.cloudapp.azure.com
export PORTAL_DOMAIN=tsicatenaxdevaksportalsrv
export CATENA_PORTAL_URL=${PORTAL_DOMAIN}.germanywestcentral.cloudapp.azure.com
export PORTAL_IP=20.79.116.193
export CONTAINER_REGISTRY_SHORT=tsicatenaxdevacr
export CONTAINER_REGISTRY=${CONTAINER_REGISTRY_SHORT}.azurecr.io
export STORAGE_ACCOUNT_NAME=tsicatenaxstorage
export POSTGRES_RESOURCE_NAME=tsicatenaxdevdb
export VERSION=latest
export IMAGE_PULL_POLICY=Always
export CATENA_ADMIN_MAIL=c-jung@t-systems.com
export NODE_RESOURCE_GROUP=tsicatenax-dev-node-rg
export K8_RESOURCE_GROUP=tsicatenax-dev-rg
export K8_RESOURCE_NAME=tsicatenax-dev-aks-services

