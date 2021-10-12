package com.catenax.tdm;

import com.catenax.tdm.model.v1.DocumentsInner;
import com.catenax.tdm.model.v1.PartId;
import com.catenax.tdm.model.v1.PartInfo;
import com.catenax.tdm.sampledata.PartSampleData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.assertj.core.api.Assertions.assertThat;


public class AspectSerializationTest {

    @Test
    public void deserialize_document_aspect() throws IOException {
        // Given
        PartId partId = new PartId();
            partId.setOneIDManufacturer("123");
            partId.setObjectIDManufacturer("456");
        PartInfo partInfo = new PartInfo();
        partInfo.setPart(partId);

        String aspectFileContent = PartSampleData.resolveResource(partInfo, "documentation");

        // When
        final ObjectMapper mapper = new ObjectMapper();
        List<DocumentsInner> documentsInners = mapper.readValue(aspectFileContent, new TypeReference<List<DocumentsInner>>() {
        });

        // Then
        assertThat(documentsInners).isNotNull();
        assertThat(documentsInners).hasSize(1);

        String json = mapper.writeValueAsString(documentsInners);
        assertThatJson(json)
                .when(IGNORING_ARRAY_ORDER)
                .isEqualTo(aspectFileContent);
    }

}
