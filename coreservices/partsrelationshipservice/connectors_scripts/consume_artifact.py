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
# The original script creates an artifact. This script only consume an artifact.
#

from resourceapi import ResourceApi
from idsapi import IdsApi
import requests
import pprint
import sys


# provider_url = sys.argv[1]
consumer_url = sys.argv[1]
# Provider alias in the connector network. The consumer needs this alias to reach out to the provider.
# provider_alias = sys.argv[3]
# Consumer alias in the connector network. The provider needs this alias to reach out to the connector.
consumed_artifact = sys.argv[2]
# consumer_alias = sys.argv[4]
relative_reference = sys.argv[3]
# User having an access to the consumer connector.
user = sys.argv[4]
password = sys.argv[5]

# Suppress ssl verification warning
requests.packages.urllib3.disable_warnings()


consumerResources = ResourceApi(consumer_url, auth=(user, password))

pprint.pprint(consumed_artifact)

data = consumerResources.get_data(consumed_artifact, relative_reference, headers={"accept": "application/json"}).text

pprint.pprint("Consumer data url to access the artifact:")
pprint.pprint(consumed_artifact + "/data")

pprint.pprint("Data obtained:")
pprint.pprint(data)

exit(0)
