# metadata-broker-open-core

![Version: 0.1.0](https://img.shields.io/badge/Version-0.1.0-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: latest](https://img.shields.io/badge/AppVersion-latest-informational?style=flat-square)

A Meta-data broker Helm chart for Kubernetes

## Requirements

| Repository | Name | Version |
|------------|------|---------|
| file://../broker-fuseki | broker-fuseki |  |
| https://helm.elastic.co | broker-elasticsearch(elasticsearch) | 7.15.0 |

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| affinity | object | `{}` | Affinity settings |
| autoscaling | object | `{"enabled":false,"maxReplicas":100,"minReplicas":1,"targetCPUUtilizationPercentage":80}` | Autoscaling settings |
| broker-elasticsearch.address | string | `""` |  |
| broker-elasticsearch.deploy | bool | `true` |  |
| broker-elasticsearch.replicas | int | `1` |  |
| broker-fuseki.address | string | `""` |  |
| broker-fuseki.deploy | bool | `true` |  |
| broker.core.debugOptions | string | `""` | If not null, debug will be enabled with exposed port. Example: debugOptions: -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 |
| broker.truststore | object | `{"p12":"","password":"changeit"}` | Truststore settings |
| broker.truststore.p12 | string | `""` | Truststore with additional certificates to be trusted (e.g. DAPS certificate) |
| broker.truststore.password | string | `"changeit"` | Truststore password |
| env.config | object | `{"COMPONENT_CATALOG_URI":"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/broker/connectors/","COMPONENT_URI":"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/broker/","DAPS_URL":"https://catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com/daps/token","DAPS_VALIDATEINCOMING":"true","ELASTICSEARCH_HOSTNAME":"elasticsearch-master","ELASTICSEARCH_PORT":"9200","INFOMODEL_VALIDATEWITHSHACL":"true","JWKS_TRUSTEDHOSTS":"catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com","SPARQL_URL":"http://{{ .Release.Name }}-broker-fuseki:3030/connectorData"}` | Environment variables that allow to configure application. Please check broker [repo](https://github.com/International-Data-Spaces-Association/metadata-broker-open-core) |
| env.secret | object | `{"KEYSTORE_ALIAS":"1","KEYSTORE_PASSWORD":"changeme"}` | Environment variables that will stored in Kubernetes secret |
| env.secret.KEYSTORE_ALIAS | string | `"1"` | Alias for keypair inside generated keystore |
| env.secret.KEYSTORE_PASSWORD | string | `"changeme"` | Password for generated keystore |
| fullnameOverride | string | `""` | Override fullname of the release |
| image.brokerCoreImage | string | `"catenaxdev001acr.azurecr.io/ids-metadata-broker"` | Metadata-broker image  |
| image.brokerCoreImageTag | string | `"latest"` | Metadata-broker image tag |
| image.pullPolicy | string | `"IfNotPresent"` | Image pull policy settings |
| imagePullSecrets | list | `[{"name":"regcred"}]` | Secret with docketconfig.json to access repository |
| ingress.annotations | object | `{"kubernetes.io/ingress.class":"nginx","nginx.ingress.kubernetes.io/rewrite-target":"/$2","nginx.ingress.kubernetes.io/use-regex":"true"}` | Ingress annotations |
| ingress.enabled | bool | `true` | Allow to enable and disable ingress |
| ingress.host | string | `"catenaxdev001akssrv.germanywestcentral.cloudapp.azure.com"` | Ingress host |
| ingress.paths | list | `[{"path":"/broker(/|$)(.*)","pathType":"Prefix","portNumber":8080,"serviceName":""}]` | Paths configuration |
| ingress.rootPath | string | `""` | Define path prefix starting with /, e.g. /broker |
| ingress.tls | object | `{"certMgr":{"enabled":false,"issuer":"letsencrypt-prod"},"enabled":true,"secretName":""}` | TLS settings |
| ingress.tls.certMgr.enabled | bool | `false` | If enabled cert-manager will be used to issue ingress cert |
| ingress.tls.certMgr.issuer | string | `"letsencrypt-prod"` | Cert-manager issuer name |
| ingress.tls.secretName | string | `""` | Release name will be used as a secretName if this field is empty |
| nameOverride | string | `""` | Override default chart name |
| nodeSelector | object | `{}` | Node selector settings |
| podAnnotations | object | `{}` | Additional pod annotations |
| podSecurityContext | object | `{}` | Pod security context |
| replicaCount | int | `1` | Replicas count |
| resources | object | `{}` | Resources usage and limits |
| securityContext | object | `{}` | Security context |
| service | object | `{"port":80,"type":"ClusterIP"}` | Service settings |
| serviceAccount.annotations | object | `{}` | Annotations to add to the service account |
| serviceAccount.create | bool | `false` | Specifies whether a service account should be created |
| serviceAccount.name | string | `""` | The name of the service account to use. -- If not set and create is true, a name is generated using the fullname template |
| tolerations | list | `[]` | Toleration settings |

----------------------------------------------
Autogenerated from chart metadata using [helm-docs v1.5.0](https://github.com/norwoodj/helm-docs/releases/v1.5.0)
