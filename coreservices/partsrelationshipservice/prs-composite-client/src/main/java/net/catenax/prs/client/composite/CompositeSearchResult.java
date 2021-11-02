package net.catenax.prs.client.composite;

import lombok.Builder;
import lombok.Value;
import net.catenax.prs.client.model.PartId;
import net.catenax.prs.client.model.PartRelationshipsWithInfos;

import java.util.Collection;

/**
 * Result of {@link CompositePartsRelationshipClient} search.
 */
@Value
@Builder
public final class CompositeSearchResult {
    /**
     * Composite search result.
     */
    private PartRelationshipsWithInfos result;

    /**
     * Tree nodes for which the parts relationship service could not be
     * resolved in the registry.
     */
    private Collection<PartId> unresolved;
}
