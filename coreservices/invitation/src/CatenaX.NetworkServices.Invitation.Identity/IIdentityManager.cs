using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

using CatenaX.NetworkServices.Invitation.Identity.Model;

namespace CatenaX.NetworkServices.Invitation.Identity
{
    public interface IIdentityManager
    {
        Task CreateRealm(CreateRealm realm);

        Task<string> CreateUser(string realm, CreateUser user);

        Task CreateGroup(string realm, CreateGroup group);

        Task AssignRolesToUser(string realm, string username, string clientId, List<string> roleNames);

        Task CreateClient(string realm, string clientName);
    }
}
