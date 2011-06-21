package eu.europeana.uim.logging.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class DefaultTypeSerializer implements TypeSerializer<Object> {

    public Class<Object> getType() {
        return Object.class;
    }

    public DBObject serialize(Object type) {
        return new BasicDBObject("object", type);
    }

    public Object parse(DBObject object) {
        return object.get("object");
    }
}
