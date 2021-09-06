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
export CLUSTER_ISSUER=letsencrypt-prod
export CATENA_SERVICE_URL=catenaxdevakssrv.germanywestcentral.cloudapp.azure.com
export PORTAL_DOMAIN=catenaxdevaksportalsrv
export PORTAL_IP=20.79.77.83
export CONTAINER_REGISTRY=catenaxdevacr.azurecr.io
export VERSION=latest
export IMAGE_PULL_POLICY=Always
export CATENA_ADMIN_MAIL=admin@example.com
export NODE_RESOURCE_GROUP=catenax-dev-node-rg
