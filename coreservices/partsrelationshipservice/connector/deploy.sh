#!/bin/bash
set -euo pipefail

export DOCKER_BUILDKIT=1

# set default values, if not provided
REGISTRY=${REGISTRY:-catenaxdev001acr.azurecr.io}
TAG=${TAG:-dev}

az acr login -n $REGISTRY

for TARGET in provider consumer; do
  IMAGE=prs-connector-$TARGET
  docker build ../ --target $TARGET -t $REGISTRY/$IMAGE:$TAG --build-arg BUILD_TARGET=$IMAGE --build-arg PRS_EDC_PKG_USERNAME=$PRS_EDC_PKG_USERNAME --build-arg PRS_EDC_PKG_PASSWORD=$PRS_EDC_PKG_PASSWORD --target $IMAGE
  docker push $REGISTRY/$IMAGE:$TAG
done

helm delete prs-connector || true
helm upgrade --install --wait prs-connector ../cd/helm/prs-connector \
  --set provider.image.repository=$REGISTRY/prs-connector-provider \
  --set provider.image.tag=$TAG \
  --set consumer.image.repository=$REGISTRY/prs-connector-consumer \
  --set consumer.image.tag=$TAG \
