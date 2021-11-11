using System;
using System.Collections.Generic;
using System.Text;

namespace CatenaX.NetworkServices.Invitation.Identity.Model
{
    public class CreateUser
    {
        public string Id { get; set; }
        public long CreatedTimestamp { get; set; }
        public string UserName { get; set; }
        public bool? Enabled { get; set; }
        public bool? Totp { get; set; }
        public bool? EmailVerified { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Email { get; set; }
        public IDictionary<string, object> ClientRoles { get; set; }
        public IEnumerable<string> Groups { get; set; }
        public string Origin { get; set; }
        public IEnumerable<string> RealmRoles { get; set; }
    }
}
