package eu.europeana.uim.logging.database;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.uim.api.LoggingEngine;

/**
 * Tests JPA entities used as persistent objects.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 4, 2011
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-context.xml", "/test-beans.xml" })
public class DatabaseEntityTest {
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
     * Tests string message type log entry.
     */
    @Test
    public void testStringDatabaseEntity() {
        Date date = new Date();

        TStringDatabaseLogEntry stringEntry = new TStringDatabaseLogEntry();
        stringEntry.setExecutionId(1l);
        stringEntry.setDate(date);
        stringEntry.setMessage(new String[] { "TEST-LOG" });
        stringEntry.setMetaDataRecordId(2l);
        stringEntry.setPluginName("TEST-PLUGIN");
        stringEntry.setLevel(LoggingEngine.Level.WARNING);

        stringHome.insert(stringEntry);
        Long oid = stringEntry.getOid();

        TStringDatabaseLogEntry storedStringEntry = stringHome.findByOid(oid);
        Assert.assertNotNull(storedStringEntry);
        Assert.assertEquals(stringEntry.getExecutionId(), storedStringEntry.getExecutionId());
        Assert.assertEquals(stringEntry.getDate(), storedStringEntry.getDate());
        Assert.assertArrayEquals(stringEntry.getMessage(), storedStringEntry.getMessage());
        Assert.assertEquals(stringEntry.getMetaDataRecordId(),
                storedStringEntry.getMetaDataRecordId());
        Assert.assertEquals(stringEntry.getPluginName(), storedStringEntry.getPluginName());
        Assert.assertEquals(stringEntry.getLevel(), storedStringEntry.getLevel());
    }

    /**
     * Tests object message type log entry.
     */
    @SuppressWarnings({ "unchecked", "cast" })
    @Test
    public void testObjectDatabaseEntity() {
        Date date = new Date();

        TObjectDatabaseLogEntry<String> entry = new TObjectDatabaseLogEntry<String>();
        entry.setExecutionId(1l);
        entry.setDate(date);
        entry.setMessage("TEST-LOG");
        entry.setMetaDataRecordId(2l);
        entry.setPluginName("TEST-PLUGIN");
        entry.setLevel(LoggingEngine.Level.WARNING);

        objectHome.insert(entry);
        long oid = entry.getOid();

        TObjectDatabaseLogEntry<String> sentry = (TObjectDatabaseLogEntry<String>)objectHome.findByOid(oid);
        Assert.assertNotNull(sentry);
        Assert.assertEquals(entry.getExecutionId(), sentry.getExecutionId());
        Assert.assertEquals(entry.getDate(), sentry.getDate());
        Assert.assertEquals(entry.getMessage(), sentry.getMessage());
        Assert.assertEquals(entry.getMetaDataRecordId(), sentry.getMetaDataRecordId());
        Assert.assertEquals(entry.getPluginName(), sentry.getPluginName());
        Assert.assertEquals(entry.getLevel(), sentry.getLevel());
    }

    /**
     * Tests duration entry.
     */
    @Test
    public void testDurationDatabaseEntity() {
        TDurationDatabaseEntry durEntry = new TDurationDatabaseEntry();
        durEntry.setPluginName("TEST-PLUGIN");
        durEntry.setDuration(10l);

        durationHome.insert(durEntry);
        long oid = durEntry.getOid();

        TDurationDatabaseEntry sdurEntry = durationHome.findByOid(oid);
        Assert.assertNotNull(sdurEntry);
        Assert.assertEquals(durEntry.getPluginName(), sdurEntry.getPluginName());
        Assert.assertEquals(durEntry.getDuration(), sdurEntry.getDuration());
    }
}
