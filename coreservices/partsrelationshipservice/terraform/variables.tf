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

variable "location" {
  type    = string
  default = "germanywestcentral"
}

variable "kubernetes_host" {
  type = string
}
variable "kubernetes_client_key_base64" {
  type = string
}
variable "kubernetes_client_certificate_base64" {
  type = string
}
variable "kubernetes_cluster_ca_certificate_base64" {
  type = string
}
