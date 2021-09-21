provider "kubernetes" {
  host                   = module.aks_services.kube_admin_config.0.host
  username               = module.aks_services.kube_admin_config.0.username
  password               = module.aks_services.kube_admin_config.0.password
  client_key             = base64decode(module.aks_services.kube_admin_config.0.client_key)
  client_certificate     = base64decode(module.aks_services.kube_admin_config.0.client_certificate)
  cluster_ca_certificate = base64decode(module.aks_services.kube_admin_config.0.cluster_ca_certificate)
}

provider "helm" {
    debug = true
    kubernetes {
        host     = module.aks_services.kube_admin_config.0.host
        client_key             = base64decode(module.aks_services.kube_admin_config.0.client_key)
        client_certificate     = base64decode(module.aks_services.kube_admin_config.0.client_certificate)
        cluster_ca_certificate = base64decode(module.aks_services.kube_admin_config.0.cluster_ca_certificate)
    }  
}
