using System.Collections.Generic;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Configuration;

namespace CatenaX.NetworkServices.Provisioning.Keycloak
{
    public class KeycloakAccessSettings
    {
        public string MetaDataPath { get; set; }
        public FederationClientSettings FederationClient { get; set; }
    }

    public class FederationClientSettings
    {
        public string ClientId { get; set; }
        public List<string> RedirectUris { get; set; }
        public string Protocol { get; set; }
        public Dictionary<string,object> Attributes { get; set; }
        public bool FullScopeAllowed { get; set; }
        public List<FederationClientProtocolMapper> ProtocolMappers{ get; set; }
    }

    public class FederationClientProtocolMapper
    {
        public string Name { get; set; }
        public string Protocol { get; set; }
        public string ProtocolMapper { get; set; }
        public bool ConsentRequired { get; set; }
        public Dictionary<string,object> Config { get; set; }
    }

    public static class KeycloakAccessSettingsExtention
    {
        public static IServiceCollection ConfigureKeycloakAccessSettings(
            this IServiceCollection services,
            IConfigurationSection section
            )
        {
            return services.Configure<KeycloakAccessSettings>(x => section.Bind(x));
        }
    }
}
