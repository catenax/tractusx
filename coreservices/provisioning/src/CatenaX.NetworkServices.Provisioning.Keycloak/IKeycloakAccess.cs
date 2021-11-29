using System.Threading.Tasks;
using System.Collections.Generic;
using Keycloak.Net.Models.RealmsAdmin;
using Keycloak.Net.Models.Groups;
using Keycloak.Net.Models.Users;

namespace CatenaX.NetworkServices.Provisioning.Keycloak
{
    public interface IKeycloakAccess
    {
        Task<IEnumerable<Realm>> GetRealmsAsync();
        Task<Group> GetGroupAsync(string realmId, string groupName);
        Task<IEnumerable<User>> GetUsersAsync(string realmId);
        Task DeleteGroupAsync(string realmId, string groupId);
        Task<IEnumerable<(Realm,Group)>> GetOnboardingRealmGroupsAsync(string triggerGroup);
        Task<string> GetSamlDescriptorCertAsync(string realm);
        Task CreateFederationClient(string realm);
    }
}