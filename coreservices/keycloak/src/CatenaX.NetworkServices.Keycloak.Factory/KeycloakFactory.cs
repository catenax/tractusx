using Keycloak.Net;
using Microsoft.Extensions.Options;

namespace CatenaX.NetworkServices.Keycloak.Factory
{
    public class KeycloakFactory : IKeycloakFactory
    {
        readonly KeycloakSettingsMap _Settings;
        public KeycloakFactory(IOptions<KeycloakSettingsMap> settings)
        {
            _Settings = settings.Value;
        }

        public KeycloakClient CreateKeycloakClient(string instance)
        {
            KeycloakSettings settings = _Settings[instance];
            return new KeycloakClient(settings.ConnectionString, settings.User, settings.Password, settings.AuthRealm);
        }
    }
}
