package eu.europeana.uim.store.mongo;

import java.util.Date;

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
public class MongodbCollection<T> extends AbstractMongoEntity<Long> implements Collection<Long> {

    private String language;

    private String oaiBaseUrl;
    private String oaiMetadataPrefix;
    private String oaiSet;

    private Date        lastModified;
    private Date        lastSynchronized;


    @Reference
    private MongoProvider provider = null;

    public MongodbCollection() {
    }

    public MongodbCollection(Long id, Provider p) {
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

	public String getOaiBaseUrl(boolean fallback) {
        if (oaiBaseUrl != null) { return oaiBaseUrl; }
        
        if (fallback) {
        return provider.getOaiBaseUrl();
        }
        return null;
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

    public String getOaiMetadataPrefix(boolean fallback) {
        if (oaiMetadataPrefix != null) { return oaiMetadataPrefix; }
        if (fallback) {
        return provider.getOaiMetadataPrefix();
        }
        return null;
    }

    public void setOaiMetadataPrefix(String oaiMetadataPrefix) {
        this.oaiMetadataPrefix = oaiMetadataPrefix;
    }
    
    @Override
    public Date getLastModified() {
        return lastModified;
    }

    @Override
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public Date getLastSynchronized() {
        return lastSynchronized;
    }

    @Override
    public void setLastSynchronized(Date lastSynchronized) {
        this.lastSynchronized = lastSynchronized;
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
        
        string += "@" + getLanguage();
        String oai = getOaiBaseUrl(true);
        if (oai != null) {
            string += " OAI:[";
            string += oai + "?verb={x}";
            
            string += getOaiSet() != null ? "&set=" + getOaiSet() : "";
            string += getOaiMetadataPrefix(true) != null ? "&metadataPrefix=" + getOaiMetadataPrefix(true) : "";
            string += "]";
        }
        return string;
    }
}
