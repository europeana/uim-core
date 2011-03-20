/* LRUCache.java - created on Mar 20, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K, V> extends LinkedHashMap<K, V> {
    
    private final int maximum;
    
    public LruCache(int maximum) {
        super(10, 0.75F, true);
        this.maximum = maximum;
    }
    
    public LruCache(int maximum, int initialCapacity) {
        super(initialCapacity, 0.75F, true);
        this.maximum = maximum;
    }
    

    public LruCache(int maximum, int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor, true);
        this.maximum = maximum;
    }


    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
       return size() > maximum;
    }

}