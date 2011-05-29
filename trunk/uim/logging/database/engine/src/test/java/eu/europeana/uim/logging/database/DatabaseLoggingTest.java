package eu.europeana.uim.logging.database;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

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
public class DatabaseLoggingTest {

    /**
     * Tests functionality of logging engine implementation based on JPA.
     */
    @Test
    public void testDatabaseLogging() {
        Date date = new Date(System.currentTimeMillis() - 1000);

        DatabaseLoggingEngine<String[]> loggingEngine = 
            new DatabaseLoggingEngine<String[]>();

        loggingEngine.log(new LoggingIngestionPlugin(), new ExecutionBean<Long>(1l),
                new MetaDataRecordBean<Long>(2l, new RequestBean<Long>(4l, null, date)), "Testing",
                Level.WARNING, "Test");
        
        List<LogEntry<Long, String[]>> executionLog = loggingEngine.getExecutionLog(new ExecutionBean<Long>(
                1l));
        
        Assert.assertNotNull(executionLog);
        Assert.assertEquals(1, executionLog.size());
        LogEntry<Long, String[]> logEntry = executionLog.get(0);
        Assert.assertEquals(new Long(1l), logEntry.getExecutionId());

        Assert.assertTrue(date.before(logEntry.getDate()));
        Assert.assertEquals("Test", logEntry.getMessage()[0]);
        Assert.assertEquals(new Long(2l), logEntry.getMetaDataRecordId());
        Assert.assertEquals(LoggingIngestionPlugin.class.getSimpleName(), logEntry.getModule());
        Assert.assertEquals(Level.WARNING, logEntry.getLevel());

        loggingEngine.logStructured(new LoggingIngestionPlugin(), new ExecutionBean<Long>(1l),
                new MetaDataRecordBean<Long>(2l, new RequestBean<Long>(4l, null, date)), "Testing",
                Level.WARNING, new String[] { "Test" });
        executionLog = loggingEngine.getStructuredExecutionLog(new ExecutionBean<Long>(1l));
        Assert.assertNotNull(executionLog);
        Assert.assertEquals(1, executionLog.size());
        logEntry = executionLog.get(0);

        Assert.assertEquals(new Long(1l), logEntry.getExecutionId());
        Assert.assertTrue(date.before(logEntry.getDate()));
        Assert.assertEquals("Test", logEntry.getMessage()[0]);
        Assert.assertEquals(new Long(2l), logEntry.getMetaDataRecordId());
        Assert.assertEquals(LoggingIngestionPlugin.class.getSimpleName(), logEntry.getModule());
        Assert.assertEquals(Level.WARNING, logEntry.getLevel());

        loggingEngine.logDuration(new LoggingIngestionPlugin(), 10l, 1);
        Long aveDur = loggingEngine.getAverageDuration(new LoggingIngestionPlugin());
        Assert.assertEquals(new Long(10l), aveDur);
    }
}
