terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "2.60.0"
    }

    azuread = {
      source  = "hashicorp/azuread"
      version = "~> 1.0"
    }

    helm = {
      source = "hashicorp/helm"
      version = "2.1.2"
    }

    kubernetes = {
      source = "hashicorp/kubernetes"
      version = "2.2.0"      
    }
  }

  backend "azurerm" {
    resource_group_name  = "terraform-rg"
    storage_account_name = "catenaxdevtfstate"
    container_name       = "tfstate"
    key                  = "catenaxdev.tfstateenv:${var.environment}"
    access_key           = var.azure_storage_access_key
  }

  required_version = "~> 1.0"
}

provider "azurerm" {
  features {}
  subscription_id = var.azure_subscription_id
  client_id       = var.azure_client_id
  client_secret   = var.azure_client_secret
  tenant_id       = var.azure_tenant_id
}
