package eu.europeana.uim.gui.cp.shared;

import java.io.Serializable;

/**
 * Collection data object used in GWT for visualization.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 28, 2011
 */
public class CollectionDTO extends DataSourceDTO {
    private String      name;
    private String      mnemonic;
    private ProviderDTO provider;

    private String      language;

    private String      oaiBaseUrl;
    private String      oaiMetadataPrefix;
    private String      oaiSet;

    private int         size;

    private String      country;

    private String      updateDate;
    private String      harvestStatus;
    private String      harvestRecords;

    /**
     * Creates a new instance of this class.
     * 
     * @param id
     */
    public CollectionDTO(Serializable id) {
        super(id);
    }

    /**
     * Creates a new instance of this class.
     */
    public CollectionDTO() {
        super();
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return provider
     */
    public ProviderDTO getProvider() {
        return provider;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return mnemonic
     */
    public String getMnemonic() {
        return mnemonic;
    }

    /**
     * @param mnemonic
     */
    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    /**
     * @param provider
     */
    public void setProvider(ProviderDTO provider) {
        this.provider = provider;
    }

    /**
     * @return in which language is the collection
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language
     *            in which language is the collection
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @param fallback
     * @return base url to retrieve the collections data
     */
    public String getOaiBaseUrl(boolean fallback) {
        if (oaiBaseUrl != null) { return oaiBaseUrl; }

        if (fallback) { return provider.getOaiBaseUrl(); }
        return null;
    }

    /**
     * @param oaiBaseUrl
     *            base url to retrieve the collections data
     */
    public void setOaiBaseUrl(String oaiBaseUrl) {
        this.oaiBaseUrl = oaiBaseUrl;
    }

    /**
     * @return set identifier for the oai
     */
    public String getOaiSet() {
        return oaiSet;
    }

    /**
     * @param oaiSet
     *            set identifier for the oai
     */
    public void setOaiSet(String oaiSet) {
        this.oaiSet = oaiSet;
    }

    /**
     * @param fallback
     * @return a prefix for the metadata retrieved from this oai
     */
    public String getOaiMetadataPrefix(boolean fallback) {
        if (oaiMetadataPrefix != null) { return oaiMetadataPrefix; }
        if (fallback) { return provider.getOaiMetadataPrefix(); }
        return null;
    }

    /**
     * @param oaiMetadataPrefix
     *            a prefix for the metadata retrieved from this oai
     */
    public void setOaiMetadataPrefix(String oaiMetadataPrefix) {
        this.oaiMetadataPrefix = oaiMetadataPrefix;
    }

    /**
     * @return size
     */
    public int getSize() {
        return size;
    }

    /**
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return update date
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     *            update date
     */
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return harvesting status
     */
    public String getHarvestStatus() {
        return harvestStatus;
    }

    /**
     * @param harvestStatus
     *            harvesting status
     */
    public void setHarvestStatus(String harvestStatus) {
        this.harvestStatus = harvestStatus;
    }

    /**
     * @return number of harvested records
     */
    public String getHarvestRecords() {
        return harvestRecords;
    }

    /**
     * @param harvestRecords
     *            number of harvested records
     */
    public void setHarvestRecords(String harvestRecords) {
        this.harvestRecords = harvestRecords;
    }

    @Override
    public String toString() {
        return getMnemonic() + "\t" + getName();
    }
}
