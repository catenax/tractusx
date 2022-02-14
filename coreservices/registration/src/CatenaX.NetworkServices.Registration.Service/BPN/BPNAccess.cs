
using CatenaX.NetworkServices.Registration.Service.BPN.Model;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace CatenaX.NetworkServices.Registration.Service.BPN
{
    public class BPNAccess : IBPNAccess
    {
        private readonly HttpClient _httpClient;

        public BPNAccess(IHttpClientFactory _httpFactory)
        {
            _httpClient = _httpFactory.CreateClient("bpn");
        }

        public async Task<List<FetchBusinessPartnerDto>> FetchBusinessPartner(string bpn)
        {
            var response = new List<FetchBusinessPartnerDto>();
            var result = await _httpClient.GetAsync($"api/catena/businesspartners/{bpn}");
            if (result.IsSuccessStatusCode)
            {
                try
                {
                    var body = JsonSerializer.Deserialize<FetchBusinessPartnerDto>(await result.Content.ReadAsStringAsync());
                    response.Add(body);

                } catch(Exception e)
                {
                    var test = 5;
                }    
            }
            
            return response;
        }
    }
}
