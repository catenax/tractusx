#!/usr/bin/env python3
import json
import sys

part_aspect_updates_file=sys.argv[1]
aspect_urls_by_manufacturer_file=sys.argv[2]
output_file=sys.argv[3]

with open(aspect_urls_by_manufacturer_file, 'r+') as f:
    aspect_urls_by_manufacturer_id = json.load(f)

default_aspect_url = "http://tdmgeneratordev.germanywestcentral.azurecontainer.io:8080"

with open(part_aspect_updates_file, 'r+') as f:
    part_aspect_updates = json.load(f)

    for aspect_update in part_aspect_updates:
        one_id_manufacturer = aspect_update["part"]["oneIDManufacturer"]
        prefix_aspect_url = aspect_urls_by_manufacturer_id.get(one_id_manufacturer, default_aspect_url)
        for aspect in aspect_update["aspects"]:
            full_aspect_url = aspect["url"]
            # take substring after :8080
            path_params = full_aspect_url.split(":8080")[1]
            new_url = prefix_aspect_url + full_aspect_url.split(":8080")[1]
            aspect["url"] = new_url
with open('test-data/dev/PartAspectUpdate.json', 'w') as outfile:
    json.dump(part_aspect_updates, outfile, indent=2)
