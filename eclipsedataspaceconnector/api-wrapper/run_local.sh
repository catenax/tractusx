#
# Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
#
# See the AUTHORS file(s) distributed with this work for additional
# information regarding authorship.
#
# See the LICENSE file(s) distributed with this work for
# additional information regarding license terms.
#

#
# Shell script to build and run Catena-X@Home for testing purposes.
#
# Prerequisites:
#   Windows, (git)-bash shell, java 11 (java) and maven (mvn) in the $PATH.
#
# Synposis:
#   ./run_local.sh
#
# Comments:
#

export DOCKER_REGISTRY="ghcr.io/catenax"
docker login $DOCKER_REGISTRY
docker pull $DOCKER_REGISTRY/edc/consumer-control-plane:catenax-at-home-latest
docker pull $DOCKER_REGISTRY/edc/consumer-data-plane:catenax-at-home-latest
docker pull $DOCKER_REGISTRY/edc/consumer-api-wrapper:catenax-at-home-latest
docker pull $DOCKER_REGISTRY/catenax/edc/provider-control-plane:catenax-at-home-latest
docker pull $DOCKER_REGISTRY/edc/provider-data-plane:catenax-at-home-latest
docker pull $DOCKER_REGISTRY/edc/provider-api-wrapper:catenax-at-home-latest
docker pull $DOCKER_REGISTRY/backend/simple-aas-adapter:catenax-at-home-latest
docker pull $DOCKER_REGISTRY/semantics/hub:catenax-at-home-latest
docker pull $DOCKER_REGISTRY/semantics/registry:catenax-at-home-latest
docker pull $DOCKER_REGISTRY/edc/consumer-aas-proxy:catenax-at-home-latest
docker-compose up -d
