using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Threading.Tasks;

using Dapper;

namespace CatenaX.NetworkServices.Invitation.Service.DataAccess
{
    public class DataAccessTest : IDataAccess
    {
        private readonly IDbConnection _dbConnection;

        public DataAccessTest(IDbConnection dbConnection)
        {
            _dbConnection = dbConnection;
        }
        public async Task<DataContract> GetData(int id)
        {
            var parameters = new { id = id };
            string sql = "SELECT * FROM public.persons WHERE PersonId = @id;";

            using (_dbConnection)
            {
                var result = await _dbConnection.QueryAsync<DataContract>(sql, parameters);
                return result.First();
            }
        }
    }
}
