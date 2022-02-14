using CatenaX.NetworkServices.Cosent.Library.Data;
using CatenaX.NetworkServices.Mockups;
using CatenaX.NetworkServices.Registration.Service.BusinessLogic;
using CatenaX.NetworkServices.Registration.Service.Model;

using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;

namespace CatenaX.NetworkServices.Registration.Service.Controllers
{
    [ApiController]
    [Route("api/registration")]
    public class RegistrationController : ControllerBase
    {
        private readonly ILogger<RegistrationController> _logger;
        private readonly HttpClient _httpClient;
        private readonly IRegistrationBusinessLogic _registrationBusinessLogic;

        public RegistrationController(ILogger<RegistrationController> logger, IHttpClientFactory clientFactory, IRegistrationBusinessLogic registrationBusinessLogic)
        {
            _logger = logger;
            _httpClient = clientFactory.CreateClient("keycloak");
            _registrationBusinessLogic = registrationBusinessLogic;
        }

        [HttpGet]
        [Route("company/{realm}/{bpn}")]
        [ProducesResponseType(typeof(Company), (int)HttpStatusCode.OK)]
        public Task<IActionResult> GetOneObjectAsync([FromRoute] string realm, [FromRoute] string bpn, [FromHeader] string authorization) =>
            ValidateTokenAsync(realm, authorization, async () =>
                new OkObjectResult(await _registrationBusinessLogic.GetCompanyByIdentifierAsync(bpn)));

        [HttpPost]
        [Route("company/{realm}/users")]
        public Task<IActionResult> CreateUsersAsync([FromRoute] string realm, [FromHeader] string authorization, [FromBody] List<UserCreationInfo> userToCreate) =>
            ValidateTokenAsync(realm, authorization, async (userInfo) => {
                await _registrationBusinessLogic.CreateUsersAsync(userToCreate, realm, authorization.Split(" ")[1], userInfo);
                return new OkResult();
            });

        [HttpPost]
        [Route("company/{realm}/custodianWallet")]
        public Task<IActionResult> CreateWallet([FromRoute] string realm, [FromHeader] string authorization, [FromBody] WalletInformation walletToCreate) =>
            ValidateTokenAsync(realm, authorization, async (userInfo) => {
                await _registrationBusinessLogic.CreateCustodianWalletAsync(walletToCreate);
                return new OkResult();
            });

        [HttpPut]
        [Route("company/{realm}/companyRoles")]
        public Task<IActionResult> SetCompanyRolesAsync([FromRoute] string realm, [FromHeader] string authorization, [FromBody] CompanyToRoles rolesToSet) =>
            ValidateTokenAsync(realm, authorization, async () => {
                await _registrationBusinessLogic.SetCompanyRolesAsync(rolesToSet);
                return new OkResult();
            });

        [HttpGet]
        [ProducesResponseType(typeof(List<CompanyRole>), (int)HttpStatusCode.OK)]
        [Route("company/{realm}/companyRoles")]
        public Task<IActionResult> GetCompanyRolesAsync([FromRoute] string realm, [FromHeader] string authorization) =>
            ValidateTokenAsync(realm, authorization, async () =>
                new OkObjectResult(await _registrationBusinessLogic.GetCompanyRolesAsync()));

        [HttpGet]
        [ProducesResponseType(typeof(List<ConsentForCompanyRole>), (int)HttpStatusCode.OK)]
        [Route("company/{realm}/consentsFoCompanyRole/{roleId}")]
        public Task<IActionResult> GetCompanyRolesAsync([FromRoute] string realm, [FromHeader] string authorization, int roleId) =>
            ValidateTokenAsync(realm, authorization, async () =>
                new OkObjectResult(await _registrationBusinessLogic.GetConsentForCompanyRoleAsync(roleId)));

        [HttpGet]
        [Route("company/{realm}/signedConsentsByCompanyId/{companyId}")]
        [ProducesResponseType(typeof(List<SignedConsent>), (int)HttpStatusCode.OK)]
        public Task<IActionResult> SignedConsentsByCompanyIdAsync([FromRoute] string realm, [FromHeader] string authorization, string companyId) =>
            ValidateTokenAsync(realm, authorization, async () =>
                new OkObjectResult(await _registrationBusinessLogic.SignedConsentsByCompanyIdAsync(companyId)));

        [HttpPut]
        [Route("company/{realm}/signConsent")]
        public Task<IActionResult> SignConsentAsync([FromRoute] string realm, [FromHeader] string authorization, [FromBody] SignConsentRequest signConsentRequest) =>
            ValidateTokenAsync(realm, authorization, async () => {
                await _registrationBusinessLogic.SignConsentAsync(signConsentRequest);
                return new OkResult();
            });

        [HttpPut]
        [Route("company/{realm}/idp")]
        public Task<IActionResult> SetIdpAsync([FromRoute] string realm, [FromHeader] string authorization, [FromBody] SetIdp idpToSet) =>
            ValidateTokenAsync(realm, authorization, async () => {
                await _registrationBusinessLogic.SetIdpAsync(idpToSet);
                return new OkResult();
            });

        [HttpPut]
        [Route("company/{realm}/finishRegistration")]
        public Task<IActionResult> FinishRegistrationAsync([FromRoute] string realm, [FromHeader] string authorization) =>
            ValidateTokenAsync(realm, authorization, async () => {
                await _registrationBusinessLogic.FinishRegistrationAsync(authorization.Split(" ")[1],realm);
                return new OkResult();
            });

        [HttpGet]
        [Route("company/{realm}/userRoles")]
        [ProducesResponseType(typeof(List<string>), (int)HttpStatusCode.OK)]
        public Task<IActionResult> GetUserRolesAsync([FromRoute] string realm, [FromHeader] string authorization) =>
            ValidateTokenAsync(realm, authorization, async () =>
                new OkObjectResult(await _registrationBusinessLogic.GetAvailableUserRoleAsync()));

        [HttpGet]
        [Route("company/{realm}/clients/{clientId}/rolesComposite")]
        [ProducesResponseType(typeof(List<string>), (int)HttpStatusCode.OK)]
        public Task<IActionResult> GetClientRolesComposite([FromRoute] string realm,[FromRoute] string clientId, [FromHeader] string authorization) =>
            ValidateTokenAsync(realm, authorization, async () =>
                new OkObjectResult(await _registrationBusinessLogic.GetClientRolesCompositeAsync(authorization.Split(" ")[1],realm,clientId)));

        [HttpGet]
        [Route("company/{realm}/clients/{clientId}/userRoleMappingsComposite")]
        [ProducesResponseType(typeof(List<string>), (int)HttpStatusCode.OK)]
        public Task<IActionResult> GetUserClientRoleMappingsCompositeAsync([FromRoute] string realm,[FromRoute] string clientId, [FromHeader] string authorization) =>
            ValidateTokenAsync(realm, authorization, async (userInfo) =>
                new OkObjectResult(await _registrationBusinessLogic.GetUserClientRoleMappingsCompositeAsync(authorization.Split(" ")[1],realm,userInfo["sub"],clientId)));

        [HttpGet]
        [Route("company/{realm}/groups")]
        [ProducesResponseType(typeof(List<string>), (int)HttpStatusCode.OK)]
        public Task<IActionResult> GetGroups([FromRoute] string realm, [FromHeader] string authorization) =>
            ValidateTokenAsync(realm, authorization, async () =>
                new OkObjectResult(await _registrationBusinessLogic.GetGroupsAsync(authorization.Split(" ")[1],realm)));
        
        [HttpGet]
        [Route("company/{realm}/userGroups")]
        [ProducesResponseType(typeof(List<string>), (int)HttpStatusCode.OK)]
        public Task<IActionResult> GetUserGroupsAsync([FromRoute] string realm, [FromHeader] string authorization) =>
            ValidateTokenAsync(realm, authorization, async (userInfo) =>
                new OkObjectResult(await _registrationBusinessLogic.GetUserGroupsAsync(authorization.Split(" ")[1],realm, userInfo["sub"])));

        private delegate Task<IActionResult> ValidatedAction();
        private delegate Task<IActionResult> ValidatedUserInfoAction(Dictionary<string, string> userInfo);

        private Task<IActionResult> ValidateTokenAsync(string realm, string authorization, ValidatedAction action) =>
            ValidateTokenAsync(realm, authorization, _ => action(), false);
        
        private async Task<IActionResult> ValidateTokenAsync(string realm, string authorization, ValidatedUserInfoAction action, bool deserialize = true)
        {
            var token = "";
            try
            {
                token = authorization.Split(" ")[1];
            }
            catch
            {
                return new StatusCodeResult((int)HttpStatusCode.Forbidden);
            }
            try
            {
                _httpClient.DefaultRequestHeaders.Authorization = new System.Net.Http.Headers.AuthenticationHeaderValue("Bearer", token);
                var response = await _httpClient.GetAsync($"{realm}/protocol/openid-connect/userinfo");
                //if (!response.IsSuccessStatusCode)
                //{
                //    return new StatusCodeResult((int)HttpStatusCode.Forbidden);
                //}
                return await action(
                    deserialize
                        ? JsonConvert.DeserializeObject<Dictionary<string, string>>(await response.Content.ReadAsStringAsync())
                        : null
                );
            }
            catch (Exception e)
            {
                _logger.LogError(e.ToString());
                return new StatusCodeResult((int)HttpStatusCode.InternalServerError);
            }
        }
    }
}
