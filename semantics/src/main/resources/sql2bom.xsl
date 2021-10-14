<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- 
#
# Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
#
# See the AUTHORS file(s) distributed with this work for additional
# information regarding authorship.
#
# See the LICENSE file(s) distributed with this work for
# additional information regarding license terms.
#
# Sample transformation from SQL to BOM JSON
-->
	<xsl:output method="text" encoding="UTF-8" omit-xml-declaration="yes" indent="no" media-type="application/json" />
	<xsl:strip-space elements="*" />

	<xsl:template match="/DataSets/bom/Row">
  <xsl:text>{
	"uniqueData" : {
       "customerUniqueID" : "</xsl:text><xsl:value-of select="PARTUNIQUEDATA_CUSTOMERUNIQUEID"/><xsl:text>",
       "manufacturerUniqueID" : "</xsl:text><xsl:value-of select="PARTUNIQUEDATA_MANUFACTURERUNIQUEID"/><xsl:text>",
       "uniqueID" : "</xsl:text><xsl:value-of select="PARTUNIQUEDATA_UNIQUEID"/><xsl:text>"
	},  
	"staticData" : {
       "manufacturerContractOneID" : "</xsl:text><xsl:value-of select="PARTSTATICDATA_MANUFACTURECONTRACTONEID"/><xsl:text>",
       "manufacturerOneID" : "</xsl:text><xsl:value-of select="PARTSTATICDATA_MANUFACTURERONEID"/><xsl:text>",
       "partNumberManufacturer" : "</xsl:text><xsl:value-of select="PARTSTATICDATA_PARTNUMBERMANUFACTURER"/><xsl:text>",
       "partNameManufacturer" : "</xsl:text><xsl:value-of select="PARTSTATICDATA_PARTNAMEMANUFACTURER"/><xsl:text>",
       "customerOneID" :"</xsl:text><xsl:value-of select="PARTSTATICDATA_CUSTOMERONEID"/><xsl:text>",
       "customerContractOneID" : "</xsl:text><xsl:value-of select="PARTSTATICDATA_CUSTOMERCONTRACTONEID"/><xsl:text>",
       "partNumberCustomer" : "</xsl:text><xsl:value-of select="PARTSTATICDATA_PARTNUMBERCUSTOMER"/><xsl:text>",
       "partNameCustomer" : "</xsl:text><xsl:value-of select="PARTSTATICDATA_PARTNAMECUSTOMER"/><xsl:text>"
	},
    "qualityAlertData" : {
      "qualityAlert" : false
    },  
    "individualData" : {
      "productionCountryCode" : "</xsl:text><xsl:value-of select="PARTINDIVIDUALDATA_PRODUCTIONCOUNTRYCODE"/><xsl:text>",
      "productionDateGMT" : "</xsl:text><xsl:value-of select="PARTINDIVIDUALDATA_PRODUCTIONDATEGMT"/><xsl:text>"
    },
    "partTree" : {
      "isParentOf" : [
	  ]
    }
}
</xsl:text>
	</xsl:template>	

</xsl:stylesheet>