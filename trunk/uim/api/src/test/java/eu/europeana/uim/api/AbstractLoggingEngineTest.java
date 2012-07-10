package eu.europeana.uim.api;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.logging.Level;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.uim.api.LoggingEngine.LogEntry;
import eu.europeana.uim.api.LoggingEngine.LogEntryFailed;
import eu.europeana.uim.api.LoggingEngine.LogEntryLink;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * Tests {@link LoggingEngine} implementations used for it.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 4, 2011
 */
public abstract class AbstractLoggingEngineTest {
    LoggingEngine<Long> engine = null;

    /**
     * Setups storage engine.
     */
    @Before
    public void setUp() {
        engine = getLoggingEngine();
        performSetUp();
    }

    /**
     * Override this for additional setup
     */
    protected void performSetUp() {
        // nothing todo
    }

    /**
     * @return configured storage engine
     */
    protected abstract LoggingEngine<Long> getLoggingEngine();

    /**
     * Tests functionality of logging engine implementation based on JPA.
     */
    @Test
    public void testSimpleLogging() {
        IngestionPlugin plugin = mock(IngestionPlugin.class);

        engine.log(Level.INFO, "test", "a", "b", "c");
        engine.log(Level.INFO, "test", "a", "d", "e");
        engine.log(Level.INFO, (String)null);

        engine.log(Level.INFO, plugin, "a", "b", "c");
        engine.log(Level.INFO, plugin, "a", "d", "e");
        engine.log(Level.INFO, plugin);

        engine.logField("modul", "field", "qualifier", 0, "a", "b", "c");
    }

    /**
     * Tests functionality of logging engine implementation based on JPA.
     */
    @SuppressWarnings({ "unchecked" })
    @Test
    public void testLoggingExecution() {
        Execution<Long> execution = mock(Execution.class);
        when(execution.getId()).thenReturn(1L);
        IngestionPlugin plugin = mock(IngestionPlugin.class);
        MetaDataRecord<Long> mdr = mock(MetaDataRecord.class);

        engine.log(execution, Level.INFO, "test", "a0", "b", "c");
        engine.log(execution, Level.INFO, "test", "a0", "d", "e");
        engine.log(execution, Level.INFO, (String)null);

        engine.log(execution, Level.INFO, plugin, "a1", "b", "c");
        engine.log(execution, Level.INFO, plugin, "a1", "d", "e");

        engine.logField("modul", "field", "qualifier", 0, "a", "b", "c");

        engine.logField(execution, "modul", mdr, "field", "qualifier", 0, "a", "b", "c");
        engine.logField(execution, plugin, mdr, "field", "qualifier", 0, "a", "b", "c");

        engine.logDuration(execution, "modul", 100l);
        engine.logDuration(execution, plugin, 100l);

        List<LogEntry> logs = engine.getLogs(execution);
        Assert.assertEquals(5, logs.size());
    }

    /**
     * Tests functionality of logging engine implementation based on JPA.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testLoggingFailed() {
        Execution<Long> execution = mock(Execution.class);
        when(execution.getId()).thenReturn(1L);
        IngestionPlugin plugin = mock(IngestionPlugin.class);
        MetaDataRecord<Long> mdr = mock(MetaDataRecord.class);
        when(mdr.getId()).thenReturn(1L);

        engine.logFailed(Level.INFO, "test", new NullPointerException(), "a", "b", "c");
        engine.logFailed(Level.INFO, "test", new NullPointerException(), "a", "d", "e");
        engine.logFailed(Level.INFO, (String)null, null);

        engine.logFailed(Level.INFO, plugin, new NullPointerException(), "a", "b", "c");
        engine.logFailed(Level.INFO, plugin, new NullPointerException(), "a", "d", "e");

        engine.logFailed(execution, Level.INFO, "test", new NullPointerException(), "a0", "b", "c");
        engine.logFailed(execution, Level.INFO, "test", new NullPointerException(), "a0", "d", "e");
        engine.logFailed(execution, Level.INFO, (String)null, null);

        engine.logFailed(execution, Level.INFO, plugin, new NullPointerException(), "a1", "b", "c");
        engine.logFailed(execution, Level.INFO, plugin, new NullPointerException(), "a2", "d", "e");

        engine.logFailed(execution, Level.INFO, plugin, new NullPointerException(), mdr, "a1", "b",
                "c");
        engine.logFailed(execution, Level.INFO, plugin, new NullPointerException(), mdr, "a2", "d",
                "e");

        List<LogEntryFailed> failedLogs = engine.getFailedLogs(execution);
        Assert.assertEquals(7, failedLogs.size());
    }

    /**
     * Tests functionality of logging engine implementation based on JPA.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void testLoggingLink() {
        Execution<Long> execution = mock(Execution.class);
        when(execution.getId()).thenReturn(1L);
        IngestionPlugin plugin = mock(IngestionPlugin.class);
        MetaDataRecord<Long> mdr = mock(MetaDataRecord.class);
        when(mdr.getId()).thenReturn(1L);

        engine.logLink("test", "http:...", 200, "a", "b", "c");
        engine.logLink("test", "http:...", 200, "a", "d", "e");
        engine.logLink((String)null, "http:...", 200, "a", "b", "c");

        engine.logLink(execution, "test", mdr, "http:...", 200, "a", "b", "c");
        engine.logLink(execution, "test", mdr, "http:...", 200, "a", "d", "e");
        engine.logLink(execution, (String)null, mdr, "http:...", 200, "a", "b", "c");

        engine.logLink(execution, plugin, mdr, "http:...", 200, "a", "b", "c");
        engine.logLink(execution, plugin, mdr, "http:...", 200, "a", "d", "e");

        List<LogEntryLink> linkLogs = engine.getLinkLogs(execution);
        Assert.assertEquals(5, linkLogs.size());
    }
}
