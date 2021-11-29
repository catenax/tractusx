using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

using Keycloak.Net;
using Keycloak.Net.Models.RealmsAdmin;
using Keycloak.Net.Models.Users;

using Keycloak.Net.Models.Groups;
using CatenaX.NetworkServices.Invitation.Identity.Model;
using PasswordGenerator;
using System.Linq;
using Keycloak.Net.Models.Clients;
using Microsoft.Extensions.Configuration;

namespace CatenaX.NetworkServices.Invitation.Identity.Identity
{
    public class KeycloakIdentityManager : IIdentityManager
    {
        private readonly KeycloakClient _client;
        private readonly string _baseAddress;

        public KeycloakIdentityManager(KeycloakClient client, string baseAddress)
        {
            _client = client;
            _baseAddress = baseAddress;
        }

        public async Task CreateRealm(CreateRealm realm)
        {
            var realmToCreate = new Realm
            {
                _Realm = realm.Name,
                Enabled = true
            };
            await _client.ImportRealmAsync(realm.Name, realmToCreate);
        }

        public async Task<string> CreateUser(string realm, CreateUser user)
        {
            var pwd = new Password();
            var result = pwd.Next();
            var userToCreate = new User
            {
                UserName = user.UserName,
                FirstName = user.FirstName,
                LastName = user.LastName,
                Groups = user.Groups,
                Credentials = new List<Credentials>() { new Credentials { Type = "Password", Value = result } },
                Enabled = true
                
            };
            await _client.CreateUserAsync(realm, userToCreate);

            return result;
        }

        public async Task CreateGroup(string realm, CreateGroup group)
        {
            var groupToCreate = new Group
            {
                Name = group.Name
            };
            await _client.CreateGroupAsync(realm, groupToCreate);
        }

        public async Task AssignRolesToUser(string realm, string username, string clientId, List<string> roleNames)
        {
            var user = await _client.GetUsersAsync(realm, username: username);
            var userId = user.First().Id;

            var result = await _client.GetRoleMappingsForUserAsync(realm, userId);
            var allClients = await _client.GetClientsAsync(realm);
            var clientKCId = allClients.Where(c => c.ClientId.Equals(clientId)).First().Id;

            var getRoles = await _client.GetRolesAsync(realm, clientKCId);
            var roles = getRoles.Where(r => roleNames.Contains(r.Name));

            await _client.AddClientRoleMappingsToUserAsync(realm, userId, clientKCId, roles);
        }

        public async Task CreateClient(string realm, string clientName)
        {
            await _client.CreateClientAsync(realm, new Client { Id = clientName, PublicClient = true, DirectAccessGrantsEnabled = true, RedirectUris = new List<string> { "http://localhost:3000/*",$"{_baseAddress}/*"  } });
        }
    }
}
