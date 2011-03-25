/* MDRFieldRegistryTest.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests implictly the {@link TKey} and the fields on {@link MDRFieldRegistry}.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public class MDRFieldRegistryTest {
    /**
     * Clears the {@link TKey}s registry.
     */
    @BeforeClass
    public static void setup() {
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
        TKey<MDRFieldRegistry, String> title = MDRFieldRegistry.title;
        Assert.assertNotNull(title);
        Assert.assertEquals(3, TKey.size());
    }
}
