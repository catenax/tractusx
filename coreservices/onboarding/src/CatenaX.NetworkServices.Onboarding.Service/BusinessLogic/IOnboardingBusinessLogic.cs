using CatenaX.NetworkServices.Cosent.Library.Data;
using CatenaX.NetworkServices.Mockups;
using CatenaX.NetworkServices.Onboarding.Service.CDQ.Model;
using CatenaX.NetworkServices.Onboarding.Service.Model;

using System.Collections.Generic;
using System.Threading.Tasks;

namespace CatenaX.NetworkServices.Onboarding.Service.BusinessLogic
{
    public interface IOnboardingBusinessLogic
    {
        Task<List<FetchBusinessPartnerDto>> GetCompanyByIdentifierAsync(string companyIdentifier);
        Task<List<string>> GetAvailableUserRoleAsync();
        Task<List<string>> GetAvailableUserRolesAsync(string token, string realm);
        Task<List<string>> GetOwnUserRolesAsync(string token, string realm, string userId);
        Task<List<CompanyRole>> GetCompanyRolesAsync();
        Task CreateUsersAsync(List<UserCreationInfo> userList, string realm, string token, Dictionary<string, string> userInfo);
        Task SetCompanyRolesAsync(CompanyToRoles rolesToSet);
        Task<List<ConsentForCompanyRole>> GetConsentForCompanyRoleAsync(int roleId);
        Task SignConsentAsync(SignConsentRequest signedConsent);
        Task<List<SignedConsent>> SignedConsentsByCompanyIdAsync(string companyId);
        Task SetIdpAsync(SetIdp idpToSet);
        Task FinishOnboardingAsync(string token, string realm);
    }
}
