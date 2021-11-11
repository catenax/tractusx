using System;
using System.Net;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

using CatenaX.NetworkServices.Provisioning.Service.BusinessLogic;

namespace CatenaX.NetworkServices.Provisioning.Service.Controllers
{
    [ApiController]
    [Route("api/provisioning")]
    public class ProvisioningController
    {
        private readonly ILogger<ProvisioningController> _logger;
        private readonly IProvisioningService _ProvisioningService;
        public ProvisioningController(ILogger<ProvisioningController> logger, IProvisioningService provisioningService)
        {
            _logger = logger;
            _ProvisioningService = provisioningService;
        }

        [HttpGet]
        [Route("trigger")]
        public async Task<ActionResult> ExecuteProvisioning()
        {
            try
            {
                await _ProvisioningService.CheckAndExecuteProvisioningAsync();
                return new OkResult();
            }
            catch (Exception e)
            {
                _logger.LogError(e.ToString());
                return new StatusCodeResult((int)HttpStatusCode.InternalServerError);
            }
        }
    }
}
