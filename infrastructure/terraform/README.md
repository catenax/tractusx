# Terraform scripts

Terraform scripts to deploy the Catena-X infrastructure on Microsoft Azure.

## Prerequisites

The following tools need to be installed on your system where you run the scripts:
- [Kubectl](https://kubernetes.io/docs/tasks/tools/)
- [Helm](https://helm.sh/docs/intro/install/)
- [Terraform](https://learn.hashicorp.com/tutorials/terraform/install-cli)
- [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli)

Alternatively, a [Github workflow](../../.github/workflows.terraform.yml) has been installed which automizes this process.
  
## Quick start

Run the following commands to deploy the infrastructure to your target landscape (e.g. dev001). Please replace the landscape string with your target in the commands. 

Since terraform has been configured to a) use a backend-based state persistence and b) use a potential Service Principal, you will need to interactively enter additional information throughout the process, such as the azure_storage_account_key, the azure_subscription_id, the azure tenant_id, the azure_client_id and your azure_client_secret. 

You will furthermore need a private/public key pair (ssh.priv, ssh.pub) to sign in to the kubernetes nodes.

1. Sign-on to Azure and select the target subscription for the landscape with `az login --tenant catenaxpocoutlook.onmicrosoft.com`
1. To upload the public ssh key, run `az storage blob upload --name dev001.ssh.pub --container-name sshkeys --account-name catenaxdevtfstate --auth-mode login --file ssh.pub --metadata TYPE=SSH_KEY`
1. From the main directory of this repository, run `terraform init -backend-config="key=catenaxdev.tfstateenv:dev001" -backend-config="access_key=azure_storage_account_key"`
1. If you haven't done so before, create a new workspace for your landscape (here dev001): `terraform workspace new dev001`
1. If you already created the workspace, select it with `terraform workspace select dev001`. You can list your existing workspaces with `terraform workspace list`
1. Temporarily mute the providers for the next step `mv provider.tf provider.tf_muted`
1. Import shared resources into the state `terraform import --var-file=environments/dev001.tfvars`
1. Reactivate the providers `mv provider.tf_muted provider.tf`
1. Run `terraform plan --var-file=environments/dev001.tfvars -out .terraform/terraform.plan` and fill in the interactive variables 
1. Run `terraform apply terraform.plan` (If you only have contributor roles in the subscription, the following error may appear:)

```
Error: authorization.RoleAssignmentsClient#Create: Failure responding to request: StatusCode=403 -- Original Error: autorest/azure: Service returned an error. Status=403 Code="AuthorizationFailed" Message="The client 'youraccount@example.com' with object id 'xxx' does not have authorization to perform action 'Microsoft.Authorization/roleAssignments/write' over scope '/subscriptions/speedboat-id/resourceGroups/catenacax1-dev-rg/providers/Microsoft.ContainerRegistry/registries/catenacax1devacr/providers/Microsoft.Authorization/roleAssignments/roleId' or the scope is invalid. If access was recently granted, please refresh your credentials."
```

1. Only if the error appears, you need to attach the container registry to the kubernetes cluster using a more privileged account by `az aks update -n catenax-dev001-aks-services -g catenax-dev001-rg --attach-acr catenaxdev001acr`. Again, if you only have contributor roles, the following error will appear:

```
Waiting for AAD role to propagate[################################    ]  90.0000%Could not create a role assignment for ACR. Are you an Owner on this subscription?
```

1. Reconfigure the postgres firewall by running `az postgres server firewall-rule create -g catenax-dev001-rg -s catenaxdev001database -n "AllowAllWindowsAzureIps" --start-ip-address "0.0.0.0" --end-ip-address "0.0.0.0"` 
1. Get the connection string and put it into your secret.sh by `az storage account show-connection-string --name catenaxdev001storage --resource-group catenax-dev001-rg`
1. Create additional databases in an existing database service for persistence by `az postgres db create -g catenax-dev001-rg -s catenaxdev001database -n partsmasterdata` 
1. run `az aks get-credentials --name catenax-dev001-aks-services --resource-group catenax-dev001-rg`

1. Deploy the Service Plane CA cluster issuer for TLS with `ISSUER_VARIANT="-service" bash -c 'cat cluster-issuer.yaml | envsubst | kubectl apply -f -'`
1. Deploy the Portal Plane CA cluster issuer for TLS with `ISSUER_VARIANT="-portal" bash -c 'cat cluster-issuer.yaml | envsubst | kubectl apply -f -'`

1. For doing docker commands, run `az acr login --name catenaxdev001acr --resource-group catenax-dev001-rg`
