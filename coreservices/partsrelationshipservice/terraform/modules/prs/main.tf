# create namespace for PRS
resource "kubernetes_namespace" "prs" {
  metadata {
    name = "prs"
  }
}

# deploy NGINX chart with Helm
resource "helm_release" "dummy" {
  name       = "dummy"
  chart      = "bitnami/rabbitmq"
  namespace  = kubernetes_namespace.prs.metadata[0].name
  repository = "https://charts.bitnami.com/bitnami"
  timeout    = 300
  
  set {
    name  = "dummy"
    value = "dummy"
  }
}
