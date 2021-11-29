using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Threading.Tasks;
using CatenaX.NetworkServices.Cosent.Library.Data;
using CatenaX.NetworkServices.Onboarding.Service.Model;

using Dapper;

namespace CatenaX.NetworkServices.Onboarding.Service.OnboardingAccess
{
    public class OnboardingDBAccess : IOnboardingDBAccess
    {
        private readonly IDbConnection _dbConnection;

        public OnboardingDBAccess(IDbConnection dbConnection)
        {
            _dbConnection = dbConnection;
        }

        public async Task<IEnumerable<CompanyRole>> GetAllCompanyRoles()
        {
            string sql = "SELECT * from public.companyroles";

            using (_dbConnection)
            {
                var result = await _dbConnection.QueryAsync<CompanyRole>(sql);
                return result;
            }
        }

        public async Task<IEnumerable<ConsentForCompanyRole>> GetConsentForCompanyRole(int roleId)
        {
            var sql = $"select * from get_company_role({roleId})";

            using (_dbConnection)
            {
                return await _dbConnection.QueryAsync<ConsentForCompanyRole>(sql);
            }
        }

        public async Task SetCompanyRoles(CompanyToRoles rolesToSet)
        {
            using (_dbConnection)
            {
                foreach (var role in rolesToSet.roles)
                {
                    var parameters = new { roleId = role, companyId = rolesToSet.CompanyId };
                    string sql = "Insert Into public.company_selected_roles (company_id, role_id) values(@companyId, @roleId)";


                    await _dbConnection.ExecuteAsync(sql, parameters);

                }
            }
        }

        public async Task SetIdp(SetIdp idpToSet)
        {
            using (_dbConnection)
            {
                    var parameters = new { companyId = idpToSet.companyId, idp = idpToSet.idp };
                    string sql = "Insert Into public.company_selected_idp (company_id, idp) values(@companyId, @idp)";
                    await _dbConnection.ExecuteAsync(sql, parameters);
            }
        }

        public async Task SignConsent(SignConsentRequest signedConsent)
        {
            var sql = $"SELECT sign_consent('{signedConsent.companyId}',{signedConsent.consentId},{signedConsent.companyRoleId}, '{signedConsent.userName}')";

            using (_dbConnection)
            {
                await _dbConnection.ExecuteAsync(sql);
            }
        }

        public async Task<IEnumerable<SignedConsent>> SignedConsentsByCompanyId(string companyId)
        {
            var sql = $"select * from get_signed_consents_for_company_id('{companyId}')";


            using (_dbConnection)
            {
                return await _dbConnection.QueryAsync<SignedConsent>(sql);
            }
        }
    }
}
