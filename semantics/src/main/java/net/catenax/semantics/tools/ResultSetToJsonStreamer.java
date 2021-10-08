/*
Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.tools;

import java.io.*;
import java.sql.*;

import com.fasterxml.jackson.core.*;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * This class is used to generate a json stream from a sql result set.
 * The column names are interpreted as paths in the resulting JSON structure
 */
public class ResultSetToJsonStreamer implements ResultSetExtractor<Void> {

  private final OutputStreamWriter writer;

  /**
   * defines a mapping of a given column by index to a json field
   */
  protected class ColSpec {
    String key;
    Integer index;

    protected ColSpec(String key, Integer index) {
      this.key=key;
      this.index=index;
    }

  }

  /**
   * @param pOutputStream the OutputStream containing the json
   */
  public ResultSetToJsonStreamer(final OutputStream pOutputStream) {
    this.writer=new OutputStreamWriter(pOutputStream);
  }


  /**
   * @param pResultSet the result set that has to be streamed in Json format
   */
  @Override
  public Void extractData(final ResultSet pResultSet) {
    try {
      // first do a mapping of columns to (embedded) json fields
      java.util.Map<String,java.util.List<ColSpec>> map = new java.util.HashMap<String,java.util.List<ColSpec>>();
      ResultSetMetaData metaData = pResultSet.getMetaData();
      int columnCount = metaData.getColumnCount();
      for (int column = 1; column <= columnCount; column++) {
        String columnName=metaData.getColumnLabel(column);
        String prefix="";
        int dot=columnName.indexOf(".");
        if(dot>=0) {
          prefix=columnName.substring(0,dot-1);
          columnName=columnName.substring(dot+1);
        }
        java.util.List<ColSpec> mapList=map.get(prefix);
        if(mapList==null) {
          mapList=new java.util.ArrayList<ColSpec>();
          map.put(prefix,mapList);
        }
        mapList.add(new ColSpec(columnName,column));
      }
  
      // start array
      boolean first=true;
      writer.write("[");

      while (pResultSet.next()) {
        if(first) {
          first=false;
        } else {
          writer.write(",");
        }
        // start object/row
        writer.write("{");
        boolean firstAttribute=true;

        // loop through the substructures  
        for(java.util.Map.Entry<String,java.util.List<ColSpec>> entry : map.entrySet()) {
          boolean firstObject=firstAttribute;
          if(firstAttribute) {
            firstAttribute=false;
          } else {
            writer.write(",");
          }
          // subobject? -> open up
          if(!"".equals(entry.getKey())) {
            writer.write("\""+entry.getKey()+"\":{");
            firstObject=true;
          }

          // output all fields
          for(ColSpec spec : entry.getValue()) {  
            if(firstObject) {
              firstObject=false;
            } else {
              writer.write(",");
            }
            writer.write("\""+spec.key+"\":\"");
            writer.write(pResultSet.getString(spec.index));
            writer.write("\"");
          }

          // subobject? ->terminate
          if(!"".equals(entry.getKey())) {
            writer.write("}");
          }
        }
        // terminate object
        writer.write("}");
      }
    // terminate array
    writer.write("]");
    // flush stream to enable webserver sending
    writer.flush();
    } catch (IOException | SQLException ex) {
      throw new RuntimeException(ex);
    }
    return null;
  }
}