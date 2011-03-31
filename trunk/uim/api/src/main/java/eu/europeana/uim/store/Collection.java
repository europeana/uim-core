package eu.europeana.uim.store;

import java.util.Date;

/*
 provider    // ref to Provider obj
 name_code   // ingestion internal shorthand for Aggregator
 name        // official name of Aggreagator (for nice reports etc)
 home_page   // homepage of Aggreagator (for nice reports etc)
 language    // Primary language for collection (used as default for all fields if not given)
 */
/**
 * This interface specifies common functionality for a collection for a specific provider.
 * 
 * @param <I>
 *            generic ID
 *            
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface Collection<I> extends DataSet<I> {
    /**
     * @return the provider holding this collection
     */
    Provider<I> getProvider();

    /**
     * @return mnemonic under which this can be recognized
     */
    String getMnemonic();

    /**
     * @param mnemonic
     *            mnemonic under which this can be recognized
     */
    void setMnemonic(String mnemonic);

    /**
     * @return name of the collection (must not be unique)
     */
    String getName();

    /**
     * @param name
     *            name of the collection (must not be unique)
     */
    void setName(String name);

    /**
     * @return in which language is the collection
     */
    String getLanguage();

    /**
     * @param language
     *            in which language is the collection
     */
    void setLanguage(String language);

    /**
     * @return base url to retrieve the collections data
     */
    String getOaiBaseUrl();

    /**
     * @param baseUrl
     *            base url to retrieve the collections data
     */
    void setOaiBaseUrl(String baseUrl);

    /**
     * @return set identifier for the oai
     */
    String getOaiSet();

    /**
     * @param set
     *            set identifier for the oai
     */
    void setOaiSet(String set);

    /**
     * @return a prefix for the metadata retrieved from this oai
     */
    String getOaiMetadataPrefix();

    /**
     * @param prefix
     *            a prefix for the metadata retrieved from this oai
     */
    void setOaiMetadataPrefix(String prefix);
    
    
    /** 
     * 
     * @return date of last modification of any record in this collection 
     */
    Date getLastModified();
    
    
    /**
     * @param date
     *          date of last modification of any record in this collection
     */
    void setLastModified(Date date);
    
    
    
    /** 
     * 
     * @return date of last (live) system update of the collection 
     */
    Date getLastSynchronized();
    
    
    /**
     * @param date
     *          date of last synchronization of all record in this collection
     */
    void setLastSynchronized(Date date);
    
    
}
