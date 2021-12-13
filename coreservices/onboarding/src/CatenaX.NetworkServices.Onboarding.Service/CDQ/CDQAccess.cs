using CatenaX.NetworkServices.Onboarding.Service.CDQ.Model;

using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace CatenaX.NetworkServices.Onboarding.Service.CDQ
{
    public class CDQAccess : ICDQAccess
    {
        private readonly HttpClient _httpClient;

        public CDQAccess(IHttpClientFactory _httpFactory)
        {
            _httpClient = _httpFactory.CreateClient("cdq");
        }

        public async Task<List<FetchBusinessPartnerDto>> FetchBusinessPartner(string companyIdentifier)
        {
            var response = new List<FetchBusinessPartnerDto>();
            var content = JsonSerializer.Serialize(new { cdqId = companyIdentifier });
            var stringContent = new StringContent(content, Encoding.UTF8, "application/json");
            var result = await _httpClient.PostAsync("v3/businesspartners/fetch", stringContent);
            if (result.IsSuccessStatusCode)
            {
                var body = JsonSerializer.Deserialize<FetchBusinessPartnerDto>(await result.Content.ReadAsStringAsync());
                response.Add(body);
                
            }
            else
            {
                //Fetch by BPN
                var lookupContent = JsonSerializer.Serialize(value: new FetchBusinessPartnerRequest {matchValue = 0.54, businessPartner = new Businesspartner { identifiers = new[] { new Identifier { value = companyIdentifier, type = new Type { technicalKey = "CX_BPN" } } }, addresses = new[] { new Address { country = new Country { shortName = "DE" } } } } });
                var lookupStringContent = new StringContent(lookupContent, Encoding.UTF8, "application/json");
                var fetchRequest = await _httpClient.PostAsync("v3/businesspartners/lookup", lookupStringContent);
                if (fetchRequest.IsSuccessStatusCode)
                {
                    var fetched = JsonSerializer.Deserialize<PaginatedFetchBusinessPartner>(await fetchRequest.Content.ReadAsStringAsync());
                    if(fetched.values.Count() == 1)
                    {
                        content = JsonSerializer.Serialize(new { cdqId = fetched.values.First().cdqId });
                        stringContent = new StringContent(content, Encoding.UTF8, "application/json");
                        result = await _httpClient.PostAsync("v3/businesspartners/fetch", stringContent);
                        if (result.IsSuccessStatusCode)
                        {
                            var body = JsonSerializer.Deserialize<FetchBusinessPartnerDto>(await result.Content.ReadAsStringAsync());
                            response.Add(body);
                            return response;
                        }
                    }
                }


                //fetchByName
                lookupContent = JsonSerializer.Serialize(value: new FetchBusinessPartnerRequest { matchValue = 0.54, businessPartner = new Businesspartner { names = new[] { new Name { value = companyIdentifier, type = new Type { technicalKey = "LOCAL" } } } } });
                lookupStringContent = new StringContent(lookupContent, Encoding.UTF8, "application/json");
                fetchRequest = await _httpClient.PostAsync("v3/businesspartners/lookup", lookupStringContent);
                if (fetchRequest.IsSuccessStatusCode)
                {
                    var fetched = JsonSerializer.Deserialize<PaginatedFetchBusinessPartner>(await fetchRequest.Content.ReadAsStringAsync());
                    response.AddRange(fetched.values);
                }
            }
            return response;
        }
    }
}
