using System.Collections.Generic;
using System.Linq;

namespace CatenaX.NetworkServices.Mockups
{
    public class QueryCompany
    {
        List<Company> companies = new List<Company>(){
new Company(){
bpn = "CAXLZJVJEBYWYYZZ",
accountGroup = "OEM",
name1 = "BMW",
addressVersion = "EN",
country = "Germany",
city = "Tucker",
postalCode = 61828,
street1 = "Ridgefield",
houseNumber = 17,
taxNumber1 = "DE084342972",
taxNumber1Type = "UST-ID",
vatNumber = "DE084342972",
vatNumberType = "UST-ID",
},
new Company(){
bpn = "CAXSPGQORIGHFAZZ",
parent = "CAXLZJVJEBYWYYZZ",
accountGroup = "OEM Plant",
name1 = "BMWPlantMunich",
addressVersion = "EN",
country = "Germany",
city = "Austell",
postalCode = 16679,
street1 = "Shaliman",
houseNumber = 31,
taxNumber1 = "DE470914600",
taxNumber1Type = "UST-ID",
vatNumber = "DE470914600",
vatNumberType = "UST-ID",
},
new Company(){
bpn = "CAXSPGQORIGHFAYZ",
parent = "CAXLZJVJEBYWYYZZ",
accountGroup = "OEM Plant",
name1 = "Contoso",
addressVersion = "EN",
country = "Germany",
city = "Austell",
postalCode = 16679,
street1 = "Shaliman",
houseNumber = 31,
taxNumber1 = "DE470914600",
taxNumber1Type = "UST-ID",
vatNumber = "DE470914600",
vatNumberType = "UST-ID",
}
};

        public Company Query(string oneID)
        {
            return companies.FirstOrDefault(c => c.bpn == oneID);
        }

    }
}