This folder contains scripts to interact with connectors.
[resourceapi.py](https://github.com/International-Data-Spaces-Association/DataspaceConnector/blob/main/scripts/tests/resourceapi.py) and [idsapi.py](https://github.com/International-Data-Spaces-Association/DataspaceConnector/blob/main/scripts/tests/idsapi.py) have been taken in the [Dataspace connector repository](https://github.com/International-Data-Spaces-Association/DataspaceConnector).
[contract_negotation_allow_access.py](contract_negotation_allow_access.py) is based on [a script from the DataspaceConnector repository](https://github.com/International-Data-Spaces-Association/DataspaceConnector/blob/main/scripts/tests/contract_negotation_allow_access.py). It was modified to fit our needs.

To create a catalog and register a new resource you can run the following command:

```bash
pipenv sync
pipenv shell
./contract_negotation_allow_access.py \
https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/producer \ 
https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/consumer \ 
https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/producer \
https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/env001/consumer \
My artifact \
https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com \
/api/v0.1/vins/YS3DD78N4X7055320/partsTree?view=AS_BUILT \
<username> \
<password>
```



