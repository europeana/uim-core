package org.theeuropeanlibrary.uim.gui.gwt.shared;

/**
 * Provider data object used in GWT for visualization.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 28, 2011
 */
public class ProviderDTO extends DataSourceDTO {
    private String name;

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
     */
    public ProviderDTO(Long id, String name) {
        super(id);
        this.name = name;
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

    @Override
    public String toString() {
        return name;
    }
}