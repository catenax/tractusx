# Landscapes connectors

This directory contains `values.yml` files which is used to configure helm release for each connectors

## Prerequisites

Make sure to provide Kubernetes entities that are not managed by helm-chart (e.g. cert-manager issuer, ingress certificate, etc.) before installing a chart.

## Folder structure

    .
    ├── shared.yml                 # Common configuration for all catena-x connectors. 
    ├── <landscape_name>           # Directory that contains configuration for landscape and it's releases.
    │   ├── landscape.yml          # Common configuration for one landscape.
    │   └── <release_name>         # Directory that contains configuration for only one connector instance.
    │       ├── values.yml         # Configuration values for connector instance
    │       └── secrets.yml        # Encrypted configuration values for connector instance
    └── ...

## Installing a chart

Make sure that you are in the main project folder. To create new release run the following command.

```bash
cd connector
helm install -f landscape/shared.yml -f landscape/<landscape_name>/landscape.yml -f landscape/<landscape_name>/<release_name>/values.yml -f landscape/<landscape_name>/<release_name>/secrets.yml <release_name> helm-chart/
```

## Working with secrets

Ansible vault is used to encrypt/decrypt secrets.
TODO: Add ansible-vault key to github action repository

### Encrypt


```bash
ansible-vault encrypt landscape/<landscape_name>/<release_name>/secrets.yml
```

### Decrypt


```bash
ansible-vault decrypt landscape/<landscape_name>/<release_name>/secrets.yml
```
