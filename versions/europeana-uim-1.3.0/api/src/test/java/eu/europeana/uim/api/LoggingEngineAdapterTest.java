/* LoggingEngineAdapterTest.java - created on Jul 17, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.api;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.logging.Level;

import org.junit.Test;

import eu.europeana.uim.UIMRegistry;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jul 17, 2011
 */
public class LoggingEngineAdapterTest {

    /**
     * 
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testLongAdapter() {
        UIMRegistry registry = new UIMRegistry();
        
        Execution<Long> execution = mock(Execution.class);
        IngestionPlugin plugin = mock(IngestionPlugin.class);
        MetaDataRecord<Long> mdr = mock(MetaDataRecord.class);
        when(mdr.getId()).thenReturn(1L);
        
        LoggingEngine<Long> engine = (LoggingEngine<Long>)registry.getLoggingEngine();
        engine.log(Level.INFO, "test", "a", "b", "c");
        engine.log(execution, Level.INFO, "test", "a", "b", "c");
        engine.log(execution, Level.WARNING, plugin, "a", "b", "c");
        engine.logDuration(execution, "test", 3L);
        engine.logDuration(execution, plugin, 6L);
        engine.logLink(execution, "uuuu", mdr, "http:...", 200, "a", "b");
        engine.logLink(execution, plugin, mdr, "http:...", 200, "a", "b");
    }
    
}
