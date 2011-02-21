package eu.europeana.uim.store.mongo;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;

/**
 * We call this MongodbCollection in order not to collide with the MongoCollection in Mongo itself
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
@Entity
public class MongodbCollection extends AbstractMongoEntity implements Collection {

    private String language;

    private String oaiBaseUrl;
    private String oaiMetadataPrefix;
    private String oaiSet;

    @Reference
    private MongoProvider provider = null;

    public MongodbCollection() {
    }

    public MongodbCollection(long id, Provider p) {
        super(id);
        this.provider = (MongoProvider) p;
    }

	public String getIdentifier() {
		return "Collection:" + getMnemonic();
	}
	


    
    public Provider getProvider() {
        return provider;
    }

    public void setProvider(MongoProvider provider) {
        this.provider = provider;
    }
    
    

    public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getOaiBaseUrl() {
        return oaiBaseUrl;
    }

    public String getOaiSet() {
        return oaiSet;
    }

    public void setOaiSet(String set) {
        oaiSet = set;
    }

    public void setOaiBaseUrl(String oaiBaseUrl) {
        this.oaiBaseUrl = oaiBaseUrl;
    }

    public String getOaiMetadataPrefix() {
        return oaiMetadataPrefix;
    }

    public void setOaiMetadataPrefix(String oaiMetadataPrefix) {
        this.oaiMetadataPrefix = oaiMetadataPrefix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MongodbCollection that = (MongodbCollection) o;

        if (oaiBaseUrl != null ? !oaiBaseUrl.equals(that.oaiBaseUrl) : that.oaiBaseUrl != null) return false;
        if (oaiMetadataPrefix != null ? !oaiMetadataPrefix.equals(that.oaiMetadataPrefix) : that.oaiMetadataPrefix != null)
            return false;
        if (oaiSet != null ? !oaiSet.equals(that.oaiSet) : that.oaiSet != null) return false;
        if (!provider.equals(that.provider)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (oaiBaseUrl != null ? oaiBaseUrl.hashCode() : 0);
        result = 31 * result + (oaiMetadataPrefix != null ? oaiMetadataPrefix.hashCode() : 0);
        result = 31 * result + (oaiSet != null ? oaiSet.hashCode() : 0);
        result = 31 * result + provider.hashCode();
        return result;
    }


    @Override
    public String toString() {
        String string = super.toString();
        string += " [";
        string += getOaiBaseUrl() != null ? getOaiBaseUrl() : (getProvider().getOaiBaseUrl() != null ? getProvider().getOaiBaseUrl() : "undefined");

        string += "?metadataPrefix=";
        string += getOaiMetadataPrefix() != null ? getOaiMetadataPrefix() : getProvider().getOaiMetadataPrefix();
        return string + "]";
    }
}
