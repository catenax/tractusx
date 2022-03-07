<!---
Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)

See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
-->

The Digital Twin & Semantic Layer is a logical and architectural component of Catena-X.
The source code under this folder contains reference implementations of relevant semantic services:
- Digital Twin Registry and
- Semantic Hub

### Build Packages:
The project requires on private package from https://maven.pkg.github.com/eclipse-dataspaceconnector/DataSpaceConnector.
Add the following configuration to your `.m2/settings.xml`:

```
    <server>
        <id>edc-github</id>
        <username>oauth2</username>
        <password>$ADD_GITHUB_ACCESS_TOKEN_HERE</password>
    </server>
```

You need to add your own GitHub Access Token. Navigate to https://github.com/settings/tokens and create a new token
with the permission `read:packages`. 

Run `mvn install` to run unit tests, build and install the package (and submodules)

### Build & Run Package Locally:

Run `./run_local.sh` to run the semantic services.

The run script accepts the following command line options:
- '-clean' runs a cleanup when in build mode
- '-build' runs submodule maven before start
- '-suspend' suspends the java machine upon startup when in debugging mode.
- '-debug' allows the java machine to be debugged (debugging port is outputed on console)
- '-proxy' uses environment variables $HTTP_PROXY_HOST and $HTTP_PROXY_PORT to set the standard Java proxy for both http and https transports.

### Build Docker:

Run `docker build -f ./Dockerfile -t $REGISTRY/semantics/services:$VERSION .`
RUN `docker push $REGISTRY/semantics/services:$VERSION`

where $REGISTRY is set to the target container/docker repository (like `tsicatenaxdevacr.azurecr.io`) and $VERSION is set to the 
deployment version (usually `latest`).

### Redeploy in target environment

Run `kubectl rollout restart deployment services -n semantics`
