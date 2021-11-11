using System.Collections.Generic;
using System.Threading.Tasks;

namespace CatenaX.NetworkServices.Provisioning.ActiveDirectory
{
    public interface IFederation
    {
        Task CreateFederationAsync(IDictionary<string,string> values);
        Task SendInvitationAsync(string email);
    }
}
