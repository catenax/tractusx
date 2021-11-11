using System.Collections.Generic;
using System.Text.Json.Serialization;

namespace CatenaX.NetworkServices.Provisioning.ActiveDirectory.Model
{
    public class SamlOrWsFedExternalDomainFederation
    {
        // "@odata.type": "microsoft.graph.samlOrWsFedExternalDomainFederation",
        [JsonPropertyName("@odata.type")]
        public string ODataType { get; set; }

        // "issuerUri": https://catenaxdev003akssrv.germanywestcentral.cloudapp.azure.com/auth/realms/master,
        [JsonPropertyName("issuerUri")]
        public string IssuerUri { get; set; }

        // "displayName": "catenaxdev003akssrv.germanywestcentral.cloudapp.azure.com",
        [JsonPropertyName("displayName")]
        public string DisplayName { get; set; }

        // "metadataExchangeUri": https://catenaxdev003akssrv.germanywestcentral.cloudapp.azure.com/metadataExchangeUri,
        [JsonPropertyName("metadataExchangeUri")]
        public string MetadataExchangeUri { get; set; }

        // "passiveSignInUri": https://catenaxdev003akssrv.germanywestcentral.cloudapp.azure.com/auth/realms/master/protocol/saml,
        [JsonPropertyName("passiveSignInUri")]
        public string PassiveSignInUri { get; set; }

        // "preferredAuthenticationProtocol": "saml",
        [JsonPropertyName("preferredAuthenticationProtocol")]
        public string PreferredAuthenticationProtocol { get; set; }

        // "domains": [
        //   {
        //     "@odata.type": "microsoft.graph.externalDomainName",
        //     "id": "catenaxdev003akssrv.germanywestcentral.cloudapp.azure.com"
        //   }
        // ],
        [JsonPropertyName("domains")]
        public IList<ExternalDomainName> Domains { get; set; }

        // "signingCertificate": "MIICmzCCAYMCBgF8cWrXvTANBgkqhkiG9w0BAQsFADARMQ8wDQYDVQQDDAZtYXN0ZXIwHhcNMjExMDExMjIxMTM5WhcNMzExMDExMjIxMzE5WjARMQ8wDQYDVQQDDAZtYXN0ZXIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCa4BMUk129HA1jxpUkVx/DzJgL2tJQC3YiXGiYHZygZ+r3Ru2+4tvHWIsCNWnl4HBL4FaXSykej24TisS6hLckkATlklKbN2V15oaOfZc3n9GICIJgkxfFzc5MiE3fkPZku3NMdY1OwCL1xFVPW/ouwOVKyGiN0xkwC2zx79mIPDPGjsL1d7mf1ZwHJ93kc7sW8/q8jAIWiVZe/9diWqNEOTbniu3n4GBLk2DxtNAIj23YCtD0ADe+i/EMO0sUlDEGVvTBKx1qu/xoqL12UQmnT5D3mU8zh4DGbbnemo40DGZKHELGSu4Jpoac+6HFP8XcEtO0oTcI67lJmSH2wsrRAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAIZAZgh+l3jHY5aA+JyuVovAblX9AoA+pH42tQY/YI1RG5+6riqAfYmBi9Km8NwZAUww9RNJiTcHNkiFac+4UcSmWAZI9HQmQPkDcH/L0Sh0cTI1LlEs7mKrmAgsOP7KC4EBykxPiRE4isi9ZyOJoBdGhy09XAo9cC58EWAQxOIPde7JOGY1AYXtiQmX6ScHHK0kBJEUjJ/0k7JB8biwmDsFZmNVBnmuPj4M0p9rcI4J4jH/5f/uRcktE0bVvioIYjnnzllLsNy+JjDB4tNEfJaIhppmIB3HctCExdG43BXGZhVI++tqdcBD+I2ckQqf9j5KmLj+z3Om433aZ0SCwIc="
        [JsonPropertyName("signingCertificate")]
        public string SigningCertificate { get; set; }
    }

    public class ExternalDomainName
    {
        // "@odata.type": "microsoft.graph.externalDomainName",
        [JsonPropertyName("@odata.type")]
        public string ODataType { get; set; }
        // "id": "catenaxdev003akssrv.germanywestcentral.cloudapp.azure.com"
        [JsonPropertyName("id")]
        public string Id { get; set; }
    }
}
