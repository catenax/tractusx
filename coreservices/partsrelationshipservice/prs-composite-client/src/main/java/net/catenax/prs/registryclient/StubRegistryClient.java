//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.registryclient;

import net.catenax.prs.client.composite.util.ItemPair;
import net.catenax.prs.client.exceptions.ConfigurationException;
import net.catenax.prs.client.model.PartId;
import net.catenax.prs.registryclient.config.PartitionDeploymentsConfig;
import net.catenax.prs.registryclient.config.PartitionsConfig;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A stub registry client.
 */
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class StubRegistryClient {
    /**
     * The key in {@link PartitionDeploymentsConfig} for the PRS API URL.
     */
    public static final String API_URL_ATTRIBUTE_KEY = "api_url";

    /**
     * Configuration for mapping from OneIDs to data space URLs.
     */
    private final Map<String, String> oneIdToUrlMappings;

    /**
     * Creates a new instance of {@link StubRegistryClient}.
     *
     * @param config      partition configuration.
     * @param deployments partition deployments configuration.
     */
    public StubRegistryClient(final PartitionsConfig config, final PartitionDeploymentsConfig deployments) {
        if (config.getPartitions().isEmpty()) {
            throw new ConfigurationException("No partitions defined");
        }
        this.oneIdToUrlMappings = config.getPartitions().stream()
                .flatMap(
                        p -> p.getOneIDs().stream()
                                .map(o -> new ItemPair<>(o, getApiUrl(deployments, p))))
                .collect(Collectors.toMap(ItemPair::getKey, ItemPair::getValue));
    }

    private String getApiUrl(final PartitionDeploymentsConfig deployments, final PartitionsConfig.PartitionConfig partitionConfig) {
        final var attributeCollection = getAsOptional(deployments, partitionConfig.getKey())
                .orElseThrow(() -> new ConfigurationException("Missing entry in partition attributes file: " + partitionConfig.getKey()));
        return getAsOptional(attributeCollection, API_URL_ATTRIBUTE_KEY)
                .orElseThrow(() -> new ConfigurationException("Missing " + API_URL_ATTRIBUTE_KEY + " key in partition attributes file for " + partitionConfig.getKey()))
                .getValue();
    }

    /**
     * Retrieve the URL for the part ID.
     *
     * @param partId the part identifier to search.
     * @return Guaranteed to never return {@literal null} in the optional value.
     */
    public Optional<String> getUrl(final PartId partId) {
        return getAsOptional(oneIdToUrlMappings, partId.getOneIDManufacturer());
    }

    private static <K, V> Optional<V> getAsOptional(final Map<K, V> map, final K key) {
        return Optional.ofNullable(map.get(key));
    }
}
