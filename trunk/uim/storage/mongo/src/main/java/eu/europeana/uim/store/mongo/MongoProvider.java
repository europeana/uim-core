package eu.europeana.uim.store.mongo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
import eu.europeana.uim.store.Provider;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
@Entity
public class MongoProvider extends AbstractMongoEntity<Long> implements Provider<Long> {

    private boolean aggregator;
    private String oaiBaseUrl;
    private String oaiMetadataPrefix;

    @Reference
    private Set<Provider<Long>> relatedOut = new HashSet<Provider<Long>>();
    @Reference
    private Set<Provider<Long>> relatedIn = new HashSet<Provider<Long>>();

    private Map<String, String> values = new HashMap<String, String>();


    public MongoProvider() {
    }

    public MongoProvider(Long id) {
        super(id);
    }

	public String getIdentifier() {
		return "Provider:" + getMnemonic();
	}
	


    public Set<Provider<Long>> getRelatedOut() {
        return relatedOut;
    }

    public Set<Provider<Long>> getRelatedIn() {
        return relatedIn;
    }

    public void setAggregator(boolean aggregator) {
        this.aggregator = aggregator;
    }

    public boolean isAggregator() {
        return aggregator;
    }

    public String getOaiBaseUrl() {
        return oaiBaseUrl;
    }

    public void setOaiBaseUrl(String baseUrl) {
        this.oaiBaseUrl = baseUrl;
    }

    public String getOaiMetadataPrefix() {
        return oaiMetadataPrefix;
    }

    public void setOaiMetadataPrefix(String prefix) {
        oaiMetadataPrefix = prefix;
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

        MongoProvider that = (MongoProvider) o;

        if (aggregator != that.aggregator) return false;
        if (oaiBaseUrl != null ? !oaiBaseUrl.equals(that.oaiBaseUrl) : that.oaiBaseUrl != null) return false;
        if (oaiMetadataPrefix != null ? !oaiMetadataPrefix.equals(that.oaiMetadataPrefix) : that.oaiMetadataPrefix != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (aggregator ? 1 : 0);
        result = 31 * result + (oaiBaseUrl != null ? oaiBaseUrl.hashCode() : 0);
        result = 31 * result + (oaiMetadataPrefix != null ? oaiMetadataPrefix.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getName();
    }
}
