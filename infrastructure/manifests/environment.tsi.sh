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
export CLUSTER_ISSUER=letsencrypt-staging
export CATENA_SERVICE_URL=tsicatenaxdevakssrv.germanywestcentral.cloudapp.azure.com
export PORTAL_DOMAIN=tsicatenaxdevaksportalsrv
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

