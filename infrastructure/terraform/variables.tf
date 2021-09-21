####################################################################################################
# Global variables
####################################################################################################

variable "prefix" {
  type    = string
  default = "catenacax1"
}

variable "location" {
  type    = string
  default = "germanywestcentral"
}

variable "environment" {
  type    = string
  description = "Environment: dev<nnn>, int or prod"
  default = "dev001"
}

variable "azure_subscription_id" {
  description = "Subscription ID of the deployment principal"
  type = string
}

variable "azure_client_id" {
  description = "Client ID of the deployment principal"
  type = string
}

variable "azure_client_secret" {
  description = "Client Secret of the deployment principal"
  type = string
}

variable "azure_tenant_id" {
  description = "Tenant ID of the deployment principal"
  type = string
}