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

1. Sign-on to Azure and select the target subscription for the landscape with `az login --tenant catenaxpocoutlook.onmicrosoft.com`
1. From the main directory of this repository, run `terraform init`
1. If you haven't done so before, create a new workspace for your landscape (here dev001): `terraform workspace new dev001`
1. If you already created the workspace, select it with `tterrerraform workspace select dev001`. You can list your existing workspaces with `terraform workspace list`
1. Run `terraform plan --var-file=environments/dev001.tfvars`
1. Run `terraform apply --var-file=environments/dev001.tfvars` (If you only have contributor roles, the following error may appear:)

```
Error: authorization.RoleAssignmentsClient#Create: Failure responding to request: StatusCode=403 -- Original Error: autorest/azure: Service returned an error. Status=403 Code="AuthorizationFailed" Message="The client 'youraccount@example.com' with object id 'xxx' does not have authorization to perform action 'Microsoft.Authorization/roleAssignments/write' over scope '/subscriptions/speedboat-id/resourceGroups/catenacax1-dev-rg/providers/Microsoft.ContainerRegistry/registries/catenacax1devacr/providers/Microsoft.Authorization/roleAssignments/roleId' or the scope is invalid. If access was recently granted, please refresh your credentials."
```

1. Need to change the admin azure ad group in the cluster configuration https://portal.azure.com/#@swbtsishowcaseoutlook.onmicrosoft.com/resource/subscriptions/f917eb77-210c-4089-ab3c-bb36b8819d84/resourceGroups/tsicatenax-dev-rg/providers/Microsoft.ContainerService/managedClusters/tsicatenax-dev-aks-services/configurationBlade to an existing/valid AAD group (there will be an "anonymous" non-working group assigned per default)

1. Import environment and secret variables `source ../manifests/environment.sh`

1. Attach the container registry to the kubernetes cluster by `az aks update -n ${K8_RESOURCE_NAME} -g ${K8_RESOURCE_GROUP} --attach-acr ${CONTAINER_REGISTRY_SHORT}`. If you only have contributor roles, the following error will appear:

```
Waiting for AAD role to propagate[################################    ]  90.0000%Could not create a role assignment for ACR. Are you an Owner on this subscription?
```

1. Create a storage account with NFS support for tables and files by `az storage account create --name ${STORAGE_ACCOUNT_NAME} --resource-group ${K8_RESOURCE_GROUP}`
1. Get the connection string and put it into your secret.sh by `az storage account show-connection-string --name ${STORAGE_ACCOUNT_NAME} --resource-group ${K8_RESOURCE_GROUP}`
1. Create an additional database in an existing database service for persistence by `az postgres db create -g ${K8_RESOURCE_GROUP} -s ${POSTGRES_RESOURCE_NAME} -n partsmasterdata` 
1. Run `az acr login --resource-group ${K8_RESOURCE_GROUP}`

1. Deploy the CA cluster issuer for TLS with `cat client-issuer.yaml | envsubst | kubectl apply`
y