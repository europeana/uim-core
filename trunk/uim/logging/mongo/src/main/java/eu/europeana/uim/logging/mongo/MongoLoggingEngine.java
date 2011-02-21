package eu.europeana.uim.logging.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.LogEntry;
import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.store.Execution;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Generic implementation of a mongo-based LoggingEngine. It does not implement the structured logging, this has to be performed by subclasses.
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class MongoLoggingEngine<T> implements LoggingEngine<T> {

    public static final String DEFAULT_UIM_DB_NAME = "UIM";
    public static final String LOG_ENTRIES = "LogEntries";
    public static final String DURATION_ENTRIES = "DurationEntries";

    Mongo mongo = null;
    private DB db = null;
    private DBCollection logEntries = null;
    private DBCollection durationEntries = null;
    private String dbName;

    private TypeSerializer<T> serializer = null;

    public MongoLoggingEngine(String dbName) {
        this.dbName = dbName;
        init();
    }

    public MongoLoggingEngine() {
        init();
    }

    private void init() {
        try {
            if (dbName == null) {
                dbName = DEFAULT_UIM_DB_NAME;
            }
            mongo = new Mongo();
            db = mongo.getDB(dbName);
            logEntries = db.getCollection(LOG_ENTRIES);
            durationEntries = db.getCollection(DURATION_ENTRIES);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    public void setTypeSerializer(String serializerClass) {
        try {
            Class serializer = Class.forName(serializerClass);
            @SuppressWarnings("unchecked")
            TypeSerializer<T> s = (TypeSerializer<T>) serializer.newInstance();
            this.serializer = s;
        } catch (Throwable t) {
            throw new RuntimeException("Could not find or initialize serializer " + serializerClass, t);
        }
    }

    public String getIdentifier() {
        return MongoLoggingEngine.class.getSimpleName();
    }

    public void log(Level level, String message, Execution execution, MetaDataRecord mdr, IngestionPlugin plugin) {
        DBObject entry = new BasicDBObject();
        entry.put("level", level.toString());
        entry.put("executionId", execution.getId());
        entry.put("mdrId", mdr.getId());
        entry.put("pluginIdentifier", plugin.getIdentifier());
        entry.put("date", new Date());
        entry.put("message", message);
        logEntries.insert(entry);
    }

    public List<LogEntry<String>> getExecutionLog(Execution execution) {
        List<LogEntry<String>> res = new ArrayList<LogEntry<String>>();
        DBCursor entries = logEntries.find(new BasicDBObject("executionId", execution.getId()));
        for (DBObject entry : entries) {
            res.add(new MongoLogEntry<String>((Date) entry.get("date"), (Long) entry.get("executionId"), Level.valueOf((String) entry.get("level")), (Long) entry.get("mdrId"), (String) entry.get("message"), (String) entry.get("pluginIdentifier")));
        }
        return res;
    }

    public void logStructured(Level level, T payload, Execution execution, MetaDataRecord mdr, IngestionPlugin plugin) {
        DBObject entry = new BasicDBObject();
        entry.put("level", level.toString());
        entry.put("executionId", execution.getId());
        entry.put("mdrId", mdr.getId());
        entry.put("pluginIdentifier", plugin.getIdentifier());
        entry.put("date", new Date());

        if(payload == null) {
            throw new RuntimeException("Can't log structured object when it is null");
        }

        DBObject serialized = null;
        try {
            serialized = serializer.serialize(payload);
        } catch(Throwable t) {
            log(Level.WARNING, "Could not log structured entry for payload '" + t.toString() + "'", execution, mdr, plugin);
        }

        entry.put("message", serialized);
        logEntries.insert(entry);
    }

    public List<LogEntry<T>> getStructuredExecutionLog(Execution execution) {
        List<LogEntry<T>> res = new ArrayList<LogEntry<T>>();
        DBCursor entries = logEntries.find(new BasicDBObject("executionId", execution.getId()));
        for (DBObject entry : entries) {
            T hydrated = serializer.parse((DBObject)entry.get("message"));
            res.add(new MongoLogEntry<T>((Date) entry.get("date"), (Long) entry.get("executionId"), Level.valueOf((String) entry.get("level")), (Long) entry.get("mdrId"), hydrated, (String) entry.get("pluginIdentifier")));
        }
        return res;
    }

    public void logDuration(IngestionPlugin plugin, Long duration, int count) {
        for (int i = 0; i < count; i++) {
            DBObject d = new BasicDBObject();
            d.put("date", new Date());
            d.put("pluginIdentifier", plugin.getIdentifier());
            d.put("duration", duration / count);
            durationEntries.insert(d);
        }
    }

    public Long getAverageDuration(IngestionPlugin plugin) {
        DBObject condition = new BasicDBObject("pluginIdentifier", plugin.getIdentifier());
        DBObject initial = new BasicDBObject();
        initial.put("count", 0);
        initial.put("totalDuration", 0);
        // FIXME http://stackoverflow.com/questions/4820334/how-to-compute-the-average-with-mongodb-and-numberlong
        String reduce = "function(duration, out) { out.count++; out.totalDuration+=duration.floatApprox; }";
        String finalize = "function(out) { out.avg = out.totalDuration.floatApprox / out.count; }";
        DBObject avg = durationEntries.group(new BasicDBObject("pluginIdentifier", true), condition, initial, reduce, finalize);
        System.out.println(avg);
        return null;
    }

    public void logDuration(IngestionPlugin plugin, Long duration, long... mdrs) {
        for (Long mdr : mdrs) {
            DBObject d = new BasicDBObject();
            d.put("date", new Date());
            d.put("pluginIdentifier", plugin.getIdentifier());
            d.put("duration", duration / mdrs.length);
            d.put("mdrId", mdr);
            durationEntries.insert(d);
        }
    }
}
