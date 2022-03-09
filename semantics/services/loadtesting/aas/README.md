# Introduction

This folder contains the loadtests for the AAS Registry.
The tool used for the loadtesting is `https://locust.io/`.

# Loadtest

The current implemented loadest does the following:

   - Creates a shell
   - Retrievs the created shell by id
   - Lookups the shell by specific asset ids

To reduce the complexity, authentication is disabled.

# Executing the test

The `docker-compose.yml` all relevant services to execute the loadtest.

   - AAS Registry (latest INT version)
   - PostgreSQL Database as persistence for the AAS Registry
   - Locust Master for the Webui
   - Locust Wokrer for the loadtest execution

# Run the load test

   1. Execute `docker-compose up -d`
   2. Open the Locust WebUI `http://localhost:8090`
   3. In the opened form enter the following:
         - Number of users = 100 (=> 10 req/s)
         - Spawn rate      = 5
         - Host            = http://aas_registry:4242
   4. Press Start. Locust will now execute the loadtest as long as you wish.
   5. You can stop the test at anytime through the UI and grab the statistics.

# Local development

The steps for local development of the loadtests are:

   1. Ensure python3 is installed
   2. Run `pip3 install -r requirements.txt`
   3. Modify the script
   4. Run `locust -f ./locust/locustfile.py --headless --users 1 --spawn-rate 1 -H http://host.docker.internal:4242`
