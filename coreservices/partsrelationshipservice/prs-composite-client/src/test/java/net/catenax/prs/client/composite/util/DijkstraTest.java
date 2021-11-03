package net.catenax.prs.client.composite.util;

import net.catenax.prs.client.model.PartId;
import net.catenax.prs.client.model.PartRelationship;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import testing.BaseDtoMother;
import testing.DtoMother;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class DijkstraTest {

    @ParameterizedTest
    @MethodSource("shortestPathLengthArgs")
    void shortestPathLength(Set<PartRelationship> edges, PartId source, PartId target, Optional<Integer> expected) {
        assertThat(Dijkstra.shortestPathLength(edges, source, target))
                .isEqualTo(expected);
    }

    static Stream<Arguments> shortestPathLengthArgs() {

        DtoMother generate = new DtoMother();
        BaseDtoMother build = new BaseDtoMother();
        PartId source = generate.partId();
        PartId target = generate.partId();
        PartId other = generate.partId();
        PartId another = generate.partId();

        return Stream.of(
                Arguments.of(Set.of(build.partRelationship(source, source)), source, source, Optional.of(0))
                , Arguments.of(Set.of(), source, target, Optional.empty())
                , Arguments.of(Set.of(build.partRelationship(source, target)), source, target, Optional.of(1))
                , Arguments.of(Set.of(build.partRelationship(source, other)), source, target, Optional.empty())
                , Arguments.of(Set.of(build.partRelationship(target, source)), source, target, Optional.empty())
                , Arguments.of(Set.of(
                        build.partRelationship(source, other)
                        , build.partRelationship(other, target)
                ), source, target, Optional.of(2))
                , Arguments.of(Set.of(
                        build.partRelationship(source, other)
                        , build.partRelationship(other, another)
                        , build.partRelationship(another, target)
                        , build.partRelationship(other, target)
                ), source, target, Optional.of(2))
                , Arguments.of(Set.of(
                        build.partRelationship(source, other)
                        , build.partRelationship(other, source)
                        , build.partRelationship(other, target)
                ), source, target, Optional.of(2))
        );
    }
}