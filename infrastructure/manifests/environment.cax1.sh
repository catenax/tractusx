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
export CLUSTER_ISSUER=letsencrypt-dev
export CATENA_SERVICE_URL=catencax1devakssrv.germanywestcentral.cloudapp.azure.com
export PORTAL_DOMAIN=catenacax1devaksportalsrv
export PORTAL_IP=20.79.77.83
export CONTAINER_REGISTRY_SHORT=catenacax1devacr
export CONTAINER_REGISTRY=${CONTAINER_REGISTRY_SHORT}.azurecr.io
export STORAGE_ACCOUNT_NAME=catenacax1storage
export POSTGRES_RESOURCE_NAME=catencax1devdb
export VERSION=latest
export IMAGE_PULL_POLICY=Always
export CATENA_ADMIN_MAIL=admin@example.com
export K8_RESOURCE_GROUP=catenacax1-dev-rg
export NODE_RESOURCE_GROUP=catenacax1-dev-node-rg
export K8_RESOURCE_NAME=catenacax1-dev-aks-services
