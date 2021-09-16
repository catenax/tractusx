####################################################################################################
# PRS infrastructure
####################################################################################################

module "prs_application_insights" {
  source = "./modules/application-insights"

  name                = "${var.prefix}-${var.environment}-prs-appi"
  resource_group_name = azurerm_resource_group.default_rg.name
  location            = azurerm_resource_group.default_rg.location
}

module "prs_postgresql" {
  source              = "./modules/postgresql"
  name                = "${var.prefix}-${var.environment}-prs-psql"
  database_name       = "prs"
  resource_group_name = azurerm_resource_group.default_rg.name
  location            = azurerm_resource_group.default_rg.location
}

module "eventhubs_namespace" {
  source              = "./modules/eventhubs_namespace"
  name                = "${var.prefix}-${var.environment}-prs-ehub"
  resource_group_name = azurerm_resource_group.default_rg.name
  location            = azurerm_resource_group.default_rg.location
}

module "eventhub_catenax_events" {
  source                  = "./modules/eventhub"
  eventhub_namespace_name = module.eventhubs_namespace.name
  name                    = "catenax_events"
  resource_group_name     = azurerm_resource_group.default_rg.name
}

module "prs" {
  source                  = "./modules/prs"
}
