package io.swagger;

import com.catenax.tdm.TransactionQueue;
import com.catenax.tdm.dao.QueueDao;
import com.catenax.tdm.model.v1.PartId;
import com.catenax.tdm.model.v1.PartInfo;
import com.catenax.tdm.sampledata.AspectSampleData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:11.13-alpine:///tdg",
        "spring.jpa.hibernate.ddl-auto=create",
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver"
})
public class AspectPersistenceTest {

    @Autowired
    private QueueDao queueDao;

    @Test
    void persist_aspect_sample_data() {
        // Given
        PartId partId = new PartId();
        partId.setOneIDManufacturer("123");
        partId.setObjectIDManufacturer("456");
        PartInfo partInfo = new PartInfo();
        partInfo.setPart(partId);

        // When
        new AspectSampleData(partInfo);
        TransactionQueue.flush(queueDao);

        // Then
        // no exception during serialization
    }
}
