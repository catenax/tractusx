package net.catenax.prs.requests;

import com.catenax.partsrelationshipservice.dtos.PartsTreeView;
import com.github.javafaker.Faker;

public class RequestMother {
    /**
     * JavaFaker instance used to generate random data.
     */
    public final Faker faker = new Faker();

    public PartsTreeByVinRequest byVin() {
        return PartsTreeByVinRequest.builder()
                .vin(faker.lorem().word())
                .view(faker.options().option(PartsTreeView.class))
                .build();
    }
}
