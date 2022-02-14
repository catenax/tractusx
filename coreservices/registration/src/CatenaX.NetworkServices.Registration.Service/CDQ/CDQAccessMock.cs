using CatenaX.NetworkServices.Registration.Service.CDQ.Model;

using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CatenaX.NetworkServices.Registration.Service.CDQ
{
    public class CDQAccessMock : ICDQAccess
    {
        public List<FetchBusinessPartnerDto> fetchBusinessPartners = new List<FetchBusinessPartnerDto>()
        {
            new FetchBusinessPartnerDto
            {
                cdqId = "CDQ:12345",
                dataSource = "someDataSource",
                businessPartner = new Businesspartner
                {
                    identifiers = new Identifier[]
                    {
                        new Identifier
                        {
                            type = new Type
                            {
                                technicalKey = "CX_BPN",
                            },
                            value = "BPN12345"
                        }
                    }
                }
            }
        };

        public Task<List<FetchBusinessPartnerDto>> FetchBusinessPartner(string companyIdentifier)
        {
            return Task.FromResult(fetchBusinessPartners.Where(d => d.cdqId.Equals(companyIdentifier)).ToList());
        }
    }
}
