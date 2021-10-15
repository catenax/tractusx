<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:pwcwc="http://pwc.t-systems.com/pdm/windchill_v02" xmlns:dmc="http://pwc.t-systems.com/datamodel/common">
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
# Transformation of relational data into digital twin registration
-->
	<xsl:output method="text" encoding="UTF-8"  omit-xml-declaration="yes" indent="no" media-type="application/json" />
	<xsl:strip-space elements="*" />
    
	<xsl:param name="SERVICE_URL"/>
	<xsl:param name="CONNECTOR_ID"/>

	<xsl:template match="/">
      <xsl:text>[
	  </xsl:text>
	  <xsl:for-each select="./DataSets/parts/Row">
	     <xsl:text>{ 
			 "id":"</xsl:text><xsl:value-of select="./UUID"/><xsl:text>",
			 "description":"</xsl:text><xsl:value-of select="./DESCRIPTION"/><xsl:text>",
			 "manufacturer":"</xsl:text><xsl:value-of select="./MANUFACTURER"/><xsl:text>",
			 "localIdentifiers": [
        		</xsl:text><xsl:if test="./CUSTOMER = 'VIN'"><xsl:text>{
            		"key": "urn:iso:std:iso:4030:ed-3:v1:en",
            		"value": "</xsl:text><xsl:value-of select="./CUSTOMERNUMBER"/><xsl:text>"
        		},</xsl:text></xsl:if><xsl:text>
        		{
            		"key": "urn:bom:com.</xsl:text><xsl:choose><xsl:when test="./CUSTOMER = 'VIN'"><xsl:value-of select="./MANUFACTURER"/></xsl:when><xsl:otherwise><xsl:value-of select="./CUSTOMER"/></xsl:otherwise></xsl:choose><xsl:text>:part-serial",
            		"value": "</xsl:text><xsl:value-of select="./PARTSERIAL"/><xsl:text>"
        		},
				{
            		"key": "urn:bom:com.</xsl:text><xsl:value-of select="./MANUFACTURER"/><xsl:text>:part-number",
            		"value": "</xsl:text><xsl:value-of select="./PARTNUMBER"/><xsl:text>"
        		}
             ],
			 "aspects": [
        		{
					"modelReference": { "urn": "urn:bamm:com.catenaX:0.0.1#Traceability" },
            		"httpEndpoints": [ {
                    	"method": "GET",
                    	"url": "urn:connector:com.ids:CallingContext?recipient=</xsl:text><xsl:value-of select="$CONNECTOR_ID"/><xsl:text>&amp;offer=offer-tdm&amp;representation=bom-aspect&amp;artifact=bom-vehicle&amp;manufacturer=</xsl:text><xsl:value-of select="./MANUFACTURER"/><xsl:text>&amp;serial=</xsl:text><xsl:value-of select="./PARTSERIAL"/><xsl:text>"
                	}]         
        		},
        		{ 
					"modelReference": { "urn": "urn:bamm:com.catenaX:0.0.1#Material" },
            		"httpEndpoints":  [ {
                    	"method": "GET",
                   	    "url": "urn:connector:com.ids:CallingContext?recipient=</xsl:text><xsl:value-of select="$CONNECTOR_ID"/><xsl:text>&amp;offer=offer-tdm&amp;representation=material-aspect&amp;artifact=material-vehicle&amp;manufacturer=</xsl:text><xsl:value-of select="./MANUFACTURER"/><xsl:text>&amp;number=</xsl:text><xsl:value-of select="./PARTNUMBER"/><xsl:text>"
                     } ] 
        		}
			 ] }</xsl:text>
  		 <xsl:if test="position() != last()">
           <xsl:text>,</xsl:text>
         </xsl:if>
	  </xsl:for-each>
      <xsl:text>
]</xsl:text>
    </xsl:template>

</xsl:stylesheet>