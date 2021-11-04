//
// Copyright (c) 2021 Copyright Holder (Catena-X Consortium)
//
// See the AUTHORS file(s) distributed with this work for additional
// information regarding authorship.
//
// See the LICENSE file(s) distributed with this work for
// additional information regarding license terms.
//
package net.catenax.prs.client.composite.util;

import java.util.AbstractMap;

/**
 * A pair of items.
 *
 * @param <K> type of the first element in the pair.
 * @param <V> type of the second element in the pair.
 */
public class ItemPair<K, V> extends AbstractMap.SimpleEntry<K, V> {
    /**
     * Creates a new instance of {@link ItemPair}.
     *
     * @param key   first element in the pair.
     * @param value second element in the pair.
     */
    public ItemPair(final K key, final V value) {
        super(key, value);
    }
}
