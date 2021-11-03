#!/bin/bash
set -euo pipefail

export DOCKER_BUILDKIT=1

# set default values, if not provided
REGISTRY=${REGISTRY:-catenaxdev001acr.azurecr.io}
TAG=${TAG:-dev}

az acr login -n $REGISTRY

for TARGET in provider consumer; do
  IMAGE=prs-connector-$TARGET
  docker build --target $TARGET -t $REGISTRY/$IMAGE:$TAG .
  docker push $REGISTRY/$IMAGE:$TAG
done

helm delete prs-connector || true
helm upgrade --install --wait prs-connector helm/prs-connector \
  --set provider.image.repository=$REGISTRY/prs-connector-provider \
  --set provider.image.tag=$TAG \
  --set consumer.image.repository=$REGISTRY/prs-connector-consumer \
  --set consumer.image.tag=$TAG \
