# Azure Blob Storage Spike

## Spike scenario

Consumer exposes an API for sending a request to copy file from file system path to Azure Blob Storage.
Consumer API allows also to check if the data transfer process was completed.
If it's completed the check state query returns Azure Storage SAS url to a file, which allows to download the file from Azure Blob Storage.

## Run the connectors

### Run or debug from Intellij

Set up the run configuration in Intellij for both Consumer and Provider with following parameters:

- Module: prs-connector-consumer (or prs-connector-provider)
- Main class: org.eclipse.dataspaceconnector.system.runtime.BaseRuntime
- VM parameters: -Dedc.fs.config=<path to dataspaceconnector-configuration.properties file>

### Provision the infrastructure

If you want to run the set up on you own subscription then:

#### Create a certificate for Azure

The cloud resources hosted in Azure require a certificate for authentication, so you need to create one:

```bash
cd terraform
# create certificate:
openssl req -newkey rsa:2048 -new -nodes -x509 -days 3650 -keyout key.pem -out cert.pem
# create pfx file, we'll need this later
openssl pkcs12 -export -in cert.pem -inkey key.pem -out cert.pfx    
```

Due to the way how Terraform interprets environment variables, we
need to store the contents of the certificate in an environment variable named `TF_VAR_CERTIFICATE`:

```bash
TF_VAR_CERTIFICATE=$(<cert.pem)
```

#### Deploy cloud resources

```bash
cd terraform
terraform init
terraform apply
```

When it's done it will log the `client_id`, `tenant_id`
, `vault-name`, `storage-container-name` and `storage-account-name`.

Use these values in dataspaceconnector-configuration.properties files both in consumer and provider folders. 

## Test the flow

The scenario uses hardcoded path to a file that is set up in the artifact catalog. So before running the query make sure you have test-document.txt file under 
location /tmp/copy/source.

### Request data transfer

Using e.g. Postman execute POST call to the consumer requesting the data transfer:

<consumer-connector-url>/api/file/test-document?connectorAddress=<provider-connector-url>

by default:
- provider-connector-url is http://localhost:8181 (when run from docker then http://provider)
- consumer-connector-url is http://localhost:9191

The response should contain PROCESS_ID. Copy it. 

### Check the status

Execute GET

<consumer-connector-url>/api/datarequest/<PROCESS_ID>/state

The response should show the current state of the process and once it's finished it returns SAS url that can be copied and executed e.g. in a browser.

## Git token

Create a [personal access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token) in GitHub, limited to **read:packages** scope.

Configure the following GitHub *repository secrets*:
- PRS_EDC_PKG_USERNAME (your_github_username)
- PRS_EDC_PKG_PASSWORD (your_github_pat_token)

## Local build

Copy `settings.xml` into your `~/.m2/` folder (or merge it with a file already there), and replace the environment variable references with the following:

```
<username>your_github_username</username>
<password>your_github_pat_token</password>
```

## Docker build

See `run-integration-test.sh` file.

## Running tests

```
export PRS_EDC_PKG_USERNAME=your_github_username
export PRS_EDC_PKG_PASSWORD=your_github_pat_token
./run-integration-test.sh
```
