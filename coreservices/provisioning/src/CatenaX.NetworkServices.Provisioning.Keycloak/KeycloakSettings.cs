using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Configuration;

namespace CatenaX.NetworkServices.Provisioning.Keycloak
{
    public class KeycloakSettings
    {
        public string ConnectionString { get; set; }
        public string User { get; set; }
        public string Password { get; set; }
        public string AuthRealm { get; set; }
    }
    public static class KeycloakSettingsExtention
    {
        public static IServiceCollection ConfigureKeycloakSettings(
            this IServiceCollection services,
            IConfigurationSection section
            )
        {
            return services.Configure<KeycloakSettings>(x => section.Bind(x));
        }
    }
}
