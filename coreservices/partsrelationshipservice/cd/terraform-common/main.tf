####################################################################################################
# PRS common infrastructure
####################################################################################################

module "prs_application_insights" {
  source = "./modules/application-insights"

  name                = "${var.prefix}-${var.environment}-prs-appi"
  resource_group_name = local.resource_group_name
  location            = local.location
}

# Create one consumer and one provider. This is a temporary set up.
# Later on, providers will be deployed in terraform-dataspace-partition as we will have one provider per partition.
# The consumer will have its own terraform and will use terraform-dataspace-partition output to be aware of all the
# provider urls.

resource "kubernetes_namespace" "prs-connectors" {
  metadata {
    name = "prs-connectors"
  }
}

# Deploy the PRS Consumer with Helm
resource "helm_release" "prs-consumer" {
  name      = "prs-consumer"
  chart     = "../helm/prs-consumer"
  namespace = kubernetes_namespace.prs-connectors.metadata[0].name
  timeout   = 300

  set {
    name  = "ingress.host"
    value = var.consumer_ingress_host
  }

  set {
    name  = "ingress.className"
    value = var.ingress_class_name
  }

  set {
    name  = "ingress.prefix"
    value = "/prsconsumer"
  }

  set {
    name  = "image.repository"
    value = "${var.image_registry}/prs-connector-consumer"
  }

  set {
    name  = "image.tag"
    value = var.image_tag
  }
}

# Deploy the PRS Provider with Helm
resource "helm_release" "prs-provider" {
  name      = "prs-provider"
  chart     = "../helm/prs-provider"
  namespace = kubernetes_namespace.prs-connectors.metadata[0].name
  timeout   = 300

  set {
    name  = "image.repository"
    value = "${var.image_registry}/prs-connector-provider"
  }

  set {
    name  = "image.tag"
    value = var.image_tag
  }
}