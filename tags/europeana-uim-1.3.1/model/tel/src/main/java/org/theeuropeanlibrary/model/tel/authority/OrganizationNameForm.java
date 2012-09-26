/* PersonNameForm.java - created on Jul 26, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.authority;

import java.util.Set;

import org.theeuropeanlibrary.model.common.party.Organization;

/**
 * Name form of a person.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Jul 26, 2011
 */
public class OrganizationNameForm extends NameForm<Organization> {
    /**
     * Creates a new instance of this class. protected so that it can only be used by repository
     * converters
     */
    protected OrganizationNameForm() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param nameForm
     *            name form
     */
    public OrganizationNameForm(Organization nameForm) {
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
    public OrganizationNameForm(Organization nameForm, Set<String> sources) {
        super(nameForm, sources);
    }
}
