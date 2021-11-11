using Microsoft.Extensions.Options;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using System.Text;
using System.Text.Json;

using CatenaX.NetworkServices.Provisioning.ActiveDirectory.Model;
using CatenaX.NetworkServices.Provisioning.Utils;

namespace CatenaX.NetworkServices.Provisioning.ActiveDirectory
{
    public class Federation : IFederation
    {
        public const string ConfigPosition = "ActiveDirectory:Federation";
        private readonly HttpClient _Client;
        private readonly FederationSettings _Settings;
        private readonly IClientToken _Token;
        private readonly StringTemplate _RequestUriTemplate;
        private readonly StringTemplate _IssuerUriTemplate;
        private readonly StringTemplate _DisplayNameTemplate;
        private readonly StringTemplate _MetadataExchangeUriTemplate;
        private readonly StringTemplate _PassiveSignInUriTemplate;
        private readonly IList<StringTemplate> _DomainIdTemplates;
        private readonly StringTemplate _SigningCertificateTemplate;

        public Federation(IOptions<FederationSettings> federationSettings, IClientToken clientToken, IHttpClientFactory clientFactory)
        {
            _Settings = federationSettings.Value;
            _Token = clientToken;
            _Client = clientFactory.CreateClient();
            _RequestUriTemplate = new StringTemplate(_Settings.CreateFederationUri);
            _IssuerUriTemplate = new StringTemplate(_Settings.CreateFederationRequest.IssuerUri);
            _DisplayNameTemplate = new StringTemplate(_Settings.CreateFederationRequest.DisplayName);
            _MetadataExchangeUriTemplate = new StringTemplate(_Settings.CreateFederationRequest.MetadataExchangeUri);
            _PassiveSignInUriTemplate = new StringTemplate(_Settings.CreateFederationRequest.PassiveSignInUri);
            _DomainIdTemplates = _Settings.CreateFederationRequest.DomainIds.Select(id => new StringTemplate(id)).ToList();
            _SigningCertificateTemplate = new StringTemplate(_Settings.CreateFederationRequest.SigningCertificate);
        }

        public Task CreateFederationAsync(IDictionary<string,string> values) =>
            PostJsonAuthenticated(_RequestUriTemplate.Apply(values), new SamlOrWsFedExternalDomainFederation {
                ODataType = _Settings.CreateFederationRequest.ODataType,
                IssuerUri = _IssuerUriTemplate.Apply(values),
                DisplayName = _DisplayNameTemplate.Apply(values),
                MetadataExchangeUri = _MetadataExchangeUriTemplate.Apply(values),
                PassiveSignInUri = _PassiveSignInUriTemplate.Apply(values),
                PreferredAuthenticationProtocol = _Settings.CreateFederationRequest.PreferredAuthenticationProtocol,
                Domains = _DomainIdTemplates.Select(template =>
                    new ExternalDomainName {
                        ODataType = _Settings.CreateFederationRequest.DomainODataType,
                        Id = template.Apply(values)
                    }).ToList(),
                SigningCertificate = _SigningCertificateTemplate.Apply(values)
            });

        public Task SendInvitationAsync(string email) =>
            PostJsonAuthenticated(_Settings.SendInvitationUri, new Invitation {
                InvitedUserEmailAddress = email,
                InviteRedirectUrl = _Settings.SendInvitationRedirectUrl,
                SendInvitationMessage = false
            });

        private async Task PostJsonAuthenticated(string url, object json)
        {
            var token = await _Token.GetTokenAsync();
            var request = new HttpRequestMessage(HttpMethod.Post, new Uri(url));
            request.Headers.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
            request.Headers.Authorization = new AuthenticationHeaderValue(token.Type,token.Value);
            request.Content = new StringContent(JsonSerializer.Serialize(json), Encoding.UTF8);
            request.Content.Headers.ContentType = new MediaTypeHeaderValue("application/json");
            (await _Client.SendAsync(request)).EnsureSuccessStatusCode();
        }
    }
}
