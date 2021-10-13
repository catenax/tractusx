package net.catenax.semantics.tools;

/*
Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A helper class that converts resultset(s) to
 * XML sources.
 */
@Service
public class ResultSetsToXmlSource {

    /**
     * converts a given resultset mapping into a XSLT source
     * @param specs a map of aliases to result sets
     * @return XSLT source
     */
    public Source convert(Map<String,ResultSet> specs) throws TransformerException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element dataSetsElement = doc.createElement("DataSets");
            for(Map.Entry<String,ResultSet> dataSet: specs.entrySet()) {
                if(dataSet.getValue()!=null) {
                    Element dataSetElement = doc.createElement(dataSet.getKey());
                    ResultSetMetaData rsmd = dataSet.getValue().getMetaData();
                    int colCount = rsmd.getColumnCount();
                    while (dataSet.getValue().next()) {
                        Element row = doc.createElement("Row");
                        for (int count = 1; count <= colCount; count++) {
                            String columnName = rsmd.getColumnLabel(count).toUpperCase();
                            Object value = dataSet.getValue().getObject(count);
                            if(!dataSet.getValue().wasNull()) {
                                Element node = doc.createElement(columnName);
                                node.appendChild(doc.createTextNode(value.toString()));
                                row.appendChild(node);
                            }
                        } // for col
                        dataSetElement.appendChild(row);
                    } // while rs.next()
                    dataSet.getValue().close();
                    dataSetsElement.appendChild(dataSetElement);   
                }         
            } // for dataSet

            doc.appendChild(dataSetsElement);
            DOMSource domSource = new DOMSource(doc);
            return domSource;
        } catch( ParserConfigurationException | SQLException e) {
            throw new TransformerException("Could not convert resultsets into XSLT source.",e);
        }
    }
}
