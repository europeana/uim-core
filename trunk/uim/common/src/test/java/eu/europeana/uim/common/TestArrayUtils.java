/* TestArrayUtils.java - created on Mar 20, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

/**
 * Tests for the utility functions for separating arrays into batches
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 20, 2011
 */
public class TestArrayUtils {

    
    /**
     * Test for long batches
     */
    @Test
    public void testLongBatches() {
        long[] data = new long[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        
        List<long[]> batches = ArrayUtils.batches(data, 3);
        assertEquals(4, batches.size());
        assertEquals(3, batches.get(0).length);
        assertEquals(3, batches.get(1).length);
        assertEquals(3, batches.get(2).length);
        assertEquals(1, batches.get(3).length);
        
        batches = ArrayUtils.batches(data, 5);
        assertEquals(2, batches.size());
        assertEquals(5, batches.get(0).length);
        assertEquals(5, batches.get(1).length);
        
        batches = ArrayUtils.batches(data, 12);
        assertEquals(1, batches.size());
        assertEquals(10, batches.get(0).length);
    }

    /**
     * Test for string batches
     */
    @Test
    public void testStringBatches() {
        String[] data = new String[] {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        
        List<String[]> batches = ArrayUtils.batches(data, 3);
        assertEquals(4, batches.size());
        assertEquals(3, batches.get(0).length);
        assertEquals(3, batches.get(1).length);
        assertEquals(3, batches.get(2).length);
        assertEquals(1, batches.get(3).length);
        
        batches = ArrayUtils.batches(data, 5);
        assertEquals(2, batches.size());
        assertEquals(5, batches.get(0).length);
        assertEquals(5, batches.get(1).length);
        
        batches = ArrayUtils.batches(data, 12);
        assertEquals(1, batches.size());
        assertEquals(10, batches.get(0).length);
    }

}
