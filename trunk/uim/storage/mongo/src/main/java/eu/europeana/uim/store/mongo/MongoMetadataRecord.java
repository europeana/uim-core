package eu.europeana.uim.store.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;
import eu.europeana.uim.store.Request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class MongoMetadataRecord implements MetaDataRecord {

    public static final String FIELD = "_field_";
    private DBObject object = new BasicDBObject();
    private Request request;
    private String identifier;

    public MongoMetadataRecord(DBObject object, Request request, String identifier, long lid) {
        this.object = object;
        this.request = request;
        this.identifier = identifier;
        object.put(AbstractMongoEntity.LID, lid);
        object.put("request", request.getId());
        object.put("identifier", identifier);
    }


    public long getId() {
        return (Long) object.get(AbstractMongoEntity.LID);
    }

    public void setObject(BasicDBObject object) {
        this.object = object;
    }

    public DBObject getObject() {
        return object;
    }

    public Request getRequest() {
        return request;
    }

    
    @Override
	public String getIdentifier() {
        return identifier;
	}

	public <N, T extends Serializable> void setFirstField(TKey<N, T> nttKey, T value) {
        BasicDBObject unqualifiedFields = (BasicDBObject) object.get(fieldName(nttKey.getFullName()));
        if(unqualifiedFields == null) {
            unqualifiedFields = new BasicDBObject();
            object.put(fieldName(nttKey.getFullName()), unqualifiedFields);
        }
        unqualifiedFields.put(FIELD + "0", value);
    }

    public <N, T extends Serializable> T getFirstField(TKey<N, T> nttKey) {
        return getField(nttKey).get(0);
    }

    public <N, T extends Serializable> void setFirstQField(TKey<N, T> nttKey, String qualifier, T value) {
        BasicDBObject qualifiedFields = (BasicDBObject) object.get(fieldName(nttKey.getFullName()));
        if(qualifiedFields == null) {
            qualifiedFields = new BasicDBObject();
            object.put(fieldName(nttKey.getFullName()), qualifiedFields);
        }
        BasicDBObject values = (BasicDBObject) qualifiedFields.get(qualifier);
        if(values == null) {
            values = new BasicDBObject();
            qualifiedFields.put(qualifier, values);
        }
        values.put(FIELD + "0", value);
    }

    public <N, T extends Serializable> void addField(TKey<N, T> nArrayListTKey, T value) {
        BasicDBObject unqualifiedFields = (BasicDBObject) object.get(fieldName(nArrayListTKey.getFullName()));
        if(unqualifiedFields == null) {
            unqualifiedFields = new BasicDBObject();
            object.put(fieldName(nArrayListTKey.getFullName()), unqualifiedFields);
        }
        unqualifiedFields.put(FIELD + new Integer(unqualifiedFields.size()).toString(), value);
    }

    @Override
    public <N, T extends Serializable> void addQField(TKey<N, T> nArrayListTKey, String qualifier, T value) {
        BasicDBObject qFields = (BasicDBObject) object.get(fieldName(nArrayListTKey.getFullName()));
        if(qFields == null) {
            qFields = new BasicDBObject();
            object.put(fieldName(nArrayListTKey.getFullName()), qFields);

        }
        BasicDBObject values = (BasicDBObject) qFields.get(qualifier);
        if(values == null) {
            values = new BasicDBObject();
            qFields.put(qualifier, values);
        }
        values.put(FIELD + new Integer(values.size()).toString(), value);
    }

    public <N, T extends Serializable> List<T> getField(TKey<N, T> nttKey) {
        List<T> res = new ArrayList<T>();

        BasicDBObject values = (BasicDBObject) object.get(fieldName(nttKey.getFullName()));
        for (String s : values.keySet()) {
            // unqualified fields
            if(s.startsWith(FIELD)) {
                res.add((T)values.get(s));
            } else {
                // qualified fields
                BasicDBObject qValues = (BasicDBObject) values.get(s);
                for(String q : qValues.keySet()) {
                    if(q.startsWith(FIELD)) {
                        res.add((T)qValues.get(q));
                    } else {
                        throw new RuntimeException("Corrupted MDR");
                    }
                }
            }
        }
        return res;
    }

    public <N, T extends Serializable> List<T> getQField(TKey<N, T> nttKey, String qualifier) {
        List<T> res = new ArrayList<T>();
        BasicDBObject data = (BasicDBObject) object.get(fieldName(nttKey.getFullName()));
        if(data == null) {
            data = new BasicDBObject();
            object.put(fieldName(nttKey.getFullName()), data);
        }
        BasicDBObject qualifiedValues = (BasicDBObject) data.get(qualifier);
        if(qualifiedValues == null) {
            qualifiedValues = new BasicDBObject();
            data.put(qualifier, qualifiedValues);
        }
        for (String s : qualifiedValues.keySet()) {
            if(s.startsWith(FIELD)) {
                res.add((T)qualifiedValues.get(s));
            }
        }
        return res;
    }
    
    public <N, T extends Serializable> T getFirstQField(TKey<N, T> nttKey, String qualifier) {
    	List<T> list = getQField(nttKey, qualifier);
    	if (list != null && !list.isEmpty()) {
    		return list.get(0);
    	}
    	return null;
    }


    private String fieldName(String name) {
        return name.replaceAll(".", "_");
    }
}
