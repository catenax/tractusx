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
# Sample transformation from DXC to Material JSON
-->
	<xsl:output method="text" encoding="UTF-8"  omit-xml-declaration="yes" indent="no" media-type="application/json" />
	<xsl:strip-space elements="*" />

	<xsl:template match="/">
		<xsl:text>{ 
	"materialDetails": {</xsl:text>

		<xsl:for-each select="./pwcwc:container/dmc:object">
			<xsl:call-template name="processNode" />
		</xsl:for-each>

		<xsl:text>
	}
}</xsl:text>
	</xsl:template>

	<xsl:template name="processNode">
<!--	<xsl:text>
		"id":"</xsl:text><xsl:call-template name="escape"><xsl:with-param name="text" select="./dmc:attribute[@name='obid']" /></xsl:call-template><xsl:text>"</xsl:text>
-->		<xsl:text> 
		"materialName" : "</xsl:text><xsl:call-template name="escape"><xsl:with-param name="text" select="./dmc:attribute[@name='material']" /></xsl:call-template><xsl:text>"</xsl:text>
		<xsl:text>,
		"materialTyp" : "</xsl:text><xsl:call-template name="escape"><xsl:with-param name="text" select="./dmc:attribute[@name='materialType']" /></xsl:call-template><xsl:text>"</xsl:text>
		<xsl:text>,
		"aggregateState" : "</xsl:text><xsl:call-template name="escape"><xsl:with-param name="text" select="./dmc:attribute[@name='materialState']" /></xsl:call-template><xsl:text>"</xsl:text>
		<xsl:text>,
		"weight" : </xsl:text><xsl:call-template name="escape"><xsl:with-param name="text" select="./dmc:attribute[@name='materialWeight']" /></xsl:call-template><xsl:text></xsl:text>
		<xsl:text>,
		"chemicalCompositionFraction" : </xsl:text><xsl:call-template name="escape"><xsl:with-param name="text" select="./dmc:attribute[@name='materialContribution']" /></xsl:call-template><xsl:text></xsl:text>
		<xsl:if test="count(./dmc:object[./dmc:attribute[@name='material']])">
		<xsl:text>,
		"chemicalComposition" : [</xsl:text>
		<xsl:for-each select="./dmc:object[./dmc:attribute[@name='material']]">
		  <xsl:text>
        {</xsl:text>
          <xsl:call-template name="processNode"/>
		  <xsl:text>
        }</xsl:text>
          <xsl:if test="position() != last()">
           <xsl:text>,</xsl:text>
          </xsl:if>
   		</xsl:for-each>
		<xsl:text>
		]</xsl:text>
		</xsl:if>		
	</xsl:template>

	<!-- Replace characters which could cause an invalid JS object, by their escape-codes. -->
	<xsl:template name="escape">
		<xsl:param name="text" />
		<xsl:param name="char" select="'\'" />
		<xsl:param name="nextChar" select="substring(substring-after('\/&quot;&#xD;&#xA;&#x9;',$char),1,1)" />

		<xsl:choose>
			<xsl:when test="$char = ''">
				<xsl:value-of select="$text" />
			</xsl:when>

			<xsl:when test="contains($text,$char)">
				<xsl:call-template name="escape">
					<xsl:with-param name="text" select="substring-before($text,$char)" />
					<xsl:with-param name="char" select="$nextChar" />
				</xsl:call-template>
				<xsl:value-of select="concat('\',translate($char,'&#xD;&#xA;&#x9;','rnt'))" />
				<xsl:call-template name="escape">
					<xsl:with-param name="text" select="substring-after($text,$char)" />
					<xsl:with-param name="char" select="$char" />
				</xsl:call-template>
			</xsl:when>

			<xsl:otherwise>
				<xsl:call-template name="escape">
					<xsl:with-param name="text" select="$text" />
					<xsl:with-param name="char" select="$nextChar" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>