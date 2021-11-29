using Microsoft.Extensions.Options;
using System.Collections.Generic;
using System.Threading.Tasks;
using System.Linq;
using CatenaX.NetworkServices.Provisioning.Keycloak;
using CatenaX.NetworkServices.Provisioning.ActiveDirectory;
using CatenaX.NetworkServices.Provisioning.Mail;

namespace CatenaX.NetworkServices.Provisioning.Service.BusinessLogic
{
    public class ProvisioningService : IProvisioningService
    {
        public static readonly string ConfigPosition = "Provisioning";
        private readonly IKeycloakAccess _KeycloakAccess;
        private readonly IFederation _Federation;
        private readonly IUserEmail _UserEmail;
        private readonly ProvisioningSettings _Settings;
        public ProvisioningService(IKeycloakAccess keycloakAccess, IFederation federation, IUserEmail userEmail, IOptions<ProvisioningSettings> settings)
        {
            _KeycloakAccess = keycloakAccess;
            _Federation = federation;
            _UserEmail = userEmail;
            _Settings = settings.Value;
        }

        public async Task CheckAndExecuteProvisioningAsync()
        {
            await Task.WhenAll(
                (await _KeycloakAccess.GetOnboardingRealmGroupsAsync(_Settings.TriggerGroup)).Select(async realmGroup => {
                    var (realm,group) = realmGroup;
                    await _KeycloakAccess.CreateFederationClient(realm._Realm);
                    await _Federation.CreateFederationAsync(new Dictionary<string,string>{
                        { "realm", realm._Realm },
                        { "base", _Settings.DomainBase },
                        { "cert", await _KeycloakAccess.GetSamlDescriptorCertAsync(realm._Realm) }
                    });
                    await _KeycloakAccess.DeleteGroupAsync(realm._Realm,group.Id);
                    await Task.WhenAll((await _KeycloakAccess.GetUsersAsync(realm._Realm)).Select(async user => {
                        await _Federation.SendInvitationAsync(user.UserName);
                        await _UserEmail.SendMailAsync(user.UserName,user.FirstName,user.LastName,realm._Realm);
                    }));
                })
            );
        }
    }
}
