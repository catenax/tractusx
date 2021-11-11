using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

using CatenaX.NetworkServices.Invitation.Identity;
using CatenaX.NetworkServices.Invitation.Identity.Model;
using CatenaX.NetworkServices.Invitation.Library;
using CatenaX.NetworkServices.Invitation.Service.DataAccess;
using CatenaX.NetworkServices.Mailing.SendMail;
using CatenaX.NetworkServices.Mockups;

using Microsoft.Extensions.Configuration;

namespace CatenaX.NetworkServices.Invitation.Service.BusinessLogic
{
    public class InvitationBusinessLogic : IInvitationBusinessLogic
    {
        private readonly IIdentityManager _identityManager;
        private readonly IMailingService _mailingService;
        private readonly IConfiguration _configuration;
        //private readonly IDataAccess _dataAccess;

        public InvitationBusinessLogic(IIdentityManager identityManager, IMailingService mailingService, IConfiguration configuration)
        {
            _identityManager = identityManager;
            _mailingService = mailingService;
            _configuration = configuration;
        }

        public async Task ExecuteInvitation(string identifier)
        {
        //    var result = await _dataAccess.GetData(int.Parse(identifier));

            //Create Realm and Admin User

            //Create Additional Users
        }

        public async Task StartInvitation(InvitationData InvitationData)
        {
            //Get Data From Member
            var query = new QueryCompany();

            var company = query.Query(InvitationData.OneId);

            var realmName = company.name1.Replace(' ','-');

            var newRealm = new CreateRealm
            {
                Name = realmName
            };

            await _identityManager.CreateRealm(newRealm);



            foreach (string group in UserRoles.Roles)
            {
                var newGroup = new CreateGroup {
                    Name = group
                };
                await _identityManager.CreateGroup(realmName, newGroup);
            }

            var newUser = new CreateUser
            {
                UserName = InvitationData.EMail,
                Email = InvitationData.EMail,
                Groups = new List<string> { "Invitation" }
            };

            var password = await _identityManager.CreateUser(realmName, newUser);

            await _identityManager.AssignRolesToUser(realmName, InvitationData.EMail, "realm-management", new List<string> { "manage-users" });

            await _identityManager.CreateClient(realmName, $"client-{realmName.ToLower()}");

            var mailParameters = new Dictionary<string, string>
            {
                { "password", password },
                { "companyname", realmName },
                { "url", $"{_configuration.GetValue<string>("BasePortalAddress")}/?company={realmName}&user={InvitationData.EMail}&oneId={InvitationData.OneId}"  }
            };

            await _mailingService.SendMails(InvitationData.EMail, mailParameters, new List<string> { "invite", "password"} );
        }
    }

}
