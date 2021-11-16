# Terraform scripts

Terraform scripts to deploy the Catena-X infrastructure on Microsoft Azure.

## Prerequisites

The following tools need to be installed on your system where you run the scripts:
- [Kubectl](https://kubernetes.io/docs/tasks/tools/)
- [Helm](https://helm.sh/docs/intro/install/)
- [Terraform](https://learn.hashicorp.com/tutorials/terraform/install-cli)
- [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli)

Alternatively, a [Github workflow](../../.github/workflows.terraform.yml) has been installed which automizes this process (on particular repositories, on particular branches)
  
## Quick start

Run the following commands to deploy the infrastructure to your target landscape (in this example: dev001). Please replace the landscape string with your target in the commands. 

Since terraform has been configured a) to use a backend-based state persistence and b) to use a potential Service Principal, you may need to interactively enter additional information throughout the process, such as the azure_storage_account_key, the azure_subscription_id, the azure tenant_id, the azure_client_id and your azure_client_secret. 

You will furthermore need a private/public key pair (ssh.priv, ssh.pub) to sign in to the kubernetes nodes (the host OS, that is).

### Terraform Actions

1. Sign-on to Azure and select the target subscription for the landscape with `az login --tenant catenaxpocoutlook.onmicrosoft.com`
1. To upload the public ssh key, run `az storage blob upload --name dev001.ssh.pub --container-name sshkeys --account-name catenaxdevtfstate --auth-mode login --file ssh.pub --metadata TYPE=SSH_KEY`
1. From the main directory of this repository, run `terraform init -backend-config="access_key=${azure_storage_account_key}"`
1. Check whether your landscape already exists (here dev001): `terraform workspace list | grep "dev001"`
1. If it does not exist, create a new workspace for your landscape (here dev001): `terraform workspace new dev001`
1. Otherwise, select the workspace with `terraform workspace select dev001`. 
1. Generate a plan by running `terraform plan --var-file=environments/dev001.tfvars -out .terraform/terraform.plan` and fill in the interactive variables (Service Principal Properties/Secrets)
1. Apply the thus generated plan by running `terraform apply terraform.plan` (If you only have contributor roles, but are not "UserAccessAdministrator", the following error may appear:)

```
Error: authorization.RoleAssignmentsClient#Create: Failure responding to request: StatusCode=403 -- Original Error: autorest/azure: Service returned an error. Status=403 Code="AuthorizationFailed" Message="The client 'youraccount@example.com' with object id 'xxx' does not have authorization to perform action 'Microsoft.Authorization/roleAssignments/write' over scope '/subscriptions/speedboat-id/resourceGroups/catenacax1-dev-rg/providers/Microsoft.ContainerRegistry/registries/catenacax1devacr/providers/Microsoft.Authorization/roleAssignments/roleId' or the scope is invalid. If access was recently granted, please refresh your credentials."
```

### Azure CLI Actions

#### AKS Attachement to ACR

Not all infrastructure is yet configurable via Terraform, so we need a few additional steps from the command line. These have been already integrated into [the terraform github workflow](../../.github/terraform.yml)

#### Database Persistence

1. Reconfigure the database/postgres firewall by running `az postgres server firewall-rule create -g catenax-dev001-rg -s catenaxdev001database -n "AllowAllWindowsAzureIps" --start-ip-address "0.0.0.0" --end-ip-address "0.0.0.0"` 
1. Create additional databases in the database/postgres service by `az postgres db create -g catenax-dev001-rg -s catenaxdev001database -n partsmasterdata` 

#### Storage/Table Persistence

1. You may need the connection string of the storage account to put it into your secret.sh by `az storage account show-connection-string --name catenaxdev001storage --resource-group catenax-dev001-rg`

#### Kubernetes Cert-Manager Issuer Config

1. Login to the Kubernetes cluster by running `az aks get-credentials --name catenax-dev001-aks-services --resource-group catenax-dev001-rg`
1. For doing docker pushs, run `az acr login --name catenaxdev001acr`
1. Deploy the Service Plane CA cluster issuer for TLS with `ISSUER_VARIANT="service" bash -c 'cat cluster-issuer.yaml | envsubst | kubectl apply -f -'`
1. Deploy the Portal Plane CA cluster issuer for TLS with `ISSUER_VARIANT="portal" bash -c 'cat cluster-issuer.yaml | envsubst | kubectl apply -f -'`

#### Single-Sign-On in Portal

1. Create a new app registration by running `az ad app create --display-name catenax-dev001-app --available-to-other-tenants true --reply-urls http://localhost:3000 https://catenaxdev001aksportal.germanywestcentral.cloudapp.azure.com --oauth2-allow-implicit-flow`
1. Add Graph/User Info permission to the just created app by running `az ad app permission add --id  ${idofappjustgenerated} --api 00000002-0000-0000-c000-000000000000 --api-permissions 311a71cc-e848-46a1-bdf8-97ff7156d8e6=Scope`

## Bugs

The mixture of kubernetes setups and deployments leads to a configuration dependencies (kubernetes provider depends on module.aks outputs). This may lead to terraform sometimes loose its configurations.
