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

  # Persist state in a storage account
  backend "azurerm" {
    resource_group_name  = "catenax-terraform"
    storage_account_name = "catenaxterraformstate"
    container_name       = "tfstate"
    key                  = "dev.terraform.tfstate"
  }

  required_version = "~> 1.0"
}

provider "azurerm" {
  features {}
}
