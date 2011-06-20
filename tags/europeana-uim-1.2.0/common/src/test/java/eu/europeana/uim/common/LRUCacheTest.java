/* LRUCacheTest.java - created on May 31, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * Tests for LRUCache
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date May 31, 2011
 */
public class LRUCacheTest {
 
    
    private void testCacheSemantically(LruCache<String, String> c) {
        c.put ("1", "one");                           // 1
        c.put ("2", "two");                           // 2 1
        c.put ("3", "three");                         // 3 2 1
        c.put ("4", "four");                          // 4 3 2
        assertNotNull(c.get("2"));                    // 2 4 3
        c.put ("5", "five");                          // 5 2 4
        c.put ("4", "second four");                   // 4 5 2
        // Verify cache content.
        
        assertEquals(3,c.size());
        assertEquals("second four",c.get("4"));
        assertEquals("five",c.get("5"));
        assertEquals("two",c.get("2"));
        assertNull(c.get("1"));
        
    }
    /**
 * Tests the lru cache functionaility
 */
@Test
 public void testLRUCache() {
     LruCache<String, String> c1 = new LruCache<String, String>(3);
     LruCache<String,String> c2  = new LruCache<String, String>(3,20);
     LruCache<String,String> c3  = new LruCache<String, String>(3,20,0.5F);
     testCacheSemantically(c1);
     testCacheSemantically(c2);
     testCacheSemantically(c3);   
 }
}
