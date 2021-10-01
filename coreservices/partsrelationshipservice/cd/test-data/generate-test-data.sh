#!/bin/bash

set -euo pipefail

# Purge data from PRS database
echo "DELETE from public.part_relationship WHERE oneidmanufacturer IN ('CAXLBRHHQAJAIOZZ', 'CAXLHNJURNRLPCZZ', 'CAXLTHAJNAHZXGZZ', 'CAXLZJVJEBYWYYZZ', 'CAXSWPFTJQEVZNZZ', 'CAXSZJVJEBYWYYZZ') OR parent_oneidmanufacturer IN ('CAXLBRHHQAJAIOZZ', 'CAXLHNJURNRLPCZZ', 'CAXLTHAJNAHZXGZZ', 'CAXLZJVJEBYWYYZZ', 'CAXSWPFTJQEVZNZZ', 'CAXSZJVJEBYWYYZZ');"
echo "DELETE from public.part_aspect WHERE public.part_aspect.oneidmanufacturer IN ('CAXLBRHHQAJAIOZZ', 'CAXLHNJURNRLPCZZ', 'CAXLTHAJNAHZXGZZ', 'CAXLZJVJEBYWYYZZ', 'CAXSWPFTJQEVZNZZ', 'CAXSZJVJEBYWYYZZ');"
echo "DELETE from public.part_attribute WHERE oneidmanufacturer IN ('CAXLBRHHQAJAIOZZ', 'CAXLHNJURNRLPCZZ', 'CAXLTHAJNAHZXGZZ', 'CAXLZJVJEBYWYYZZ', 'CAXSWPFTJQEVZNZZ', 'CAXSZJVJEBYWYYZZ');"

# Generate SQL to load part_relationship data (parent-child relationships)
echo 'COPY public.part_relationship (oneidmanufacturer, objectidmanufacturer, parent_oneidmanufacturer, parent_objectidmanufacturer, part_relationship_list_id, upload_date_time) FROM stdin CSV;'
jq -r '(now | strftime("%Y-%m-%dT%H:%M:%S%z")) as $n | .[].relationships | .[].relationship | [.child.oneIDManufacturer, .child.objectIDManufacturer, .parent
.oneIDManufacturer, .parent.objectIDManufacturer, "78F4BB1B-2EBB-418C-9C16-3E74BACCBEAC", $n] | @csv' "PartRelationshipUpdateList.json"
echo '\.'

# Generate SQL to load part_aspect data (aspect URLs)
echo 'COPY public.part_aspect (name, oneidmanufacturer, objectidmanufacturer, url, effect_time, last_modified_time) FROM stdin CSV;'
jq -r '(now | strftime("%Y-%m-%dT%H:%M:%S%z")) as $n | .[] | .part as $p | .aspects[] | [.name, $p.oneIDManufacturer, $p.objectIDManufacturer, .url, $n, $n]
| @csv' "PartAspectUpdate.json"
echo '\.'

# Generate SQL to load part_attribute data (partTypeName field)
echo 'COPY public.part_attribute (name, oneidmanufacturer, objectidmanufacturer, value, effect_time, last_modified_time) FROM stdin CSV;'
jq -r '(now | strftime("%Y-%m-%dT%H:%M:%S%z")) as $n | .[] | .part as $p | ["partTypeName", $p.oneIDManufacturer, $p.objectIDManufacturer, .partTypeName, $n,
 $n] | @csv' "PartTypeNameUpdate.json"
echo '\.'
