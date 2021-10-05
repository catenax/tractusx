# Parts Relationship Service

## How to run

The two following subsections provide instructions for running either only the infrastructure on docker-compose and the application in the IDE, or for running the full stack (including the application) in docker-compose.

### Docker-compose + IDE

* Start the necessary infrastructure by running `docker-compose up`

* Start the application from your favorite IDE

### Docker-compose full stack

* (Optional) Copy the file `.env.example` to `.env` and provide your Application Insights connection string.

* Run `docker-compose --profile prs up`

## Work with sample data

* Retrieve one sample preloaded BOM:

```bash
curl -X GET "http://localhost:8080/api/v0.1/vins/BMWOVCDI21L5DYEUU/partsTree?view=AS_BUILT"
```

## Inspect data

### Get persisted data

```bash
docker exec -it db bash -c 'psql -U $POSTGRES_USER postgres'
```

```sql
select * from part_relationship;
```

## Swagger UI

- Swagger UI: http://localhost:8080/swagger-ui
- API docs: http://localhost:8080/api-docs
- API docs in yaml:  http://localhost:8080/api-docs.yaml

## Terraform

See [Terraform deployment](terraform).

## Deploy to DEV

The new version of the application is deployed to DEV on merge to main through the `PRS Deploy` workflow.
The workflow builds a new image, pushes it to ACR and deploys it to Kubernetes. If you make changes to Terraform, these changes will be applied as the workflow runs `terraform apply`.
If you want to make sure the PRS deployment will work well with your changes, you can run the `PRS Deploy` workflow manually on your branch. Note that other PRs merged to main will cause Terraform to potentially roll back those changes.

## Deploy to INT

A deployment to the INT environment can be triggered manually with the `PRS Deploy` workflow as well by overriding the default target environment parameter to "int". INT environment deployments should be coordinated with the consumers of the PRS systems.

## Load Test Data

Test data can be loaded using `PRS Load Test Data` workflow. This workflow is triggered manually. It checks out the json files stored in
[test-data folder](./coreservices/partsrelationshipservice/cd/test-data), converts the data into sql queries and inserts the data into PRS database.
Before inserting all records with oneIds from json files are deleted from the database.

## Smoke tests

[Smoke tests](integration-tests/src/test/java/net/catenax/prs/smoketest) are running against the application and the consumer connectors in the PRS Smoke tests pipeline.
If you run the smoke tests locally, by default it will run against the application directly.
If you want to run it against connector you need to add the following VM options:
`-DbaseURI=<consumer-artifact-uri> -Dusername=<username-to-access-consumer> -Dpassword=<password>`
