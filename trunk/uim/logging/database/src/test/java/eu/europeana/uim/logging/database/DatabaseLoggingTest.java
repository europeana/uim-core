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
@ContextConfiguration(locations = { "/test-context.xml", "/test-beans.xml",
        "/test-logging-beans.xml" })
public class DatabaseLoggingTest {
    @Autowired
    private TStringDatabaseLogEntryHome     stringHome;

    @Autowired
    private TObjectDatabaseLogEntryHome     objectHome;

    @Autowired
    private TDurationDatabaseEntryHome      durationHome;

    @Autowired
    private DatabaseLoggingEngine<String[]> loggingEngine;

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
     * Tests functionality of logging engine implementation based on JPA.
     */
    @Test
    public void testDatabaseLogging() {
        Date date = new Date();

        loggingEngine.log(new LoggingIngestionPlugin(), new ExecutionBean<Long>(1l),
                new MetaDataRecordBean<Long>(2l, new RequestBean<Long>(4l, null, date)), "Testing",
                Level.WARNING, "Test");
        List<LogEntry<Long, String[]>> executionLog = loggingEngine.getExecutionLog(new ExecutionBean<Long>(
                1l));
        Assert.assertNotNull(executionLog);
        Assert.assertEquals(1, executionLog.size());
        LogEntry<Long, String[]> logEntry = executionLog.get(0);
        Assert.assertEquals(new Long(1l), logEntry.getExecutionId());
        Assert.assertFalse(date.before(logEntry.getDate()));
        Assert.assertEquals("Test", logEntry.getMessage()[0]);
        Assert.assertEquals(new Long(2l), logEntry.getMetaDataRecordId());
        Assert.assertEquals(LoggingIngestionPlugin.class.getSimpleName(), logEntry.getPluginName());
        Assert.assertEquals(Level.WARNING, logEntry.getLevel());

        loggingEngine.logStructured(new LoggingIngestionPlugin(), new ExecutionBean<Long>(1l),
                new MetaDataRecordBean<Long>(2l, new RequestBean<Long>(4l, null, date)), "Testing",
                Level.WARNING, new String[] { "Test" });
        executionLog = loggingEngine.getStructuredExecutionLog(new ExecutionBean<Long>(1l));
        Assert.assertNotNull(executionLog);
        Assert.assertEquals(1, executionLog.size());
        logEntry = executionLog.get(0);
        Assert.assertEquals(new Long(1l), logEntry.getExecutionId());
        Assert.assertFalse(date.before(logEntry.getDate()));
        Assert.assertEquals("Test", logEntry.getMessage()[0]);
        Assert.assertEquals(new Long(2l), logEntry.getMetaDataRecordId());
        Assert.assertEquals(LoggingIngestionPlugin.class.getSimpleName(), logEntry.getPluginName());
        Assert.assertEquals(Level.WARNING, logEntry.getLevel());

        loggingEngine.logDuration(new LoggingIngestionPlugin(), 10l, 1);
        Long aveDur = loggingEngine.getAverageDuration(new LoggingIngestionPlugin());
        Assert.assertEquals(new Long(10l), aveDur);
    }
}
