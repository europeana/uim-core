/* TopicNameForm.java - created on Nov 3, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.authority;

import java.util.Set;

import org.theeuropeanlibrary.model.common.spatial.NamedPlace;

/**
 * Name form of a place.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Nov 3, 2011
 */
public class NamedPlaceNameForm extends NameForm<NamedPlace> {
    /**
     * Creates a new instance of this class. protected so that it can only be used by repository
     * converters
     */
    protected NamedPlaceNameForm() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param nameForm
     *            name form
     */
    public NamedPlaceNameForm(NamedPlace nameForm) {
        super(nameForm);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param nameForm
     *            name form
     * @param sources
     *            codes of the data sources where the name form is used
     */
    public NamedPlaceNameForm(NamedPlace nameForm, Set<String> sources) {
        super(nameForm, sources);
    }
}
