/* CollectionBeanTest.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.store.bean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import eu.europeana.uim.store.Execution;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public class ExecutionBeanTest {

    @Test
    public void testExecutionSetterGetter() {
        Execution<Long> bean = new ExecutionBean<Long>(1L);
        bean.setName("name");
        bean.setWorkflow("workflow");
        
        assertEquals(new Long(1), bean.getId());
        assertEquals("name", bean.getName());
        assertEquals("workflow", bean.getWorkflow());
        
        assertFalse(bean.isCanceled());
        assertFalse(bean.isActive());
        
        bean.setActive(true);
        assertTrue(bean.isActive());
        
        
        assertEquals(0, bean.getSuccessCount());
        assertEquals(0, bean.getProcessedCount());
        assertEquals(0, bean.getFailureCount());
        
        bean.setSuccessCount(1);
        bean.setProcessedCount(2);
        bean.setFailureCount(3);
        
        assertEquals(1, bean.getSuccessCount());
        assertEquals(2, bean.getProcessedCount());
        assertEquals(3, bean.getFailureCount());
        
        
    }

}
