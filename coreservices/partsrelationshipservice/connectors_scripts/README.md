## Interact with Connectors

This folder contains scripts to interact with connectors. It explains how we can create an artifact and consume the data
of the artifact through a consumer.
[resourceapi.py](https://github.com/International-Data-Spaces-Association/DataspaceConnector/blob/main/scripts/tests/resourceapi.py) and [idsapi.py](https://github.com/International-Data-Spaces-Association/DataspaceConnector/blob/main/scripts/tests/idsapi.py) have been taken in the [Dataspace connector repository](https://github.com/International-Data-Spaces-Association/DataspaceConnector).

[create_catalof_and_artifact.py](./create_catalof_and_artifact.py) and [consume_artifact.py](./consume_artifact.py) are based on [a script from the DataspaceConnector repository](https://github.com/International-Data-Spaces-Association/DataspaceConnector/blob/main/scripts/tests/contract_negotation_allow_access.py).
[create_catalof_and_artifact.py](./create_catalof_and_artifact.py) creates a catalog and an artifact accessible via an access url (our PRS api in our case).
[consume_artifact.py](./consume_artifact.py) Find the first artifact of the first catalog accessible and tries to access
the artifact data by calling the access_url of the artifact. You can specify the pathparams and query params that needs to be appended to the access url to access a resource.

## Create a catalog and an artifact.

### Use create_catalog_and_artifact.py
```bash
pipenv sync
pipenv shell
./create_catalog_and_artifact.py \
<provider-url> \
<provider-internal-alias> \
<artifact-title> \
<access-url-to-access-the-artifact> \
<admin> \
<password>
```

### Run it in env001 environment.
```bash
pipenv sync
pipenv shell
./create_catalog_and_artifact.py \
"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/producer" \
"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/producer" \
"PRS" \
"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com" \
<admin> \
<password>
```

## Consume an artifact

```bash
pipenv sync
pipenv shell
python consume_artifact.py \
"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/producer" \
"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/consumer" \
"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/producer" \
"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/consumer" \
"/api/v0.1/vins/YS3DD78N4X7055320/partsTree?view=AS_BUILT" \
<admin> \
<password1>
```

## Consume data in env001
```bash
pipenv sync
pipenv shell
python consume_artifact.py \
<consumer-url> \
<consumer-internal-alias> \
<provider-url> \
<provider-internal-alias> \
 <pathparams-and-query-params-to-append-to-the-url-to-access-a-specific-resource> \
<admin> \
<password>
```

```bash
pipenv sync
pipenv shell
python contract_negotation_allow_access.py \ 
<provider-url> \
<consumer-url> \
<provider-internal-alias> \
<connector-internal-alias> \
<your-artifact-title> \
<artifact-to-access-the-artifact> \
<pathparams-and-query-params-to-append-to-the-url-to-access-a-specific-resource> \
<admin> \
<password>
```