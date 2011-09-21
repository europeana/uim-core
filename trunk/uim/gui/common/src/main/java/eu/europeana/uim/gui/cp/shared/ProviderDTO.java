package eu.europeana.uim.gui.cp.shared;

/**
 * Provider data object used in GWT for visualization.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 28, 2011
 */
public class ProviderDTO extends DataSourceDTO {
    private String name;
    private String mnemonic;

    private String oaiBaseUrl;
    private String oaiMetadataPrefix;

    private String country;

    /**
     * Creates a new instance of this class.
     */
    public ProviderDTO() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param id
     */
    public ProviderDTO(Long id) {
        super(id);
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return oai prefix a prefix for the metadata retrieved from this oai
     */
    public String getOaiMetadataPrefix() {
        return oaiMetadataPrefix;
    }

    /**
     * @param oaiPrefix
     *            a prefix for the metadata retrieved from this oai
     */
    public void setOaiMetadataPrefix(String oaiPrefix) {
        this.oaiMetadataPrefix = oaiPrefix;
    }

    /**
     * @return base url to retrieve the providers data
     */
    public String getOaiBaseUrl() {
        return oaiBaseUrl;
    }

    /**
     * @param oaiBaseUrl
     *            base url to retrieve the providers data
     */
    public void setOaiBaseUrl(String oaiBaseUrl) {
        this.oaiBaseUrl = oaiBaseUrl;
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

    @Override
    public String toString() {
        return getMnemonic() + "\t" + getName();
    }
}