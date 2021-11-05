#!/bin/bash
set -euo pipefail
export DOCKER_BUILDKIT=1
docker-compose build --build-arg PRS_EDC_PKG_USERNAME=$PRS_EDC_PKG_USERNAME --build-arg PRS_EDC_PKG_PASSWORD=$PRS_EDC_PKG_PASSWORD
docker-compose up --exit-code-from=integration-test --abort-on-container-exit
