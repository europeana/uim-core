package eu.europeana.uim.store.mongo;

import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

import eu.europeana.uim.api.AbstractStorageEngineTest;
import eu.europeana.uim.api.StorageEngine;

/**
 * Configuration class for MongoDB StorageEngineTests
 * 
 * @author Georgios Markakis
 */
@RunWith(JUnit4.class)
public class MongoStorageTest extends AbstractStorageEngineTest<Long> {

	private MongoStorageEngine mongoEngine = null;

    private Mongo m = null;	
	
	/**
	 * Run before each test
	 */
	@Before
    public void setupTest(){
		
		try {
			m = new Mongo();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
	}
    
	/**
	 * Run after each test
	 */
	@After
    public void cleanup(){
		m.dropDatabase("UIMTEST");
	}
	
	/* (non-Javadoc)
	 * @see eu.europeana.uim.store.AbstractStorageEngineTest#getStorageEngine()
	 */
	@Override
	protected StorageEngine<Long> getStorageEngine() {
		   if (mongoEngine == null) {
			      try {
			    	m = new Mongo();
			        MongoStorageEngine engine = new MongoStorageEngine("UIMTEST");
			        m.dropDatabase("UIMTEST");
			        engine.initialize();
			        mongoEngine = engine;
			      }
			      catch(Exception e) {
			          e.printStackTrace();
			      }
			    }
			    else {
			      return mongoEngine;
			    }
		   return mongoEngine;
	}
	
	
	

}
