# Interact with Connectors

PRS API should be accessible via a consumer connector. An artifact linked to PRS API is created via a provider connector.
This document explains how we can create an artifact and consume the data of the artifact through a consumer.

## Create a catalog and an artifact

```bash
pipenv sync
pipenv shell
./create_catalog_and_artifact.py \
<provider-url> \
<provider-internal-alias> \
<catalog_title> \
<artifact_title> \
<access-url-to-access-the-artifact> \
<username> \
<password>
```

## Create a catalog and an artifact in the dev001 environment

```bash
pipenv sync
pipenv shell
./create_catalog_and_artifact.py \
"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/producer" \
"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/producer" \
"PRS catalog" \
"PRS" \
"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com" \
<username> \
<password>
```

## Negotiate contract and consume the data of an artifact

```bash
pipenv sync
pipenv shell
./consume_artifact.py \
<provider-url> \
<consumer-url> \
<provider-internal-alias> \
<consumer-internal-alias> \
<pathparams-and-query-params-to-append-to-the-url-to-access-a-specific-resource> \
<username> \
<password>
```

## Negotiate contract and consume the data of an artifact in the env001 environment

```bash
pipenv sync
pipenv shell
./negotiate_contract_and_consume_artifact.py \
"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/producer" \
"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/consumer" \
"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/producer" \
"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/consumer" \
"/api/v0.1/vins/YS3DD78N4X7055320/partsTree?view=AS_BUILT" \
<username> \
<password>
```

## Consume data when contract is already negotiated.
When running the negotiate_contract_and_consume_artifact.py script, please note the url printed by the script under "Consumer data url to access the artifact".
This url should end by /data. You need to append path params and query params to the url to make your query.

## Scripts explanation

[resourceapi.py](https://github.com/International-Data-Spaces-Association/DataspaceConnector/blob/main/scripts/tests/resourceapi.py) and [idsapi.py](https://github.com/International-Data-Spaces-Association/DataspaceConnector/blob/main/scripts/tests/idsapi.py) have been taken in the [Dataspace connector repository](https://github.com/International-Data-Spaces-Association/DataspaceConnector).
[create_catalog_and_artifact.py](./create_catalof_and_artifact.py) and [consume_artifact.py](./negotiate_contract_and_consume_artifact.py) are based on [a script from the DataspaceConnector repository](https://github.com/International-Data-Spaces-Association/DataspaceConnector/blob/main/scripts/tests/contract_negotation_allow_access.py).
[create_catalog_and_artifact.py](./create_catalof_and_artifact.py) creates a catalog and an artifact accessible via an access url (our PRS api in our case).
[consume_artifact.py](./negotiate_contract_and_consume_artifact.py) Finds the first artifact of the first catalog accessible and tries to access the artifact data by calling the access_url of the artifact. It takes the first artifact for simplicity as we are supposed to register only one artifact. You can specify the pathparams and query params that needs to be appended to the access url to access a resource.