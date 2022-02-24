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
# Sample digital twin from file
-->
	<xsl:output method="text" encoding="UTF-8"  omit-xml-declaration="yes" indent="no" media-type="application/json" />
	<xsl:strip-space elements="*" />
    
	<xsl:param name="SERVICE_URL"/>
	<xsl:param name="ADAPTER_URL"/>
	<xsl:param name="CONNECTOR_ID"/>

	<xsl:template match="/">
      <xsl:text>{
  "description": [
    {
      "language": "en",
      "text": "The shell for a brake system"
    }
  ],
  "globalAssetId": {
    "value": [
      "urn:twin:com.tsystems#3c7556f7-6956-4360-8036-d03e5a79c3c8"
    ]
  },
  "idShort": "brake_dt_2019_snr.asm",
  "identification": "urn:twin:com.tsystems#3c7556f7-6956-4360-8036-d03e5a79c3c8",
  "specificAssetIds": [
        {
            "key": "http://pwc.t-systems.com/datamodel/common",
            "value": "0000000251"
        },
        {
            "key": "VR:wt.part.WTPart",
            "value": "25054146@nis11c130.epdm-d.edm.dsh.de"
        }
  ],
  "submodelDescriptors": [
    {
      "description": [
        {
          "language": "en",
          "text": "Catena-X Traceability Aspect Implementation"
        }
      ],
      "idShort": "brake-traceability",
      "identification": "urn:bamm:com.catenaX:0.0.1#Traceability#4a738a24-b7d8-4989-9cd6-387772f40565",
      "semanticId": {
        "value": [
          "urn:bamm:com.catenaX:0.0.1#Traceability"
        ]
      },
      "endpoints": [
        {
          "interface": "IDS",
          "protocolInformation": {
            "endpointAddress": "urn:Vocabulary:com.ids:Connector?recipient=</xsl:text><xsl:value-of select="$CONNECTOR_ID"/><xsl:text>&amp;offer=offer-windchill&amp;representation=bom-aspect&amp;artifact=brake",
            "endpointProtocol": "GET",
            "endpointProtocolVersion": "1.0"
          }
        }
      ]
    },
    {
      "description": [
        {
          "language": "en",
          "text": "Catena-X Material Aspect Implementation"
        }
      ],
      "idShort": "brake-material",
      "identification": "urn:bamm:com.catenaX:0.0.1#Material#dae4d249-6d66-4818-b576-bf52f3b9ae90",
      "semanticId": {
        "value": [
          "urn:bamm:com.catenaX:0.0.1#Material"
        ]
      },
      "endpoints": [
        {
          "interface": "IDS",
          "protocolInformation": {
            "endpointAddress": "urn:Vocabulary:com.ids:Connector?recipient=</xsl:text><xsl:value-of select="$CONNECTOR_ID"/><xsl:text>&amp;offer=offer-windchill&amp;representation=material-aspect&amp;artifact=brake",
            "endpointProtocol": "GET",
            "endpointProtocolVersion": "1.0"
          }
        }
      ]
    }
  ]
}
</xsl:text>
    </xsl:template>

</xsl:stylesheet>