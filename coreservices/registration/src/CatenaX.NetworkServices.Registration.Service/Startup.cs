using CatenaX.NetworkServices.Mailing.SendMail;
using CatenaX.NetworkServices.Mailing.Template;
using CatenaX.NetworkServices.Registration.Service.BPN;
using CatenaX.NetworkServices.Registration.Service.BusinessLogic;
using CatenaX.NetworkServices.Registration.Service.CDQ;
using CatenaX.NetworkServices.Registration.Service.Custodian;
using CatenaX.NetworkServices.Registration.Service.RegistrationAccess;

using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.HttpsPolicy;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Microsoft.OpenApi.Models;

using Npgsql;

using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Threading.Tasks;

namespace CatenaX.NetworkServices.Registration.Service
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
            services.AddSwaggerGen(c =>
            {
                c.SwaggerDoc("v1", new OpenApiInfo { Title = "CatenaX.NetworkServices.Registration.Service", Version = "v1" });
            });
            services.AddHttpClient("keycloak", c =>
            {
                c.BaseAddress = new Uri($"{ Configuration.GetValue<string>("KeyCloakConnectionString")}/auth/realms/");
            });
            
            services.AddTransient<IRegistrationBusinessLogic, RegistrationBusinessLogic>();
            services.AddTransient<IRegistrationDBAccess, RegistrationDBAccess>();

            services.AddCustodianService(Configuration.GetSection("Custodian"));

            services.AddTransient<IBPNAccess, BPNAccess>();
            services.AddHttpClient("bpn", c =>
            {
                c.BaseAddress = new Uri($"{ Configuration.GetValue<string>("BPN_Address")}");
            });
            if (Configuration.GetValue<bool?>("CDQ_Enabled") != null && Configuration.GetValue<bool>("CDQ_Enabled"))
            {
                services.AddTransient<ICDQAccess, CDQAccess>();
                services.AddHttpClient("cdq", c =>
                {
                    c.DefaultRequestHeaders.Add("X-API-KEY", Configuration.GetValue<string>("CDQ_SubscriptionKey"));
                    c.BaseAddress = new Uri($"{ Configuration.GetValue<string>("CDQ_Address")}");
                });
            }
            else
            {
                services.AddTransient<ICDQAccess, CDQAccessMock>();
            }
            services.AddTransient<IDbConnection>(conn => new NpgsqlConnection(Configuration.GetValue<string>("PostgresConnectionString")));
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
            app.UseSwagger();
            app.UseSwaggerUI(c => c.SwaggerEndpoint("/swagger/v1/swagger.json", "CatenaX.NetworkServices.Registration.Service v1"));

            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });
        }
    }
}
