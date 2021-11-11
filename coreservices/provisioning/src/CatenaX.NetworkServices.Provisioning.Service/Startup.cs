using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;

using CatenaX.NetworkServices.Mailing.SendMail;
using CatenaX.NetworkServices.Mailing.Template;
using CatenaX.NetworkServices.Provisioning.Mail;
using CatenaX.NetworkServices.Provisioning.ActiveDirectory;
using CatenaX.NetworkServices.Provisioning.Keycloak;
using CatenaX.NetworkServices.Provisioning.Service.BusinessLogic;

namespace CatenaX.NetworkServices.Provisioning.Service
{
    public class Startup
    {
        public IConfiguration Configuration { get; }
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddControllers();
            services.AddHttpClient()
                    .AddSingleton<IClientToken, ClientToken>()
                    .AddTransient<IFederation, Federation>()
                    .AddTransient<IKeycloakFactory, KeycloakFactory>()
                    .AddTransient<IKeycloakAccess,KeycloakAccess>()
                    .AddTransient<ISendMail,SendMail>()
                    .AddTransient<ITemplateManager,TemplateManager>()
                    .AddTransient<IUserEmail,UserEmail>()
                    .AddTransient<IProvisioningService,ProvisioningService>()
                    .ConfigureMailSettings(Configuration.GetSection(UserEmail.ProviderPosition))
                    .ConfigureTemplateSettings(Configuration.GetSection(UserEmail.TemplatePosition))
                    .ConfigureUserEmailSettings(Configuration.GetSection(UserEmail.UserEmailPosition))
                    .ConfigureKeycloakSettings(Configuration.GetSection(KeycloakAccess.ConfigPosition))
                    .ConfigureKeycloakAccessSettings(Configuration.GetSection(KeycloakAccess.ConfigPosition))
                    .ConfigureClientSettings(Configuration.GetSection(ClientToken.ConfigPosition))
                    .ConfigureFederationSettings(Configuration.GetSection(Federation.ConfigPosition))
                    .ConfigureProvisioningSettings(Configuration.GetSection(ProvisioningService.ConfigPosition));
        }
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            app.UseRouting();

            app.UseEndpoints(endpoints =>
                endpoints.MapControllers()
            );
        }
    }
}
