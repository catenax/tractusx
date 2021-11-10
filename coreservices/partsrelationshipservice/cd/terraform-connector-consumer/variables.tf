variable "resource_group_name" {
  type        = string
  description = "Resource group used to deploy resources."
  default     = "catenax-dev001-rg"
}

variable "aks_cluster_name" {
  type        = string
  description = "Azure Kubernetes cluster to deploy in."
  default     = "catenax-dev001-aks-services"
}

variable "consumer_ingress_host" {
  type        = string
  description = "Ingress host to reach the consumer."
  default     = "catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com"
}

variable "ingress_class_name" {
  type        = string
  description = "Ingress class name for the given environment (\"nginx\" for DEV and \"service\" for INT)"
  default     = "nginx"
}

variable "image_registry" {
  type        = string
  description = "Registry containing connector images."
  default     = "catenaxdev001acr.azurecr.io"
}

variable "image_tag" {
  type        = string
  description = "Connector image tag that will be deployed."
}
