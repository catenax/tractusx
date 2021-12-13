export class CompanyDetails {
    public cdqId: string;
    public dataSource: string;
    public businessPartner: Businesspartner;
}
export class Businesspartner {
    public names: Name[];
    public identifiers: Identifier[];
    public categories: object[];
    public addresses: Address[];
    public formattedSapRecord: Formattedsaprecord;
    public types: object[];
}
export class Formattedsaprecord {
    public narp: string;
    public stceg: string;
    public country: string;
    public countryCode: string;
    public city: string;
    public postalCode: string;
    public street1: string;
    public houseNum: string;
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
    public administrativeAreas: object[];
    public postCodes: Postcode[];
    public localities: Locality[];
    public thoroughfares: Thoroughfare[];
    public premises: object[];
    public postalDeliveryPoints: object[];
    public types: Type[];
    public formattedAddress: Formattedaddress;
}
export class Country {
    public shortName: string;
    public value: string;
}
export class Formattedaddress {
    public country: string;
    public locality: string;
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