
data "azurerm_client_config" "current" {}

resource "azurerm_key_vault" "consumer-vault" {
  name                        = "${var.environment}-consumer-vault"
  location                    = var.location
  resource_group_name         = var.resource_group_name
  enabled_for_disk_encryption = false
  tenant_id                   = data.azurerm_client_config.current.tenant_id
  soft_delete_retention_days  = 7
  purge_protection_enabled    = false

  sku_name                  = "standard"
  enable_rbac_authorization = true
}

resource "azurerm_storage_account" "connector-blobstore" {
  name                     = "${var.environment}consumersa"
  resource_group_name      = var.resource_group_name
  location                 = var.location
  account_tier             = "Standard"
  account_replication_type = "GRS"
  account_kind             = "StorageV2"
}

# Primary key for the blob store.
resource "azurerm_key_vault_secret" "blobstorekey" {
  name         = "${azurerm_storage_account.connector-blobstore.name}-key"
  value        = azurerm_storage_account.connector-blobstore.primary_access_key
  key_vault_id = azurerm_key_vault.consumer-vault.id
}

# Role assignment so that the primary identity may access the vault.
resource "azurerm_role_assignment" "primary-id" {
  scope                = azurerm_key_vault.consumer-vault.id
  role_definition_name = "Key Vault Secrets Officer"
  principal_id         = var.prs_connector_consumer_object_id
}

# Role assignment so that the currently logged in user may access the vault, needed to add secrets.
resource "azurerm_role_assignment" "current-user" {
  scope                = azurerm_key_vault.consumer-vault.id
  role_definition_name = "Key Vault Secrets Officer"
  principal_id         = data.azurerm_client_config.current.object_id
}