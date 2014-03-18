/* TimeTextualExpression.java - created on 22 de Mar de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.theeuropeanlibrary.model.common.FieldId;
import org.theeuropeanlibrary.model.common.Identifier;

/**
 * A textual expression of an instance of period which was not possible to parse into a structured
 * data
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 22 de Mar de 2011
 */
public class TemporalTextualExpression extends Temporal {
    /**
     * A time textual expression
     */
    @FieldId(1)
    private String           text;

    /**
     * Identifiers of the time period in external data sets
     */
    @FieldId(2)
    private List<Identifier> identifiers;

    /**
     * Creates a new instance of this class.
     */
    public TemporalTextualExpression() {
        this.identifiers = new ArrayList<Identifier>(1);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param text
     *            A time textual expression
     * @param identifiers
     *            Identifiers of the time period in external data sets
     */
    public TemporalTextualExpression(String text, List<Identifier> identifiers) {
        super();
        if (text == null) { throw new IllegalArgumentException(
                "Argument 'text' should not be null!"); }
        this.text = text;
        if (identifiers == null) {
            this.identifiers = new ArrayList<Identifier>(1);
        } else {
            this.identifiers = identifiers;
        }
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param text
     * @param identifiers
     */
    public TemporalTextualExpression(String text, Identifier... identifiers) {
        this(text, Arrays.asList(identifiers));
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param text
     */
    public TemporalTextualExpression(String text) {
        this.text = text;
        identifiers = new ArrayList<Identifier>(1);
    }

    /**
     * @return a time textual expression
     */
    public String getText() {
        return text;
    }

    /**
     * @param text
     *            a time textual expression
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return identifiers
     */
    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    /**
     * @param identifiers
     */
    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    @Override
    public String getDisplay() {
        if(subject!=null) {
            String subjectHeadingString = subject.getSubjectHeadingDisplay();
            if(!subjectHeadingString.isEmpty())
                return text+subjectHeadingString;
        }
        return text;
    }

    /**
     * @return a String readable by a human according to subject heading rules
     */
    public String getSubjectHeadingDisplay() {
        String placeName=text;
        if(subject!=null) {
            String subjectHeadingString = subject.getSubjectHeadingDisplay();
            if(!subjectHeadingString.isEmpty())
                return placeName+subjectHeadingString;
            else
                return placeName;
        }else
            return placeName;
    }

    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((identifiers == null) ? 0 : identifiers.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TemporalTextualExpression other = (TemporalTextualExpression)obj;
        if (identifiers == null) {
            if (other.identifiers != null) return false;
        } else if (!CollectionUtils.isEqualCollection(identifiers, other.identifiers))
            return false;
        if (text == null) {
            if (other.text != null) return false;
        } else if (!text.equals(other.text)) return false;
        return true;
    }
}
