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

        public Task CheckAndExecuteProvisioning()
        {
            return _KeycloakAccess.GetOnboardingRealmGroupsAsync(_Settings.TriggerGroup)
                .ContinueWith(taskRealmGroups =>
                    Task.WhenAll(taskRealmGroups.Result.Select(realmGroup => {
                        var (realm,group) = realmGroup;
                        return _KeycloakAccess.GetClientAttributeAsync(realm.Id, _Settings.ClientId, _Settings.CertAttribute)
                            .ContinueWith(taskCert => {
                                var federationParams = new Dictionary<string,string>{
                                    { "realm", realm.Id },
                                    { "base", _Settings.DomainBase },
                                    { "cert", taskCert.Result }
                                };
                                return _Federation.CreateFederation(federationParams).ContinueWith(_ =>
                                    _KeycloakAccess.GetUsers(realm.Id)
                                        .ContinueWith(taskUsers =>
                                            Task.WhenAll(taskUsers.Result.Select(user =>
                                                _UserEmail.SendMail(user.Email,user.FirstName,user.LastName,realm.Id)
                                            )).Wait()
                                        )
                                    ).ContinueWith(_ =>
                                        _KeycloakAccess.DeleteGroup(realm.Id,group.Id).Wait()
                                    );
                            });
                        })
                    ).Wait()
                );
        }
    }
}
