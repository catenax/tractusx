variable "resource_group_name" {
  type        = string
  description = "Resource group used to deploy resources."
  default     = "catenax-dev001-rg"
}

variable "environment" {
  description = "identifying string that is used in all azure resources"
  default     = "dev"
}

variable "prs_connector_consumer_object_id" {
  type = string
}

variable "location" {
  type        = string
  description = "Azure region where to create resources."
}
