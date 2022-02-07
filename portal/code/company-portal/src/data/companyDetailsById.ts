export class FetchBusinessPartnerDto
{
    public bpn: string;
    public relations: object[];
    public identifiers: Identifier[];
    public names: Name[];
    public legalForm: Legalform;
    public status: string;
    public addresses: Address[];
    public profile: Profile;
    public types: string[];
    public bankAccounts: Bankaccount[];
    public roles: string[];
}

export class Legalform
{
    public type: string;
    public value: string;
    public shortName: string;
    public number: number;
}

export class Profile
{
    public classifications: Classification[];
}

export class Classification
{
    public type: string;
    public value: string;
    public shortName: string;
    public number: number;
}

export class Identifier
{
    public type: string;
    public registration: Registration;
    public value: string;
    public shortName: string;
    public number: number;
}

export class Registration
{
    public hardeningGrade: string;
    public issuingAgency: Issuingagency;
    public status: string;
    public initialRegistration: string;
    public lastUpdate: string;
}

export class Issuingagency
{
    public value: string;
    public shortName: string;
    public number: number;
}

export class Name
{
    public type: string;
    public value: string;
    public shortName: string;
    public number: number;
}

export class Address
{
    public bpn: string;
    public identifiers: Identifier[];
    public careOf: Careof;
    public countryCode: string;
    public administrativeAreas: Administrativearea[];
    public postCodes: Postcode[];
    public localities: Locality[];
    public thoroughfares: Thoroughfare[];
    public premises: Premis[];
    public postalDeliveryPoints: Postaldeliverypoint[];
    public type: string;
    public versions: Version[];
}

export class Careof
{
    public value: string;
    public shortName: string;
    public number: number;
}


export class Administrativearea
{
    public name: string;
    public codes: object[];
    public type: string;
}

export class Postcode
{
    public type: string;
    public value: string;
    public shortName: string;
    public number:number;
}

export class Locality
{
    public type: string;
    public value: string;
    public shortName: string;
    public number: number;
}

export class Thoroughfare
{
    public type: string;
    public value: string;
    public shortName: string;
    public number: number;
}

export class Premis
{
    public type: string;
    public value: string;
    public shortName: string;
    public number: number;
}

export class Postaldeliverypoint
{
    public type: string;
    public value: string;
    public shortName: string;
    public number: number;
}

export class Version
{
    public characterSet: string;
    public languageCode: string;
}

export class Bankaccount
{
    public trustScores: string;
    public currencyCode: string;
    public internationalBankAccountIdentifier: string;
    public internationalBankIdentifier: string;
    public nationalBankAccountIdentifier: string;
    public nationalBankIdentifier: string;
}
