####################################################################################################
# PRS infrastructure
####################################################################################################

data "azurerm_application_insights" "main" {
  name                = var.application_insights_name
  resource_group_name = data.azurerm_resource_group.main.name
}

module "prs_postgresql" {
  source              = "./modules/postgresql"
  name                = "${var.prefix}-${var.environment}-${var.dataspace_partition}-prs-psql"
  database_name       = "prs"
  resource_group_name = local.resource_group_name
  location            = local.location
}

module "eventhubs_namespace" {
  source              = "./modules/eventhubs_namespace"
  name                = "${var.prefix}-${var.environment}-${var.dataspace_partition}-prs-ehub"
  resource_group_name = local.resource_group_name
  location            = local.location
}

module "eventhub_catenax_events" {
  source                                     = "./modules/eventhub"
  eventhub_namespace_name                    = module.eventhubs_namespace.name
  name                                       = "catenax_events"
  resource_group_name                        = local.resource_group_name
  location                                   = local.location
  capture_storage_account_name               = "${var.prefix}${var.environment}${var.dataspace_partition}msg"
  receive_and_send_primary_connection_string = module.eventhubs_namespace.receive_and_send_primary_connection_string
}

# create namespace for PRS
resource "kubernetes_namespace" "prs" {
  metadata {
    name = "prs-${var.dataspace_partition}"
  }
}

locals {
  ingress_prefix                    = "/${var.dataspace_partition}/mtpdc"
  ingress_prefix_prs                = "${local.ingress_prefix}/prs"
  ingress_prefix_connector_provider = "${local.ingress_prefix}/connector"
  api_url                           = "https://${var.ingress_host}${local.ingress_prefix_prs}"
}

# Retrieve the Key Vault for storing generated identity information and credentials
data "azurerm_key_vault" "identities" {
  name                = "${var.prefix}-${var.environment}-prs-id"
  resource_group_name = "catenax-terraform"
}

# Retrieve the Client ID for the PRS Connector from the central Key Vault.
# Use the Consumer certificate for now.
data "azurerm_key_vault_secret" "prs_connector_consumer_client_id" {
  name         = "prs-connector-consumer-client-id"
  key_vault_id = data.azurerm_key_vault.identities.id
}

# Retrieve the Certificate for the PRS Connector from the central Key Vault.
# Use the Consumer certificate for now.
# Note that the data source is actually a Certificate in Key Vault, and not a Secret.
# However this actually works, and retrieves the Certificate base64 encoded.
# An advantage of this method is that the "Key Vault Secrets User" (read-only)
# role is then sufficient to export the certificate.
# This is documented at https://docs.microsoft.com/azure/key-vault/certificates/how-to-export-certificate.
data "azurerm_key_vault_secret" "prs_connector_consumer_certificate" {
  name         = "prs-connector-consumer-certificate"
  key_vault_id = data.azurerm_key_vault.identities.id
}

# Deploy the PRS service with Helm
resource "helm_release" "prs" {
  name      = "prs-${var.dataspace_partition}"
  chart     = "../helm/prs"
  namespace = kubernetes_namespace.prs.metadata[0].name
  timeout   = 300

  set {
    name  = "ingress.host"
    value = var.ingress_host
  }

  set {
    name  = "ingress.className"
    value = var.ingress_class_name
  }

  set {
    name  = "ingress.prefix"
    value = local.ingress_prefix_prs
  }

  set {
    name  = "prs.image.repository"
    value = "${var.image_registry}/prs-api"
  }

  set {
    name  = "prs.image.tag"
    value = var.image_tag
  }

  set {
    name  = "prs.apiUrl"
    value = local.api_url
  }

  set {
    name  = "brokerproxy.image.repository"
    value = "${var.image_registry}/broker-proxy"
  }

  set {
    name  = "brokerproxy.image.tag"
    value = var.image_tag
  }

  set_sensitive {
    name  = "applicationInsights.connectionString"
    value = data.azurerm_application_insights.main.connection_string
  }

  set {
    name  = "eventHubs.name"
    value = module.eventhub_catenax_events.eventhub_name
  }

  set {
    name  = "eventHubs.namespace"
    value = module.eventhubs_namespace.name
  }

  set_sensitive {
    name  = "eventHubs.sendConnectionString"
    value = module.eventhub_catenax_events.send_primary_connection_string
  }

  set_sensitive {
    name  = "eventHubs.receiveConnectionString"
    value = module.eventhub_catenax_events.receive_primary_connection_string
  }

  set {
    name  = "postgresql.url"
    value = "jdbc:postgresql://${module.prs_postgresql.fqdn}/${module.prs_postgresql.db_name}?sslmode=require"
  }

  set {
    name  = "postgresql.postgresqlUsername"
    value = module.prs_postgresql.administrator_username
  }

  set_sensitive {
    name  = "postgresql.postgresqlPassword"
    value = module.prs_postgresql.administrator_login_password
  }

  # Use set_sensitive since the value is already marked as sensitive by Terraform,
  # as it comes from Key Vault. Otherwise, all set { } variables would be hidden
  # from the Terraform plan display.
  set_sensitive {
    name  = "edc.vault.clientId"
    value = data.azurerm_key_vault_secret.prs_connector_consumer_client_id.value
  }

  set {
    name  = "edc.vault.tenantId"
    value = data.azurerm_key_vault.identities.tenant_id
  }

  set {
    name  = "edc.vault.name"
    value = data.azurerm_key_vault.consumer-vault.name
  }

  set_sensitive {
    name  = "identity.certificateBase64"
    value = data.azurerm_key_vault_secret.prs_connector_consumer_certificate.value
  }
}

# Deploy the PRS Provider with Helm
resource "helm_release" "prs-connector-provider" {
  name      = "prs-${var.dataspace_partition}-prs-connector-provider"
  chart     = "../helm/prs-connector-provider"
  namespace = kubernetes_namespace.prs.metadata[0].name
  timeout   = 300

  set {
    name  = "ingress.host"
    value = var.ingress_host
  }

  set {
    name  = "ingress.className"
    value = var.ingress_class_name
  }

  set {
    name  = "ingress.prefix"
    value = local.ingress_prefix_connector_provider
  }

  set {
    name  = "image.repository"
    value = "${var.image_registry}/prs-connector-provider"
  }

  set {
    name  = "image.tag"
    value = var.image_tag
  }

  set_sensitive {
    name  = "applicationInsights.connectionString"
    value = data.azurerm_application_insights.main.connection_string
  }

  set {
    name  = "prs.apiUrl"
    value = local.api_url
  }
}
