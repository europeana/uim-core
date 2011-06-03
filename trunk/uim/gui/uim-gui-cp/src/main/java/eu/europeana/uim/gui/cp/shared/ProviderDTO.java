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
     * @param name
     * @param mnemonic
     */
    public ProviderDTO(Long id, String name, String mnemonic) {
        super(id);
        this.name = name;
        this.mnemonic = mnemonic;
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
    
    @Override
    public String toString() {
        return getMnemonic() + "\t" + getName();
    }
}