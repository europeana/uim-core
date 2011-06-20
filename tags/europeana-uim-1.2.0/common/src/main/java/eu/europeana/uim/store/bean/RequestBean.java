package eu.europeana.uim.store.bean;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Request;

/**
 * Implementation of {@link Request} using Long as ID.
 * 
 * @param <I>
 *            unique ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class RequestBean<I> extends AbstractEntityBean<I> implements Request<I> {

    private Collection<I> collection;
    private Date          date;
    
    private Map<String, String> values = new HashMap<String, String>();

    /**
     * Creates a new instance of this class.
     */
    public RequestBean() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param id
     *            unique ID
     * @param collection
     * @param date
     */
    public RequestBean(I id, Collection<I> collection, Date date) {
        super(id);
        this.collection = collection;
        this.date = DateUtils.truncate(date, Calendar.SECOND);
    }

    @Override
    public Collection<I> getCollection() {
        return collection;
    }

    /**
     * @param collection
     */
    public void setCollection(Collection<I> collection) {
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

    
    
}
