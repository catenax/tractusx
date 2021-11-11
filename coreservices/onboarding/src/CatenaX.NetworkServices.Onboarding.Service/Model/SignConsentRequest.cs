namespace CatenaX.NetworkServices.Onboarding.Service.Model
{
    public class SignConsentRequest
    {
        public int companyRoleId { get; set; }

        public int consentId { get; set; }

        public string companyId { get; set; }

        public string userName { get; set; }
    }
}
