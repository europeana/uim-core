package eu.europeana.uim.store.bean;

import java.io.Serializable;
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
public class RequestBean<I> extends AbstractEntityBean<I> implements Request<I>,Serializable {

	private static final long serialVersionUID = 1L;
	
	private Collection<I>       collection;
    private Date                date;

    private Date                from;
    private Date                till;

    private boolean             failed;

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

}
