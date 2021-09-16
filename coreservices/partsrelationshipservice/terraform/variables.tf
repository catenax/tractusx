####################################################################################################
# Global variables
####################################################################################################

variable "prefix" {
  type    = string
  default = "catenaprs1"
}

variable "environment" {
  type    = string
  default = "dev"
}

variable "resource_group_name" {
  type = string
}

variable "aks_cluster_name" {
  type = string
}

variable "image_registry" {
  type = string
}

variable "image_tag" {
  type = string
}

variable "release_name" {
  type = string
}

variable "ingress_host" {
  type = string
}
