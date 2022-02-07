namespace CatenaX.NetworkServices.Registration.Service.CDQ.Model
{

    public class FetchBusinessPartnerDto
    {
        public string cdqId { get; set; }
        public string dataSource { get; set; }
        public Businesspartner businessPartner { get; set; }
    }

    public class Businesspartner
    {
        public Name[] names { get; set; }
        public Legalform legalForm { get; set; }
        public Identifier[] identifiers { get; set; }
        public object[] categories { get; set; }
        public Address[] addresses { get; set; }
        public string externalId { get; set; }
        public Formattedsaprecord formattedSapRecord { get; set; }
        public object[] types { get; set; }
    }

    public class Legalform
    {
        public string name { get; set; }
    }

    public class Formattedsaprecord
    {
        public string name1 { get; set; }
        public string legalEntity { get; set; }
        public string legalForm { get; set; }
        public string narp { get; set; }
        public string stceg { get; set; }
        public string country { get; set; }
        public string countryCode { get; set; }
        public string region { get; set; }
        public string regionCodeSap { get; set; }
        public string regionCode { get; set; }
        public string county { get; set; }
        public string countyCode { get; set; }
        public string city { get; set; }
        public string district { get; set; }
        public string street1 { get; set; }
        public string houseNum { get; set; }
        public string latitude { get; set; }
        public string longitude { get; set; }
        public string postalCode { get; set; }
    }

    public class Name
    {
        public Type type { get; set; }
        public string value { get; set; }
    }

    public class Type
    {
        public string url { get; set; }
        public string name { get; set; }
        public string technicalKey { get; set; }
    }

    public class Identifier
    {
        public Type type { get; set; }
        public string value { get; set; }
        public Status status { get; set; }
    }
    public class Status
    {
        public string technicalKey { get; set; }
    }

    public class Address
    {
        public Country country { get; set; }
        public Administrativearea[] administrativeAreas { get; set; }
        public Postcode[] postCodes { get; set; }
        public Locality[] localities { get; set; }
        public Thoroughfare[] thoroughfares { get; set; }
        public object[] premises { get; set; }
        public Geographiccoordinates geographicCoordinates { get; set; }
        public Type[] types { get; set; }
        public Formattedaddress formattedAddress { get; set; }
    }

    public class Country
    {
        public string shortName { get; set; }
        public string value { get; set; }
    }

    public class Geographiccoordinates
    {
        public float latitude { get; set; }
        public float longitude { get; set; }
    }

    public class Formattedaddress
    {
        public string country { get; set; }
        public string administrativeArea { get; set; }
        public string region { get; set; }
        public string regionCode { get; set; }
        public string locality { get; set; }
        public string district { get; set; }
        public string postalCode { get; set; }
        public string thoroughfare { get; set; }
    }

    public class Administrativearea
    {
        public string value { get; set; }
        public string shortName { get; set; }
    }

    public class Postcode
    {
        public string value { get; set; }
        public Type type { get; set; }
    }

    public class Locality
    {
        public string value { get; set; }
    }

    public class Thoroughfare
    {
        public Type type { get; set; }
        public string number { get; set; }
        public string value { get; set; }
    }


}



