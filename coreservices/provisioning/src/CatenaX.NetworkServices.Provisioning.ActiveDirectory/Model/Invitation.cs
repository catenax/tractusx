using System.Text.Json.Serialization;

namespace CatenaX.NetworkServices.Provisioning.ActiveDirectory.Model
{
    public class Invitation
    {
    // "invitedUserEmailAddress": <recipient>,
        [JsonPropertyName("invitedUserEmailAddress")]
        public string InvitedUserEmailAddress { get; set; }

    // "inviteRedirectUrl": https://catenax-dev003-app-portal.azurewebsites.net/home/dashboard,
        [JsonPropertyName("inviteRedirectUrl")]
        public string InviteRedirectUrl { get; set; }

    // "sendInvitationMessage": "false"
        [JsonPropertyName("sendInvitationMessage")]
        public bool SendInvitationMessage { get; set; }
    }
}
