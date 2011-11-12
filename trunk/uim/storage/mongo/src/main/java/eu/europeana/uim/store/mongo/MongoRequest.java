package eu.europeana.uim.store.mongo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Request;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
@Entity
public class MongoRequest<T> extends AbstractMongoEntity<Long> implements Request<Long> {


    @Reference
    private MongodbCollection collection = null;

    private Map<String, String> values = new HashMap<String, String>();

    private Date date = new Date();

    public MongoRequest() {
    }

    public MongoRequest(Long id, MongodbCollection c) {
        super(id);
        this.collection = c;
    }

    public MongoRequest(Long id, MongodbCollection c, Date d) {
        super(id);
        this.collection = c;
        this.date = d;
    }

	
	public String getIdentifier() {
		return "Request:" + getCollection().getMnemonic() + " " + getDate();
	}
	

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(MongodbCollection collection) {
        this.collection = collection;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public void putValue(String key, String value) {
        values.put(key, value);
    }

    @Override
    public String getValue(String key) {
        return values.get(key);
    }

    
    
    @Override
    public Map<String, String> values() {
         return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MongoRequest that = (MongoRequest) o;

        if (!collection.equals(that.collection)) return false;
        if (!date.equals(that.date)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + collection.hashCode();
        result = 31 * result + date.hashCode();
        return result;
    }

	@Override
	public void setDataFrom(Date from) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Date getDataFrom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDataTill(Date till) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Date getDataTill() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFailed(boolean failed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isFailed() {
		// TODO Auto-generated method stub
		return false;
	}
}
