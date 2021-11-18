variable "prefix" {
  type        = string
  description = "First part of name prefix used in naming resources. Use only lowercase letters and numbers."
  default     = "cxmtpdc1"
}

variable "environment" {
  type        = string
  description = "Second part of name prefix used in naming resources. Use only lowercase letters and numbers."
  default     = "dev"
}

variable "resource_group_name" {
  type        = string
  description = "Resource group used to deploy resources."
  default     = "catenax-terraform"
}

variable "terraform_cd_principal_client_id" {
  type        = string
  description = "Client ID (Application ID) of the service principal used in Terraform Deployment GitHub Action (AZURE_CREDENTIALS GitHub secret)."
  default     = "bcbeb0a5-c079-4b7b-a9d5-8e87de3958fc"
}

variable "developers_group_object_id" {
  type        = string
  description = "Object ID of the Azure AD Group for PRS developers, to be granted read access to the identities Key Vault."
  default     = "6d96ded6-3a62-4ac6-90f9-6025282e7af9"
}
