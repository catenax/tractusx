using CatenaX.NetworkServices.Onboarding.Service.CDQ.Model;

using System.Collections.Generic;
using System.Threading.Tasks;

namespace CatenaX.NetworkServices.Onboarding.Service.CDQ
{
    public interface ICDQAccess
    {
        Task<List<FetchBusinessPartnerDto>> FetchBusinessPartner(string companyIdentifier);
    }
}
