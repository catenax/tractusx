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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AspectSerializationTest {

    @Test
    public void deserialize_document_aspect() throws IOException {
        PartId partId = new PartId();
            partId.setOneIDManufacturer("123");
            partId.setObjectIDManufacturer("456");
        PartInfo partInfo = new PartInfo();
        partInfo.setPart(partId);

        String content = PartSampleData.resolveResource(partInfo, "documentation");

        final ObjectMapper mapper = new ObjectMapper();
        List<DocumentsInner> documentsInners = mapper.readValue(content, new TypeReference<List<DocumentsInner>>() {
        });

        assertThat(documentsInners).isNotNull();
    }

}
