# Create one consumer and one provider. This is a temporary set up.
# Later on, providers will be deployed in terraform-dataspace-partition as we will have one provider per partition.
# The consumer will use terraform-dataspace-partition output to be aware of all the provider urls.

resource "kubernetes_namespace" "prs-connectors" {
  metadata {
    name = "prs-connectors"
  }
}

data "azurerm_application_insights" "main" {
  name                = var.application_insights_name
  resource_group_name = data.azurerm_resource_group.main.name
}

data "azurerm_key_vault" "consumer-vault" {
  name                = "${var.prefix}-${var.environment}-consumer"
  resource_group_name = var.resource_group_name
}

# Retrieve the Key Vault for storing generated identity information and credentials
data "azurerm_key_vault" "identities" {
  name                = "${var.prefix}-${var.environment}-prs-id"
  resource_group_name = "catenax-terraform"
}

# Retrieve the prs_connector_consumer_object_id secret.
data "azurerm_key_vault_secret" "prs_connector_consumer_client_id" {
  name         = "prs-connector-consumer-client-id"
  key_vault_id = data.azurerm_key_vault.identities.id
}

data "azurerm_key_vault_certificate" "prs_connector_consumer_cert" {
  name         = "generated-cert"
  key_vault_id = data.azurerm_key_vault.identities.id
}

# Deploy the PRS Consumer with Helm
resource "helm_release" "prs-connector-consumer" {
  name      = "prs-connector-consumer"
  chart     = "../helm/prs-connector-consumer"
  namespace = kubernetes_namespace.prs-connectors.metadata[0].name
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
    value = "/prs-connector-consumer"
  }

  set {
    name  = "image.repository"
    value = "${var.image_registry}/prs-connector-consumer"
  }

  set {
    name  = "image.tag"
    value = var.image_tag
  }

  set {
    name = "edc.vault.clientid"
    value = data.azurerm_key_vault_secret.prs_connector_consumer_client_id.value
  }

  set {
    name = "edc.vault.tenantid"
    value = data.azurerm_key_vault.identities.tenant_id
  }

  set {
    name = "edc.vault.name"
    value = data.azurerm_key_vault.consumer-vault.name
  }

  set {
    name = "edc.storage.account.name"
    value = "${var.prefix}${var.environment}consumer"
  }

  set_sensitive {
    name = "identity.certificate"
    value = data.azurerm_key_vault_certificate.prs_connector_consumer_cert.certificate_data_base64
  }

  set_sensitive {
    name  = "applicationInsights.connectionString"
    value = data.azurerm_application_insights.main.connection_string
  }
}

# Deploy the PRS Provider with Helm
resource "helm_release" "prs-connector-provider" {
  name      = "prs-connector-provider"
  chart     = "../helm/prs-connector-provider"
  namespace = kubernetes_namespace.prs-connectors.metadata[0].name
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
    value = "/prs-connector-provider"
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
    value = var.prs_api_url
  }
}

module "connector_storage" {
  source              = "./modules/connector_storage"
  environment         = var.environment
  location            = local.location
  prefix              = var.prefix
  resource_group_name = var.resource_group_name
}
