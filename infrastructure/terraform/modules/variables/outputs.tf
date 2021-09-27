output "nodecount" {
  value = "${var.k8s_nodecount_map[var.size]}"
}

output "vmsize" {
  value = "${var.k8s_vmsize_map[var.size]}"
}