/* Topic.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.subject;

import java.util.ArrayList;
import java.util.List;

import org.theeuropeanlibrary.model.common.FieldId;
import org.theeuropeanlibrary.model.common.Identifier;
import org.theeuropeanlibrary.model.common.time.Temporal;

/**
 * The topic class holding information about the actual topic name and an optional description.
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 */
public class TitleSubject extends Subject {
    /**
     * Title dates (typically the date of publication)
     */
    @FieldId(5)
    private Temporal           titleDates;

    /**
     * any other information regarding the subject/title
     */
    @FieldId(6)
    private String             miscellaneousInformation;

    /**
     * Identifiers of the topic in external data sets
     */
    @FieldId(7)
    protected List<Identifier> identifiers;

    /**
     * Creates a new instance of this class.
     */
    public TitleSubject() {
        this(null);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param identifiers
     *            Identifiers of the topic in external data sets
     */
    public TitleSubject(List<Identifier> identifiers) {
        if (identifiers == null) {
            this.identifiers = new ArrayList<Identifier>(0);
        } else {
            this.identifiers = identifiers;
        }
    }


    /**
     * Creates a new instance of this class.
     * @param miscellaneousInformation 
     * 
     * @param identifiers
     *            Identifiers of the topic in external data sets
     */
    public TitleSubject(String miscellaneousInformation, List<Identifier> identifiers) {
        this.miscellaneousInformation = miscellaneousInformation;
        if (identifiers == null) {
            this.identifiers = new ArrayList<Identifier>(0);
        } else {
            this.identifiers = identifiers;
        }
    }

    /**
     * @return Title dates (typically the date of publication)
     */
    public Temporal getTitleDates() {
        return titleDates;
    }

    /**
     * @param titleDates
     *            Title dates (typically the date of publication)
     */
    public void setTitleDates(Temporal titleDates) {
        this.titleDates = titleDates;
    }

    /**
     * Returns the miscellaneousInformation.
     * 
     * @return any other information regarding the subject/title
     */
    public String getMiscellaneousInformation() {
        return miscellaneousInformation;
    }

    /**
     * @param miscellaneousInformation
     *            any other information regarding the subject/title
     */
    public void setMiscellaneousInformation(String miscellaneousInformation) {
        this.miscellaneousInformation = miscellaneousInformation;
    }

    /**
     * @return identifiers of topics like TEL, MACS, LCSH, ...
     */
    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    /**
     * @param identifiers
     *            identifiers of topics like TEL, MACS, LCSH, ...
     */
    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((identifiers == null) ? 0 : identifiers.hashCode());
        result = prime * result +
                 ((miscellaneousInformation == null) ? 0 : miscellaneousInformation.hashCode());
        result = prime * result + ((titleDates == null) ? 0 : titleDates.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        TitleSubject other = (TitleSubject)obj;
        if (identifiers == null) {
            if (other.identifiers != null) return false;
        } else if (!identifiers.equals(other.identifiers)) return false;
        if (miscellaneousInformation == null) {
            if (other.miscellaneousInformation != null) return false;
        } else if (!miscellaneousInformation.equals(other.miscellaneousInformation)) return false;
        if (titleDates == null) {
            if (other.titleDates != null) return false;
        } else if (!titleDates.equals(other.titleDates)) return false;
        return true;
    }
}
