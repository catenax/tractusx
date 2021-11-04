//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.client.composite;

import lombok.RequiredArgsConstructor;
import net.catenax.prs.client.ApiException;
import net.catenax.prs.client.api.PartsRelationshipServiceApi;
import net.catenax.prs.client.composite.util.Dijkstra;
import net.catenax.prs.client.composite.util.ItemPair;
import net.catenax.prs.client.exceptions.ApiClientException;
import net.catenax.prs.client.exceptions.CompositionException;
import net.catenax.prs.client.model.PartId;
import net.catenax.prs.client.model.PartInfo;
import net.catenax.prs.client.model.PartRelationship;
import net.catenax.prs.client.model.PartRelationshipsWithInfos;
import net.catenax.prs.client.requests.PartsTreeByObjectIdRequest;
import net.catenax.prs.registryclient.StubRegistryClient;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * Client library for Parts Relationship service, allowing to assemble a parts tree from multiple
 * data space partitions.
 */
@RequiredArgsConstructor
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class CompositePartsRelationshipClient {

    /**
     * Registry client.
     */
    private final StubRegistryClient registryClient;

    /**
     * Supplier for Client stub. Not shared, as its configuration is updated between search
     * operations to access various servers.
     */
    private final Supplier<PartsRelationshipServiceApi> clientSupplier;

    /**
     * Generate a new instance of {@link CompositePartsRelationshipClient}.
     *
     * @param registryClient the Registry client.
     */
    public CompositePartsRelationshipClient(final StubRegistryClient registryClient) {
        this(registryClient, () -> new PartsRelationshipServiceApi());
    }

    /**
     * Retrieve a parts tree for a given part.
     *
     * @param request Parts Tree request.
     * @return the parts tree potentially assembled from multiple servers. Never returns {@literal null}.
     * @throws ApiClientException on communication or server errors.
     */
    public CompositeSearchResult getPartsTree(final PartsTreeByObjectIdRequest request) {
        final PartId partId = new PartId()
                .oneIDManufacturer(request.getOneIDManufacturer())
                .objectIDManufacturer(request.getObjectIDManufacturer());
        return search(partId, request);
    }

    private CompositeSearchResult search(final PartId partId, final PartsTreeByObjectIdRequest request) {
        final Search search = new Search(partId, request, clientSupplier);
        search.run();
        return search.result();
    }

    /**
     * Helper class to store in-progress composite search data.
     */
    private final class Search {
        /**
         * Template API request.
         */
        private final PartsTreeByObjectIdRequest request;

        /**
         * PRS Client stub. Not shared, as its configuration is updated between search
         * operations to access various servers.
         */
        private final PartsRelationshipServiceApi prsClient;

        /**
         * Accumulated {@link PartInfo}s.
         */
        private final Set<PartInfo> partInfos = new LinkedHashSet<>();

        /**
         * Accumulated {@link PartRelationship}s.
         */
        private final Set<PartRelationship> partRelationships = new LinkedHashSet<>();

        /**
         * Accumulated {@link PartId}s not found in registry.
         */
        private final Set<PartId> unresolved = new LinkedHashSet<>();

        /**
         * Tree nodes to visit for potential additional API requests.
         */
        private final Stack<ItemPair<PartId, Integer>> partIds = new Stack<>();

        private Search(
                final PartId rootPartId,
                final PartsTreeByObjectIdRequest request,
                final Supplier<PartsRelationshipServiceApi> clientSupplier) {
            this.request = request;
            this.prsClient = clientSupplier.get();
            partIds.push(new ItemPair<>(rootPartId, request.getDepth()));
        }

        private void run() {
            while (!partIds.empty()) {
                final ItemPair<PartId, Integer> partIdAndDepth = partIds.pop();
                final PartId partId = partIdAndDepth.getKey();
                final Integer depth = partIdAndDepth.getValue();
                final var prsUrl = registryClient.getUrl(partId);
                prsUrl.ifPresentOrElse(
                        url -> retrievePartialTree(url, partId, depth),
                        () -> unresolved.add(partId));
            }
        }

        private void retrievePartialTree(final String prsUrl, final PartId partId, final Integer depth) {
            prsClient.getApiClient().setBasePath(prsUrl);
            PartRelationshipsWithInfos tree;
            try {
                tree = prsClient.getPartsTreeByOneIdAndObjectId(
                        partId.getOneIDManufacturer(),
                        partId.getObjectIDManufacturer(),
                        request.getView(),
                        request.getAspect(),
                        depth);
            } catch (ApiException e) {
                throw new ApiClientException(e);
            }
            partRelationships.addAll(requireNonNull(tree.getRelationships()));
            partInfos.addAll(requireNonNull(tree.getPartInfos()));
            tree.getRelationships().stream()
                    .map(PartRelationship::getChild)
                    .filter(p -> !prsUrl.equals(registryClient.getUrl(p).orElse(null)))
                    .forEach(p -> addCandidateForNextSearch(partId, depth, p));
        }

        private void addCandidateForNextSearch(final PartId rootPartId, final Integer depth, final PartId partId) {
            final Integer usedDepth = Dijkstra.shortestPathLength(partRelationships, rootPartId, partId)
                    .orElseThrow(() -> new CompositionException("Unconnected parts returned by PRS"));
            final int remainingDepth = depth - usedDepth;
            if (remainingDepth > 0) {
                partIds.push(new ItemPair<>(partId, remainingDepth));
            }
        }

        private CompositeSearchResult result() {
            final var result = new PartRelationshipsWithInfos()
                    .relationships(List.copyOf(partRelationships))
                    .partInfos(List.copyOf(partInfos));
            return CompositeSearchResult.builder()
                    .result(result)
                    .unresolved(Collections.unmodifiableSet(unresolved))
                    .build();
        }
    }
}
