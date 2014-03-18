/* CollectionBeanTest.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.store.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Test;

import eu.europeana.uim.store.Request;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public class RequestBeanTest {
    /**
     * 
     */
    @Test
    public void testRequestSetterGetter() {
        Request<Long> bean = new RequestBean<Long>(1L, null, new Date(0));

        assertEquals(new Long(1), bean.getId());
        assertEquals(new Date(0), bean.getDate());
    }

    /**
     * 
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
