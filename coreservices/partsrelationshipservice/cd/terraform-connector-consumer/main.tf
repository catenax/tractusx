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
