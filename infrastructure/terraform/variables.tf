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