package eu.europeana.uim.logging.database.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.logging.Level;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
    private TLogEntryHome         logHome;

    @Autowired
    private TLogEntryFailedHome   logFailedHome;

    @Autowired
    private TLogEntryLinkHome     logLinkHome;

    @Autowired
    private TLogEntryDurationHome logDurationHome;

    /**
     * Truncates all tables.
     */
    @Before
    public void setup() {
        logHome.truncate();
        logFailedHome.truncate();
        logLinkHome.truncate();
        logDurationHome.truncate();
    }

    /**
     * Tests string message type log entry.
     */
    @Test
    public void testLogEntity() {
        Date date = new Date();

        TLogEntry entry = new TLogEntry(Level.WARNING, "module", date, "a", "b", "c");

        logHome.insert(entry);
        Long oid = entry.getOid();

        TLogEntry storedEntry = logHome.findByOid(oid);
        assertNotNull(storedEntry);
        assertEquals(entry.getDate(), storedEntry.getDate());
        
        assertEquals("module", storedEntry.getModule());
        assertEquals(entry.getModule(), storedEntry.getModule());
        
        assertEquals(Level.WARNING, storedEntry.getLevel());
        assertEquals(entry.getLevel(), storedEntry.getLevel());

        assertEquals(3, storedEntry.getMessages().length);
        assertEquals("a", storedEntry.getMessages()[0]);
        assertEquals("b", storedEntry.getMessages()[1]);
        assertEquals("c", storedEntry.getMessages()[2]);
        assertArrayEquals(entry.getMessages(), storedEntry.getMessages());
        
        assertNull(storedEntry.getExecution());
        assertNull(storedEntry.getMetaDataRecord());


    }

    /**
     * Tests duration entry.
     */
    @Test
    public void testDurationDatabaseEntity() {
        TLogEntryDuration durEntry = new TLogEntryDuration();
        durEntry.setModule("TEST-PLUGIN");
        durEntry.setDuration(10l);

        logDurationHome.insert(durEntry);
        long oid = durEntry.getOid();

        TLogEntryDuration sdurEntry = logDurationHome.findByOid(oid);
        assertNotNull(sdurEntry);
        assertEquals(durEntry.getModule(), sdurEntry.getModule());
        assertEquals(durEntry.getDuration(), sdurEntry.getDuration());
    }
}
