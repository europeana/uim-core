package eu.europeana.uim.store.mongo;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
import eu.europeana.uim.store.Provider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
@Entity
public class MongoProvider extends AbstractMongoEntity implements Provider {

    private boolean aggregator;
    private String oaiBaseUrl;
    private String oaiMetadataPrefix;

    @Reference
    private List<Provider> relatedOut = new ArrayList<Provider>();
    @Reference
    private List<Provider> relatedIn = new ArrayList<Provider>();


    public MongoProvider() {
    }

    public MongoProvider(long id) {
        super(id);
    }

	public String getIdentifier() {
		return "Provider:" + getMnemonic();
	}
	


    public List<Provider> getRelatedOut() {
        return relatedOut;
    }

    public List<Provider> getRelatedIn() {
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
