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
    private TLogEntryFieldHome    logFieldHome;

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
    public void testLogEntry() {
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

        storedEntry.setModule("MODULE");
        storedEntry.setMessage(new String[] {});

        logHome.update(storedEntry);
        storedEntry = logHome.findByOid(oid);
        assertNotNull(storedEntry);
        assertEquals(entry.getDate(), storedEntry.getDate());

        assertEquals("MODULE", storedEntry.getModule());
        assertEquals(3, storedEntry.getMessages().length);
    }

    /**
     * Tests string message type log entry.
     */
    @Test
    public void testLogEntryFailed() {
        Date date = new Date();
        TLogEntryFailed entry = new TLogEntryFailed(1L, Level.WARNING, "module", "stacktrace",
                date, 2L, "a", "b", "c");

        logFailedHome.insert(entry);
        Long oid = entry.getOid();

        TLogEntryFailed storedEntry = logFailedHome.findByOid(oid);
        assertNotNull(storedEntry);
        assertEquals(entry.getDate(), storedEntry.getDate());

        assertEquals("module", storedEntry.getModule());
        assertEquals(entry.getModule(), storedEntry.getModule());

        assertEquals(Level.WARNING, storedEntry.getLevel());
        assertEquals(entry.getLevel(), storedEntry.getLevel());

        assertEquals("stacktrace", storedEntry.getStacktrace());
        assertEquals(entry.getStacktrace(), storedEntry.getStacktrace());

        assertEquals(3, storedEntry.getMessages().length);
        assertEquals("a", storedEntry.getMessages()[0]);
        assertEquals("b", storedEntry.getMessages()[1]);
        assertEquals("c", storedEntry.getMessages()[2]);
        assertArrayEquals(entry.getMessages(), storedEntry.getMessages());

        assertNotNull(storedEntry.getExecution());
        assertNotNull(storedEntry.getMetaDataRecord());

        storedEntry.setModule("MODULE");
        storedEntry.setMessage(new String[] {});

        logFailedHome.update(storedEntry);
        storedEntry = logFailedHome.findByOid(oid);
        assertNotNull(storedEntry);
        assertEquals(entry.getDate(), storedEntry.getDate());

        assertEquals("MODULE", storedEntry.getModule());
        assertEquals(3, storedEntry.getMessages().length);
    }

    /**
     * Tests string message type log entry.
     */
    @Test
    public void testLogEntryLink() {
        Date date = new Date();
        TLogEntryLink entry = new TLogEntryLink(1L, "module", 2L, "link", date, 200, "a", "b", "c");

        logLinkHome.insert(entry);
        Long oid = entry.getOid();

        TLogEntryLink storedEntry = logLinkHome.findByOid(oid);
        assertNotNull(storedEntry);
        assertEquals(entry.getDate(), storedEntry.getDate());

        assertEquals("module", storedEntry.getModule());
        assertEquals(entry.getModule(), storedEntry.getModule());

        assertEquals("link", storedEntry.getLink());
        assertEquals(entry.getLink(), storedEntry.getLink());

        assertEquals(3, storedEntry.getMessages().length);
        assertEquals("a", storedEntry.getMessages()[0]);
        assertEquals("b", storedEntry.getMessages()[1]);
        assertEquals("c", storedEntry.getMessages()[2]);
        assertArrayEquals(entry.getMessages(), storedEntry.getMessages());

        assertEquals(200, storedEntry.getStatus());
        assertEquals(entry.getStatus(), storedEntry.getStatus());

        assertNotNull(storedEntry.getExecution());
        assertNotNull(storedEntry.getMetaDataRecord());

        storedEntry.setModule("MODULE");
        storedEntry.setMessage(new String[] {});

        logLinkHome.update(storedEntry);
        storedEntry = logLinkHome.findByOid(oid);
        assertNotNull(storedEntry);
        assertEquals(entry.getDate(), storedEntry.getDate());

        assertEquals("MODULE", storedEntry.getModule());
        assertEquals(3, storedEntry.getMessages().length);
    }

    /**
     * Tests duration entry.
     */
    @Test
    public void testDurationDatabaseEntity() {
        Date date = new Date();
        TLogEntryDuration entry = new TLogEntryDuration(1L, "module", date, 5L);

        logDurationHome.insert(entry);
        long oid = entry.getOid();

        TLogEntryDuration storedEntry = logDurationHome.findByOid(oid);
        assertNotNull(storedEntry);
        assertEquals(entry.getModule(), storedEntry.getModule());
        assertEquals(entry.getDate(), storedEntry.getDate());
        assertEquals(entry.getDuration(), storedEntry.getDuration());
        assertEquals("module", storedEntry.getModule());

        storedEntry.setModule("MODULE");

        logDurationHome.update(storedEntry);
        storedEntry = logDurationHome.findByOid(oid);
        assertNotNull(storedEntry);
        assertEquals(entry.getDate(), storedEntry.getDate());

        assertEquals("MODULE", storedEntry.getModule());
    }

    /**
     * Tests string message type log entry.
     */
    @Test
    public void testLogEntryField() {
        Date date = new Date();
        TLogEntryField entry = new TLogEntryField(1L, "module", 2L, "field", "qualifier", date, 200,
                "a", "b", "c");

        logFieldHome.insert(entry);
        Long oid = entry.getOid();

        TLogEntryField storedEntry = logFieldHome.findByOid(oid);
        assertNotNull(storedEntry);
        assertEquals(entry.getDate(), storedEntry.getDate());

        assertEquals("module", storedEntry.getModule());
        assertEquals(entry.getModule(), storedEntry.getModule());

        assertEquals("field", storedEntry.getField());
        assertEquals(entry.getField(), storedEntry.getField());

        assertEquals("qualifier", storedEntry.getQualifier());
        assertEquals(entry.getQualifier(), storedEntry.getQualifier());

        assertEquals(3, storedEntry.getMessages().length);
        assertEquals("a", storedEntry.getMessages()[0]);
        assertEquals("b", storedEntry.getMessages()[1]);
        assertEquals("c", storedEntry.getMessages()[2]);
        assertArrayEquals(entry.getMessages(), storedEntry.getMessages());

        assertEquals(200, storedEntry.getStatus());
        assertEquals(entry.getStatus(), storedEntry.getStatus());

        assertNotNull(storedEntry.getExecution());
        assertNotNull(storedEntry.getMetaDataRecord());

        storedEntry.setModule("MODULE");
        storedEntry.setMessage(new String[] {});

        logFieldHome.update(storedEntry);
        storedEntry = logFieldHome.findByOid(oid);
        assertNotNull(storedEntry);
        assertEquals(entry.getDate(), storedEntry.getDate());

        assertEquals("MODULE", storedEntry.getModule());
        assertEquals(3, storedEntry.getMessages().length);
    }

}
