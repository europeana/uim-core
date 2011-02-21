package eu.europeana.uim.store.mongo

import com.mongodb.Mongo
import eu.europeana.uim.api.StorageEngine
import eu.europeana.uim.store.AbstractStorageEngineTest
import org.junit.Assert.fail
import scala.collection.JavaConversions._
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
@RunWith(classOf[JUnit4])
object MongoStorageTest {
  var mongoEngine: MongoStorageEngine = null
  val m: Mongo = new Mongo
}

class MongoStorageTest extends AbstractStorageEngineTest {


  protected[store] def getStorageEngine:MongoStorageEngine = {
    if (MongoStorageTest.mongoEngine == null) {
      try {
        val engine: MongoStorageEngine = new MongoStorageEngine("UIMTEST")
        MongoStorageTest.m.dropDatabase("UIMTEST")
        engine.initialize
        MongoStorageTest.mongoEngine = engine
      }
      catch {
        case e: Exception => {
          e.printStackTrace
          fail("Could not initialize mongodb storage engine")
        }
      }
    }
    else {
      return MongoStorageTest.mongoEngine
    }
    return null

  }

  protected[store] override def performSetUp = {
    MongoStorageTest.m.getDB("UIMTEST").getCollectionNames() filter (name => name.startsWith("Mongo") || name.startsWith("records") ) foreach (name => MongoStorageTest.m.getDB("UIMTEST").getCollection(name).drop)
  }

  override def testCreateAndGetProvider = {
    // TODO figure out why the hell JUnit doesn't invoke @Before for the first method of the suite
    super.setUp;
    super.testCreateAndGetProvider
  }
}