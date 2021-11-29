alter table model alter column model_definition TYPE VARCHAR(16384);

insert into model(id,private,name,publisher,type,version,model_definition) VALUES (
    'urn:bamm:com.catenaX:0.0.1:Material',FALSE,'Material','Catena-X Consortium','BAMM','0.0.1',
    '@prefix xsd: <http://www.w3.org/2001/XMLSchema#>.
@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#>.
@prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#>.
@prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#>.
@prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#>.
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>.
@prefix : <urn:bamm:com.catenaX:0.0.1#>.

:Material a bamm:Aspect;
    bamm:name "Material";
    bamm:operations ();
    bamm:properties (:materialDetails).
:materialDetails a bamm:Property;
    bamm:name "materialDetails";
    bamm:characteristic :MaterialCharacteristic.
:MaterialCharacteristic a bamm:Characteristic;
    bamm:name "MaterialCharacteristic";
    bamm:dataType :MaterialEntity.
:MaterialEntity a bamm:Entity;
    bamm:name "MaterialEntity";
    bamm:properties (:materialName :materialType :aggregateState [
  bamm:property :chemicalComposition;
  bamm:optional "true"^^xsd:boolean
] :weight [
  bamm:property :chemicalCompositionFraction;
  bamm:optional "true"^^xsd:boolean
]).
:materialName a bamm:Property;
    bamm:name "materialName";
    bamm:characteristic :MaterialNamesEnumeration;
    bamm:description "The name of a material"@en;
    bamm:exampleValue "aluminium".
:materialType a bamm:Property;
    bamm:name "materialType";
    bamm:characteristic :MaterialTypesEnumeration;
    bamm:description "The type of a material"@en;
    bamm:exampleValue "metal".
:aggregateState a bamm:Property;
    bamm:name "aggregateState";
    bamm:characteristic :AggregateStatesEnumeration;
    bamm:description "The aggregate state of a material at ambient conditions"@en;
    bamm:exampleValue "solid".
:chemicalComposition a bamm:Property;
    bamm:name "chemicalComposition";
    bamm:characteristic :materialCollection.
:materialCollection a bamm-c:Collection;
    bamm:name "materialCollection";
    bamm-c:elementCharacteristic :MaterialCharacteristic.
:MaterialNamesEnumeration a bamm-c:Enumeration;
    bamm:name "MaterialNamesEnumeration";
    bamm:dataType xsd:string;
    bamm:description "An enumeration of possible material names"@en;
    bamm-c:values ("aluminium" "polyamide" "others").
:MaterialTypesEnumeration a bamm-c:Enumeration;
    bamm:name "MaterialTypesEnumeration";
    bamm:dataType xsd:string;
    bamm:description "An enumeration of possible types of a material"@en;
    bamm-c:values ("metal" "plastic" "other").
:AggregateStatesEnumeration a bamm-c:Enumeration;
    bamm:name "AggregateStatesEnumeration";
    bamm:dataType xsd:string;
    bamm:description "An enumeration of possible aggregate states"@en;
    bamm-c:values ("gas" "liquid" "solid").
:weight a bamm:Property;
    bamm:name "weight";
    bamm:characteristic :Weight.
:Weight a bamm-c:Quantifiable;
    bamm:name "Weight";
    bamm:dataType xsd:double;
    bamm:description "Weight of the material"@en;
    bamm-c:unit unit:kilogram.
:chemicalCompositionFraction a bamm:Property;
    bamm:name "chemicalCompositionFraction";
    bamm:characteristic :Fraction;
    bamm:description "Fraction of the material in a chemical composition. Is not set if material is not part of a material composition."@en;
    bamm:exampleValue "0.23"^^xsd:double.
:Fraction a bamm:Characteristic;
    bamm:name "Fraction";
    bamm:dataType xsd:double;
    bamm:description "Fraction of something"@en.
:Constraint1 a bamm:Constraint;
    bamm:name "Constraint1".
:Constraint2 a bamm:Constraint;
    bamm:name "Constraint2".
'
);

insert into model(id,private,name,publisher,type,version,model_definition) VALUES (
    'urn:bamm:com.catenaX:0.0.1:ProductDescription',FALSE,'ProductDescription','Catena-X Consortium','BAMM','0.0.1',
    '@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .
@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#> .
@prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#> .
@prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#> .
@prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#> .
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>.
@prefix : <urn:bamm:com.catenaX:0.0.1#>.

:ProductDescription a bamm:Aspect;
    bamm:name "ProductDescription";
    bamm:properties (:productID :productName :productDescription :productionDateGMT 
    [
        bamm:property :buildPosition;
        bamm:optional "true"^^xsd:boolean
    ]
    [
        bamm:property :liquidBearing;
        bamm:optional "true"^^xsd:boolean
    ]
    [
        bamm:property :productWeight;
        bamm:optional "true"^^xsd:boolean
    ]
    [
        bamm:property :performanceIndicator;
        bamm:optional "true"^^xsd:boolean
    ]
    [
        bamm:property :productDimension;
        bamm:optional "true"^^xsd:boolean
    ]
    [
        bamm:property :productType;
        bamm:optional "true"^^xsd:boolean
    ] );
    bamm:operations ();
    bamm:preferredName "Product Description"@en, "Produktbeschreibung"@de.
:productName a bamm:Property;
    bamm:name "productName";
    bamm:description "name of the product"@en;
    bamm:characteristic bamm-c:Text .
:productDescription a bamm:Property;
    bamm:name "productDescription";
    bamm:description "description of the product"@en;
    bamm:characteristic bamm-c:Text .
:productID a bamm:Property;
    bamm:name "productID";
    bamm:description "identification of the product"@en;
    bamm:characteristic bamm-c:Text .
:buildPosition a bamm:Property;
    bamm:name "buildPosition";
    bamm:description "building position of the product"@en;
    bamm:characteristic bamm-c:Text .
:liquidBearing a bamm:Property;
    bamm:name "liquidBearing";
    bamm:description "indicator whether a liquid bearing is present"@en;
    bamm:characteristic bamm-c:Boolean .

:performanceIndicator a bamm:Property;
    bamm:name "performanceIndicator";
    bamm:description "The performance indicator"@en;
    bamm:characteristic :PerformanceIndicatorCharacteristic .
:PerformanceIndicatorCharacteristic a bamm-c:SingleEntity;
    bamm:name "performanceIndicatorCharacteristic";
    bamm:dataType :PerformanceIndicatorEntity .
:PerformanceIndicatorEntity a bamm:Entity;
    bamm:name "performanceIndicatorEntity";
    bamm:properties (:electrictiyCapacityMin :electrictiyCapacityMax) .
:electrictiyCapacityMin a bamm:Property;
    bamm:name "electricCapacityMin";
    bamm:description "The minimal electric capacity"@en;
    bamm:characteristic :Capacity .
:electrictiyCapacityMax a bamm:Property;
    bamm:name "electricCapacityMax";
    bamm:description "The maximal electric capacity"@en;
    bamm:characteristic :Capacity .
:Capacity a bamm-c:Measurement;
    bamm:name "capacity";
    bamm:description "A capacity value"@en;
    bamm-c:unit unit:farad;
    bamm:dataType xsd:double .

:productWeight a bamm:Property;
    bamm:name "productWeight";
    bamm:description "weight in gramm"@en;
    bamm:characteristic :Weight .
:Weight a bamm-c:Measurement;
    bamm:name "weight";
    bamm:description "weight in g"@en;
    bamm-c:unit unit:gram;
    bamm:dataType xsd:double .

:productionDateGMT a bamm:Property;
    bamm:name "productionDateGMT";
    bamm:description "the production data in GMT time zone"@en;
    bamm:exampleValue "2021-09-30"^^xsd:dateTime;
    bamm:characteristic bamm-c:Timestamp .

:productDimension a bamm:Property;
    bamm:name "productDimension";
    bamm:description "The performance indicator"@en;
    bamm:characteristic :ProductDimension3D .
:ProductDimension3D a bamm-c:SingleEntity;
    bamm:name "productDimension3D";
    bamm:dataType :ProductDimension3DEntity .
:ProductDimension3DEntity a bamm:Entity;
    bamm:name "productDimension3DEntity";
    bamm:properties (:dimensionX :dimensionY :dimensionZ) .
:dimensionX a bamm:Property;
    bamm:name "dimensionX";
    bamm:description "The minimal electric capacity"@en;
    bamm:characteristic :Length .
:dimensionY a bamm:Property;
    bamm:name "dimensionY";
    bamm:description "The maximal electric capacity"@en;
    bamm:characteristic :Length .
:dimensionZ a bamm:Property;
    bamm:name "dimensionZ";
    bamm:description "The maximal electric capacity"@en;
    bamm:characteristic :Length .

:Length a bamm-c:Measurement;
    bamm:name "length";
    bamm:description "A length measurement value"@en;
    bamm-c:unit unit:centimetre;
    bamm:dataType xsd:double .

:productType a bamm:Property;
    bamm:name "productType";
    bamm:description "product type is described by a list of document types"@en;
    bamm:characteristic :SetOfDocumentTypes .
:SetOfDocumentTypes a bamm-c:Set;
    bamm:name "setOfDocumentTypes";
    bamm:description "a set of document types"@en;
    bamm-c:elementCharacteristic bamm-c:Text .
'
);

insert into model(id,private,name,publisher,type,version,model_definition) VALUES (
    'urn:bamm:com.catenaX:0.0.1:ProductUsage',FALSE,'ProductUsage','Catena-X Consortium','BAMM','0.0.1',
    '@prefix xsd: <http://www.w3.org/2001/XMLSchema#>.
@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#>.
@prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#>.
@prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#>.
@prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#>.
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>.
@prefix : <urn:bamm:com.catenax:0.0.1#>.

:ProductUsage a bamm:Aspect;
    bamm:name "ProductUsage";
    bamm:properties (:lifeSpan :stateOfHealth :stateOfCharge :mileage :numberOfChargingCycles :voltage :residualCurrentHazard :lifeSpanAsPlanned :numberOfChargingCyclesAsPlanned);
    bamm:operations ();
    bamm:preferredName "Product Usage"@en.
:lifeSpan a bamm:Property;
    bamm:name "lifeSpan";
    bamm:preferredName "lifeSpan"@en;
    bamm:characteristic :LifeSpan.
:stateOfHealth a bamm:Property;
    bamm:name "stateOfHealth";
    bamm:preferredName "State of Health"@en;
    bamm:characteristic :PercentOfSomething.
:stateOfCharge a bamm:Property;
    bamm:name "stateOfCharge";
    bamm:characteristic :PercentOfSomething.
:mileage a bamm:Property;
    bamm:name "mileage";
    bamm:characteristic :Mileage;
    bamm:description "The mileage stated by the vehicle"@en.
:numberOfChargingCycles a bamm:Property;
    bamm:name "numberOfChargingCycles";
    bamm:characteristic :NumberOfCycles;
    bamm:exampleValue "10000"^^xsd:long.
:voltage a bamm:Property;
    bamm:name "voltage";
    bamm:characteristic :Voltage.
:residualCurrentHazard a bamm:Property;
    bamm:name "residualCurrentHazard";
    bamm:characteristic :Current;
    bamm:description "The residual current still present."@en;
    bamm:exampleValue "40"^^xsd:double.
:LifeSpan a bamm-c:Quantifiable;
    bamm:name "LifeSpan";
    bamm:preferredName "LifeSpan"@en;
    bamm:dataType xsd:double;
    bamm:description "The Life Span"@en;
    bamm-c:unit unit:year.
:PercentOfSomething a bamm-c:Quantifiable;
    bamm:name "PercentOfSomething";
    bamm:dataType xsd:double;
    bamm-c:unit unit:percent.
:Mileage a bamm-c:Quantifiable;
    bamm:name "Mileage";
    bamm:dataType xsd:double;
    bamm-c:unit unit:kilometre.
:NumberOfCycles a bamm:Characteristic;
    bamm:name "NumberOfCycles";
    bamm:dataType xsd:long.
:Voltage a bamm-c:Measurement;
    bamm:name "Voltage";
    bamm:dataType xsd:double;
    bamm-c:unit unit:volt.
:Current a bamm-c:Measurement;
    bamm:name "Current";
    bamm:dataType xsd:double;
    bamm-c:unit unit:ampere.
:lifeSpanAsPlanned a bamm:Property;
    bamm:name "lifeSpanAsPlanned";
    bamm:characteristic :LifeSpan;
    bamm:description "as planned lifeSpan for one specific vehicle"@en.
:numberOfChargingCyclesAsPlanned a bamm:Property;
    bamm:name "numberOfChargingCyclesAsPlanned";
    bamm:characteristic :NumberOfCycles;
    bamm:description "as planned number of charging cycles per planned life span"@en.

'
);

insert into model(id,private,name,publisher,type,version,model_definition) VALUES (
    'urn:bamm:com.bosch.nexeed.digitaltwin:1.0.0:ReturnRequest',FALSE,'ReturnRequest','Robert Bosch GmbH','BAMM','1.0.0',
    '@prefix xsd: <http://www.w3.org/2001/XMLSchema#>.
@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#>.
@prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#>.
@prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#>.
@prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#>.
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>.
@prefix : <urn:bamm:com.bosch.nexeed.digitaltwin:1.0.0#>.

:ReturnRequest a bamm:Aspect;
    bamm:name "ReturnRequest";
    bamm:properties (:return);
    bamm:operations ();
    bamm:description "Aspect to indicate if there is a return request for a material"@en.
:return a bamm:Property;
    bamm:name "return";
    bamm:characteristic bamm-c:Boolean;
    bamm:description "Describes the fact whether a vehicle, assembly or part needs to be returned to requestor."@en;
    bamm:exampleValue "yes"^^xsd:boolean.
'
);

insert into model(id,private,name,publisher,type,version,model_definition) VALUES (
    'urn:bamm:com.catenaX:0.0.1#Traceability',FALSE,'Traceability','Catena-X Consortium','BAMM','0.0.1',
    '@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .
@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#> .
@prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#> .
@prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#> .
@prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#> .
@prefix : <urn:bamm:com.catenaX:0.0.1#> .

:Traceability a bamm:Aspect ;
   bamm:name "Traceability" ;
   bamm:preferredName "Traceability"@en ;
   bamm:description "Traceability"@en ;
   bamm:properties ( :staticData :uniqueData :individualData :supplierTree :partTree :qualityAlertData ) ;
   bamm:operations () .

:individualData a bamm:Property ;
   bamm:name "individualData" ;
   bamm:preferredName "Individual Data"@en ;
   bamm:characteristic :PartIndividualDataCharacteristic .

:PartIndividualDataCharacteristic a bamm-c:SingleEntity ;
   bamm:name "PartIndividualDataCharacteristic" ;
   bamm:dataType :PartIndividualDataEntity .

:PartIndividualDataEntity a bamm:Entity ;
   bamm:name "PartIndividualDataEntity" ;
   bamm:preferredName "Individual Data"@en ;
   bamm:properties ( :productionCountryCode :productionDateGMT ) .

:productionCountryCode a bamm:Property ;
   bamm:name "productionCountryCode" ;
   bamm:preferredName "Country code of production"@en ;
   bamm:description "Country code of production"@en ;
   bamm:characteristic :CountryCodeCharacteristic ;
   bamm:exampleValue "HUR" .

:CountryCodeCharacteristic a bamm-c:Code ;
   bamm:name "CountryCodeCharacteristic" ;
   bamm:dataType xsd:string .

:productionDateGMT a bamm:Property ;
   bamm:name "productionDateGMT" ;
   bamm:preferredName "Production Date (GMT)"@en ;
   bamm:description "Production date without timestamp"@en ;
   bamm:characteristic :DateWithoutTimestamp ;
   bamm:exampleValue "2021-05-30"^^xsd:date .

:DateWithoutTimestamp a bamm:Characteristic ;
   bamm:name "DateWithoutTimestamp" ;
   bamm:preferredName "Date (without timestamp)"@en ;
   bamm:description "Date without timestamp conformant to ISO 8601"@en ;
   bamm:dataType xsd:date .

:partTree a bamm:Property ;
   bamm:name "partTree" ;
   bamm:preferredName "Part Tree Parent"@en ;
   bamm:characteristic :PartTreeParentCharacteristic .
   
:supplierTree a bamm:Property ;
   bamm:name "supplierTree" ;
   bamm:preferredName "Supplier Tree"@en ;
   bamm:characteristic :PartTreeParentCharacteristic .
   
:PartTreeParentCharacteristic a bamm:Characteristic ;
   bamm:name "PartTreeParentCharacteristic" ;
   bamm:dataType :PartTreeParentEntity .

:PartTreeParentEntity a bamm:Entity ;
   bamm:name "PartTreeParentEntity" ;
   bamm:properties ( :isParentOf ) .

:isParentOf a bamm:Property ;
   bamm:name "isParentOf" ;
   bamm:preferredName "is Parent of"@en ;
   bamm:description "Set of Parts, identified by ID"@en ;
   bamm:characteristic :IsParentOfCharacteristic .

:IsParentOfCharacteristic a bamm-c:Set ;
   bamm:name "IsParentOfCharacteristic" ;
   bamm:dataType xsd:string .

:qualityAlertData a bamm:Property ;
   bamm:name "qualityAlertData" ;
   bamm:preferredName "Quality Alert Data"@en ;
   bamm:characteristic :QualityAlertDataCharacteristic .

:QualityAlertDataCharacteristic a bamm-c:SingleEntity ;
   bamm:name "QualityAlertDataCharacteristic" ;
   bamm:dataType :QualityAlertDataEntity .

:QualityAlertDataEntity a bamm:Entity ;
   bamm:name "QualityAlertDataEntity" ;
   bamm:properties ( :qualityAlert :qualityType ) ;
   bamm:preferredName "Quality Alert Data"@en .

:qualityAlert a bamm:Property ;
   bamm:name "qualityAlert" ;
   bamm:preferredName "Quality Alert"@en ;
   bamm:characteristic bamm-c:Boolean ;
   bamm:exampleValue "true"^^xsd:boolean .

:qualityType a bamm:Property ;
   bamm:name "qualityType" ;
   bamm:preferredName "Quality Type"@en ;
   bamm:characteristic :QualityTypeEnum ;
   bamm:exampleValue "major"^^xsd:string .

:QualityTypeEnum a bamm-c:Enumeration ;
   bamm:name "QualityTypeEnum" ;
   bamm:dataType xsd:string ;
   bamm-c:values ( "major" "minor" "critical" ) .

:staticData a bamm:Property ;
   bamm:name "staticData" ;
   bamm:preferredName "Static Data"@en ;
   bamm:characteristic :PartStaticDataCharacteristic .

:PartStaticDataCharacteristic a bamm-c:SingleEntity ;
   bamm:name "PartStaticDataCharacteristic" ;
   bamm:dataType :PartStaticDataEntity .

:PartStaticDataEntity a bamm:Entity ;
   bamm:name "PartStaticDataEntity" ;
   bamm:properties ( :customerContractOneID :customerOneID :manufactureContractOneID :manufacturerOneID :partNameCustomer :partNameManufacturer :partNumberCustomer :partNumberManufacturer ) ;
   bamm:preferredName "Static Data"@en .

:customerContractOneID a bamm:Property ;
   bamm:name "customerContractOneID" ;
   bamm:preferredName "Customer Contract OneID"@en ;
   bamm:description "OneID of customer contract partner"@en ;
   bamm:characteristic :OneIDBusinessPartner .

:customerOneID a bamm:Property ;
   bamm:name "customerOneID" ;
   bamm:preferredName "Customer OneID"@en ;
   bamm:description "OneID of customer plant"@en ;
   bamm:characteristic :OneIDBusinessPartner ;
   bamm:exampleValue "CUSTOMER_ID" .

:manufactureContractOneID a bamm:Property ;
   bamm:name "manufactureContractOneID" ;
   bamm:preferredName "Manufacture Contract OneID"@en ;
   bamm:description "OneID of manufacturer contract partner"@en ;
   bamm:characteristic :OneIDBusinessPartner .

:manufacturerOneID a bamm:Property ;
   bamm:name "manufacturerOneID" ;
   bamm:preferredName "Manufacturer OneID"@en ;
   bamm:description "OneID of manufacturer partner"@en ;
   bamm:characteristic :OneIDBusinessPartner ;
   bamm:exampleValue "ERROR_CP" .

:partNameCustomer a bamm:Property ;
   bamm:name "partNameCustomer" ;
   bamm:preferredName "Name of the product (customer)"@en ;
   bamm:description "Name of the part family of customer"@en ;
   bamm:characteristic bamm-c:Text ;
   bamm:exampleValue "MIRROR_1" .

:partNameManufacturer a bamm:Property ;
   bamm:name "partNameManufacturer" ;
   bamm:preferredName "Name of the product (manufacturer)"@en ;
   bamm:description "Name of the part family of manufacture"@en ;
   bamm:characteristic bamm-c:Text ;
   bamm:exampleValue "KLEBER1" .

:partNumberCustomer a bamm:Property ;
   bamm:name "partNumberCustomer" ;
   bamm:preferredName "Part number (customer)"@en ;
   bamm:description "Type/Part No. of customer product"@en ;
   bamm:characteristic :PartNumberCharacteristic ;
   bamm:exampleValue "1122334455" .

:partNumberManufacturer a bamm:Property ;
   bamm:name "partNumberManufacturer" ;
   bamm:preferredName "Part number (manufacturer)"@en ;
   bamm:description "Number of part family of manufacture"@en ;
   bamm:characteristic :PartNumberCharacteristic .

:OneIDBusinessPartner a bamm-c:Code ;
   bamm:name "OneIDBusinessPartner" ;
   bamm:preferredName "OneID BusinessPartner"@en ;
   bamm:dataType xsd:string .

:PartNumberCharacteristic a bamm:Characteristic ;
   bamm:name "PartNumberCharacteristic" ;
   bamm:preferredName "part number"@en ;
   bamm:dataType xsd:string .

:uniqueData a bamm:Property ;
   bamm:name "uniqueData" ;
   bamm:preferredName "Unique Data"@en ;
   bamm:characteristic :PartUniqueDataCharacteristic .

:PartUniqueDataCharacteristic a bamm-c:SingleEntity ;
   bamm:name "PartUniqueDataCharacteristic" ;
   bamm:dataType :PartUniqueDataEntity .

:PartUniqueDataEntity a bamm:Entity ;
   bamm:name "PartUniqueDataEntity" ;
   bamm:properties ( :customerUniqueID :manufacturerUniqueID :uniqueID ) ;
   bamm:preferredName "Unique Data Entity"@en .

:customerUniqueID a bamm:Property ;
   bamm:name "customerUniqueID" ;
   bamm:preferredName "Customer Unique ID"@en ;
   bamm:description "SerialNo. Of customer"@en ;
   bamm:characteristic :IdCharacteristic ;
   bamm:exampleValue "333" .

:manufacturerUniqueID a bamm:Property ;
   bamm:name "manufacturerUniqueID" ;
   bamm:preferredName "Manufacturer Unique ID"@en ;
   bamm:description "SerialNo. of manufacturer"@en ;
   bamm:characteristic :IdCharacteristic ;
   bamm:exampleValue "Xxx" .

:uniqueID a bamm:Property ;
   bamm:name "uniqueID" ;
   bamm:preferredName "unique ID"@en ;
   bamm:description "this is a local unique ID to identiy the part"@en ;
   bamm:characteristic :IdCharacteristic ;
   bamm:exampleValue "1AB" .

:IdCharacteristic a bamm:Characteristic ;
   bamm:name "IdCharacteristic" ;
   bamm:dataType xsd:string .

'
);

insert into model(id,private,name,publisher,type,version,model_definition) VALUES (
    'urn:bamm:com.catenaX:0.0.1:GPDMAspectPoC',FALSE,'GPDM Aspect','Catena-X Consortium','BAMM','0.0.1',
    '@prefix xsd: <http://www.w3.org/2001/XMLSchema#> .
@prefix bamm: <urn:bamm:io.openmanufacturing:meta-model:1.0.0#> .
@prefix unit: <urn:bamm:io.openmanufacturing:unit:1.0.0#> .
@prefix bamm-c: <urn:bamm:io.openmanufacturing:characteristic:1.0.0#> .
@prefix bamm-e: <urn:bamm:io.openmanufacturing:entity:1.0.0#> .
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>.
@prefix : <urn:bamm:com.catenaX:0.0.1#>.

:OneIDBusinessPartner a bamm-c:Code;
    bamm:name "OneIDBusinessPartner";
    bamm:preferredName "OneID BusinessPartner"@en;
    bamm:dataType xsd:string.
:GPDMAspectPoC a bamm:Aspect;
    bamm:name "GPDMAspectPoC";
    bamm:preferredName "GPDM Aspect"@en;
    bamm:properties ([
  bamm:property :businessPartnerData;
  bamm:optional "true"^^xsd:boolean
] :contactData);
    bamm:operations ().
:businessPartnerData a bamm:Property;
    bamm:name "businessPartnerData";
    bamm:preferredName "Business Partner Data"@en;
    bamm:characteristic :BusinessPartnerDataCharacteristic.
:BusinessPartnerDataEntity a bamm:Entity;
    bamm:name "BusinessPartnerDataEntity";
    bamm:properties ([
  bamm:property :oneId;
  bamm:optional "true"^^xsd:boolean
] :businessPartnerType :businessPartnerNames).
:BusinessPartnerDataCharacteristic a bamm-c:SingleEntity;
    bamm:name "BusinessPartnerDataCharacteristic";
    bamm:dataType :BusinessPartnerDataEntity.
:oneId a bamm:Property;
    bamm:name "oneId";
    bamm:preferredName "oneID"@en;
    bamm:characteristic :OneIDBusinessPartner.
:businessPartnerType a bamm:Property;
    bamm:name "businessPartnerType";
    bamm:preferredName "businessPartnerType"@en;
    bamm:characteristic :BusinessPartnerTypeCharacteristic.
:BusinessPartnerTypeCharacteristic a bamm:Characteristic;
    bamm:name "BusinessPartnerTypeCharacteristic";
    bamm:dataType xsd:string.
:SetOfBusinessPartnerName a bamm-c:Set;
    bamm:name "SetOfBusinessPartnerName";
    bamm:preferredName "Business Partner Names"@en;
    bamm:dataType :BusinessPartnerName.
:BusinessPartnerName a bamm:Entity;
    bamm:name "BusinessPartnerName";
    bamm:preferredName "Business Partner Name"@en;
    bamm:properties (:name :type :legalForm).
:name a bamm:Property;
    bamm:name "name";
    bamm:preferredName "Name"@en;
    bamm:characteristic bamm-c:Text.
:type a bamm:Property;
    bamm:name "type";
    bamm:preferredName "Type"@en;
    bamm:characteristic :BusinessPartnerNameTypeEnum;
    bamm:exampleValue "registered_name".
:BusinessPartnerNameTypeEnum a bamm-c:Enumeration;
    bamm:name "BusinessPartnerNameTypeEnum";
    bamm:preferredName "BusinessPartnerNameTypeEnum"@en;
    bamm:dataType xsd:string;
    bamm-c:values ("registered_name" "local_name" "international_name" "transliterated_name" "DBA_name" "VAT_registered_name").
:legalForm a bamm:Property;
    bamm:name "legalForm";
    bamm:preferredName "Legal Form"@en;
    bamm:characteristic bamm-c:Text.
:businessPartnerNames a bamm:Property;
    bamm:name "businessPartnerNames";
    bamm:preferredName "businessPartnerNames"@en;
    bamm:characteristic :SetOfBusinessPartnerName.
:contactData a bamm:Property;
    bamm:name "contactData";
    bamm:preferredName "contactData"@en;
    bamm:characteristic :ContactDataCharacteristic.
:ContactDataCharacteristic a bamm-c:SingleEntity;
    bamm:name "ContactDataCharacteristic";
    bamm:dataType :ContactDataEntity.
:ContactDataEntity a bamm:Entity;
    bamm:name "ContactDataEntity";
    bamm:preferredName "ContactDataEntity"@en;
    bamm:properties ([
  bamm:property :eMailAddress;
  bamm:optional "true"^^xsd:boolean
] [
  bamm:property :url;
  bamm:optional "true"^^xsd:boolean
]).
:eMailAddress a bamm:Property;
    bamm:name "eMailAddress";
    bamm:preferredName "eMailAddress"@en;
    bamm:characteristic :EMailAddressTrait.
:url a bamm:Property;
    bamm:name "url";
    bamm:preferredName "URL"@en;
    bamm:characteristic bamm-c:ResourcePath.
:EMailCharacteristic a bamm:Characteristic;
    bamm:name "EMailCharacteristic";
    bamm:preferredName "EMailCharacteristic"@en;
    bamm:dataType xsd:string.
:EMailAddressTrait a bamm-c:Trait;
    bamm:name "EMailAddressTrait";
    bamm-c:baseCharacteristic :EMailCharacteristic;
    bamm-c:constraint :RegularExpressionForEMail.
:RegularExpressionForEMail a bamm-c:RegularExpressionConstraint;
    bamm:name "RegularExpressionForEMail";
    bamm:preferredName "RegularExpressionForEMail"@en;
    bamm:value "tbd".

'
);