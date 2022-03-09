import uuid
import json

from locust import HttpUser, task, between, tag

class DataPreperationTask(HttpUser):
    wait_time = between(1, 5)
  
    @tag('createShellDescriptor')
    @task
    def createAasDescriptor(self):
        shell = generate_shell()
        with open("shell_data.json", "a", encoding="utf-8") as shell_data_file:
            lookup_entry = json.dumps({ 
                "id" : shell['identification'],
                "specificAssetIds": shell['specificAssetIds']
            })
            shell_data_file.write(f"{lookup_entry}\n")
        self.client.post("/registry/shell-descriptors", data=json.dumps(shell), headers= { 'Content-Type' : 'application/json'})

def generate_shell():
    aasId = uuid.uuid4()
    globalAssetId = uuid.uuid4()
    specificAssetId1 = uuid.uuid4()
    specificAssetId2 = uuid.uuid4()
    return {
              "description": [
                {
                  "language": "en",
                  "text": "The shell for a vehicle"
                }
              ],
              "globalAssetId": {
                "value": [
                    str(globalAssetId)
                ]
              },
              "idShort": "future concept x",
              "identification": str(aasId),
              "specificAssetIds": [
                {
                  "key": "MaterialId",
                  "value": str(specificAssetId1)
                },
                {
                  "key": "PartId",
                  "value": str(specificAssetId2)
                }
              ],
              "submodelDescriptors": [
                {
                  "description": [
                    {
                      "language": "en",
                      "text": "Provides base vehicle information"
                    }
                  ],
                  "idShort": "vehicle base details",
                  "identification": "4a738a24-b7d8-4989-9cd6-387772f40565",
                  "semanticId": {
                    "value": [
                        "urn:bamm:com.catenax.vehicle:0.1.1"
                    ]
                  },
                  "endpoints": [
                    {
                      "interface": "HTTP",
                      "protocolInformation": {
                        "endpointAddress": "https://catena-x.net/vehicle/basedetails/",
                        "endpointProtocol": "HTTPS",
                        "endpointProtocolVersion": "1.0"
                      }
                    }
                  ]
                },
                {
                  "description": [
                    {
                      "language": "en",
                      "text": "Provides base vehicle information"
                    }
                  ],
                  "idShort": "vehicle part details",
                  "identification": "dae4d249-6d66-4818-b576-bf52f3b9ae90",
                  "semanticId": {
                    "value": [
                        "urn:bamm:com.catenax.vehicle:0.1.1#PartDetails"
                    ]
                  },
                  "endpoints": [
                    {
                      "interface": "HTTP",
                      "protocolInformation": {
                        "endpointAddress": "https://catena-x.net/vehicle/partdetails/",
                        "endpointProtocol": "HTTPS",
                        "endpointProtocolVersion": "1.0"
                      }
                    }
                  ]
                }
              ]
        }