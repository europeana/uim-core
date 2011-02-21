package eu.europeana.uim.gui.gwt.shared;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class Provider extends DataSource {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Provider() {
        super();
    }

    public Provider(Long id, String name) {
        super(id);
        this.name = name;
    }
}