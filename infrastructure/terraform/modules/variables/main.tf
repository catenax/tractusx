variable "k8s_nodecount_map" {
  description = "Number of nodes in the AKS cluster"
  type = map
  default = {
    small        = "1"
    smallmedium  = "2"
    medium       = "3"
    large        = "5"
  }
}

variable "k8s_vmsize_map" {
  description = "VM size of the nodes in the AKS cluster"
  type = map
  default = {
    small        = "Standard_D2s_v3"
    smallmedium  = "Standard_D2s_v3"
    medium       = "Standard_D4s_v4"
    large        = "Standard_D4s_v4"
  }
}