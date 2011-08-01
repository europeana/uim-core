package eu.europeana.uim.store;

import java.util.Map;
import java.util.Set;

/*
 aggregator // ref to Aggregator obj
 name_code       // ingestion internal shorthand for Aggregator
 name            // official name of Aggreagator (for nice reports etc)
 home_page       // homepage of Aggreagator (for nice reports etc)
 country         // Country for Provider
 content_type    // enum PROVT_MUSEUM, PROVT_ARCHIVE, PROVT_LIBRARY, PROVT_AUDIO_VIS_ARCH, PROVT_AGGREGATOR

 */
/**
 * A provider holds different collection and most likely serves as entity like a national library.
 * 
 * @param <I>
 *            generic ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface Provider<I> extends UimEntity<I> {
    /**
     * @return retrieve providers to which this provider seems to be dependent on
     */
    Set<Provider<I>> getRelatedOut();

    /**
     * @return retrieve providers for whom this provider seems to be dependent
     */
    Set<Provider<I>> getRelatedIn();

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
     * @return name of the provider (must not be unique)
     */
    String getName();

    /**
     * @param name
     *            name of the provider (must not be unique)
     */
    void setName(String name);

    /**
     * @param aggregator
     *            Is it an aggregator itself?
     */
    void setAggregator(boolean aggregator);

    /**
     * @return Is it an aggregator itself?
     */
    boolean isAggregator();

    /**
     * @return base url to retrieve the providers data
     */
    String getOaiBaseUrl();

    /**
     * @param baseUrl
     *            base url to retrieve the providers data
     */
    void setOaiBaseUrl(String baseUrl);

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
     * string key,value pairs for arbitraty information on colleciton level
     * 
     * @param key
     * @param value
     */
    void putValue(String key, String value);

    /**
     * retrieve the stirng value for the specific key
     * 
     * @param key
     * @return the string value or null
     */
    String getValue(String key);

    
    /**
     * string key,value pairs for arbitraty information on colleciton level
     * 
     * @param key
     * @param value
     */
    void putValue(ControlledVocabularyKeyValue key, String value);

    /**
     * retrieve the stirng value for the specific key
     * 
     * @param key
     * @return the string value or null
     */
    String getValue(ControlledVocabularyKeyValue key);
    

    
    
    /**
     * @return the values map
     */
    Map<String, String> values();
}
