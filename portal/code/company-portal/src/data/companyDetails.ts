export class CompanyDetails {
    public cdqId: string;
    public dataSource: string;
    public businessPartner: Businesspartner;
}
export class Businesspartner {
    public names: Name[];
    public legalForm: Legalform;
    public identifiers: Identifier[];
    public categories: [];
    public addresses: Address[];
    public externalId: string;
    public formattedSapRecord: Formattedsaprecord;
    public types: [];
}

export class Legalform {
    public name: string;
}

export class Formattedsaprecord {
    public name1: string;
    public legalEntity: string;
    public legalForm: string;
    public narp: string;
    public stceg: string;
    public country: string;
    public countryCode: string;
    public region: string;
    public regionCodeSap: string;
    public regionCode: string;
    public county: string;
    public countyCode: string;
    public city: string;
    public district: string;
    public street1: string;
    public houseNum: string;
    public latitude: string;
    public longitude: string;
    public postalCode: string;
}
export class Name {
    public type: Type;
    public value: string;
    public language: Language;
}
export class Type {
    public url: string;
    public name: string;
    public technicalKey: string;
}
export class Language {
}
export class Identifier {
    public type: Type;
    public value: string;
    public status: Status;
}
export class Status {
    public technicalKey: string;
}
export class Address {
    public country: Country;
    public administrativeAreas: Administrativearea[];
    public postCodes: Postcode[];
    public localities: Locality[];
    public thoroughfares: Thoroughfare[];
    public premises: object[];
    public postalDeliveryPoints: object[];
    public types: Type[];
    public formattedAddress: Formattedaddress;
    public geographicCoordinates: Geographiccoordinates

}

export class Administrativearea {
    public value: string;
    public shortName: string;
}
export class Country {
    public shortName: string;
    public value: string;
}
export class Formattedaddress {
    public country: string;
    public administrativeArea: string;
    public region: string;
    public regionCode: string;
    public locality: string;
    public district: string;
    public postalCode: string;
    public thoroughfare: string;
}
export class Postcode {
    public value: string;
    public type: Type;
}
export class Locality {
    public value: string;
}
export class Thoroughfare {
    public type: Type;
    public number: string;
    public value: string;
}

export class Geographiccoordinates {
    public latitude: number;
    public longitude: number;
}
export class CompanyRole {
    public id: number;
    public title: string;
}


export class UserRole {
    public role: string;
}

export class ConsentForCompanyRoles {
    public role_id: number;
    public role_title: string;
    public consent_id: number;
    public consent_title: string;
    public link: string;
}
export abstract class CompanyTechnicalKey {
    public static International = "INTERNATIONAL";
}



