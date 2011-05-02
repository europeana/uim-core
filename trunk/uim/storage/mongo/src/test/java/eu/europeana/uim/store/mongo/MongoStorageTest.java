package eu.europeana.uim.store.mongo;

import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.store.AbstractStorageEngineTest;

@RunWith(JUnit4.class)
public class MongoStorageTest extends AbstractStorageEngineTest<Long> {

	private MongoStorageEngine mongoEngine = null;

    private Mongo m = null;	
	
	@Before
    public void setupTest(){
		
		try {
			m = new Mongo();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	@After
    public void cleanup(){
		//m.dropDatabase("UIMTEST");
	}
	
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
			          
			          
			          //fail("Could not initialize mongodb storage engine");

			      }
			    }
			    else {
			      return mongoEngine;
			    }
		   return null;
	}
	
	
	

}
