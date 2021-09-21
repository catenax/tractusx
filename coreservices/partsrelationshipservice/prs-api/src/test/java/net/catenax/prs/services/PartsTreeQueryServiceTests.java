package net.catenax.prs.services;

import com.catenax.partsrelationshipservice.dtos.PartsTreeView;
import com.github.javafaker.Faker;
import net.catenax.prs.requests.VinPartsTreeRequest;
import net.catenax.prs.util.StubResourcesHelper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PartsTreeQueryServiceTests {

    @Test
    public void getPartsTreeShouldReturnNonNullResponse() throws Exception {
        // Arrange
        var sut = new PartsTreeQueryService(new StubResourcesHelper());
        var faker = new Faker();
        var request = VinPartsTreeRequest.builder()
                .vin(faker.lorem().word())
                .view(faker.options().option(PartsTreeView.class))
                .build();

        // Act
        var response = sut.getPartsTree(request);

        // Assert
        assertThat(response).isNotNull();
    }
}