using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

using CatenaX.NetworkServices.Invitation.Library;

namespace CatenaX.NetworkServices.Invitation.Service.BusinessLogic
{
    public interface IInvitationBusinessLogic
    {
        Task ExecuteInvitation(string identifier);

        Task StartInvitation(InvitationData InvitationData);
    }
}
