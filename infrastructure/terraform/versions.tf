terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "2.77.0"
    }

    azuread = {
      source  = "hashicorp/azuread"
      version = "2.3.0"
    }

    helm = {
      source = "hashicorp/helm"
      version = "2.3.0"
    }

    kubernetes = {
      source = "hashicorp/kubernetes"
      version = "2.5.0"
    }
  }

  backend "azurerm" {
    resource_group_name  = "terraform-rg"
    storage_account_name = "cxtsidevtfstate"
    container_name       = "tfstate"
    key                  = "cxtsidev.tfstate"
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

