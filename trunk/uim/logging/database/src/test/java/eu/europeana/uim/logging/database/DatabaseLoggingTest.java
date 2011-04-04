package eu.europeana.uim.logging.database;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.uim.api.LogEntry;
import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.api.LoggingEngine.Level;
import eu.europeana.uim.store.bean.ExecutionBean;
import eu.europeana.uim.store.bean.MetaDataRecordBean;
import eu.europeana.uim.store.bean.RequestBean;
import eu.europeana.uim.util.LoggingIngestionPlugin;

/**
 * Tests {@link DatabaseLoggingEngine} and {@link LogEntry} implementations used for it.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 4, 2011
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml", "/test-beans.xml" })
public class DatabaseLoggingTest {
    @Autowired
    private TStringDatabaseLogEntryHome stringHome;

    @Autowired
    private TObjectDatabaseLogEntryHome objectHome;

    @Autowired
    private TDurationDatabaseEntryHome  durationHome;

    /**
     * Truncates all tables.
     */
    @Before
    public void setup() {
        stringHome.truncate();
        objectHome.truncate();
        durationHome.truncate();
    }

    /**
     * Tests individual JPA classes necessary for logging engine.
     */
    @Test
    public void testTableEntries() {
        Date date = new Date();

        TStringDatabaseLogEntry stringEntry = new TStringDatabaseLogEntry();
        stringEntry.setExecutionId(1l);
        stringEntry.setDate(date);
        stringEntry.setMessage("TEST-LOG");
        stringEntry.setMetaDataRecordId(2l);
        stringEntry.setPluginName("TEST-PLUGIN");
        stringEntry.setLevel(LoggingEngine.Level.WARNING);

        stringHome.insert(stringEntry);
        Long oid = stringEntry.getOid();

        TStringDatabaseLogEntry storedStringEntry = stringHome.findByOid(oid);
        Assert.assertNotNull(storedStringEntry);
        Assert.assertEquals(stringEntry.getExecutionId(), storedStringEntry.getExecutionId());
        Assert.assertEquals(stringEntry.getDate(), storedStringEntry.getDate());
        Assert.assertEquals(stringEntry.getMessage(), storedStringEntry.getMessage());
        Assert.assertEquals(stringEntry.getMetaDataRecordId(),
                storedStringEntry.getMetaDataRecordId());
        Assert.assertEquals(stringEntry.getPluginName(), storedStringEntry.getPluginName());
        Assert.assertEquals(stringEntry.getLevel(), storedStringEntry.getLevel());

        TObjectDatabaseLogEntry entry = new TObjectDatabaseLogEntry();
        entry.setExecutionId(1l);
        entry.setDate(date);
        entry.setMessage("TEST-LOG");
        entry.setMetaDataRecordId(2l);
        entry.setPluginName("TEST-PLUGIN");
        entry.setLevel(LoggingEngine.Level.WARNING);

        objectHome.insert(entry);
        oid = entry.getOid();

        TObjectDatabaseLogEntry sentry = objectHome.findByOid(oid);
        Assert.assertNotNull(sentry);
        Assert.assertEquals(entry.getExecutionId(), sentry.getExecutionId());
        Assert.assertEquals(entry.getDate(), sentry.getDate());
        Assert.assertEquals(entry.getMessage(), sentry.getMessage());
        Assert.assertEquals(entry.getMetaDataRecordId(), sentry.getMetaDataRecordId());
        Assert.assertEquals(entry.getPluginName(), sentry.getPluginName());
        Assert.assertEquals(entry.getLevel(), sentry.getLevel());

        TDurationDatabaseEntry durEntry = new TDurationDatabaseEntry();
        durEntry.setPluginName("TEST-PLUGIN");
        durEntry.setDuration(10l);

        durationHome.insert(durEntry);
        oid = durEntry.getOid();

        TDurationDatabaseEntry sdurEntry = durationHome.findByOid(oid);
        Assert.assertNotNull(sdurEntry);
        Assert.assertEquals(durEntry.getPluginName(), sdurEntry.getPluginName());
        Assert.assertEquals(durEntry.getDuration(), sdurEntry.getDuration());
    }

    /**
     * Tests functionality of logging engine implementation based on JPA.
     */
    @Test
    public void testDatabaseLogging() {
        Date date = new Date();

        DatabaseLoggingEngine<String> engine = new DatabaseLoggingEngine<String>(stringHome, objectHome, durationHome);

        engine.log(Level.WARNING, "Test", new ExecutionBean<Long>(1l),
                new MetaDataRecordBean<Long>(2l, new RequestBean<Long>(4l, null, date)),
                new LoggingIngestionPlugin());
        List<LogEntry<Long, String>> executionLog = engine.getExecutionLog(new ExecutionBean<Long>(
                1l));
        Assert.assertNotNull(executionLog);
        Assert.assertEquals(1, executionLog.size());
        LogEntry<Long, String> logEntry = executionLog.get(0);
        Assert.assertEquals(new Long(1l), logEntry.getExecutionId());
        Assert.assertFalse(date.before(logEntry.getDate()));
        Assert.assertEquals("Test", logEntry.getMessage());
        Assert.assertEquals(new Long(2l), logEntry.getMetaDataRecordId());
        Assert.assertEquals(LoggingIngestionPlugin.class.getSimpleName(), logEntry.getPluginName());
        Assert.assertEquals(Level.WARNING, logEntry.getLevel());

        engine.logStructured(Level.WARNING, "Test", new ExecutionBean<Long>(1l),
                new MetaDataRecordBean<Long>(2l, new RequestBean<Long>(4l, null, date)),
                new LoggingIngestionPlugin());
        executionLog = engine.getStructuredExecutionLog(new ExecutionBean<Long>(1l));
        Assert.assertNotNull(executionLog);
        Assert.assertEquals(1, executionLog.size());
        logEntry = executionLog.get(0);
        Assert.assertEquals(new Long(1l), logEntry.getExecutionId());
        Assert.assertFalse(date.before(logEntry.getDate()));
        Assert.assertEquals("Test", logEntry.getMessage());
        Assert.assertEquals(new Long(2l), logEntry.getMetaDataRecordId());
        Assert.assertEquals(LoggingIngestionPlugin.class.getSimpleName(), logEntry.getPluginName());
        Assert.assertEquals(Level.WARNING, logEntry.getLevel());

        engine.logDuration(new LoggingIngestionPlugin(), 10l, 1);
        Long aveDur = engine.getAverageDuration(new LoggingIngestionPlugin());
        Assert.assertEquals(new Long(10l), aveDur);
    }
}
