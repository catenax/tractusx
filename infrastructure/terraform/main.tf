####################################################################################################
# Variables for stage and size based on the current workspace context
####################################################################################################

locals {
  stage   = "${lookup(var.workspace_to_stage_map, terraform.workspace, "dev")}"
  size    = "${lookup(var.stage_to_size_map, local.stage, "small")}"
}

module "landscape_variables" {
  source  = "./modules/variables"
  stage   = local.stage
  size    = local.size
}

####################################################################################################
# Shared infrastructure (shared services resource group, landscape resource group, monitoring, ...)
####################################################################################################

# this is just queried/imported as its does not belong to this state
data "azurerm_resource_group" "shared_services_rg" {
  name     = "shared-services-rg"
}

resource "azurerm_resource_group" "default_rg" {
  name     = "${var.prefix}-${var.environment}-rg"
  location = var.location

  tags = {
    environment = "${var.environment}"
  }
}

resource "azurerm_log_analytics_workspace" "shared" {
  name                = "${var.prefix}-${var.environment}-log"
  resource_group_name = data.azurerm_resource_group.shared_services_rg.name
  location            = data.azurerm_resource_group.shared_services_rg.location
  sku                 = "PerGB2018"
  retention_in_days   = 30

  tags = {
    environment = "shared_services"
  }

  depends_on = [ data.azurerm_resource_group.shared_services_rg ]
}

####################################################################################################
# Azure Virtual Networks
####################################################################################################

module "aks_vnet" {
  source              = "./modules/vnet"
  name                = "${var.prefix}-${var.environment}-aks-vnet"
  resource_group_name = azurerm_resource_group.default_rg.name
  location            = azurerm_resource_group.default_rg.location

  address_space = ["192.168.0.0/16"]
  subnets = [
    {
      name : "${var.prefix}-${var.environment}-aks-node-subnet"
      address_prefixes : ["192.168.1.0/24"]
    },
    {
      name : "${var.prefix}-${var.environment}-aks-ingress-subnet"
      address_prefixes : ["192.168.2.0/24"]
    }
  ]

  tags = {
    environment = "${var.environment}"
  }
}

####################################################################################################
# Azure Container Registry (Shared)
###################################################################################################

# is just queried/imported as it does not belong to this state
data "azurerm_container_registry" "shared_acr" {
  name                = "${var.prefix}acr"
  resource_group_name = data.azurerm_resource_group.shared_services_rg.name
  depends_on = [ data.azurerm_resource_group.shared_services_rg ]
}

####################################################################################################
# Azure Kubernetes Service (AKS): Service Cluster
####################################################################################################

module "aks_services" {
  source                           = "./modules/aks"
  resource_group_name              = azurerm_resource_group.default_rg.name
  location                         = azurerm_resource_group.default_rg.location
  # client_id                        = "your-service-principal-client-appid"
  # client_secret                    = "your-service-principal-client-password"
  kubernetes_version               = "1.20.7"
  orchestrator_version             = "1.20.7"
  prefix                           = "${var.prefix}-${var.environment}-aks-services"
  cluster_name                     = "${var.prefix}-${var.environment}-aks-services"
  dns_prefix                       = "${var.prefix}${var.environment}akssrv"
  network_plugin                   = "kubenet"
  public_ssh_key                   = "${var.aks_public_ssh_key}"  
  enable_role_based_access_control = true
  rbac_aad_managed                 = true
  rbac_aad_admin_user_names        = []
  rbac_aad_admin_group_object_id   = "${var.aks_admin_group_id}" 

  enable_http_application_routing  = false
  enable_azure_policy              = false
  enable_auto_scaling              = false
  node_resource_group              = "${var.prefix}-${var.environment}-node-rg"
  vnet_subnet_id                   = module.aks_vnet.subnet_ids["${var.prefix}-${var.environment}-aks-node-subnet"]
  os_disk_size_gb                  = 50
  agents_count                     = module.landscape_variables.nodecount # Please set `agents_count` `null` while `enable_auto_scaling` is `true` to avoid possible `agents_count` changes.
  agents_size                      = module.landscape_variables.vmsize
  agents_max_pods                  = 100
  agents_pool_name                 = "exnodepool"
  agents_availability_zones        = []
  agents_type                      = "VirtualMachineScaleSets"

  net_profile_pod_cidr             = "172.40.0.0/16"
  net_profile_dns_service_ip       = "172.41.0.10"
  net_profile_docker_bridge_cidr   = "172.42.0.1/16"
  net_profile_service_cidr         = "172.41.0.0/16"
  net_profile_outbound_type        = "loadBalancer"

  enable_log_analytics_workspace   = true
  log_analytics_workspace_group    = azurerm_log_analytics_workspace.shared.resource_group_name
  log_analytics_workspace_id       = azurerm_log_analytics_workspace.shared.id
  log_analytics_workspace_name     = azurerm_log_analytics_workspace.shared.name

  tags = {
    environment = "${var.environment}"
  }

  depends_on = [module.aks_vnet]
}

# add the role to the identity the kubernetes cluster was assigned
resource "azurerm_role_assignment" "aks_to_acr" {
  scope                = data.azurerm_container_registry.shared_acr.id
  role_definition_name = "AcrPull"
  principal_id         = module.aks_services.kubelet_identity.0.object_id
  depends_on=[data.azurerm_container_registry.shared_acr]
}

####################################################################################################
# NGINX Ingress with TLS
####################################################################################################

# Create static public IP Address to be used by NGINX ingress controller
resource "azurerm_public_ip" "ingress_ip" {
  name                = "${var.prefix}-${var.environment}-ingress-pip"
  location            = azurerm_resource_group.default_rg.location
  resource_group_name = "${module.aks_services.node_resource_group}"
  sku                 = "Standard"
  allocation_method   = "Static"
  domain_name_label   = "${var.prefix}${var.environment}akssrv"
  
  tags = {
    environment                  = "${var.environment}"
    kubernetes-dns-label-service = "ingress-service/ingress-service-ingress-nginx-controller"
  }
}

# Create static public IP Address to be used by the portal
resource "azurerm_public_ip" "portal_ip" {
  name                = "${var.prefix}-${var.environment}-portal-pip"
  location            = azurerm_resource_group.default_rg.location
  resource_group_name = "${module.aks_services.node_resource_group}"
  sku                 = "Standard"
  allocation_method   = "Static"
  domain_name_label   = "${var.prefix}${var.environment}aksportal"
  
  tags = {
    environment                  = "${var.environment}"
    kubernetes-dns-label-service = "ingress-portal/ingress-portal-ingress-nginx-controller"
  }
}

# create namespace for NGINX ingress controller resources
resource "kubernetes_namespace" "ingress_service_namespace" {
  metadata {
    name = "ingress-service"
  }
}

# deploy NGINX ingress controller with Helm
resource "helm_release" "nginx_ingress_service" {
  name       = "ingress-service"
  chart      = "ingress-nginx"
  namespace  = kubernetes_namespace.ingress_service_namespace.metadata[0].name
  repository = "https://kubernetes.github.io/ingress-nginx"
  timeout    = 300
  
  set {
    name  = "controller.service.loadBalancerIP"
    value = "${azurerm_public_ip.ingress_ip.ip_address}"
  }

  set {
    name = "controller.ingressClass"
    value = "service"
  }

  set {
    name = "controller.ingressClassResource.name"
    value = "service"
  }

  set {
    name  = "controller.service.annotations.\"service\\.beta\\.kubernetes\\.io/azure-load-balancer-resource-group\""
    value = "${module.aks_services.node_resource_group}"
  }

  set {
    name  = "controller.service.annotations.\"service\\.beta\\.kubernetes\\.io/azure-dns-label-name\""
    value = "${var.prefix}${var.environment}akssrv"
  }
 
  depends_on = [kubernetes_namespace.ingress_service_namespace, module.aks_services, azurerm_public_ip.ingress_ip]
}

# create a second namespace for portal NGINX ingress controller resources
resource "kubernetes_namespace" "ingress_portal_namespace" {
  metadata {
    name = "ingress-portal"
  }
}

# deploy a second NGINX ingress controller with Helm
resource "helm_release" "nginx_ingress_portal" {
  name       = "ingress-portal"
  chart      = "ingress-nginx"
  namespace  = kubernetes_namespace.ingress_portal_namespace.metadata[0].name
  repository = "https://kubernetes.github.io/ingress-nginx"
  timeout    = 300
  
  set {
    name  = "controller.service.loadBalancerIP"
    value = "${azurerm_public_ip.portal_ip.ip_address}"
  }

  set {
    name = "controller.ingressClass"
    value = "portal"
  }

  set {
    name = "controller.ingressClassResource.name"
    value = "portal"
  }
  
  set {
    name  = "controller.service.annotations.\"service\\.beta\\.kubernetes\\.io/azure-load-balancer-resource-group\""
    value = "${module.aks_services.node_resource_group}"
  }

  set {
    name  = "controller.service.annotations.\"service\\.beta\\.kubernetes\\.io/azure-dns-label-name\""
    value = "${var.prefix}${var.environment}aksportal"
  }

  depends_on = [kubernetes_namespace.ingress_portal_namespace, module.aks_services, azurerm_public_ip.portal_ip]
}

####################################################################################################
# cert-manager for TLS with Letsencrypt certificates
####################################################################################################

# create namespace for cert-manager resources
resource "kubernetes_namespace" "cert_manager_namespace" {
  metadata {
    name = "cert-manager"
    labels = {
      "cert-manager.io/disable-validation" = "true"
    }
  }
}

# Deploy cert-manager for TLS with Helm
resource "helm_release" "cert-manager" {
  name       = "cert-manager"
  chart      = "cert-manager"
  version    = "v1.6.1"

  namespace  = kubernetes_namespace.cert_manager_namespace.metadata[0].name
  repository = "https://charts.jetstack.io"
  timeout    = 300
  
  set {
    name  = "installCRDs"
    value = "true"
  }
}

####################################################################################################
# Create namespaces for services
####################################################################################################

# Connector DNS
resource "kubernetes_namespace" "cdns_namespace" {
  metadata {
    name = "cdns"
  }
}

# Catena-X Portal
resource "kubernetes_namespace" "portal_namespace" {
  metadata {
    name = "portal"
  }
}

# IDS Components
resource "kubernetes_namespace" "ids_namespace" {
  metadata {
    name = "ids"
  }
}

# Business Partner Service
resource "kubernetes_namespace" "businesspartners_namespace" {
  metadata {
    name = "businesspartners"
  }
}

# Central Parts Relationship Service
resource "kubernetes_namespace" "partsrelationship_namespace" {
  metadata {
    name = "partsrelationship"
  }
}

# Semantic Services
resource "kubernetes_namespace" "semantics_namespace" {
  metadata {
    name = "semantics"
  }
}

# Sample Connectors
resource "kubernetes_namespace" "connector_namespace" {
  metadata {
    name = "dataspace-connector"
  }
}

# IAM
resource "kubernetes_namespace" "iam_namespace" {
  metadata {
    name = "iam"
  }
}

####################################################################################################
# Create a database service
####################################################################################################

resource "azurerm_postgresql_server" "database" {
  name                = "${var.prefix}${var.environment}database"
  resource_group_name = azurerm_resource_group.default_rg.name 
  location            = azurerm_resource_group.default_rg.location  

  administrator_login          =  var.catenax_admin
  administrator_login_password =  var.catenax_admin_password

  sku_name   = "B_Gen5_1"
  version    = "11"
  storage_mb = 61440

  geo_redundant_backup_enabled = false
  auto_grow_enabled            = false

  public_network_access_enabled    = true
  ssl_enforcement_enabled          = true
  ssl_minimal_tls_version_enforced = "TLS1_2"
}

####################################################################################################
# Create a storage account
####################################################################################################

resource "azurerm_storage_account" "appstorage" {
  name                     = "${var.prefix}${var.environment}storage"
  resource_group_name      = azurerm_resource_group.default_rg.name 
  location                 = azurerm_resource_group.default_rg.location  
  account_tier             = "Standard"
  account_replication_type = "LRS"
  shared_access_key_enabled = true

  #network_rules {
  #  default_action             = "Allow"
  #  virtual_network_subnet_ids = [module.aks_vnet.subnet_ids["${var.prefix}-${var.environment}-aks-node-subnet"]]
  #}
}
