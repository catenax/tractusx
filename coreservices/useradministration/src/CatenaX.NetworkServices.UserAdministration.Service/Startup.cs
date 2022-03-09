using Microsoft.AspNetCore.Authentication;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.OpenApi.Models;
using CatenaX.NetworkServices.Keycloak.Authentication;
using CatenaX.NetworkServices.Keycloak.DBAccess;
using CatenaX.NetworkServices.Keycloak.Factory;
using CatenaX.NetworkServices.Mailing.SendMail;
using CatenaX.NetworkServices.Mailing.Template;
using CatenaX.NetworkServices.Provisioning.Library;
using CatenaX.NetworkServices.UserAdministration.Service.BusinessLogic;
using System.IdentityModel.Tokens.Jwt;

using Npgsql;

using System.Data;

namespace CatenaX.NetworkServices.UserAdministration.Service
{
    public class Startup
    {
        private static string TAG = typeof(Startup).Namespace;
        private static string VERSION = "v2";

        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddControllers();
            services.AddAuthentication(x =>
            {
                x.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
                x.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
            }).AddJwtBearer(options => Configuration.Bind("JwtBearerOptions",options));
            services.AddSwaggerGen(c => c.SwaggerDoc(VERSION, new OpenApiInfo { Title = TAG, Version = VERSION }))
                    .AddTransient<IUserAdministrationBusinessLogic,UserAdministrationBusinessLogic>()
                    .AddTransient<IMailingService, MailingService>()
                    .AddTransient<ISendMail, SendMail>()
                    .AddTransient<ITemplateManager, TemplateManager>()
                    .ConfigureTemplateSettings(Configuration.GetSection(TemplateSettings.Position))
                    .ConfigureMailSettings(Configuration.GetSection(MailSettings.Position))
                    .AddTransient<IClaimsTransformation, KeycloakClaimsTransformation>()
                    .AddTransient<IKeycloakFactory, KeycloakFactory>()
                    .AddTransient<IProvisioningManager, ProvisioningManager>()
                    .ConfigureKeycloakSettingsMap(Configuration.GetSection("Keycloak"))
                    .ConfigureProvisioningSettings(Configuration.GetSection("Provisioning"))
                    .Configure<JwtBearerOptions>(options => Configuration.Bind("JwtBearerOptions",options));
            
            services.AddTransient<IUserAdministrationBusinessLogic, UserAdministrationBusinessLogic>()
                    .ConfigureUserAdministrationSettings(Configuration.GetSection("Invitation"));

            services.AddTransient<IKeycloakDBAccess, KeycloakDBAccess>()
                    .AddTransient<IDbConnection>(conn => new NpgsqlConnection(Configuration.GetValue<string>("CentralIdpDatabaseConnectionString")));

            services.AddTransient<IUserAdministrationBusinessLogic, UserAdministrationBusinessLogic>();
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }
            if (Configuration.GetValue<bool?>("SwaggerEnabled") != null && Configuration.GetValue<bool>("SwaggerEnabled"))
            {
                app.UseSwagger( c => c.RouteTemplate = "/api/useradministration/swagger/{documentName}/swagger.{json|yaml}");
                app.UseSwaggerUI(c => {
                    c.SwaggerEndpoint(string.Format("/api/useradministration/swagger/{0}/swagger.json",VERSION), string.Format("{0} {1}",TAG,VERSION));
                    c.RoutePrefix = "api/useradministration/swagger";
                });
            }

            //app.UseHttpsRedirection();

            app.UseRouting();

            JwtSecurityTokenHandler.DefaultInboundClaimTypeMap.Clear();

            app.UseAuthentication();
            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });
        }
    }
}
