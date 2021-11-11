using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Threading.Tasks;

using CatenaX.NetworkServices.Invitation.Identity.Identity;
using CatenaX.NetworkServices.Invitation.Service.BusinessLogic;
using CatenaX.NetworkServices.Invitation.Service.Controllers;

using Keycloak.Net.Models.Clients;
using Keycloak.Net.Models.RealmsAdmin;
using Keycloak.Net.Models.Roles;

using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging.Abstractions;

using Newtonsoft.Json;

using NSubstitute;
using NSubstitute.ExceptionExtensions;

using Xunit;

namespace CatenaX.NetworkServices.Invitation.Service.Tests
{
    public class InvitationControllerTest
    {
        [Fact]
        public async Task ExecuteInvitation_ReturnsInternalServerError_OnException()
        {
            //Setup
            var InvitationBusinessLogic = Substitute.For<IInvitationBusinessLogic>(); 
            InvitationBusinessLogic.ExecuteInvitation(Arg.Is("testIdent")).Throws<ArgumentException>();
            var sut = new InvitationController(NullLogger<InvitationController>.Instance, InvitationBusinessLogic);
            
            //Execute
            var result = await sut.ExecuteInvitation("testIdent");
            Assert.IsType<StatusCodeResult>(result);
            var statusCodeResult = result as StatusCodeResult;
            Assert.Equal(((int)HttpStatusCode.InternalServerError).ToString(), statusCodeResult.StatusCode.ToString());
        }
    }
}
