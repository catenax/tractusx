#!/bin/bash
set -euox pipefail
TARGET=$1
PARTITION=$2
export DOCKER_BUILDKIT=1

# set default values, if not provided
REGISTRY=${REGISTRY:-catenaxdev001acr.azurecr.io}
TAG=${TAG:-dev}

az acr login -n $REGISTRY

IMAGE=prs-connector-$TARGET
echo $IMAGE
docker build ../ --target $TARGET -t $REGISTRY/$IMAGE:$TAG --build-arg BUILD_TARGET=$IMAGE --build-arg PRS_EDC_PKG_USERNAME=$PRS_EDC_PKG_USERNAME --build-arg PRS_EDC_PKG_PASSWORD=$PRS_EDC_PKG_PASSWORD --target $IMAGE
docker push $REGISTRY/$IMAGE:$TAG

helm delete prs-${TARGET} || true
helm upgrade --install --wait prs-${TARGET} ../cd/helm/prs-${TARGET} \
  --set image.repository=$REGISTRY/$IMAGE \
  --set image.tag=$TAG