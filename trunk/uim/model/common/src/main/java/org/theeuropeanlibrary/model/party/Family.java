/* Person.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.party;

import java.util.List;

import org.theeuropeanlibrary.model.Identifier;

/**
 * Class represents a family.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 18, 2011
 */
public class Family extends Party {
    /**
     * Creates a new instance of this class.
     */
    public Family() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param familyName
     *            name of the family
     */
    public Family(String familyName) {
        super(familyName);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param familyName
     *            name of the family
     * @param identifiers
     *            Identifiers of the party
     */
    public Family(String familyName, List<Identifier> identifiers) {
        super(familyName, identifiers);
    }
}
