package eu.europeana.uim.store.mongo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * Mongo implementation of the {@link MetaDataRecord}. We make use of Mongo's document-storage
 * nature and store MDRs as basic documents (i.e. basic mongo db objects). In order to conform to
 * the {@link MetaDataRecord} interface, we wrap a {@link DBObject} which is the object persisted by
 * MongoDB.
 * 
 * We store unqualified fields with the prefix FIELD, qualified fields have as prefix the name() of
 * the qualifying Enum.
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class MongoMetadataRecord<Long> implements MetaDataRecord<Long> {
    public static final String FIELD  = "_field_";
    private DBObject           object = new BasicDBObject();
    private Collection         collection;
    private String             identifier;

    public MongoMetadataRecord(DBObject object, Collection collection, String identifier, long lid) {
        this.object = object;
        this.collection = collection;
        this.identifier = identifier;
        object.put(AbstractMongoEntity.LID, lid);
        object.put("collection", collection.getId());
        object.put("identifier", identifier);
    }

    public Long getId() {
        return (Long)object.get(AbstractMongoEntity.LID);
    }

    public void setObject(BasicDBObject object) {
        this.object = object;
    }

    public DBObject getObject() {
        return object;
    }

    public Collection getCollection() {
        return collection;
    }

    public String getIdentifier() {
        return identifier;
    }

    public <N, T extends Serializable> void setFirstField(TKey<N, T> nttKey, T value) {
        BasicDBObject unqualifiedFields = (BasicDBObject)object.get(fieldName(nttKey.getFullName()));
        if (unqualifiedFields == null) {
            unqualifiedFields = new BasicDBObject();
            object.put(fieldName(nttKey.getFullName()), unqualifiedFields);
        }
        unqualifiedFields.put(FIELD + "0", value);
    }

    public <N, T extends Serializable> void setFirstQField(TKey<N, T> nttKey, String qualifier,
            T value) {
        BasicDBObject qualifiedFields = (BasicDBObject)object.get(fieldName(nttKey.getFullName()));
        if (qualifiedFields == null) {
            qualifiedFields = new BasicDBObject();
            object.put(fieldName(nttKey.getFullName()), qualifiedFields);
        }
        BasicDBObject values = (BasicDBObject)qualifiedFields.get(qualifier);
        if (values == null) {
            values = new BasicDBObject();
            qualifiedFields.put(qualifier, values);
        }
        values.put(FIELD + "0", value);
    }

    @Override
    public <N, T> void addField(TKey<N, T> key, T value, Enum<?>... qualifiers) {
        BasicDBObject qFields = (BasicDBObject)object.get(fieldName(key.getFullName()));
        if (qFields == null) {
            qFields = new BasicDBObject();
            object.put(fieldName(key.getFullName()), qFields);
        }

        // TODO adapt this to also store the type of the Enum<?> values so that we can re-hydrate
// them when retrieving them from the storage.

        for (Enum<?> q : qualifiers) {
            qFields.put(q.name(), value);
        }

    }

    public <N, T> List<QualifiedValue<T>> getField(TKey<N, T> nttKey) {
        Map<String, QualifiedValue<T>> qFields = new HashMap<String, QualifiedValue<T>>();
        List<QualifiedValue<T>> res = new ArrayList<QualifiedValue<T>>();

        BasicDBObject values = (BasicDBObject)object.get(fieldName(nttKey.getFullName()));
        for (String s : values.keySet()) {
            // unqualified fields
            if (s.startsWith(FIELD)) {
                QualifiedValue<T> val = new QualifiedValue<T>((T)values.get(s),
                        new HashSet<Enum<?>>());
                res.add(val);
            } else {

                // qualified fields
                T value = (T)values.get(s);
                QualifiedValue<T> v = qFields.get(s);
                if (v == null) {
                    Set<Enum<?>> qualifiers = new HashSet<Enum<?>>();
                    v = new QualifiedValue<T>(value, qualifiers);
                    qFields.put(s, v);
                }

                // add the qualifiers

                // TODO Enum<?> deserialization
                // we need to store the type of the Enum (class) so we can re-create it via
// reflection
// v.getQualifiers().add( dehydrated qualifier )

            }
        }
        return res;
    }

    @Override
    public <N, T> List<T> getPlainField(TKey<N, T> key, Enum<?>... qualifiers) {
        List<T> res = new ArrayList<T>();
        BasicDBObject data = (BasicDBObject)object.get(fieldName(key.getFullName()));
        if (data == null) {
            data = new BasicDBObject();
            object.put(fieldName(key.getFullName()), data);
        }

        for (Enum<?> qualifier : qualifiers) {
            T value = (T)data.get(qualifier.name());
            res.add(value);
        }
        return res;
    }

    @Override
    public <N, T> T getFirstField(TKey<N, T> key, Enum<?>... qualifiers) {
        return getPlainField(key, qualifiers).get(0);
    }

    @Override
    public <N, T> List<QualifiedValue<T>> deleteField(TKey<N, T> key) {
        // TODO
        return null;
    }

    private String fieldName(String name) {
        return name.replaceAll(".", "_");
    }

}
