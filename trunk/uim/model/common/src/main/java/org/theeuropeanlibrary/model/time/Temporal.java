/* Time.java - created on Mar 21, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.time;



import org.theeuropeanlibrary.model.FieldId;
import org.theeuropeanlibrary.model.subject.Subject;

/**
 * Base class for all time concerned data like a specific date, an uncertain date, a period or a
 * named period.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public abstract class Temporal {
    /**
     * If this spatial temporal is in the role of a subject additional informations can be retrieved
     * throught this subject object.
     */
    @FieldId(5)
    protected Subject subject;

    /**
     * @return If this temporal entity is in the role of a subject additional informations can be
     *         retrieved throught this subject object.
     */
    public Subject getSubject() {
        return subject;
    }

    /**
     * @param subject
     *            If this temporal entity is in the role of a subject additional informations can be
     *            retrieved throught this subject object.
     */
    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    /**
     * @return a string representation of the period for end user display
     */
    public abstract String getDisplay();
    
    
    @Override
    public String toString() {
    	return getDisplay();
    }
}
