using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Configuration;

namespace CatenaX.NetworkServices.Provisioning.Service.BusinessLogic
{
    public class ProvisioningSettings
    {
        public string TriggerGroup { get; set; }
        public string DomainBase { get; set; }
    }
    public static class ProvisioningSettingsExtention
    {
        public static IServiceCollection ConfigureProvisioningSettings(
            this IServiceCollection services,
            IConfigurationSection section
            )
        {
            return services.Configure<ProvisioningSettings>(x => section.Bind(x));
        }
    }

}
