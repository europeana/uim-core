package eu.europeana.uim.gui.cp.shared;

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

    /**
     * Creates a new instance of this class.
     * 
     * @param id
     * @param name
     * @param mnemonic
     * @param provider
     */
    public CollectionDTO(Long id, String name, String mnemonic, ProviderDTO provider) {
        super(id);
        this.name = name;
        this.mnemonic = mnemonic;
        this.provider = provider;
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

    @Override
    public String toString() {
        return getMnemonic() + "\t" + getName();
    }
}
