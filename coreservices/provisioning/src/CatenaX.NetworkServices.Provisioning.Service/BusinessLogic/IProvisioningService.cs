using System.Threading.Tasks;

namespace CatenaX.NetworkServices.Provisioning.Service.BusinessLogic
{
    public interface IProvisioningService
    {
        Task CheckAndExecuteProvisioningAsync();
    }
}
