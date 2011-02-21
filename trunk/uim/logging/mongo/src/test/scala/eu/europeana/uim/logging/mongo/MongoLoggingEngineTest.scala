package eu.europeana.uim.logging.mongo

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{Spec, BeforeAndAfterAll, BeforeAndAfterEach}
import org.mockito.Mockito._
import eu.europeana.uim.api.LoggingEngine.Level
import org.scalatest.mock.MockitoSugar
import eu.europeana.uim.store.Execution
import java.lang.String
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.collection.JavaConversions._
import eu.europeana.uim.{MDRFieldRegistry, MetaDataRecord}
import java.io.Serializable
import eu.europeana.uim.api.{LogEntry, IngestionPlugin}
import com.mongodb.{BasicDBObject, DBObject, Mongo}
import reflect.BeanProperty


class DummyType(@BeanProperty var some:String) extends Serializable {


}

class DummyTypeSerializer extends TypeSerializer[DummyType] {
  def serialize(`type` : DummyType) = {
    new BasicDBObject("some", `type`.some)
  }

  def parse(`object` : DBObject) = {
    new DummyType(`object`.get("some").asInstanceOf[String])
  }

  def getType = {
    classOf[DummyType]
  }
}

/**
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
@RunWith(classOf[JUnitRunner])
class MongoLoggingEngineTest extends Spec with BeforeAndAfterAll with BeforeAndAfterEach with ShouldMatchers with MockitoSugar {

  val DB: String = "UIMLOGTEST"
  val engine: MongoLoggingEngine[DummyType] = new MongoLoggingEngine[DummyType](DB)
  engine.setTypeSerializer(classOf[DummyTypeSerializer].getName);
  val m: Mongo = new Mongo();

  override protected def beforeAll() = {
    m.dropDatabase(DB);
  }

  override protected def afterEach() = {
    m.getDB(DB).getCollectionNames() filter (name => name.equals(MongoLoggingEngine.LOG_ENTRIES) ) foreach (name => m.getDB(DB).getCollection(name).drop)
  }

  it("should log simple entries") {
    engine.log(Level.INFO, "Processed MDR", execution(0), mdr(0), plugin("dummy"))

    val saved:DBObject = m.getDB(DB).getCollection(MongoLoggingEngine.LOG_ENTRIES).findOne
    saved should not be (null)
    saved.get("level") should equal (Level.INFO.toString)
    saved.get("message") should equal ("Processed MDR")
    saved.get("executionId") should equal (0l)
    saved.get("mdrId") should equal (0l)
    saved.get("pluginIdentifier") should equal ("dummy")
  }

  it("should retrieve simple entries") {
    engine.log(Level.INFO, "Processed MDR", execution(0), mdr(0), plugin("dummy"))

    val retrievedList:java.util.List[LogEntry[String]] = engine.getExecutionLog(execution(0));
    retrievedList.size should equal (1)
    val retrieved:LogEntry[String] = retrievedList(0)
    retrieved.getLevel should equal (Level.INFO)
    retrieved.getMessage should equal ("Processed MDR")
    retrieved.getExecutionId should equal (0l)
    retrieved.getMetaDataRecordId should equal (0l)
    retrieved.getPluginIdentifier should equal ("dummy")
  }

  it("should store simple durations") {
    engine.logDuration(plugin("dummy"), 10l, 2);

    val saved:java.util.List[DBObject] = m.getDB(DB).getCollection(MongoLoggingEngine.DURATION_ENTRIES).find(new BasicDBObject("pluginIdentifier", "dummy")).toArray
    saved.size should equal (2)
    saved(0).get("duration") should equal (5l);
    saved(1).get("duration") should equal (5l);
  }

/*  it("should calculate average durations per plugin") {
    engine.logDuration(plugin("dummy"), 10l, 2);
    engine.logDuration(plugin("dummy"), 25l, 3);
    engine.getAverageDuration(plugin("dummy")) should equal (7l)
  }
*/

  it("should log structured objects") {
    engine.logStructured(Level.INFO, new DummyType("hello"), execution(0), mdr(0), plugin("dummy"))

    val saved:DBObject = m.getDB(DB).getCollection(MongoLoggingEngine.LOG_ENTRIES).findOne
    saved should not be (null)
    saved.get("level") should equal (Level.INFO.toString)
    saved.get("executionId") should equal (0l)
    saved.get("mdrId") should equal (0l)
    saved.get("pluginIdentifier") should equal ("dummy")
    val embedded:DBObject = saved.get("message").asInstanceOf[DBObject]
    embedded should not be (null)
    embedded.get("some") should equal ("hello")
  }



  def mdr(id: Long):MetaDataRecord = {
    val mdr = mock[MetaDataRecord]
    when(mdr.getId).thenReturn(id)
    mdr
  }

  def execution(id: Long):Execution = {
    val e = mock[Execution]
    when(e.getId).thenReturn(id)
    e
  }

  def plugin(id: String):IngestionPlugin = {
    val p = mock[IngestionPlugin]
    when(p.getIdentifier).thenReturn(id)
    p
  }


}



