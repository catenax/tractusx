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
# Sample digital twin from file
-->
	<xsl:output method="text" encoding="UTF-8"  omit-xml-declaration="yes" indent="no" media-type="application/json" />
	<xsl:strip-space elements="*" />
    
	<xsl:param name="SERVICE_URL"/>
	<xsl:param name="ADAPTER_URL"/>
	<xsl:param name="CONNECTOR_ID"/>

	<xsl:template match="/">
      <xsl:text>[
{
    "id":"3c7556f7-6956-4360-8036-d03e5a79c3c8",
    "aspects": [
        {
            "httpEndpoints": [
                {
                    "method": "GET",
                    "url": "urn:Vocabulary:com.ids:Connector?recipient=</xsl:text><xsl:value-of select="$CONNECTOR_ID"/><xsl:text>&amp;offer=offer-windchill&amp;representation=bom-aspect&amp;artifact=bom-brake"
                }
            ],
            "modelReference": {
                "urn": "urn:bamm:com.catenaX:0.0.1#Traceability"
            }
        },
        {
            "httpEndpoints": [
                {
                    "method": "GET",
                    "url": "urn:Vocabulary:com.ids:Connector?recipient=</xsl:text><xsl:value-of select="$CONNECTOR_ID"/><xsl:text>&amp;offer=offer-windchill&amp;representation=material-aspect&amp;artifact=material-brake"
                }
            ],
            "modelReference": {
                "urn": "urn:bamm:com.catenaX:0.0.1#Material"
            }
        }
    ],
    "description": "brake_dt_2019_snr.asm",
    "localIdentifiers": [
        {
            "key": "http://pwc.t-systems.com/datamodel/common",
            "value": "0000000251"
        },
        {
            "key": "VR:wt.part.WTPart",
            "value": "25054146@nis11c130.epdm-d.edm.dsh.de"
        }
    ],
    "manufacturer": "T-Systems International GmbH"
}
]</xsl:text>
    </xsl:template>

</xsl:stylesheet>