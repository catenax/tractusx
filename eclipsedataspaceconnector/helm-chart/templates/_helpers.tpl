{{/*
Expand the name of the chart.
*/}}
{{- define "edc.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "edc.fullname" -}}
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
{{- define "edc.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "edc.labels" -}}
helm.sh/chart: {{ include "edc.chart" . }}
{{ include "edc.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "edc.selectorLabels" -}}
app.kubernetes.io/name: {{ include "edc.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "edc.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "edc.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{/*
Set environment variables
*/}}
{{- define "edc.env-variables" -}}
{{- $name := include "edc.fullname" .}}
{{- range $key, $value := .Values.env.config }}
- name: {{ $key }}
  valueFrom:
    configMapKeyRef:
      name: {{ $name }}
      key: {{ $key }}
{{- end }}
{{- range $key, $value := .Values.env.secrets }}
- name: {{ $key }}
  valueFrom:
    secretKeyRef:
      name: {{ $name }}
      key: {{ $key }}
{{- end }}
{{- end }}

{{/*
Set java options
*/}}
{{- define "edc.java-options" -}}
{{- range $key, $value := .Values.javaOptions }}
- "-D{{ $key }}={{ $value }}"
{{- end }}
{{- end }}

{{/*
Generate private key
*/}}
{{- define "gen.secret" -}}
{{- $secret := lookup "v1" "Secret" .Release.Namespace (include "edc.fullname" .) -}}
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
