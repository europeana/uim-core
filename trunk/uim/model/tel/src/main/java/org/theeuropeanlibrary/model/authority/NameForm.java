/* NameForm.java - created on 11 de Jul de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.authority;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.theeuropeanlibrary.model.common.FieldId;

/**
 * A variant form of the name of an entity (Person or Corporate), and information about where it is
 * used
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @param <T>
 *            the type of party
 * @date 11 de Jul de 2011
 */
public abstract class NameForm<T> {
    /**
     * The name form
     */
    @FieldId(1)
    private T           nameForm;

    /**
     * TEL codes of the data sources where the name form is used
     */
    @FieldId(2)
    private Set<String> sources;

    /**
     * Creates a new instance of this class. protected so that it can only be used by repository
     * converters
     */
    protected NameForm() {
        sources = new HashSet<String>();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param nameForm
     *            name form
     */
    public NameForm(T nameForm) {
        this.nameForm = nameForm;
        sources = new HashSet<String>();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param nameForm
     *            name form
     * @param sources
     *            codes of the data sources where the name form is used
     */
    public NameForm(T nameForm, Set<String> sources) {
        super();
        this.nameForm = nameForm;
        this.sources = sources;
    }

    /**
     * @return name form
     */
    public T getNameForm() {
        return nameForm;
    }

    /**
     * @param nameForm
     *            name form
     */
    public void setNameForm(T nameForm) {
        this.nameForm = nameForm;
    }

    /**
     * @return codes of the data sources where the name form is used
     */
    public Set<String> getSources() {
        return sources;
    }

    /**
     * @param sources
     *            VIAF codes of the data sources where the name form is used
     */
    public void setSources(Set<String> sources) {
        this.sources = sources;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nameForm == null) ? 0 : nameForm.hashCode());
        result = prime * result + ((sources == null) ? 0 : sources.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        NameForm<?>other = (NameForm<?>)obj;
        if (nameForm == null) {
            if (other.nameForm != null) return false;
        } else if (!nameForm.equals(other.nameForm)) return false;
        if (sources == null) {
            if (other.sources != null) return false;
        } else if (!CollectionUtils.isEqualCollection(sources, other.sources)) return false;
        return true;
    }
    
    
    
    
}
