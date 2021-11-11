using System;

namespace CatenaX.NetworkServices.Provisioning.Keycloak
{
    public class GroupNotDeletedException: Exception
    {
        public GroupNotDeletedException(string realm, string group):
            base(String.Format("group {0} in realm {1} not deleted",group,realm)) {}
    }
}
