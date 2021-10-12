DROP TABLE IF EXISTS material;

CREATE TABLE material (
  id INTEGER,
  parent INTEGER,
  dataprovider VARCHAR(250),
  level VARCHAR(250) NOT NULL,
  owner VARCHAR(250) NOT NULL,
  owner_type VARCHAR(250) NOT NULL,
  material_name VARCHAR(250),
  vda_class VARCHAR(250),
  aggregate_state VARCHAR(250),
  material_type VARCHAR(250),
  chemical_composition VARCHAR(250),
  chemical_composition_fraction VARCHAR(250),
  weight VARCHAR(250)
);


INSERT INTO material (id, parent, dataprovider, level, owner, owner_type, material_name, vda_class, aggregate_state, material_type, chemical_composition, chemical_composition_fraction, weight) VALUES
(1, null,'','Vehicle','','','','','','','','',''),
(2,1,'BMW','Vehicle','BMW','8P61','Eisen','1.1','solid','Steels / cast steels / sintered steel','[1762]','0,24','327,6'),
(3,1,'BMW','Vehicle','BMW','8P61','Polyethylen','5.5.1','solid','Plastics','[2889]','0,12','163,8'),
(4,1,'BMW','Vehicle','BMW','8P61','Polyamid 6','5.5.1','solid','Plastics','[23315]','0,03','40,95'),
(5,1,'BMW','Vehicle','BMW','8P61','Aluminium (Metall)','2.1','solid','Aluminium and aluminium alloys','[781]','0,21','286,65'),
(6,1,'BMW','Vehicle','BMW','8P61','Paraffinwachse und Kohlenwasserstoffwachse, oxidiert, Lithiumsalze','9.7','liquid','Washing water, battery acids','[3367]','0,08','109,2'),
(7,1,'BMW','Vehicle','BMW','8P61','Kleber','6.2','solid','Adhesives, sealants','[9999,2812]','0,04','54,6'),
(8,1,'BMW','Vehicle','BMW','8P61','Carbon Steel (1008)','1.1.2','solid','highly alloyed','[728088751]','0,28','382,2'),
(9,null,'','VM ZB SE09 HVS MH D-MUSTER EXV','','','','','','','','','181,37'),
(10,9,'BMW','VM ZB SE09 HVS MH D-MUSTER EXV','BMW','2412117','Eisen','1.1','solid','Steels / cast steels / sintered steel','[1762]','0,139163036885924','25,24'),
(11,9,'BMW','VM ZB SE09 HVS MH D-MUSTER EXV','BMW','2412117','Polyethylen','5.5.1','solid','Plastics','[2889]','0,110161548216353','19,98'),
(12,9,'BMW','VM ZB SE09 HVS MH D-MUSTER EXV','BMW','2412117','Aluminium (Metall)','2.1','solid','Aluminium and aluminium alloys','[1347348,3367,1370,1994,1931,781]','0,194629762364228','35,3'),
(13,9,'BMW','VM ZB SE09 HVS MH D-MUSTER EXV','BMW','2412117','Paraffinwachse und Kohlenwasserstoffwachse, oxidiert, Lithiumsalze','9.7','liquid','Washing water, battery acids','[1347348,3367,1370,1994,1931,781]','0,548602304681039','99,5'),
(14,9,'BMW','VM ZB SE09 HVS MH D-MUSTER EXV','BMW','2412117','Kleber','6.2','solid','Adhesives, sealants','[9999,2812]','0,00744334785245631','1,35'),
(15,null,'','ZB LU HVS SP41 G30 G31','','','','','','','','','3224,68'),
(16,15,'BMW','ZB LU HVS SP41 G30 G31','BMW','8849262-01','Eisen','1.1','solid','Steels / cast steels / sintered steel','[1762]','1,69179026299829','306,84'),
(17,15,'BMW','ZB LU HVS SP41 G30 G31','BMW','8849262-01','Polyethylen','5.5.1','solid','Plastics','[2889]','1,25257760379335','227,18'),
(18,15,'BMW','ZB LU HVS SP41 G30 G31','BMW','8849262-01','Aluminium (Metall)','2.1','solid','Aluminium and aluminium alloys','[1347348,3367,1370,1994,1931,781]','2,57650107515025','467,3'),
(19,15,'BMW','ZB LU HVS SP41 G30 G31','BMW','8849262-01','Copper','3.1','solid','Copper','[1347348,3367,1370,1994,1931,781]','2,42016072905861','737,21'),
(20,15,'BMW','ZB LU HVS SP41 G30 G31','BMW','8849262-01','Paraffinwachse und Kohlenwasserstoffwachse, oxidiert, Lithiumsalze','9.7','liquid','Washing water, battery acids','[1347348,3367,1370,1994,1931,781]','8,18657991950157','1484,8'),
(21,15,'BMW','ZB LU HVS SP41 G30 G31','BMW','8849262-01','Kleber','6.2','solid','Adhesives, sealants','[9999,2812]','0,00744334785245631','1,35'),
(22,null,'','ZB ZELLMODUL PHEV1 34AH POS 16S1P U3','','','','','','','','','304,612'),
(23,22,'BMW','ZB ZELLMODUL PHEV1 34AH POS 16S1P U3','BMW','8840841-04','Eisen','1.1','solid','Steels / cast steels / sintered steel','[1762]','0,098531902879729','30,014'),
(24,22,'BMW','ZB ZELLMODUL PHEV1 34AH POS 16S1P U3','BMW','8840841-04','Polyethylen','5.5.1','solid','Plastics','[2889]','0,0723806022087114','22,048'),
(25,22,'BMW','ZB ZELLMODUL PHEV1 34AH POS 16S1P U3','BMW','8840841-04','Aluminium (Metall)','2.1','solid','Aluminium and aluminium alloys','[1347348,3367,1370,1994,1931,781]','0,151208750804302','46,06'),
(26,22,'BMW','ZB ZELLMODUL PHEV1 34AH POS 16S1P U3','BMW','8840841-04','Copper','3.1','solid','Copper','[1347348,3367,1370,1994,1931,781]','0,190438984675587','58,01'),
(27,22,'BMW','ZB ZELLMODUL PHEV1 34AH POS 16S1P U3','BMW','8840841-04','Paraffinwachse und Kohlenwasserstoffwachse, oxidiert, Lithiumsalze','9.7','liquid','Washing water, battery acids','[1347348,3367,1370,1994,1931,781]','0,48743975943167','148,48'),
(28,null,'','ZB ZELLMODUL PHEV1 34AH NEG 16S1P U3','','','','','','','','','18,932'),
(29,28,'BMW','ZB ZELLMODUL PHEV1 34AH NEG 16S1P U3','BMW','8840838-04','Eisen','1.1','solid','Steels / cast steels / sintered steel','[1762]','0,0979294316501162','1,854'),
(30,28,'BMW','ZB ZELLMODUL PHEV1 34AH NEG 16S1P U3','BMW','8840838-04','Polyethylen','5.5.1','solid','Plastics','[2889]','0,0701457849144306','1,328'),
(31,28,'BMW','ZB ZELLMODUL PHEV1 34AH NEG 16S1P U3','BMW','8840838-04','Aluminium (Metall)','2.1','solid','Aluminium and aluminium alloys','[1347348,3367,1370,1994,1931,781]','0,151066976547644','2,86'),
(32,28,'BMW','ZB ZELLMODUL PHEV1 34AH POS 16S1P U3','BMW','8840838-04','Copper','3.1','solid','Copper','[1347348,3367,1370,1994,1931,781]','0,190682442425523','3,61'),
(33,28,'BMW','ZB ZELLMODUL PHEV1 34AH NEG 16S1P U3','BMW','8840838-04','Paraffinwachse und Kohlenwasserstoffwachse, oxidiert, Lithiumsalze','9.7','liquid','Washing water, battery acids','[1347348,3367,1370,1994,1931,781]','0,490175364462286','9,28'),
(34,null,'','LI-ION ZELLE Z10 PHEV1 34AH U3.0 LF2 RPT','','','','','','','','','1,077'),
(35,34,'SAMSUNG','LI-ION ZELLE Z10 PHEV1 34AH U3.0 LF2 RPT','SAMSUNG','8844604-01','Eisen','1.1','solid','Steels / cast steels / sintered steel','[1762]','0,0872794800371402','0,094'),
(36,34,'SAMSUNG','LI-ION ZELLE Z10 PHEV1 34AH U3.0 LF2 RPT','SAMSUNG','8844604-01','Polyethylen','5.5.1','solid','Plastics','[2889]','0,0306406685236769','0,033'),
(37,34,'SAMSUNG','LI-ION ZELLE Z10 PHEV1 34AH U3.0 LF2 RPT','SAMSUNG','8844604-01','Cathode Aluminium','2.1','solid','Aluminium and aluminium alloys','[1347348,3367,1370,1994,1931,781]','0,148560817084494','0,16'),
(38,34,'SAMSUNG','LI-ION ZELLE Z10 PHEV1 34AH U3.0 LF2 RPT','SAMSUNG','8844604-01','Anode Copper','3.1','solid','Copper','[1347348,3367,1370,1994,1931,781]','0,194986072423398','0,21'),
(39,34,'SAMSUNG','LI-ION ZELLE Z10 PHEV1 34AH U3.0 LF2 RPT','SAMSUNG','8844604-01','Paraffinwachse und Kohlenwasserstoffwachse, oxidiert, Lithiumsalze','9.7','liquid','Washing water, battery acids','[1347348,3367,1370,1994,1931,781]','0,538532961931291','0,58'),
(40,null,'','LU GETRIEBE GA8L51CZ B48B20M1  CODE SXJ8','','','','','','','','','85,62'),
(41,40,'ZF','LU GETRIEBE GA8L51CZ B48B20M1  CODE SXJ8','ZF','8844604-01','Eisen','1.1','solid','Steels / cast steels / sintered steel','[1762]','71,4020427112349','76,9'),
(42,40,'ZF','LU GETRIEBE GA8L51CZ B48B20M1  CODE SXJ8','ZF','8844604-01','Polyethylen','5.5.1','solid','Plastics','[2889]','0,21355617455896','0,23'),
(43,40,'ZF','LU GETRIEBE GA8L51CZ B48B20M1  CODE SXJ8','ZF','8844604-01','Cathode Aluminium','2.1','solid','Aluminium and aluminium alloys','[1347348,3367,1370,1994,1931,781]','6,96378830083566','7,5'),
(44,40,'ZF','LU GETRIEBE GA8L51CZ B48B20M1  CODE SXJ8','ZF','8844604-01','Anode Copper','3.1','solid','Copper','[1347348,3367,1370,1994,1931,781]','0,194986072423398','0,21'),
(45,40,'ZF','LU GETRIEBE GA8L51CZ B48B20M1  CODE SXJ8','ZF','8844604-01','Paraffinwachse und Kohlenwasserstoffwachse, oxidiert, Lithiumsalze','9.2','liquid','Lubricants','[1347348,3367,1370,1994,1931,781]','0,724233983286908','0,78'),

(46,null,'IMDS','Material','IMDS','1768','Material_1768','0','composite','composite','1762','1','0'),
(47,null,'IMDS','Material','IMDS','2889','Material_2889','0','composite','composite','2889','1','0'),
(48,null,'IMDS','Material','IMDS','23315','Material_23315','0','composite','composite','23315','1','0'),
(49,null,'IMDS','Material','IMDS','781','Material_781','0','composite','composite','781','1','0'),
(50,null,'IMDS','Material','IMDS','3367','Material_3367','0','composite','composite','3367','1','0'),
(51,null,'IMDS','Material','IMDS','9999','Material_9999','0','composite','composite','9999','1','0'),
(52,null,'IMDS','Material','IMDS','2812','Material_2812','0','composite','composite','2812','1','0'),
(53,null,'IMDS','Material','IMDS','728088751','Material_728088751','0','composite','composite','728088751','1','0');

DROP TABLE IF EXISTS chemical_material_relation;
CREATE TABLE chemical_material_relation (
  material_id INTEGER NOT NULL,
  chemical_id INTEGER NOT NULL
);

INSERT INTO chemical_material_relation(material_id,chemical_id) VALUES
(2,46),
(3,47),
(4,48),
(5,49),
(6,50),
(7,51),
(7,52),
(8,53);


DROP TABLE IF EXISTS material_composition;
CREATE TABLE material_composition (
  parent_id INTEGER NOT NULL,
  child_id INTEGER NOT NULL
);

INSERT INTO material_composition(parent_id,child_id) VALUES
(1,2),
(1,3),
(1,4),
(1,5),
(1,6),
(1,7),
(1,8),
(9,10),
(9,11),
(9,12),
(9,13),
(9,14),
(15,16),
(15,17),
(15,18),
(15,19),
(15,20),
(15,21),
(22,23),
(22,24),
(22,25),
(22,26),
(22,27),
(28,29),
(28,30),
(28,31),
(28,32),
(28,33),
(34,35),
(34,36),
(34,37),
(34,38),
(34,39),
(40,41),
(40,42),
(40,43),
(40,44),
(40,45);



DROP TABLE IF EXISTS traceability;

CREATE TABLE traceability (
  manufacturerOneId VARCHAR(250),	
  manufactureContractOneId VARCHAR(250),	
  partNameManufacturer VARCHAR(250),	
  partNumberManufacturer VARCHAR(250),	
  customerOneID VARCHAR(250),	
  customerContractOneID VARCHAR(250),	
  partNameCustomer VARCHAR(250),	
  partNumberCustomer VARCHAR(250),		
  uniqueID VARCHAR(250) ,	
  manufacturerUniqueID VARCHAR(250) ,	
  customerUniqueID VARCHAR(250) ,		
  productionCountryCode VARCHAR(250) ,	
  productionDateGMT VARCHAR(250) 
);

DROP TABLE IF EXISTS documentation;

CREATE TABLE documentation (
  manufacturerOneId VARCHAR(250)
);


DROP TABLE IF EXISTS return_request;

CREATE TABLE return_request (
  manufacturerOneId VARCHAR(250) 
);

DROP TABLE IF EXISTS productusage;

CREATE TABLE productusage (
  manufacturerOneId VARCHAR(250) 
);

DROP TABLE IF EXISTS productdescription;

CREATE TABLE productdescription (
  manufacturerOneId VARCHAR(250) 
);

DROP TABLE IF EXISTS technical_data;

CREATE TABLE technical_data (
  manufacturerOneId VARCHAR(250)
);

DROP TABLE IF EXISTS eol_recycler;

CREATE TABLE eol_recycler (
  manufacturerOneId VARCHAR(250)	
);

DROP TABLE IF EXISTS eol_reuse;

CREATE TABLE eol_reuse (
  manufacturerOneId VARCHAR(250) NOT NULL
);
