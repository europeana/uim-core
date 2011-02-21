package eu.europeana.uim.logging.mongo;

import com.mongodb.DBObject;

/**
 * This interface defines the contract necessary to persist an arbitrary entity with the mongo based logging engine
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public interface TypeSerializer<T> {

    /**
     * The type this serializer handles
     */
    Class<T> getType();

    /**
     * Serializes a type into a DBObject
     *
     * @param type the object to serialize
     * @return a DBObject
     */
    DBObject serialize(T type);

    /**
     * De-serializes an object from the mongo storage
     * @param object the DBObject to dehydrate
     * @return a Typed object
     */
    T parse(DBObject object);
}
