<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:pwcwc="http://pwc.t-systems.com/pdm/windchill_v02" xmlns:dmc="http://pwc.t-systems.com/datamodel/common">
<!-- 
#
# Copyright (c) 2021-2022 T-Systems International GmbH (Catena-X Consortium)
#
# See the AUTHORS file(s) distributed with this work for additional
# information regarding authorship.
#
# See the LICENSE file(s) distributed with this work for
# additional information regarding license terms.
#
# Sample transformation from SQL to Material JSON
-->
	<xsl:output method="text" encoding="UTF-8"  omit-xml-declaration="yes" indent="no" media-type="application/json" />
	<xsl:strip-space elements="*" />

	<xsl:param name="SERVICE_URL"/>

  <xsl:template match="/">
    <xsl:for-each select="/DataSets/material/Row[LEVEL='0']">
	<xsl:text>{ 
		"materialDetails": {</xsl:text>
     <xsl:call-template name="processNode"/>
    </xsl:for-each>
	<xsl:text>
		}
	}</xsl:text>
  </xsl:template>

	<xsl:template name="processNode">
		<xsl:text> 
		"materialName" : "</xsl:text><xsl:value-of select="MATERIAL_NAME"/><xsl:text>",
		"materialType" : "</xsl:text><xsl:value-of select="MATERIAL_TYPE"/><xsl:text>",
		"aggregateState" : "</xsl:text><xsl:value-of select="AGGREGATE_STATE"/><xsl:text>",
		"weight" : </xsl:text><xsl:value-of select="WEIGHT"/><xsl:text>,
		"chemicalCompositionFraction" : </xsl:text><xsl:value-of select="CHEMICAL_COMPOSITION_FRACTION"/><xsl:text>,
		"chemicalComposition" : [</xsl:text>
    <xsl:variable name="parentid" select="./ID"/>
		<xsl:for-each select="/DataSets/material/Row[PARENT=$parentid]">
			<xsl:text>{</xsl:text>
				<xsl:call-template name="processNode"/>
			<xsl:text>}</xsl:text>			
		<xsl:if test="position() != last()">
			<xsl:text>,</xsl:text>
		</xsl:if>
		</xsl:for-each>
	<xsl:text>]</xsl:text> 
	</xsl:template>

</xsl:stylesheet>