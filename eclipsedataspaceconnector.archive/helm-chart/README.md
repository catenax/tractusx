# edc

![Version: 0.2.0](https://img.shields.io/badge/Version-0.2.0-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: 6.3.0](https://img.shields.io/badge/AppVersion-6.3.0-informational?style=flat-square)

A Helm chart for [IDS Dataspace Connector](https://github.com/International-Data-Spaces-Association/DataspaceConnector/tree/main/charts/edc).

## How-to

Predefined configuration allows you to start dataspace connector instance with one simple command:

```bash
helm install <release_name> .
```

Connector will start with selfsigned certificates in test mode without persistence.

## Running in production mode

To start connector in productive mode you should made a few changes in configuration.

### Certificate

Certificate is the essential part of configuration that allow you to register dataspace connector inside DAPS server. Certificates can be configured with 3 different ways.

***Cert-manager***

If you have cert-manager installed in your Kubernetes cluster, you can use it to issue a certificate. Provide `custom-values.yml` file with the following:

```yml
certificate:
  dnsName: yourname.example.com
  certManagerCert:
    enabled: false
    issuer:
      kind: ClusterIssuer
      name: letsencrypt-staging
env:
  secrets:
    KEYSTORE_PASSWORD: very_secret_pass
```

Create a release with command:

```bash
helm install -f custom-values.yml <release_name> .
```

***Selfsigned certificates***

You can generate private key using helm and then create certificate using init-container. Provide `custom-values.yml` file with following configuration:

```yml
certificate:
  dnsName: yourname.example.com
  selfSignedCert:
    enabled: true
env:
  secrets:
    KEYSTORE_PASSWORD: very_secret_pass
```

Create a release with command:

```bash
helm install -f custom-values.yml <release_name> .
```

***Using existing certificate***

If you already have existing keystore and truststore files, you can provide it using helm's `--set-file` flag and passing passwords with `custom-values.yml` file:

```yml
certificate:
  dnsName: yourname.example.com
env:
  secrets:
    KEYSTORE_PASSWORD: very_secret_pass
```

Create release with command:

```bash
helm install --set-file 'keystore\.p12'=<path_to_keystore> \
             -f custom-values.yml <release_name> .
```

### Truststore

Make sure to provide a truststore file that contain trusted CA of DAPS, broker and other services. Create a Kubernetes secret with this truststore file and then configure helm chart to use it with `custom-values.yml`:

```yml
certificate:
  truststore:
    secretName: "secretname"
    secretKey: "truststore.p12"
env:
  secrets:
    TRUSTSTORE_PASSWORD: very_secret_pass
```

Create a release with command:

```bash
helm install -f custom-values.yml <release_name> .
```

### Enabling persistence

To deploy PostgreSQL alongside with connector, provide following configuration inside `custom-values.yml`:

```yml
postgresql:
  enabled: true
  postgresqlUsername: "password"
  postgresqlPassword: "username"
  postgresqlDatabase: "test"
  service:
    port: "5432"
```

### Registration in DAPS

After successful instance startup, you have to register you conncetor in DAPS server. Connectors extract it's ID from certificate file using x509 SKI and AKI extension; DAPS using certificate to validate incoming tokens using public key from it. There is a command, which will give you all necessary values for registration:

```bash
kubectl -n <your_namespace> logs -c init-adjust-config $(kubectl get pod -l app.kubernetes.io/instance=<release_name> -o jsonpath="{.items[0].metadata.name}")
```

### Exposing with ingress

If you have an ingress controller setted up in your Kubernetes cluster and you want to expose the connector, you can simply configure it with the parameters below. Please note that DNS will be used from `certificate.dnsName` section from values.yml, check [Certificate](#certificate) section for configuration examples.

```yaml
ingress:
  enabled: false
  tls:
    enabled: false
    secretName: "secret-name-that-contain-tls-key-and-crt"
    certMgr: {}
      # kind: "cluster-issuer or issuer"  
      # issuer: "issuer name"
```

## Requirements

| Repository | Name | Version |
|------------|------|---------|
| https://charts.bitnami.com/bitnami | postgresql | 10.4.6 |

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| "keystore.p12" | string | `""` | Keystore file can be provided with `--set-file 'keystore\.p12'=<path_to_your_keystore_file>` |
| "truststore.p12" | string | `""` | Truststore file can be provided with `--set-file 'truststore\.p12'=<path_to_your_truststore_file>` |
| affinity | object | `{}` | Affinity configuration |
| certificate | object | `{"certManagerCert":{"enabled":false, "issuer":{"kind":"ClusterIssuer", "name":"letsencrypt-staging"}}, "dnsName":"localhost", "selfSignedCert":{"enabled":false}, "truststore":{"passwordSecretKey":"password", "passwordSecretName":"", "secretKey":"truststore.p12", "secretName":""}}` | Connector certificate settings. If certManagerCert.enabled and selfSignedCert.enabled both set to `false`, then existing certificate will be used. Check "truststore.p12" and "keystore.p12" sections description for configuring existing certificates |
| certificate.certManagerCert | object | `{"enabled":false, "issuer":{"kind":"ClusterIssuer", "name":"letsencrypt-staging"}}` | If `certManagerCert.enabled` set to `true` certificate will be issued with cert-manager. |
| certificate.certManagerCert.issuer.kind | string | `"ClusterIssuer"` | Cert-manager issuer type |
| certificate.certManagerCert.issuer.name | string | `"letsencrypt-staging"` | Cert-manager issuer name |
| certificate.dnsName | string | `"localhost"` | DNS name for which certificate is issued |
| certificate.selfSignedCert | object | `{"enabled":false}` | If `enabled` set to `true`, helm will generate private key which is placed in kubernetes secret. On pod' startup init container will create self-signed certificate using private key. |
| certificate.truststore | object | `{"passwordSecretKey":"password", "passwordSecretName":"", "secretKey":"truststore.p12", "secretName":""}` | Fill this section if you want to provide existing truststore -- TODO: merge provided truststore with certificate truststore |
| env.config.CERTIFICATE_PATH | string | `"/var/run/certs/"` | Path to directory with certificates |
| env.config.CONFIGURATION_DIR | string | `"/etc/edc/"` | Path to directory where all configuration files are stored |
| env.config.CONFIGURATION_PATH | string | `"/etc/edc/config.json"` | Path to `config.json` file |
| env.config.DAPS_KEY_URL | string | `"https://daps.aisec.fraunhofer.de/.well-known/jwks.json"` | URI to DAPS server jwks keys endpoint |
| env.config.DAPS_KEY_URL_KID | string | `"{'https://daps.aisec.fraunhofer.de/.well-known/jwks.json':'default'}"` | URI to DAPS server jwkd keys endpoin |
| env.config.DAPS_TOKEN_URL | string | `"https://daps.aisec.fraunhofer.de/token"` | URI to DAPS server token endpoint |
| env.config.DAPS_URL | string | `"https://daps.aisec.fraunhofer.de/"` | URI to DAPS server |
| env.config.DEPLOY_MODE | string | `"idsc:TEST_DEPLOYMENT"` | Connectore deploy mode. Can be `idsc:PRODUCTIVE_DEPLOYMENT` or `idsc:TEST_DEPLOYMENT` |
| env.config.KEYSTORE_FILENAME | string | `"keystore.p12"` |  |
| env.config.LOGGING_CONFIG | string | `"file:///etc/edc/log4j2.xml"` | Path to `log4j2.xml` to configure logging. File will be mounted to `CONFIGURATION_DIR` folder. |
| env.config.SERVER_SSL_ENABLED | bool | `false` | Enables SSL if `true`. Disables SSL if `false`. |
| env.config.SPRING_CONFIG_LOCATION | string | `"file:///etc/edc/application.properties"` | Path to `application.properties` file. File will be mounted to `CONFIGURATION_DIR` folder. |
| env.config.TRUSTSTORE_FILENAME | string | `"truststore.p12"` |  |
| env.secrets.KEYSTORE_PASSWORD | string | `"password"` | Password to use with provided `keystore.p12` file. Password for default keystore is `password` |
| env.secrets.SPRING_SECURITY_APP_PASSWORD | string | `"password1"` | Password to access connector api |
| env.secrets.SPRING_SECURITY_USER_PASSWORD | string | `"password1"` | Username to access connector api |
| env.secrets.TRUSTSTORE_PASSWORD | string | `"password"` | Password to use with provided `truststore.p12` file. Password for default truststore is `password` |
| image.pullPolicy | string | `"Always"` | Pod pull policy |
| image.repository | string | `"ghcr.io/international-data-spaces-association/edc"` | Dataspace connector Docker image |
| image.tag | string | `""` | Dataspcace connector Docker image tag. If not set, Chart AppVersion will be used |
| imagePullSecrets | list | `[]` | Secret with dockerconfig.json file to pull image from private container registry |
| ingress.annotations | object | `{}` | Ingress additional annotations |
| ingress.className | string | `"service"` | Ingress class name |
| ingress.enabled | bool | `false` | If set to `false`, ingress will not be configured. If set to `true`, service will be exposed with `certificate.dnsName` name |
| ingress.pathType | string | `"ImplementationSpecific"` | Ingress path type |
| ingress.rootPath | string | `""` | Path prefix for ingress configuration. If empty then `/` path will be used |
| ingress.tls.certMgr | object | `{}` | If configured, cert-manager will be used to issue a certificate |
| ingress.tls.enabled | bool | `false` |  |
| ingress.tls.secretName | string | `""` | If not set then chart full name will be used |
| nodeSelector | object | `{}` | Node selector labels |
| podAnnotations | object | `{}` | Additional pod annotations |
| podSecurityContext | object | `{}` | Pod security context configuration |
| postgresql.enabled | bool | `false` | If set to true, postgres sts will be deployed and configured to use with connector. If set to false, connector will use internal h2 database. |
| postgresql.postgresqlDatabase | string | `"test"` | PostgreSQL instance DB name |
| postgresql.postgresqlPassword | string | `"username"` | PostgreSQL instance password |
| postgresql.postgresqlUsername | string | `"password"` | PostgreSQL instance username |
| postgresql.service.port | string | `"5432"` | PostgreSQL instance service port |
| remoteDebugEnabled | bool | `false` | Remote debug options. If set to true, port 5005 will be exposed. |
| replicaCount | int | `1` | Connector replicas count |
| resources | object | `{}` | Resources usege and limits configuration |
| securityContext | object | `{}` | Security context configuration |
| service.port | int | `8080` | Service port |
| service.type | string | `"ClusterIP"` | Service type |
| tolerations | list | `[]` | Pod toleration configuration |

----------------------------------------------
Autogenerated from chart metadata using [helm-docs v1.5.0](https://github.com/norwoodj/helm-docs/releases/v1.5.0)
