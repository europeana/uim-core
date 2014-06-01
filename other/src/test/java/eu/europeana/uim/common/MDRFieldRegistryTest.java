/* MDRFieldRegistryTest.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests implictly the {@link TKey} and the fields on {@link MDRFieldRegistry}.
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 16, 2011
 */
public class MDRFieldRegistryTest {

    /**
     * Clears the {@link TKey}s registry.
     */
    @Before
    public void setup() {
        TKey.clear();
    }

    /**
     * Checks if the {@link TKey}s registry is empty.
     */
    @Test
    public void testUntouched() {
        Assert.assertEquals(0, TKey.size());
    }

    /**
     * Tests filling of {@link TKey}s registryl
     */
    @Test
    public void testFieldRegistry() {
        TKey<MDRFieldRegistry, String> record = MDRFieldRegistry.rawrecord;
        Assert.assertNotNull(record);
        Assert.assertEquals(2, TKey.size());
    }
}
