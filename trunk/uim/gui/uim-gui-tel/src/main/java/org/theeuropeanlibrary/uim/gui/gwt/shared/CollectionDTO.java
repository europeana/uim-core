package org.theeuropeanlibrary.uim.gui.gwt.shared;

/**
 * Collection data object used in GWT for visualization.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 28, 2011
 */
public class CollectionDTO extends DataSourceDTO {
    private String      name;
    private String mnemonic;
    private ProviderDTO provider;
    private Integer     total;

    /**
     * Creates a new instance of this class.
     * 
     * @param id
     * @param name
     * @param mnemonic
     * @param provider
     * @param total
     */
    public CollectionDTO(Long id, String name, String mnemonic, ProviderDTO provider, Integer total) {
        super(id);
        this.name = name;
        this.mnemonic = mnemonic;
        this.provider = provider;
        this.total = total;
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
     * @return total number of records
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * @param total
     *            number of records
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return getMnemonic() + "\t" + getName();
    }
}
