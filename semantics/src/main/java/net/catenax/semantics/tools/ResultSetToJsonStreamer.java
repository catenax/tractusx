/*
Copyright (c) 2021 T-Systems International GmbH (Catena-X Consortium)
See the AUTHORS file(s) distributed with this work for additional
information regarding authorship.

See the LICENSE file(s) distributed with this work for
additional information regarding license terms.
*/
package net.catenax.semantics.tools;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * This class is used to generate a json stream from a sql result set.
 */
public class ResultSetToJsonStreamer implements ResultSetExtractor<Void> {

  private final OutputStream outputStream;

  /**
   * @param pOutputStream the OutputStream containing the json
   */
  public ResultSetToJsonStreamer(final OutputStream pOutputStream) {
    this.outputStream = pOutputStream;
  }

  /**
   * @param pResultSet the result set that has to be streamed in Json format
   */
  @Override
  public Void extractData(final ResultSet pResultSet) {
    ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    try (JsonGenerator jasonGenerator = mapper.getFactory().createGenerator(outputStream, JsonEncoding.UTF8)) {
      ResultSetMetaData metaData = pResultSet.getMetaData();
      int columnCount = metaData.getColumnCount();
      jasonGenerator.writeStartArray();
      while (pResultSet.next()) {
        jasonGenerator.writeStartObject();
        for (int column = 1; column <= columnCount; column++) {
          jasonGenerator.writeObjectField(metaData.getColumnName(column), pResultSet.getObject(column));
        }
        jasonGenerator.writeEndObject();
      }
      jasonGenerator.writeEndArray();
      jasonGenerator.flush();
    } catch (IOException | SQLException ex) {
      throw new RuntimeException(ex);
    }
    return null;
  }
}