using System.Linq;
using System.Collections.Generic;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Configuration;

namespace CatenaX.NetworkServices.Mailing.Template
{
    public class TemplateSettings: Dictionary<string,TemplateSetting>
    {
        public const string Position = "MailingService:Templates";
    }
    public class TemplateSetting
    {
        public string Subject { get; set; }
        public string Body { get; set; }
        public bool html { get; set; }
    }
    public static class TemplateSettingsExtention
    {
        public static IServiceCollection ConfigureTemplateSettings(
            this IServiceCollection services,
            IConfigurationSection section
            )
        {
            return services.Configure<TemplateSettings>(x => section
                .GetChildren()
                .Aggregate(x,(y,z) => {
                    y.Add(z.Key,z.Get<TemplateSetting>());
                    return y;
                }));
        }
    }
}
