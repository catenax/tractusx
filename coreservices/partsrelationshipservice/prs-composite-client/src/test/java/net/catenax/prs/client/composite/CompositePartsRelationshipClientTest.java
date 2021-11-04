package net.catenax.prs.client.composite;

import com.github.javafaker.Faker;
import net.catenax.prs.client.ApiClient;
import net.catenax.prs.client.ApiException;
import net.catenax.prs.client.Configuration;
import net.catenax.prs.client.api.PartsRelationshipServiceApi;
import net.catenax.prs.client.exceptions.ApiClientException;
import net.catenax.prs.client.exceptions.CompositionException;
import net.catenax.prs.client.model.PartId;
import net.catenax.prs.client.model.PartInfo;
import net.catenax.prs.client.model.PartRelationship;
import net.catenax.prs.client.model.PartRelationshipsWithInfos;
import net.catenax.prs.client.requests.PartsTreeByObjectIdRequest;
import net.catenax.prs.registryclient.StubRegistryClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import testing.BaseDtoMother;
import testing.DtoMother;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompositePartsRelationshipClientTest {

    @Mock
    StubRegistryClient registry;
    @Mock
    PartsRelationshipServiceApi client;

    ApiClient apiClient = Configuration.getDefaultApiClient();

    Faker faker = new Faker();

    DtoMother generate = new DtoMother();
    BaseDtoMother build = new BaseDtoMother();

    PartRelationship relationship = generate.partRelationship();
    PartInfo partInfo = generate.partInfo();

    PartRelationshipsWithInfos emptyPrsResult = build.partRelationshipsWithInfos(List.of(), List.of());

    PartRelationshipsWithInfos prsResult = build.partRelationshipsWithInfos(
            List.of(relationship),
            List.of(partInfo));

    PartId parent = relationship.getParent();
    PartId child = relationship.getChild();

    PartsTreeByObjectIdRequest request = PartsTreeByObjectIdRequest.builder()
            .oneIDManufacturer(parent.getOneIDManufacturer())
            .objectIDManufacturer(parent.getObjectIDManufacturer())
            .view(faker.lorem().word())
            .aspect(faker.lorem().word())
            .depth(faker.number().numberBetween(5, 10))
            .build();

    CompositePartsRelationshipClient sut;

    String url = faker.internet().url();

    @BeforeEach
    void setUp() {
        sut = new CompositePartsRelationshipClient(registry, () -> client);

        lenient()
                .when(client.getApiClient())
                .thenReturn(apiClient);
    }

    @Test
    void getPartsTree_WhenUnresolved_Returns() {
        // Act
        var result = sut.getPartsTree(request);

        // Assert
        assertThat(result)
                .isEqualTo(
                        CompositeSearchResult.builder()
                                .result(emptyPrsResult)
                                .unresolved(Set.of(parent))
                                .build());
    }

    @Test
    void getPartsTree_WhenDepthReached() throws Exception {
        // Arrange
        request = request.toBuilder()
                .depth(1)
                .build();
        when(registry.getUrl(parent)).thenReturn(Optional.of(url));

        whenRequestIsMade()
                .thenReturn(prsResult);

        // Act
        var result = sut.getPartsTree(request);

        // Assert
        // child is not included in unresolved, since client doesn't attempt to fetch it
        assertThat(result)
                .isEqualTo(
                        CompositeSearchResult.builder()
                                .result(prsResult)
                                .unresolved(Set.of())
                                .build());
    }

    @Test
    void getPartsTree_WhenEmptyResult() throws Exception {
        // Arrange
        when(registry.getUrl(parent)).thenReturn(Optional.of(url));

        whenRequestIsMade()
                .thenReturn(emptyPrsResult);

        // Act
        var result = sut.getPartsTree(request);

        // Assert
        assertThat(result)
                .isEqualTo(
                        CompositeSearchResult.builder()
                                .result(emptyPrsResult)
                                .unresolved(Set.of())
                                .build());
    }

    @Test
    void getPartsTree_WhenApiException_Rethrows() throws Exception {
        // Arrange
        when(registry.getUrl(parent)).thenReturn(Optional.of(url));

        ApiException apiException = new ApiException();
        whenRequestIsMade()
                .thenThrow(apiException);

        // Act + Assert
        assertThatThrownBy(() -> sut.getPartsTree(request))
                .isInstanceOf(ApiClientException.class)
                .hasCause(apiException);
    }

    @Test
    void getPartsTree_WhenInvalidResult_Throws() throws Exception {
        // Arrange
        when(registry.getUrl(parent)).thenReturn(Optional.of(url));

        // Set up mock PRS to return part relationships that do not match the requested part ID
        whenRequestIsMade()
                .thenReturn(generate.partRelationshipsWithInfos());

        assertThatThrownBy(() -> sut.getPartsTree(request))
                .isInstanceOf(CompositionException.class)
                .hasMessage("Unconnected parts returned by PRS");
    }

    @Test
    void getPartsTree() throws Exception {
        // Arrange
        when(registry.getUrl(parent)).thenReturn(Optional.of(url));

        whenRequestIsMade()
                .thenReturn(prsResult);

        // Act
        var result = sut.getPartsTree(request);

        // Assert
        assertThat(apiClient.getBasePath())
                .isEqualTo(url);

        assertThat(result)
                .isEqualTo(
                        CompositeSearchResult.builder()
                                .result(prsResult)
                                .unresolved(Set.of(child))
                                .build());
    }

    @Test
    void getPartsTree_MultipleLevels() throws Exception {
        // Arrange

        // Set up two data space partitions A and B, where partition A
        // contains two OneIDs ("co-tenants").

        String urlA = faker.internet().url();
        String urlB = faker.internet().url();

        String oneIdA1 = parent.getOneIDManufacturer();
        String oneIdA2 = faker.company().name();
        String oneIdB = faker.company().name();

        var a = parent;
        var b = build.partId(oneIdA1, "b-" + randomObjectId());
        var c = build.partId(oneIdA2, "c-" + randomObjectId());
        var d = build.partId(oneIdB, "d-" + randomObjectId());
        var e = build.partId(oneIdB, "e-" + randomObjectId());

        var partInfoA = generate.partInfo();
        var partInfoB = generate.partInfo();

        when(registry.getUrl(a)).thenReturn(Optional.of(urlA));
        when(registry.getUrl(b)).thenReturn(Optional.of(urlA));
        when(registry.getUrl(c)).thenReturn(Optional.of(urlA));
        when(registry.getUrl(d)).thenReturn(Optional.of(urlB));
        when(registry.getUrl(e)).thenReturn(Optional.of(urlB));

        var resultA = build.partRelationshipsWithInfos(
                List.of(
                        build.partRelationship(a, b),
                        build.partRelationship(b, c),
                        build.partRelationship(c, d)
                ),
                List.of(partInfoA));

        whenRequestIsMade()
                .thenReturn(resultA);

        var resultB = build.partRelationshipsWithInfos(
                List.of(
                        build.partRelationship(d, e)
                ),
                List.of(partInfoB));

        when(client
                .getPartsTreeByOneIdAndObjectId(
                        d.getOneIDManufacturer(),
                        d.getObjectIDManufacturer(),
                        request.getView(),
                        request.getAspect(),
                        request.getDepth() - 3))
                .thenReturn(resultB);

        // Act
        var result = sut.getPartsTree(request);

        // Assert
        assertThat(apiClient.getBasePath())
                .isEqualTo(urlB);

        var expectedResult = build.partRelationshipsWithInfos(
                union(resultA.getRelationships(), resultB.getRelationships()),
                union(resultA.getPartInfos(), resultB.getPartInfos()));

        assertThat(result)
                .isEqualTo(
                        CompositeSearchResult.builder()
                                .result(expectedResult)
                                .unresolved(Set.of())
                                .build());
    }

    @Test
    void constructor() {
        new CompositePartsRelationshipClient(registry);
    }

    private OngoingStubbing<PartRelationshipsWithInfos> whenRequestIsMade() throws ApiException {
        return when(client
                .getPartsTreeByOneIdAndObjectId(
                        request.getOneIDManufacturer(),
                        request.getObjectIDManufacturer(),
                        request.getView(),
                        request.getAspect(),
                        request.getDepth()));
    }


    private <T> List<T> union(List<T> left, List<T> right) {
        return Stream.of(left, right)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private String randomObjectId() {
        return faker.lorem().characters(10, 20);
    }
}