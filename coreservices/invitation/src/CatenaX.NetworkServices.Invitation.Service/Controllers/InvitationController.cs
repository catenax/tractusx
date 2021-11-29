using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Threading.Tasks;

using CatenaX.NetworkServices.Invitation.Library;
using CatenaX.NetworkServices.Invitation.Service.BusinessLogic;

using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

namespace CatenaX.NetworkServices.Invitation.Service.Controllers
{
    [ApiController]
    [Route("api/invitation")]
    public class InvitationController : ControllerBase
    {

        private readonly ILogger<InvitationController> _logger;
        private readonly IInvitationBusinessLogic _logic;

        public InvitationController(ILogger<InvitationController> logger, IInvitationBusinessLogic logic)
        {
            _logger = logger;
            _logic = logic;
        }

        [HttpPut]
        [Route("execute/{identifier}")]
        public async Task<IActionResult> ExecuteInvitation(string identifier)
        {
            try
            {
                await _logic.ExecuteInvitation(identifier);
                return new OkResult();
            } catch(Exception e)
            {
                _logger.LogError(e.ToString());
                return new StatusCodeResult((int)HttpStatusCode.InternalServerError);
            }
        }

        [HttpPost]
        public async Task<IActionResult> ExecuteInvitation([FromBody] InvitationData InvitationData)
        {
            try
            {
                await _logic.StartInvitation(InvitationData);
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
