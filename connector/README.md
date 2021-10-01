# Catena-X Connector 
 
## Background
Catena-X implements IDS (International Dataspace). The IDS Connector is a central component that is used as a communication gateway by each Adapter (Data provisioning and ingestion component linked to the backend systems of the business partners), Service (Platform-Shared logic and infrastructure component) and App (Business logic with frontend facilities). The Catena-X connector is an adaption of the Dataspace Connector which is an open source implementation of the IDS Connector specification. The Catena-X connector will connect to a persistence service for storing its state (and sharing it in the case of loadbalancing multiple connector instances into one logical instance). It will have
one networking layer/face to the business partner, service or app network side. It will have one messaging layer/face to the Catena-X connector network (maybe by VPN or other network bridging technology). 

So it may be deployed on premise as well as a cloud service.

## Remarks

Dataspace Connector stores its configuration upon first run in the database, so a change in configuration needs to be done 
in the deployment config as well as the database.

## Testing it
For testing connector functionalities, there are two scripts provided:
- [run_local.sh] (-build)? (provider|consumer)? -debug
- [run_k8.sh] for which [../infrastructure/manifests/environment.sh] should be adapted and sourced.
