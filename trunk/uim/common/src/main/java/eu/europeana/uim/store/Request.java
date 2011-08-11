package eu.europeana.uim.store;

import java.util.Date;
import java.util.Map;


/*
 collection  // ref to Collection obj
 created_time    // time stamp creation

 status      // enum     REQS_INIT     just created, under construction
 //          REQS_IMPORTED all related mdrs are referenced & created
 //          REQS_ABORTED  processing of this request aborted due to error
 //          REQS_PENDING_SIGNOFF   waiting for aproval
 //          REQS_ACCEPTED          aproved for production  only one req/collection can be in this state
 //          REQS_PRODUCTION        sent to production      only one req/collection can be in this state
 aproved_user    // User that aproved this request
 aproved_time    // time stamp aproval

 submitted_time  // time stamp, sent to production


 err_msg         // If in REQS_ABORTED state, explanation of what whent wrong

 */
/**
 * A request to provide metadata records from a particular collection at a particular date.
 * 
 * @param <I>
 *            generic ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface Request<I> extends UimDataSet<I> {
    /**
     * @return for which collection this request has been started
     */
    Collection<I> getCollection();

    /**
     * @return a specific date when this request has been initiated
     */
    Date getDate();

    
    /**
     * @param from
     */
    public void setDataFrom(Date from);
    
    /**
     * @return a specific date which reflects the period of thime the
     * request did process (null if not specified)
     */
    Date getDataFrom();

    
    /**
     * @param till
     */
    public void setDataTill(Date till);
    /**
     * @return a specific date which reflects the period of time the
     * request did process (same as @see getDate() in most cases)
     */
    Date getDataTill();
    
    
    /**
     * @param failed
     */
    public void setFailed(boolean failed);
    /**
     * @return true if something went wrong during the processing so
     * that it is unclear if "everything" is done.
     */
    boolean isFailed();
    
    
    /** string key,value pairs for arbitraty information on colleciton level
     * @param key
     * @param value
     */
    void putValue(String key, String value);
    
    
    /** retrieve the stirng value for the specific key
     * @param key
     * @return the string value or null
     */
    String getValue(String key);

    /**
     * @return the values map
     */
    Map<String, String> values();

}
