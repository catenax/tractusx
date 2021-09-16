####################################################################################################
# PRS infrastructure
####################################################################################################

module "prs_application_insights" {
  source = "./modules/application-insights"

  name                = "${var.prefix}-${var.environment}-prs-appi"
  resource_group_name = local.resource_group_name
  location            = local.location
}

module "prs_postgresql" {
  source              = "./modules/postgresql"
  name                = "${var.prefix}-${var.environment}-prs-psql"
  database_name       = "prs"
  resource_group_name = local.resource_group_name
  location            = local.location
}

module "eventhubs_namespace" {
  source              = "./modules/eventhubs_namespace"
  name                = "${var.prefix}-${var.environment}-prs-ehub"
  resource_group_name = local.resource_group_name
  location            = local.location
}

module "eventhub_catenax_events" {
  source                  = "./modules/eventhub"
  eventhub_namespace_name = module.eventhubs_namespace.name
  name                    = "catenax_events"
  resource_group_name     = local.resource_group_name
}

# create namespace for PRS
resource "kubernetes_namespace" "prs" {
  metadata {
    name = "prs"
  }
}

# Deploy the PRS service with Helm
resource "helm_release" "prs" {
  name       = "prs"
  chart      = "../helm/prs"
  namespace  = "prs-deployed-by-terraform"
  timeout    = 300

  set {
    name = "ingress.host"
    value = "TODO"
  }

  set {
    name  = "prs.image.repository"
    value = "${var.registry}/prs"
  }

  set {
    name = "prs.image.tag"
    value = var.tag
  }

  set {
    name = "brokerproxy.image.tag"
    value = var.tag
  }

  set {
    name = "applicationInsights.connectionString"
    value = module.prs_application_insights.connection_string
  }

  set {
    name = "eventHubs.name"
    value = module.eventhub_catenax_events.eventhub_name
  }

  set {
    name = "eventHubs.namespace"
    value = module.eventhubs_namespace.name
  }

  set {
    name = "eventHubs.sendConnectionString"
    value = module.eventhub_catenax_events.send_primary_connection_string
  }

  set {
    name = "eventHubs.listenConnectionString"
    value = module.eventhub_catenax_events.listen_primary_connection_string
  }

  set {
    name = "postgresql.url"
    value = module.prs_postgresql.fqdn
  }

  set {
    name = "postgresql.postgresqlUsername"
    value = module.prs_postgresql.administrator_username
  }

  set {
    name = "postgresql.postgresqlPassword"
    value = module.prs_postgresql.administrator_login_password
  }

}
