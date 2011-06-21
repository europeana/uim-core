package org.theeuropeanlibrary.uim.gui.gwt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

/**
 * Base class for all store relevant data objects.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 28, 2011
 */
public abstract class DataSourceDTO implements IsSerializable {
    /**
     * The key provider that provides the unique ID of a contact.
     */
    public static final ProvidesKey<DataSourceDTO> KEY_PROVIDER = new ProvidesKey<DataSourceDTO>() {
        @Override
        public Object getKey(DataSourceDTO item) {
            return item == null ? null : item.getId();
        }
    };
    
    /**
     * unique ID as Long
     */
    private Long id;

    /**
     * Creates a new instance of this class.
     */
    public DataSourceDTO() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param id
     */
    public DataSourceDTO(Long id) {
        this.id = id;
    }

    /**
     * @return unique identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            unique identifier
     */
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        DataSourceDTO other = (DataSourceDTO)obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }
}
