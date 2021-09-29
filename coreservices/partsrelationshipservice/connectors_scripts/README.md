This folder contains scripts to interact with connectors.
[resourceapi.py](https://github.com/International-Data-Spaces-Association/DataspaceConnector/blob/main/scripts/tests/resourceapi.py) and [idsapi.py](https://github.com/International-Data-Spaces-Association/DataspaceConnector/blob/main/scripts/tests/idsapi.py) have been taken in the [Dataspace connector repository](https://github.com/International-Data-Spaces-Association/DataspaceConnector).
[contract_negotation_allow_access.py](contract_negotation_allow_access.py) is based on [a script from the DataspaceConnector repository](https://github.com/International-Data-Spaces-Association/DataspaceConnector/blob/main/scripts/tests/contract_negotation_allow_access.py). It was modified to fit our needs.

`contract_negotation_allow_access.py` creates a catalog and an artifact accessible via an access url (our PRS api in our case).
The script verifies that the resource is accessible through connector by trying to access the data. That's why you need
to specify the pathparams and query params to access a resource to check if this work.

The script should be used like this:
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

To create a catalog and register the PRS artifact in our environment you can run the following command:
```bash
pipenv sync
pipenv shell
./contract_negotation_allow_access.py \
https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/producer \ 
https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/consumer \ 
https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/producer \
https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/consumer \
"PRS" \
"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com" \
"/api/v0.1/vins/YS3DD78N4X7055320/partsTree?view=AS_BUILT" \
<username> \
<password>
```

If you are running connectors locally you can also run:
```bash
pipenv sync
pipenv shell
python contract_negotation_allow_access.py \ 
http://localhost:8080 \
http://localhost:8081 \
http://provider-dataspace-connector \
http://consumer-dataspace-connector \
"My artifact title" \
"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com" \
"/api/v0.1/vins/YS3DD78N4X7055320/partsTree?view=AS_BUILT" \
admin \
password
```



