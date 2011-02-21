package eu.europeana.uim.gui.gwt.shared;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class Collection extends DataSource {

    private String name;
    private Provider provider;
    private Integer total;

    public Collection(Long id, String name, Provider provider, Integer total) {
        super(id);
        this.name = name;
        this.provider = provider;
        this.total = total;
    }

    public Collection() {
        super();
    }

    public String getName() {
        return name;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
