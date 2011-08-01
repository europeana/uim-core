package eu.europeana.uim.store.bean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eu.europeana.uim.store.ControlledVocabularyKeyValue;
import eu.europeana.uim.store.Provider;

/**
 * In-memory implementation of {@link Provider} using Long as ID.
 * 
 * @param <I>
 *            unique ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class ProviderBean<I> extends AbstractNamedEntityBean<I> implements Provider<I> {
    private Set<Provider<I>> relatedOut = new HashSet<Provider<I>>();
    private Set<Provider<I>> relatedIn  = new HashSet<Provider<I>>();

    private String            name;

    private String            oaiBaseUrl;
    private String            oaiMetadataPrefix;

    private boolean           aggregator;

    private Map<String, String> values = new HashMap<String, String>();

    /**
     * Creates a new instance of this class.
     */
    public ProviderBean() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param id
     *            unique ID
     */
    public ProviderBean(I id) {
        super(id);
    }

    @Override
    public String getOaiMetadataPrefix() {
        return oaiMetadataPrefix;
    }

    @Override
    public void setOaiMetadataPrefix(String oaiPrefix) {
        this.oaiMetadataPrefix = oaiPrefix;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getOaiBaseUrl() {
        return oaiBaseUrl;
    }

    @Override
    public void setOaiBaseUrl(String oaiBaseUrl) {
        this.oaiBaseUrl = oaiBaseUrl;
    }

    @Override
    public boolean isAggregator() {
        return aggregator;
    }

    @Override
    public void setAggregator(boolean aggregator) {
        this.aggregator = aggregator;
    }

    @Override
    public Set<Provider<I>> getRelatedOut() {
        return relatedOut;
    }

    @Override
    public Set<Provider<I>> getRelatedIn() {
        return relatedIn;
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
	public void putValue(ControlledVocabularyKeyValue key, String value) {
		putValue(key.getFieldId(),value);
		
	}

	@Override
	public String getValue(ControlledVocabularyKeyValue key) {
		return getValue(key.getFieldId());
	}
    
    @Override
    public Map<String, String> values() {
         return values;
    }

    
    @Override
    public String toString() {
        String string = super.toString();
        
        String oai = getOaiBaseUrl();
        if (oai != null) {
            string += " OAI:[";
            string += oai + "?verb={x}";
            string += getOaiMetadataPrefix() != null ? "&metadataPrefix=" + getOaiMetadataPrefix() : "";
            string += "]";
        }
        return string;
    }
}
