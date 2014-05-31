/* LRUCache.java - created on Mar 20, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Simple wrapper around a LinkedHashMap with a maximum number of elements to
 * implement a Least Recently Used cache.
 *
 * @param <K> key type
 * @param <V> value type
 *
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date May 31, 2011
 */
public class LruCache<K, V> extends LinkedHashMap<K, V> {

    private final int maximum;

    /**
     * Creates a new instance of this class.
     *
     * @param maximum maximum number of entries
     */
    public LruCache(int maximum) {
        super(10, 0.75F, true);
        this.maximum = maximum;
    }

    /**
     * Creates a new instance of this class.
     *
     * @param maximum maximum number of entries
     * @param initialCapacity the initial size of the underlying HashMap
     */
    public LruCache(int maximum, int initialCapacity) {
        super(initialCapacity, 0.75F, true);
        this.maximum = maximum;
    }

    /**
     * Creates a new instance of this class.
     *
     * @param maximum maximum number of entries
     * @param initialCapacity the initial size of the underlying HashMap
     * @param loadFactor the load factor. If the hash map is filled larger than
     * that number, the size is automatically increased.
     */
    public LruCache(int maximum, int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor, true);
        this.maximum = maximum;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > maximum;
    }
}
