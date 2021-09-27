{{/*
Expand the name of the chart.
*/}}
{{- define "dataspace-connector.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "dataspace-connector.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "dataspace-connector.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "dataspace-connector.labels" -}}
helm.sh/chart: {{ include "dataspace-connector.chart" . }}
{{ include "dataspace-connector.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "dataspace-connector.selectorLabels" -}}
app.kubernetes.io/name: {{ include "dataspace-connector.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "dataspace-connector.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "dataspace-connector.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Set environment variables
*/}}
{{- define "dataspace-connector.env-variables" -}}
{{- $name := include "dataspace-connector.fullname" .}}
{{- range $key, $value := .Values.env.config }}
- name: {{ $key }}
  valueFrom:
    configMapKeyRef:
      name: {{ $name }}
      key: {{ $key }}
{{- end }}
{{- if .Values.postgresql.enabled }}
{{- range $i, $key := list "SPRING_JPA_DATABASE_PLATFORM" "SPRING_DATASOURCE_DRIVER_CLASS_NAME" "SPRING_DATASOURCE_URL" "SPRING_DATASOURCE_PLATFORM" }}
- name: {{ $key }}
  valueFrom:
    configMapKeyRef:
      name: {{ $name }}
      key: {{ $key }}
{{ end -}}
{{- end }}
{{- range $key, $value := .Values.env.secrets }}
- name: {{ $key }}
  valueFrom:
    secretKeyRef:
      name: {{ $name }}
      key: {{ $key }}
{{- end }}
{{- if .Values.postgresql.enabled }}
{{- range $i, $key := list "SPRING_DATASOURCE_PASSWORD" "SPRING_DATASOURCE_USERNAME" }}
- name: {{ $key }}
  valueFrom:
    secretKeyRef:
      name: {{ $name }}
      key: {{ $key }}
{{ end -}}
{{- end }}
{{- end }}

{{/*
Generate private key
*/}}
{{- define "gen.secret" -}}
{{- $secret := lookup "v1" "Secret" .Release.Namespace (include "dataspace-connector.fullname" .) -}}
{{- if $secret -}}
{{/*
   Reusing existing secret data
*/}}
tls.key: {{ $secret.data.key }}
{{- else -}}
{{/*
    Generate new data
*/}}
{{- $key := genPrivateKey "rsa" }}
tls.key: {{ $key | b64enc }}
{{- end -}}
{{- end -}} 

{{- define "protocol" }}
{{- if .Values.ingress.enabled }}
  {{- if .Values.ingress.tls.enabled }}
    {{- printf "https" }}
  {{- else }}
    {{- printf "http" }}
  {{- end }}
{{- else }}
    {{- if .Values.env.config.SERVER_SSL_ENABLED }}
  {{- printf "https" }}
  {{- else }}
    {{- printf "http" }}
  {{- end }}
{{- end }}
{{- end }}
