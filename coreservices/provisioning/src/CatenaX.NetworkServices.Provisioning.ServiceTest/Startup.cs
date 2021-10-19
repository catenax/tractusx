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
            services.AddSingleton<IClientToken, ClientToken>()
                    .AddTransient<IFederation, Federation>()
                    .AddTransient<IKeycloakFactory, KeycloakFactory>()
                    .AddTransient<IKeycloakAccess,KeycloakAccess>()
                    .AddTransient<ISendMail,SendMail>()
                    .AddTransient<ITemplateManager,TemplateManager>()
                    .AddTransient<IUserEmail,UserEmail>()
                    .ConfigureMailSettings(Configuration.GetSection(UserEmail.ProviderPosition))
                    .ConfigureTemplateSettings(Configuration.GetSection(UserEmail.TemplatePosition))
                    .ConfigureUserEmailSettings(Configuration.GetSection(UserEmail.UserEmailPosition))
                    .ConfigureKeycloakSettings(Configuration.GetSection(KeycloakSettings.Position))
                    .ConfigureClientSettings(Configuration.GetSection(ClientSettings.Position))
                    .ConfigureFederationSettings(Configuration.GetSection(FederationSettings.Position));
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
