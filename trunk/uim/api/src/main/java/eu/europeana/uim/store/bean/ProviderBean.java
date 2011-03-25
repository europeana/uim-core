package eu.europeana.uim.store.bean;

import java.util.HashSet;
import java.util.Set;

import eu.europeana.uim.store.Provider;

/**
 * In-memory implementation of {@link Provider} using Long as ID.
 * 
 * @param <I>
 *            unique ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @date Mar 22, 2011
 */
public class ProviderBean<I> extends AbstractNamedEntityBean<I> implements Provider<I> {
    private Set<Provider<I>> relatedOut = new HashSet<Provider<I>>();
    private Set<Provider<I>> relatedIn  = new HashSet<Provider<I>>();

    private String            name;

    private String            oaiBaseUrl;
    private String            oaiMetadataPrefix;

    private boolean           aggregator;

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
}
