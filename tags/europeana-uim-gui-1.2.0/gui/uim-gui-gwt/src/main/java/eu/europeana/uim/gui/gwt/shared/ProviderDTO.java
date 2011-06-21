package eu.europeana.uim.gui.gwt.shared;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class ProviderDTO extends DataSourceDTO {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProviderDTO() {
        super();
    }

    public ProviderDTO(Long id, String name) {
        super(id);
        this.name = name;
    }
}