using Microsoft.Identity.Client;

namespace CatenaX.NetworkServices.Provisioning.ActiveDirectory.Model
{
    public class Token
    {
        public Token(AuthenticationResult authenticationResult)
        {
            Value = authenticationResult.AccessToken;
            Type = authenticationResult.TokenType;
        }
        public string Value { get; }
        public string Type { get; }
    }
}
