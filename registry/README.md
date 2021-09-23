<!---
Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)

See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
-->

The registry is an architectural component of Catena-X. 

### Configuration:

All configuration placed in `infrastructure/manifests/semanticlayer.yaml`

### Build Package:

Run `mvn install` to run unit tests, build and install the package.

### Build & Run Package Locally:

Run `./run_local.sh` to freshly build the package and run a local registry server.

### Build Docker:

Run `mvn package -DskipTests`
Run `docker build -t $REGISTRY/registry:$VERSION .`
RUN `docker push $REGISTRY/registry:$VERSION`

where $REGISTRY is set to the target container/docker repository (like `tsicatenaxdevacr.azurecr.io`) and $VERSION is set to the 
deployment version (usually `latest`).

### Redeploy in target environment

Run `kubectl rollout restart deployment registry -n registry`