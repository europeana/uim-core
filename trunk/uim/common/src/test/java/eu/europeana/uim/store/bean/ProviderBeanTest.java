/* CollectionBeanTest.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.store.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import eu.europeana.uim.store.Provider;

/**
 * Tests basic operations on implementation {@link ProviderBean} for {@link Provider}.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public class ProviderBeanTest {
    /**
     * Tests creating of bean and all getter and setter methods on it.
     */
    @Test
    public void testProviderSetterGetter() {
        Provider<Long> bean = new ProviderBean<Long>(1L);
        bean.setMnemonic("mnemonic");
        bean.setName("name");
        bean.setOaiBaseUrl("Pbase");
        bean.setOaiMetadataPrefix("Pmeta");
        bean.setAggregator(true);

        assertEquals(new Long(1), bean.getId());
        assertEquals("mnemonic", bean.getMnemonic());
        assertEquals("name", bean.getName());
        assertEquals("Pbase", bean.getOaiBaseUrl());
        assertEquals("Pmeta", bean.getOaiMetadataPrefix());
    }

    /**
     * Tests putting arbitrary string values on bean and retrieving them.
     */
    @Test
    public void testProviderValues() {
        Provider<Long> bean = new ProviderBean<Long>(1L);
        bean.putValue("a", null);
        assertEquals(1, bean.values().size());
        assertNull(bean.getValue("a"));

        bean.putValue("a", "a");
        assertEquals(1, bean.values().size());
        assertEquals("a", bean.getValue("a"));
    }
}
