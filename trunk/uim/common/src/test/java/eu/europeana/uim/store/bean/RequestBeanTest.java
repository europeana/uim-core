/* CollectionBeanTest.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.store.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Test;

import eu.europeana.uim.store.Request;

/**
 * Tests basic operations on implementation {@link RequestBean} for {@link Request}.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public class RequestBeanTest {
    /**
     * Tests creating of bean and all getter and setter methods on it.
     */
    @Test
    public void testRequestSetterGetter() {
        Request<Long> bean = new RequestBean<Long>(1L, null, new Date(0));

        assertEquals(new Long(1), bean.getId());
        assertEquals(new Date(0), bean.getDate());
    }

    /**
     * Tests putting arbitrary string values on bean and retrieving them.
     */
    @Test
    public void testRequestValues() {
        Request<Long> bean = new RequestBean<Long>(1L, null, new Date());
        bean.putValue("a", null);
        assertEquals(1, bean.values().size());
        assertNull(bean.getValue("a"));

        bean.putValue("a", "a");
        assertEquals(1, bean.values().size());
        assertEquals("a", bean.getValue("a"));
    }
}
