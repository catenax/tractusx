using CatenaX.NetworkServices.Cosent.Library.Data;
using CatenaX.NetworkServices.Mockups;
using CatenaX.NetworkServices.Onboarding.Service.BusinessLogic;
using CatenaX.NetworkServices.Onboarding.Service.Model;

using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;

namespace CatenaX.NetworkServices.Onboarding.Service.Controllers
{
    [ApiController]
    [Route("api/onboarding")]
    public class OnboardingController : ControllerBase
    {
        private readonly ILogger<OnboardingController> _logger;
        private readonly HttpClient _httpClient;
        private readonly IOnboardingBusinessLogic _onboardingBusinessLogic;

        public OnboardingController(ILogger<OnboardingController> logger, IHttpClientFactory clientFactory, IOnboardingBusinessLogic onboardingBusinessLogic)
        {
            _logger = logger;
            _httpClient = clientFactory.CreateClient("keycloak");
            _onboardingBusinessLogic = onboardingBusinessLogic;
        }

        [HttpGet]
        [Route("company/{realm}/{oneId}")]
        [ProducesResponseType(typeof(Company), (int)HttpStatusCode.OK)]
        public Task<IActionResult> GetOneObjectAsync([FromRoute] string realm, [FromRoute] string oneId, [FromHeader] string authorization) =>
            ValidateTokenAsync(realm, authorization, async () =>
                new OkObjectResult(await _onboardingBusinessLogic.GetCompanyByIdentifierAsync(oneId)));

        [HttpPost]
        [Route("company/{realm}/users")]
        public Task<IActionResult> CreateUsersAsync([FromRoute] string realm, [FromHeader] string authorization, [FromBody] List<UserCreationInfo> userToCreate) =>
            ValidateTokenAsync(realm, authorization, async (userInfo) => {
                await _onboardingBusinessLogic.CreateUsersAsync(userToCreate, realm, authorization.Split(" ")[1], userInfo);
                return new OkResult();
            });

        [HttpPut]
        [Route("company/{realm}/companyRoles")]
        public Task<IActionResult> SetCompanyRolesAsync([FromRoute] string realm, [FromHeader] string authorization, [FromBody] CompanyToRoles rolesToSet) =>
            ValidateTokenAsync(realm, authorization, async () => {
                await _onboardingBusinessLogic.SetCompanyRolesAsync(rolesToSet);
                return new OkResult();
            });

        [HttpGet]
        [ProducesResponseType(typeof(List<CompanyRole>), (int)HttpStatusCode.OK)]
        [Route("company/{realm}/companyRoles")]
        public Task<IActionResult> GetCompanyRolesAsync([FromRoute] string realm, [FromHeader] string authorization) =>
            ValidateTokenAsync(realm, authorization, async () =>
                new OkObjectResult(await _onboardingBusinessLogic.GetCompanyRolesAsync()));

        [HttpGet]
        [ProducesResponseType(typeof(List<ConsentForCompanyRole>), (int)HttpStatusCode.OK)]
        [Route("company/{realm}/consentsFoCompanyRole/{roleId}")]
        public Task<IActionResult> GetCompanyRolesAsync([FromRoute] string realm, [FromHeader] string authorization, int roleId) =>
            ValidateTokenAsync(realm, authorization, async () =>
                new OkObjectResult(await _onboardingBusinessLogic.GetConsentForCompanyRoleAsync(roleId)));

        [HttpGet]
        [Route("company/{realm}/signedConsentsByCompanyId/{companyId}")]
        [ProducesResponseType(typeof(List<SignedConsent>), (int)HttpStatusCode.OK)]
        public Task<IActionResult> SignedConsentsByCompanyIdAsync([FromRoute] string realm, [FromHeader] string authorization, string companyId) =>
            ValidateTokenAsync(realm, authorization, async () =>
                new OkObjectResult(await _onboardingBusinessLogic.SignedConsentsByCompanyIdAsync(companyId)));

        [HttpPut]
        [Route("company/{realm}/signConsent")]
        public Task<IActionResult> SignConsentAsync([FromRoute] string realm, [FromHeader] string authorization, [FromBody] SignConsentRequest signConsentRequest) =>
            ValidateTokenAsync(realm, authorization, async () => {
                await _onboardingBusinessLogic.SignConsentAsync(signConsentRequest);
                return new OkResult();
            });

        [HttpPut]
        [Route("company/{realm}/idp")]
        public Task<IActionResult> SetIdpAsync([FromRoute] string realm, [FromHeader] string authorization, [FromBody] SetIdp idpToSet) =>
            ValidateTokenAsync(realm, authorization, async () => {
                await _onboardingBusinessLogic.SetIdpAsync(idpToSet);
                return new OkResult();
            });

        [HttpPut]
        [Route("company/{realm}/finishOnboarding")]
        public Task<IActionResult> FinishOnboardingAsync([FromRoute] string realm, [FromHeader] string authorization) =>
            ValidateTokenAsync(realm, authorization, async () => {
                await _onboardingBusinessLogic.FinishOnboardingAsync(authorization.Split(" ")[1],realm);
                return new OkResult();
            });

        [HttpGet]
        [Route("company/{realm}/userRoles")]
        [ProducesResponseType(typeof(List<string>), (int)HttpStatusCode.OK)]
        public Task<IActionResult> GetUserRolesAsync([FromRoute] string realm, [FromHeader] string authorization) =>
            ValidateTokenAsync(realm, authorization, async () =>
                new OkObjectResult(await _onboardingBusinessLogic.GetAvailableUserRoleAsync()));

        [HttpGet]
        [Route("company/{realm}/availableUserRoles")]
        [ProducesResponseType(typeof(List<string>), (int)HttpStatusCode.OK)]
        public Task<IActionResult> GetAvailableUserRoles([FromRoute] string realm, [FromHeader] string authorization) =>
            ValidateTokenAsync(realm, authorization, async () =>
                new OkObjectResult(await _onboardingBusinessLogic.GetAvailableUserRolesAsync(authorization.Split(" ")[1],realm)));
        
        [HttpGet]
        [Route("company/{realm}/ownUserRoles")]
        [ProducesResponseType(typeof(List<string>), (int)HttpStatusCode.OK)]
        public Task<IActionResult> GetOwnUserRolesAsync([FromRoute] string realm, [FromHeader] string authorization) =>
            ValidateTokenAsync(realm, authorization, async (userInfo) =>
                new OkObjectResult(await _onboardingBusinessLogic.GetOwnUserRolesAsync(authorization.Split(" ")[1],realm, userInfo["sub"])));

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
                if (!response.IsSuccessStatusCode)
                {
                    return new StatusCodeResult((int)HttpStatusCode.Forbidden);
                }
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
