using CatenaX.NetworkServices.Cosent.Library.Data;
using CatenaX.NetworkServices.Mockups;
using CatenaX.NetworkServices.Registration.Service.BPN.Model;
using CatenaX.NetworkServices.Registration.Service.Model;

using System.Collections.Generic;
using System.Threading.Tasks;

namespace CatenaX.NetworkServices.Registration.Service.BusinessLogic
{
    public interface IRegistrationBusinessLogic
    {
        Task<List<FetchBusinessPartnerDto>> GetCompanyByIdentifierAsync(string companyIdentifier);
        Task<List<string>> GetAvailableUserRoleAsync();
        Task<List<string>> GetClientRolesCompositeAsync(string token, string realm, string clientId);
        Task<List<string>> GetUserClientRoleMappingsCompositeAsync(string token, string realm, string userId, string clientId);
        Task<List<string>> GetGroupsAsync(string token, string realm);
        Task<List<string>> GetUserGroupsAsync(string token, string realm, string userId);
        Task<List<CompanyRole>> GetCompanyRolesAsync();
        Task CreateUsersAsync(List<UserCreationInfo> userList, string realm, string token, Dictionary<string, string> userInfo);
        Task SetCompanyRolesAsync(CompanyToRoles rolesToSet);
        Task<List<ConsentForCompanyRole>> GetConsentForCompanyRoleAsync(int roleId);
        Task SignConsentAsync(SignConsentRequest signedConsent);
        Task<List<SignedConsent>> SignedConsentsByCompanyIdAsync(string companyId);
        Task SetIdpAsync(SetIdp idpToSet);
        Task FinishRegistrationAsync(string token, string realm);

        Task CreateCustodianWalletAsync(WalletInformation information);
    }
}
