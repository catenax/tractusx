//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.connector.consumer.registry;

import net.catenax.prs.connector.consumer.configuration.PartitionDeploymentsConfig;
import net.catenax.prs.connector.consumer.configuration.PartitionsConfig;
import net.catenax.prs.connector.requests.PartsTreeByObjectIdRequest;
import org.eclipse.dataspaceconnector.spi.EdcException;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A stub registry client.
 */
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
public class StubRegistryClient {
    /**
     * The key in {@link PartitionDeploymentsConfig} for the Provider Connector URL.
     */
    public static final String CONNECTOR_URL_ATTRIBUTE_KEY = "connector_url";

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

    private static <K, V> Optional<V> getAsOptional(final Map<K, V> map, final K key) {
        return Optional.ofNullable(map.get(key));
    }

    private String getApiUrl(final PartitionDeploymentsConfig deployments, final PartitionsConfig.PartitionConfig partitionConfig) {
        final var attributeCollection = getAsOptional(deployments, partitionConfig.getKey())
                .orElseThrow(() -> new ConfigurationException("Missing entry in partition attributes file: " + partitionConfig.getKey()));
        return getAsOptional(attributeCollection, CONNECTOR_URL_ATTRIBUTE_KEY)
                .orElseThrow(() -> new ConfigurationException("Missing " + CONNECTOR_URL_ATTRIBUTE_KEY + " key in partition attributes file for " + partitionConfig.getKey()))
                .getValue();
    }

    /**
     * Retrieve the URL for the part ID.
     *
     * @param request the request containing part identifier to search.
     * @return Guaranteed to never return {@literal null} in the optional value.
     */
    public Optional<String> getUrl(final PartsTreeByObjectIdRequest request) {
        return getAsOptional(oneIdToUrlMappings, request.getOneIDManufacturer());
    }

    /**
     * Exception thrown in case of invalid configuration.
     */
    public static final class ConfigurationException extends EdcException {
        /**
         * Generate a new instance of a {@link ConfigurationException}
         *
         * @param message Exception message.
         */
        public ConfigurationException(final String message) {
            super(message);
        }
    }

    /**
     * XXX.
     *
     * @param <K> XXX.
     * @param <V> XXX.
     */
    private static final class ItemPair<K, V> extends AbstractMap.SimpleEntry<K, V> {
        private ItemPair(final K key, final V value) {
            super(key, value);
        }
    }
}
