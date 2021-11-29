using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace CatenaX.NetworkServices.Invitation.Service.DataAccess
{
    public interface IDataAccess
    {
        public Task<DataContract> GetData(int id);
    }
}
