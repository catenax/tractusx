package net.catenax.prs.connector.consumer.service;

import com.github.javafaker.Faker;
import net.catenax.prs.connector.requests.FileRequest;
import net.catenax.prs.connector.requests.PartsTreeByObjectIdRequest;

public class RequestMother {

    Faker faker = new Faker();

    public PartsTreeByObjectIdRequest request() {
        return PartsTreeByObjectIdRequest.builder()
                .oneIDManufacturer(faker.company().name())
                .objectIDManufacturer(faker.lorem().characters(10, 20))
                .view("AS_BUILT")
                .depth(faker.number().numberBetween(1, 5))
                .build();

    }

    public FileRequest fileRequest() {
        return FileRequest.builder()
                .partsTreeRequest(request())
                .build();
    }

}
