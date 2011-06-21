package eu.europeana.uim.gui.gwt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class DataSourceDTO implements IsSerializable {

    private Long id;
	private String identifier;

    public DataSourceDTO() {
    }

    public DataSourceDTO(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getIdentifier(){
    	return identifier;
    }
    
    public void setIdentifier(String identifier) {
    	this.identifier = identifier;
    }
}
