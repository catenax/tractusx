//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.systemtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.catenax.prs.client.composite.CompositePartsRelationshipClient;
import net.catenax.prs.client.requests.PartsTreeByObjectIdRequest;
import net.catenax.prs.registryclient.StubRegistryClient;
import net.catenax.prs.registryclient.config.PartitionDeploymentsConfig;
import net.catenax.prs.registryclient.config.PartitionsConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import static java.lang.String.format;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * System tests verify that PRS returns expected loaded data for the given environment
 *
 * @see <a href="https://confluence.catena-x.net/display/ARTI/MTPDC+Testing">MTPDC Testing</a>
 */
@Tag("SystemTests")
public class SystemTests extends SystemTestsBase {

    private static final String VEHICLE_ONEID = "CAXSWPFTJQEVZNZZ";
    private static final String VEHICLE_OBJECTID = "UVVZI9PKX5D37RFUB";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static StubRegistryClient registryClient;

    private final CompositePartsRelationshipClient client = new CompositePartsRelationshipClient(registryClient);

    @BeforeAll
    static void setUp() throws Exception {
        var partitions = readJson("../cd/dataspace-partitions.json", PartitionsConfig.class, "");

        var partitionAttributes = readJson("../dataspace-deployments.json", PartitionDeploymentsConfig.class,
                "For development, see README.md for instructions on downloading the file.");

        registryClient = new StubRegistryClient(partitions, partitionAttributes);
    }

    private static <T> T readJson(String path, Class<T> type, String message) throws IOException {
        try {
            return objectMapper.readValue(Paths.get(path).toFile(), type);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found: " + path + ". " + message);
        }
    }

    @Test
    void getPartsTreeByOneIdAndObjectId(TestInfo testInfo) throws Exception {
        // Arrange
        var environment = System.getProperty("environment", "dev");

        // getResourceAsStream returns null if resource not found
        var resource = getClass().getResourceAsStream(format("%s-%s-expected.json",
                testInfo.getTestMethod().orElseThrow().getName(),
                environment));

        // skip test on INT environment
        assumeTrue(resource != null, "Test not available on environment " + environment);

        // Act
        var response =
                client.getPartsTree(
                        PartsTreeByObjectIdRequest.builder()
                                .oneIDManufacturer(VEHICLE_ONEID)
                                .objectIDManufacturer(VEHICLE_OBJECTID)
                                .view("AS_BUILT")
                                .aspect(ASPECT_MATERIAL)
                                .depth(2)
                                .build());

        // Assert
        assertThatJson(response.getResult())
            .when(IGNORING_ARRAY_ORDER)
            .isEqualTo(new String(resource.readAllBytes()));

        assertThat(response.getUnresolved()).isEmpty();
    }
}
