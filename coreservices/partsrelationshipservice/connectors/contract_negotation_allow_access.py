#!/usr/bin/env python3
#
# Copyright 2020 Fraunhofer Institute for Software and Systems Engineering
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

from resourceapi import ResourceApi
from idsapi import IdsApi
import requests
import pprint
import sys


providerUrl = sys.argv[1]
consumerUrl = sys.argv[2]
provider_alias = sys.argv[3]
consumer_alias = sys.argv[4]
user = sys.argv[5]
password = sys.argv[6]
print("Setting provider url:", providerUrl)
print("Setting consumer url:", consumerUrl)
print("Setting provider alias as:", provider_alias)
print("Setting consumer alias as:", consumer_alias)

# Suppress ssl verification warning
requests.packages.urllib3.disable_warnings()

# Provider
provider = ResourceApi(providerUrl, auth=(user, password))

## Create resources
catalog = provider.create_catalog()
offers = provider.create_offered_resource()
representation = provider.create_representation()
artifact = provider.create_artifact(data=
{
"title": "an aspect",
"accessUrl": "https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com",
"automatedDownload": "true"
})
relative_reference="/api/v0.1/vins/YS3DD78N4X7055320/partsTree?view=AS_BUILT"
contract = provider.create_contract()
use_rule = provider.create_rule()

## Link resources
provider.add_resource_to_catalog(catalog, offers)
provider.add_representation_to_resource(offers, representation)
provider.add_artifact_to_representation(representation, artifact)
provider.add_contract_to_resource(offers, contract)
provider.add_rule_to_contract(contract, use_rule)

print("Created provider resources")

# Consumer
consumer = IdsApi(consumerUrl, auth=(user, password))

# Replace localhost references
offers = offers.replace(providerUrl, provider_alias)
artifact = artifact.replace(providerUrl, provider_alias)

# IDS
# Call description
offer = consumer.descriptionRequest(provider_alias + "/api/ids/data", offers)
pprint.pprint(offer)

# Negotiate contract
obj = offer["ids:contractOffer"][0]["ids:permission"][0]
obj["ids:target"] = artifact
response = consumer.contractRequest(
    provider_alias + "/api/ids/data", offers, artifact, False, obj
)
pprint.pprint(response)

# Pull data
agreement = response["_links"]["self"]["href"]

consumerResources = ResourceApi(consumerUrl, auth=(user, password))
artifacts = consumerResources.get_artifacts_for_agreement(agreement)
pprint.pprint(artifacts)

first_artifact = artifacts["_embedded"]["artifacts"][0]["_links"]["self"]["href"]
pprint.pprint(first_artifact)

data = consumerResources.get_data(first_artifact, relative_reference).text
pprint.pprint(data)

exit(0)