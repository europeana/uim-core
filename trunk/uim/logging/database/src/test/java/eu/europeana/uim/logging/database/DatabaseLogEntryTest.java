package eu.europeana.uim.logging.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.concurrent.Semaphore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "/test-context.xml",
        "/test-beans.xml"
})
public class DatabaseLogEntryTest {

	@Autowired
	DatabaseLogEntryHome entryHome;

	private static Semaphore lock = new Semaphore(1);
	
	@Before
	public void start() throws InterruptedException{
		lock.acquire();
	}

	
	@After
	public void clean(){
		entryHome.truncate();
		
		lock.release();
	}

	
	@Test
	public void testEntryLifeCycle() {
		DatabaseLogEntry entry = new DatabaseLogEntry();
		entry.setLevel("level");
		
		assertEquals("Plain setter/getter testing", entry.getLevel(), "level");
		
		entryHome.insert(entry);
		
		assertNotNull(entry.getOid());
		
		DatabaseLogEntry entry2 = entryHome.findByOid(entry.getOid());
		assertNotSame("Persistence instance", entry, entry2);
		
		assertEquals(entry.getLevel(), entry2.getLevel());
	}
	
	
	
}
