using System;
using Keycloak.Net;

namespace CatenaX.NetworkServices.Provisioning.Keycloak
{
    public interface IKeycloakFactory
    {
        KeycloakClient CreateKeycloakClient();

        string GetAdminRealm();

        string GetConnectionString();
    }
}
