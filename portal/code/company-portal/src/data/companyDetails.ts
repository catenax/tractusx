export class CompanyDetails{
    public bpn: string;
    public parent: string;
    public accountGroup: string;
    public name1: string;
    public name2: string;
    public name3: string;
    public name4: string;
    public addressVersion: string;
    public country: string;
    public city: string;
    public postalCode: 0;
    public street1: string;
    public street2: string;
    public street3: string;
    public houseNumber: 0;
    public taxNumber1: string;
    public taxNumber1Type: string;
    public taxNumber2: string;
    public taxNumber2Type: string;
    public taxNumber3: string;
    public taxNumber3Type: string;
    public taxNumber4: string;
    public taxNumber4Type: string;
    public taxNumber5: string;
    public taxNumber5Type: string;
    public vatNumber: string;
    public vatNumberType: string
  }

 export class CompanyRole{
    public id: number;
    public title: string;
 }

 export class UserRole{
    public role: string;
 }

 export class ConsentForCompanyRoles{
    public role_id : number;
    public role_title : string;
    public consent_id : number;
    public consent_title : string;
    public link: string;
 }