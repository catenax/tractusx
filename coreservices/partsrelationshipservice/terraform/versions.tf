terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "2.60.0"
    }

    helm = {
      source = "hashicorp/helm"
      version = "2.1.2"
    }
  }

  # Persist state in a storage account
  backend "azurerm" {
    resource_group_name  = "catenax-terraform"
    storage_account_name = "catenaxterraformstate"
    container_name       = "tfstate"
    key                  = "prs.prs.dev.terraform.tfstate"
  }

  required_version = "~> 1.0"
}

provider "azurerm" {
  features {}
}
