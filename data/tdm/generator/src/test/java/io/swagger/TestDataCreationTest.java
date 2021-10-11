package io.swagger;

import com.catenax.tdm.api.CatenaXApiControllerDelegateImpl;
import com.catenax.tdm.model.v1.PartId;
import com.catenax.tdm.model.v1.PartTypeNameUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:11.13-alpine:///tdg",
        "spring.jpa.hibernate.ddl-auto=create",
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
        "spring.jpa.properties.hibernate.enable_lazy_load_no_trans=true"
})
public class TestDataCreationTest {

    @Autowired
    private CatenaXApiControllerDelegateImpl delegate;

    @Test
    void create_and_persist_vehicle_with_aspects() {
        // Given
        String oneId = "CAXSWPFTJQEVZNZZ";
        delegate.createVehicle(oneId, 1, "G30");

        // When
        List<PartTypeNameUpdate> partTypeNameUpdate = delegate.getPartTypeNameUpdate(oneId, null, null);

        PartId vehiclePart = partTypeNameUpdate.stream()
                .filter(p -> "vehicle".equalsIgnoreCase(p.getPartTypeName()))
                .map(PartTypeNameUpdate::getPart)
                .findFirst().orElseThrow(() -> new RuntimeException("Vehicle part not found"));

        List<Object> aspects = delegate.getAspect("all", vehiclePart.getOneIDManufacturer(), vehiclePart.getObjectIDManufacturer()).getBody();

        // Then
        assertThat(aspects).isNotEmpty();
    }
}
