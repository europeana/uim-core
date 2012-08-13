/* TKeyTest.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.uim.common.TKey;

/**
 * Test for TKey
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 16, 2011
 */
public class TKeyTest {
    /**
     * setup method for tkeys which clears all TKeys.
     */
    @Before
    public void setUp() {
        TKey.clear();
    }

    /**
     * Test if TKey can be resolved correctly
     */
    @Test
    public void testKeyResolve() {
        TKey<TKeyTest, String> register = TKey.register(TKeyTest.class, "test", String.class);

        TKey<TKeyTest, Serializable> resolved = TKey.resolve(TKeyTest.class, "test");

        assertSame(register, resolved);
    }

    /**
     * Test if TKey can be resolved correctly with namespaces
     */
    @Test
    public void testNamespace() {
        TKey.register(TKey.class, "test", String.class);

        TKey<TKeyTest, Serializable> resolved = TKey.resolve(TKeyTest.class, "test");

        assertNull(resolved);
    }

    /**
     * Test if TKey can be resolved correctly with namespaces
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testRoundtrip() {
        TKey<TKey, String> key = TKey.register(TKey.class, "test", String.class);

        String string = key.toString();
        TKey<?, ?> key2 = TKey.fromString(string);

        assertSame(key, key2);
    }

    /**
     * This tests the backward compatible way of getting a key from a string
     */
    @SuppressWarnings({ "rawtypes" })
    @Test
    public void testFromStringBackwardCompatibility() {
        TKey<TKey, String> key = TKey.register(TKey.class, "key", String.class);
        String string = TKey.class.getName() + "/key";
        TKey<?, ?> tkey = TKey.fromString(string);
        assertNotNull(tkey);
        assertSame(key, tkey);
        String string2 = TKey.class.getName() + "/key2";
        TKey<?, ?> tkey2 = TKey.fromString(string2);
        assertNull(tkey2);
    }
}
