/**
 * 
 */
package eu.europeana.uim.store.mongo;

import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

import eu.europeana.uim.api.AbstractMetaDataRecordTest;
import eu.europeana.uim.api.StorageEngine;

/**
 * @author geomark
 *
 */
public class MongoStorageMetaDataRecordTest extends AbstractMetaDataRecordTest{

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
