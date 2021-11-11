#!/bin/bash

set -euo pipefail
set -x

curl -O https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh
chmod +x wait-for-it.sh
curl -O https://raw.githubusercontent.com/kadwanev/retry/master/retry
chmod +x retry
./wait-for-it.sh -t 60 provider1:8181
./wait-for-it.sh -t 60 provider2:8181
./wait-for-it.sh -t 60 provider3:8181
./wait-for-it.sh -t 60 provider4:8181
./wait-for-it.sh -t 60 consumer:9191
mkdir -p /tmp/copy/source /tmp/copy/dest
cp /ci/test-document-*.json /tmp/copy/source/
requestId=$(curl -f -X POST 'http://consumer:9191/api/file/test-document-1?connectorAddress=http://provider1:8181/&destination=/tmp/copy/dest')
./retry -s 5 -t 120 "ls /tmp/copy/dest/ && test \$(curl -f http://consumer:9191/api/job/$requestId/state) == COMPLETED"
echo
cat /tmp/copy/dest/test-document-*
