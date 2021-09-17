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

  required_version = "~> 0.14"
}

provider "azurerm" {
  features {}
}
