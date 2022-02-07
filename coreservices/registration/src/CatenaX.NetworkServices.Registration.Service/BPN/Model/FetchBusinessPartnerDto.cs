using System;

namespace CatenaX.NetworkServices.Registration.Service.BPN.Model
{


    public class FetchBusinessPartnerDto
    {
        public string bpn { get; set; }
        public object[] relations { get; set; }
        public Identifier[] identifiers { get; set; }
        public Name[] names { get; set; }
        public Legalform legalForm { get; set; }
        public string status { get; set; }
        public Address[] addresses { get; set; }
        public Profile profile { get; set; }
        public string[] types { get; set; }
        public Bankaccount[] bankAccounts { get; set; }
        public string[] roles { get; set; }
    }

    public class Legalform
    {
        public string type { get; set; }
        public string value { get; set; }
        public string shortName { get; set; }
        public int? number { get; set; }
    }

    public class Profile
    {
        public Classification[] classifications { get; set; }
    }

    public class Classification
    {
        public string type { get; set; }
        public string value { get; set; }
        public string shortName { get; set; }
        public int? number { get; set; }
    }

    public class Identifier
    {
        public string type { get; set; }
        public Registration registration { get; set; }
        public string value { get; set; }
        public string shortName { get; set; }
        public int? number { get; set; }
    }

    public class Registration
    {
        public string hardeningGrade { get; set; }
        public Issuingagency issuingAgency { get; set; }
        public string status { get; set; }
        public DateTime initialRegistration { get; set; }
        public DateTime lastUpdate { get; set; }
    }

    public class Issuingagency
    {
        public string value { get; set; }
        public string shortName { get; set; }
        public int? number { get; set; }
    }

    public class Name
    {
        public string type { get; set; }
        public string value { get; set; }
        public string shortName { get; set; }
        public int? number { get; set; }
    }

    public class Address
    {
        public string bpn { get; set; }
        public Identifier[] identifiers { get; set; }
        public Careof careOf { get; set; }
        public string countryCode { get; set; }
        public Administrativearea[] administrativeAreas { get; set; }
        public Postcode[] postCodes { get; set; }
        public Locality[] localities { get; set; }
        public Thoroughfare[] thoroughfares { get; set; }
        public Premis[] premises { get; set; }
        public Postaldeliverypoint[] postalDeliveryPoints { get; set; }
        public string type { get; set; }
        public Version[] versions { get; set; }
    }

    public class Careof
    {
        public string value { get; set; }
        public string shortName { get; set; }
        public int? number { get; set; }
    }


    public class Administrativearea
    {
        public string name { get; set; }
        public object[] codes { get; set; }
        public string type { get; set; }
    }

    public class Postcode
    {
        public string type { get; set; }
        public string value { get; set; }
        public string shortName { get; set; }
        public int? number { get; set; }
    }

    public class Locality
    {
        public string type { get; set; }
        public string value { get; set; }
        public string shortName { get; set; }
        public int? number { get; set; }
    }

    public class Thoroughfare
    {
        public string type { get; set; }
        public string value { get; set; }
        public string shortName { get; set; }
        public int? number { get; set; }
    }

    public class Premis
    {
        public string type { get; set; }
        public string value { get; set; }
        public string shortName { get; set; }
        public int? number { get; set; }
    }

    public class Postaldeliverypoint
    {
        public string type { get; set; }
        public string value { get; set; }
        public string shortName { get; set; }
        public int? number { get; set; }
    }

    public class Version
    {
        public string characterSet { get; set; }
        public string languageCode { get; set; }
    }

    public class Bankaccount
    {
        public double[] trustScores { get; set; }
        public string currencyCode { get; set; }
        public string internationalBankAccountIdentifier { get; set; }
        public string internationalBankIdentifier { get; set; }
        public string nationalBankAccountIdentifier { get; set; }
        public string nationalBankIdentifier { get; set; }
    }



}



