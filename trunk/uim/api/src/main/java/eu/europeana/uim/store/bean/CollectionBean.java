package eu.europeana.uim.store.bean;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;

/**
 * Implements {@link Collection} using Longs as IDs and holding all information in memory.
 * 
 * @param <I>
 *            unique ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @date Mar 22, 2011
 */
public class CollectionBean<I> extends AbstractNamedEntityBean<I> implements Collection<I> {
    private Provider<I> provider;

    private String      language;

    private String      oaiBaseUrl;
    private String      oaiMetadataPrefix;
    private String      oaiSet;

    /**
     * Creates a new instance of this class.
     */
    public CollectionBean() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param id
     * @param provider
     */
    public CollectionBean(I id, Provider<I> provider) {
        super(id);
        this.provider = provider;
    }

    @Override
    public Provider<I> getProvider() {
        return provider;
    }

    /**
     * @param provider
     */
    public void setProvider(Provider<I> provider) {
        this.provider = provider;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String getOaiBaseUrl() {
        if (oaiBaseUrl != null) { return oaiBaseUrl; }
        return provider.getOaiBaseUrl();
    }

    @Override
    public void setOaiBaseUrl(String oaiBaseUrl) {
        this.oaiBaseUrl = oaiBaseUrl;
    }

    @Override
    public String getOaiSet() {
        return oaiSet;
    }

    /**
     * @param oaiSet
     *            set identifier for OAI
     */
    @Override
    public void setOaiSet(String oaiSet) {
        this.oaiSet = oaiSet;
    }

    @Override
    public String getOaiMetadataPrefix() {
        if (oaiMetadataPrefix != null) { return oaiMetadataPrefix; }
        return provider.getOaiMetadataPrefix();
    }

    @Override
    public void setOaiMetadataPrefix(String oaiMetadataPrefix) {
        this.oaiMetadataPrefix = oaiMetadataPrefix;
    }

    @Override
    public String toString() {
        String string = super.toString();
        string += " [";
        string += getOaiBaseUrl() != null ? getOaiBaseUrl()
                : (getProvider().getOaiBaseUrl() != null ? getProvider().getOaiBaseUrl()
                        : "undefined");

        string += "?metadataPrefix=";
        string += getOaiMetadataPrefix() != null ? getOaiMetadataPrefix()
                : getProvider().getOaiMetadataPrefix();
        return string + "]";
    }
}
