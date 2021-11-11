using Keycloak.Net;
using Microsoft.Extensions.Options;

namespace CatenaX.NetworkServices.Provisioning.Keycloak
{
    public class KeycloakFactory : IKeycloakFactory
    {
        readonly KeycloakSettings _Settings;
        public KeycloakFactory(IOptions<KeycloakSettings> settings)
        {
            _Settings = settings.Value;
        }

        public KeycloakClient CreateKeycloakClient()
        {
            return new KeycloakClient(_Settings.ConnectionString, _Settings.User, _Settings.Password, _Settings.AuthRealm);
        }

        public string GetAdminRealm()
        {
            return _Settings.AuthRealm;
        }

        public string GetConnectionString()
        {
            return _Settings.ConnectionString;
        }
    }
}
