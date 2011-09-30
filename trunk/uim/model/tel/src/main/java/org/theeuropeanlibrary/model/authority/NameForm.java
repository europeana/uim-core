/* NameForm.java - created on 11 de Jul de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.authority;

import java.util.HashSet;
import java.util.Set;

import org.theeuropeanlibrary.model.FieldId;

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
}
