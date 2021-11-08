#!/bin/bash

set -euxo pipefail

REGISTRY=$1
IMAGE=$2
TAG=$3

export DOCKER_BUILDKIT=1

docker build --build-arg BUILD_TARGET=$IMAGE --target $IMAGE -t $REGISTRY/$IMAGE:$TAG .
if ! docker push $REGISTRY/$IMAGE:$TAG; then
  az acr login -n $REGISTRY
  docker push $REGISTRY/$IMAGE:$TAG
fi
