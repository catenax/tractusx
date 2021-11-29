using Microsoft.Extensions.Options;
using Microsoft.Identity.Client;
using System;
using System.Threading.Tasks;

using CatenaX.NetworkServices.Provisioning.ActiveDirectory.Model;

namespace CatenaX.NetworkServices.Provisioning.ActiveDirectory
{
    public class ClientToken:IClientToken
    {
        public const string ConfigPosition = "ActiveDirectory:Client";
        string[] scopes = new string[] { "https://graph.microsoft.com/.default" };
        private readonly ClientSettings _Config;
        IConfidentialClientApplication _App;

        public ClientToken(IOptions<ClientSettings> clientSettings)
        {
            _Config = clientSettings.Value;
            _App = ConfidentialClientApplicationBuilder.Create(_Config.ClientId)
                                          .WithClientSecret(_Config.ClientSecret)
                                          .WithAuthority(new Uri(_Config.Authority))
                                          .Build();
        }

        public async Task<Token> GetTokenAsync() => new Token(await _App.AcquireTokenForClient(scopes).ExecuteAsync());
    }
}
