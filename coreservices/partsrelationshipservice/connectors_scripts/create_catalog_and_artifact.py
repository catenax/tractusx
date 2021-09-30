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

#
# This script is based on the following script:
# https://github.com/International-Data-Spaces-Association/DataspaceConnector/blob/main/scripts/tests/contract_negotation_allow_access.py
# It creates a catalog, negotiate a contract and create an artifact.
# The original script is modified in order to register an artifact containing an access_url, so that the provider can
# call an API to access the artifact data instead of storing the data in its database.
#

from resourceapi import ResourceApi
from idsapi import IdsApi
import requests
import pprint
import sys


provider_url = sys.argv[1]
# Provider alias in the connector network. The consumer needs this alias to reach out to the provider.
provider_alias = sys.argv[2]
catalog_title = sys.argv[3]
artifact_title = sys.argv[4]
# URL to access the artifact.
access_url = sys.argv[5]
# User having an access to the  provider connector.
user = sys.argv[6]
password = sys.argv[7]

print("Setting provider url:", provider_url)
print("Setting provider alias as:", provider_alias)

# Suppress ssl verification warning
requests.packages.urllib3.disable_warnings()

# Provider
provider = ResourceApi(provider_url, auth=(user, password))

## Create resources
catalog = provider.create_catalog(data={"title": catalog_title})
offers = provider.create_offered_resource()

representation = provider.create_representation()

artifact = provider.create_artifact(data=
{
"title": artifact_title,
"accessUrl": access_url,
"automatedDownload": "true"
})

contract = provider.create_contract()
use_rule = provider.create_rule()

## Link resources
provider.add_resource_to_catalog(catalog, offers)
provider.add_representation_to_resource(offers, representation)
provider.add_artifact_to_representation(representation, artifact)
provider.add_contract_to_resource(offers, contract)
provider.add_rule_to_contract(contract, use_rule)

print(offers)
