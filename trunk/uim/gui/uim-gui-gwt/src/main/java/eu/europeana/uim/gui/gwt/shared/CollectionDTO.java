package eu.europeana.uim.gui.gwt.shared;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class CollectionDTO extends DataSourceDTO {

    private String name;
    private ProviderDTO provider;
    private Integer total;

    public CollectionDTO(Long id, String name, ProviderDTO provider, Integer total) {
        super(id);
        this.name = name;
        this.provider = provider;
        this.total = total;
    }

    public CollectionDTO() {
        super();
    }

    public String getName() {
        return name;
    }

    public ProviderDTO getProvider() {
        return provider;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProvider(ProviderDTO provider) {
        this.provider = provider;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
