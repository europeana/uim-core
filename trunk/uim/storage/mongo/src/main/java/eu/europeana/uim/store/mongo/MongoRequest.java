package eu.europeana.uim.store.mongo;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

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

    private Date                date;

    private Date                from;
    private Date                till;

    private boolean             failed;
    
    
    
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

    @Override
    public Date getDate() {
        return date;
    }

    /**
     * @param date
     */
    public void setDate(Date date) {
        this.date = DateUtils.truncate(date, Calendar.SECOND);
    }

    /**
     * Returns the from.
     * 
     * @return the from
     */
    @Override
    public Date getDataFrom() {
        return from;
    }

    /**
     * Sets the from to the given value.
     * 
     * @param from
     *            the from to set
     */
    @Override
    public void setDataFrom(Date from) {
        this.from = from;
    }

    /**
     * Returns the till.
     * 
     * @return the till
     */
    @Override
    public Date getDataTill() {
        if (till == null) return getDate();
        return till;
    }

    /**
     * Sets the till to the given value.
     * 
     * @param till
     *            the till to set
     */
    @Override
    public void setDataTill(Date till) {
        this.till = till;
    }
    
    /**
     * Returns the failed.
     * @return the failed
     */
    @Override
    public boolean isFailed() {
        return failed;
    }

    /**
     * Sets the failed to the given value.
     * @param failed the failed to set
     */
    @Override
    public void setFailed(boolean failed) {
        this.failed = failed;
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
    public String toString() {
        return "Request [collection=" + collection + ", date=" + date + "]";
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


}
