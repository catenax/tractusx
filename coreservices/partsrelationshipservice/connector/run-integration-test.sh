#!/bin/bash
set -euo pipefail

if ! [ -f /tmp/cert.pfx ]; then
  echo "ERROR: Missing file /tmp/cert.pfx (see README.md)"
  exit 1
fi

cd ..
export DOCKER_BUILDKIT=1
docker-compose --profile connector build --build-arg PRS_EDC_PKG_USERNAME=$PRS_EDC_PKG_USERNAME --build-arg PRS_EDC_PKG_PASSWORD=$PRS_EDC_PKG_PASSWORD
docker-compose --profile connector --profile prs up --exit-code-from=connector-integration-test --abort-on-container-exit
