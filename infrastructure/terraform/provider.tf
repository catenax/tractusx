variable "aks_host" {
  description = "Kubernetes Host. Use module.aks_services.kube_admin_config if not set."
  type = string
  default = ""
}

variable "aks_user" {
  description = "Kubernetes User. Use module.aks_services.kube_admin_config if not set."
  type = string
  default = ""
}

variable "aks_password" {
  description = "Kubernetes Password. Use module.aks_services.kube_admin_config if not set."
  type = string
  default = ""
}

variable "aks_client_key" {
  description = "Kubernetes Client Key. Use module.aks_services.kube_admin_config if not set."
  type = string
  default = ""
}

variable "aks_client_certificate" {
  description = "Kubernetes Client Certificate. Use module.aks_services.kube_admin_config if not set."
  type = string
  default = ""
}

variable "aks_cluster_certificate" {
  description = "Kubernetes Cluster Certificate. Use module.aks_services.kube_admin_config if not set."
  type = string
  default = ""
}

provider "kubernetes" {
  host                   = ( var.aks_host != "") ?  var.aks_host : module.aks_services.kube_admin_config.0.host
  username               = ( var.aks_user != "") ?  var.aks_user : module.aks_services.kube_admin_config.0.username
  password               = ( var.aks_password != "") ?  var.aks_password : module.aks_services.kube_admin_config.0.password 
  client_key             = base64decode(( var.aks_client_key != "") ?  var.aks_client_key : module.aks_services.kube_admin_config.0.client_key)
  client_certificate     = base64decode(( var.aks_client_certificate != "") ?  var.aks_client_certificate : module.aks_services.kube_admin_config.0.client_certificate)
  cluster_ca_certificate = base64decode(( var.aks_cluster_certificate != "") ?  var.aks_cluster_certificate : module.aks_services.kube_admin_config.0.cluster_ca_certificate)
}

provider "helm" {
    debug = true
    kubernetes {
      host                   = ( var.aks_host != "") ? var.aks_host :  module.aks_services.kube_admin_config.0.host
      client_key             = base64decode(( var.aks_client_key != "" ) ?  var.aks_client_key : module.aks_services.kube_admin_config.0.client_key)
      client_certificate     = base64decode(( var.aks_client_certificate != "") ?  var.aks_client_certificate : module.aks_services.kube_admin_config.0.client_certificate)
      cluster_ca_certificate = base64decode(( var.aks_cluster_certificate != "") ?  var.aks_cluster_certificate : module.aks_services.kube_admin_config.0.cluster_ca_certificate)
    }  
}
