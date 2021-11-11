using Keycloak.Net;
using Keycloak.Net.Models.Clients;
using Keycloak.Net.Models.Groups;
using Keycloak.Net.Models.RealmsAdmin;
using Keycloak.Net.Models.Users;
using Microsoft.Extensions.Options;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using System.Xml.Serialization;

using CatenaX.NetworkServices.Provisioning.Keycloak.Models;
using CatenaX.NetworkServices.Provisioning.Utils;

namespace CatenaX.NetworkServices.Provisioning.Keycloak
{
    public class KeycloakAccess:IKeycloakAccess
    {
        public const string ConfigPosition = "KeyCloak";

        readonly KeycloakClient _Client;
        readonly HttpClient _HttpClient;
        readonly string _AdminRealm;
        readonly StringTemplate _MetaDataTemplate;
        readonly FederationClientSettings _FederationClient;
        public KeycloakAccess(IKeycloakFactory keycloakFactory, IHttpClientFactory httpClientFactory, IOptions<KeycloakAccessSettings> settings)
        {
            _Client = keycloakFactory.CreateKeycloakClient();
            _HttpClient = httpClientFactory.CreateClient();
            _AdminRealm = keycloakFactory.GetAdminRealm();
            _MetaDataTemplate = new StringTemplate(keycloakFactory.GetConnectionString()+settings.Value.MetaDataPath);
            _FederationClient = settings.Value.FederationClient;
        }
        public async Task DeleteGroupAsync(string realmId, string groupId)
        {
            var result = await _Client.DeleteGroupAsync(realmId,groupId);
            if (!result) throw new GroupNotDeletedException(realmId,groupId);
        }
        public async Task<Group> GetGroupAsync(string realmId, string groupName)
        {
            var groups = await _Client.GetGroupHierarchyAsync(realmId,null,null,groupName);
            return groups.FirstOrDefault( group => group.Name == groupName );
        }
        public async Task<IEnumerable<(Realm,Group)>> GetOnboardingRealmGroupsAsync(string triggerGroup)
        {
            return await Task.WhenAll((await GetRealmsAsync()).Select(async realm =>
                (realm,await GetGroupAsync(realm._Realm,triggerGroup))
            )).ContinueWith(taskWhenAll =>
                taskWhenAll.Result.Where(x => x.Item2 != null)
            );
        }
        public Task<IEnumerable<User>> GetUsersAsync(string realmId)
        {
            return _Client.GetUsersAsync(realmId);
        }
        public Task<IEnumerable<Realm>> GetRealmsAsync()
        {
            return _Client.GetRealmsAsync(_AdminRealm);
        }
        public async Task<string> GetSamlDescriptorCertAsync(string realm)
        {
            var parameters = new Dictionary<string,string> {
                { "realm", realm }
            };
            var request = new HttpRequestMessage(HttpMethod.Get, new Uri(_MetaDataTemplate.Apply(parameters)));
            request.Headers.Accept.Add(new MediaTypeWithQualityHeaderValue("application/xml"));

            var entityDescriptor = ((EntityDescriptor)new XmlSerializer(typeof(EntityDescriptor)).Deserialize(
                await (await _HttpClient.SendAsync(request)).EnsureSuccessStatusCode().Content.ReadAsStreamAsync()
            ));
            var idpssodescriptor = entityDescriptor == null ? null : entityDescriptor.IDPSSODescriptor;
            var keyDescriptor = idpssodescriptor == null ? null : idpssodescriptor.KeyDescriptor;
            var keyInfo = keyDescriptor == null ? null : keyDescriptor.KeyInfo;
            var x509Data = keyInfo.X509Data == null ? null : keyInfo.X509Data;
            return x509Data == null ? null : x509Data.X509Certificate;
        }
        public Task CreateFederationClient(string realm) =>
            _Client.CreateClientAsync(realm, new Client {
                ClientId = _FederationClient.ClientId,
                RedirectUris = _FederationClient.RedirectUris,
                Protocol = _FederationClient.Protocol,
                Attributes = _FederationClient.Attributes,
                FullScopeAllowed = _FederationClient.FullScopeAllowed,
                ProtocolMappers = _FederationClient.ProtocolMappers.Select(mapper =>
                    new ClientProtocolMapper {
                        Name = mapper.Name,
                        Protocol = mapper.Protocol,
                        ProtocolMapper = mapper.ProtocolMapper,
                        ConsentRequired = mapper.ConsentRequired,
                        Config = new ClientConfig {
                            UserAttribute = (string)mapper.Config["user.attribute"],
                            FriendlyName = (string)mapper.Config["friendly.name"],
                            AttributeName = (string)mapper.Config["attribute.name"]
                        }
                    }
                ).ToList()
            });
    }
}
