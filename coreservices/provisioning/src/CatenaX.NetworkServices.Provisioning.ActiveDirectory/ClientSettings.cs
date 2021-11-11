using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Configuration;

namespace CatenaX.NetworkServices.Provisioning.ActiveDirectory
{
    public class ClientSettings
    {
        public string Authority { get; set; }
        public string ClientId { get; set; }
        public string ClientSecret { get; set; }
    }
    public static class ClientSettingsExtention
    {
        public static IServiceCollection ConfigureClientSettings(
            this IServiceCollection services,
            IConfigurationSection section
            )
        {
            return services.Configure<ClientSettings>(x => section.Bind(x));
        }
    }

}
