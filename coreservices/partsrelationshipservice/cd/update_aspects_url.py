#!/usr/bin/env python3
import json
import sys

# File containing the PartAspectUpdate json file that needs to be updated.
part_aspect_updates_file=sys.argv[1]
# File containing the mapping between oneIDManufacturer and their aspectUrl.
# The format should be { "oneIDManufacturer1": "aspectUrl1", "oneIDManufacture21": "aspectUrl2"}
aspect_urls_by_manufacturer_file=sys.argv[2]
# File where the new PartAspectUpdate json will be written. It can be the same as the file used as an input.
output_file=sys.argv[3]
# Aspect URL that will be used if the oneIDManufacturer has no aspect url.
default_aspect_url=sys.argv[4] # "http://tdmgeneratordev.germanywestcentral.azurecontainer.io:8080"

with open(aspect_urls_by_manufacturer_file, 'r+') as f:
    aspect_urls_by_manufacturer_id = json.load(f)

with open(part_aspect_updates_file, 'r+') as f:
    part_aspect_updates = json.load(f)

    for aspect_update in part_aspect_updates:
        one_id_manufacturer = aspect_update["part"]["oneIDManufacturer"]
        prefix_aspect_url = aspect_urls_by_manufacturer_id.get(one_id_manufacturer, default_aspect_url)
        for aspect in aspect_update["aspects"]:
            full_aspect_url = aspect["url"]
            # The tdmgeneratordev urls that we want to replace ends by /catena-x/tdm/1.0/aspect.
            # If it does not contain /catena-x/tdm/1.0/aspect it means that the URL is a consumer artifact. Then it should end by /data
            # Get substring after /catena-x/tdm/1.0/aspect or after /data
            path_params = full_aspect_url.split("/catena-x/tdm/1.0/aspect")[1] if "/catena-x/tdm/1.0/aspect" in full_aspect_url else full_aspect_url.split("/data")[1]
            new_url = prefix_aspect_url + path_params
            aspect["url"] = new_url
with open(output_file, 'w') as outfile:
    json.dump(part_aspect_updates, outfile, indent=2)
