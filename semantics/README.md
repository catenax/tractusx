<!---
Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)

See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
-->

The semantic layer is an architectural component of Catena-X. 

### Configuration:

All configuration placed in `../infrastructure/manifests/semantics.yaml`

### Build Package:

Run `mvn install` to run unit tests, build and install the package (and submodules)

### Build & Run Package Locally:

Run `./run_local.sh` to freshly build the package and run semantic services.

### Build Docker:

Run `docker build -f ./services/Dockerfile -t $REGISTRY/semantics/services:$VERSION .`
RUN `docker push $REGISTRY/semantics/services:$VERSION`

Run `docker build -f ./dapter/Dockerfile -t $REGISTRY/semantics/adapter:$VERSION .`
RUN `docker push $REGISTRY/semantics/adapter:$VERSION`

where $REGISTRY is set to the target container/docker repository (like `tsicatenaxdevacr.azurecr.io`) and $VERSION is set to the 
deployment version (usually `latest`).

### Redeploy in target environment

Run `kubectl rollout restart deployment services -n semantics`
Run `kubectl rollout restart deployment adapter -n semantics`