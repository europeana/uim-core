package eu.europeana.uim.store.bean;

import java.io.Serializable;

import eu.europeana.uim.store.UimEntity;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Implements the {@link UimEntity} using Longs as keys.
 *
 * @param <I> unique ID
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public abstract class AbstractEntityBean<I> implements UimEntity<I>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * unique ID as Long
     */
    private I id;

    /**
     * external identifiers connected to this entity
     */
//    private final Set<Object> externalIds = new LinkedHashSet<>();
    /**
     * Creates a new instance of this class.
     */
    public AbstractEntityBean() {
        super();
    }

    /**
     * Creates a new instance of this class.
     *
     * @param id
     */
    public AbstractEntityBean(I id) {
        this.id = id;
    }

    @Override
    public I getId() {
        return id;
    }

    /**
     * @param id unique storage engine based ID
     */
    public void setId(I id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

//    @Override
//    public Set<Object> getExternalIdentifiers() {
//        return Collections.unmodifiableSet(externalIds);
//    }
//
//    @Override
//    public void addExternalIdentifier(Object externalId) {
//        externalIds.add(externalId);
//    }
//
//    @Override
//    public void removeExternalIdentifier(Object externalId) {
//        externalIds.remove(externalId);
//    }
    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AbstractEntityBean other = (AbstractEntityBean) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
