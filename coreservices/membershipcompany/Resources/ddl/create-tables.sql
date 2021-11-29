CREATE TABLE IF NOT EXISTS business_partners (
    bpn VARCHAR(200) PRIMARY KEY,
    parent VARCHAR(200),
    accountGroup VARCHAR(200),
    name1 VARCHAR(200) NOT NULL,
    name2 VARCHAR(200),
    name3 VARCHAR(200),
    name4 VARCHAR(200),
    addressVersion VARCHAR(200),
    country VARCHAR(200),
    city VARCHAR(200),
    postalCode VARCHAR(200),
    street1 VARCHAR(200),
    street2 VARCHAR(200),
    street3 VARCHAR(200),
    houseNumber VARCHAR(200),
    taxNumber1 VARCHAR(200),
    taxNumber1Type VARCHAR(200),
    taxNumber2 VARCHAR(200),
    taxNumber2Type VARCHAR(200),
    taxNumber3 VARCHAR(200),
    taxNumber3Type VARCHAR(200),
    taxNumber4 VARCHAR(200),
    taxNumber4Type VARCHAR(200),
    taxNumber5 VARCHAR(200),
    taxNumber5Type VARCHAR(200),
    vatNumber VARCHAR(200),
    vatNumberType VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS member_companies (
    bpn VARCHAR(200) NOT NULL,
    name VARCHAR(200) NOT NULL,
    parent VARCHAR(200),
    roles JSON NOT NULL
);

CREATE TABLE IF NOT EXISTS member_company_roles (
    rolename VARCHAR(200) NOT NULL
);
