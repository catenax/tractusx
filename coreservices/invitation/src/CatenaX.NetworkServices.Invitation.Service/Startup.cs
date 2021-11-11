using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

using CatenaX.NetworkServices.Mailing.SendMail;
using CatenaX.NetworkServices.Mailing.Template;
using CatenaX.NetworkServices.Invitation.Identity;
using CatenaX.NetworkServices.Invitation.Identity.Identity;
using CatenaX.NetworkServices.Invitation.Service.BusinessLogic;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.HttpsPolicy;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;

namespace CatenaX.NetworkServices.Invitation.Service
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddControllers();
            services.AddTransient<IIdentityManager>(x => new KeycloakIdentityManager(new Keycloak.Net.KeycloakClient(Configuration.GetValue<string>("KeyCloakConnectionString"), Configuration.GetValue<string>("KeyCloakUser"), Configuration.GetValue<string>("KeyCloakUserPassword"), authRealm: "master"), Configuration.GetValue<string>("BasePortalAddress")));
            services.AddTransient<IInvitationBusinessLogic,InvitationBusinessLogic>();
            services.AddTransient<IMailingService, MailingService>();
            services.AddTransient<ISendMail, SendMail>()
            .AddTransient<ITemplateManager, TemplateManager>()
            .ConfigureTemplateSettings(Configuration.GetSection(TemplateSettings.Position))
            .ConfigureMailSettings(Configuration.GetSection(MailSettings.Position));
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            //app.UseHttpsRedirection();

            app.UseRouting();

            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });
        }
    }
}
