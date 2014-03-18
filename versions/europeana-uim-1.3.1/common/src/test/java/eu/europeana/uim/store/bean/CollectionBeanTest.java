/* CollectionBeanTest.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.store.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;

/**
 * Tests basic operations on implementation {@link CollectionBean} for {@link Collection}.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public class CollectionBeanTest {
    /**
     * Tests creating of bean and all getter and setter methods on it.
     */
    @Test
    public void testCollectionSetterGetter() {
        Provider<Long> provider = new ProviderBean<Long>(1L);
        provider.setMnemonic("pmnemonic");
        provider.setName("pname");
        provider.setOaiBaseUrl("Pbase");
        provider.setOaiMetadataPrefix("Pmeta");

        CollectionBean<Long> bean = new CollectionBean<Long>();
        bean.setProvider(provider);
        bean.setId(2L);
        bean.setLanguage("a1");
        bean.setMnemonic("mnemonic");
        bean.setName("name");
        bean.setOaiSet("cset");

        assertEquals(new Long(2), bean.getId());
        assertEquals("a1", bean.getLanguage());
        assertEquals("mnemonic", bean.getMnemonic());
        assertEquals("name", bean.getName());
        assertEquals("cset", bean.getOaiSet());
        assertEquals("Pbase", bean.getOaiBaseUrl(true));
        assertEquals("Pmeta", bean.getOaiMetadataPrefix(true));
        assertNull(bean.getOaiBaseUrl(false));
        assertNull(bean.getOaiMetadataPrefix(false));

        bean.setOaiBaseUrl("cbase");
        bean.setOaiMetadataPrefix("cmeta");
        assertEquals("cbase", bean.getOaiBaseUrl(true));
        assertEquals("cmeta", bean.getOaiMetadataPrefix(true));
    }

    /**
     * Tests putting arbitrary string values on bean and retrieving them.
     */
    @Test
    public void testCollectionValues() {
        CollectionBean<Long> bean = new CollectionBean<Long>(1L, null);
        bean.putValue("a", null);
        assertEquals(1, bean.values().size());
        assertNull(bean.getValue("a"));

        bean.putValue("a", "a");
        assertEquals(1, bean.values().size());
        assertEquals("a", bean.getValue("a"));
    }
}
