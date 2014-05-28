/* Person.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.party;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.theeuropeanlibrary.model.common.FieldId;
import org.theeuropeanlibrary.model.common.Identifier;
import org.theeuropeanlibrary.model.common.time.Instant;

/**
 * Class represents a meeting.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 18, 2011
 */
public class Meeting extends Organization {
    /**
     * When the meeting was held
     */
    @FieldId(4)
    private Instant date;

    /**
     * Creates a new instance of this class.
     */
    public Meeting() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param partyName
     *            name of the meeting
     */
    public Meeting(String partyName) {
        super(partyName);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param meetingName
     *            name of the meeting
     * @param date
     *            When the meeting was held
     */
    public Meeting(String meetingName, Instant date) {
        super(meetingName);
        this.date = date;
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param meetingName
     *            name of the meeting
     * @param date
     *            When the meeting was held
     * @param identifiers
     */
    public Meeting(String meetingName, Instant date, List<Identifier> identifiers) {
        super(meetingName, identifiers);
        this.date = date;
    }

    /**
     * @return When the meeting was held
     */
    public Instant getDate() {
        return date;
    }

    /**
     * @param date
     *            When the meeting was held
     */
    public void setDate(Instant date) {
        this.date = date;
    }

    /**
     * @return a string representation of the party for end user display
     */
    @Override
    public String getDisplay() {
        String ret=partyName;
        if (subdivision != null && !subdivision.trim().isEmpty()) 
            ret = StringUtils.stripEnd(ret, ".,;:?") + ". " + subdivision;
        if (date != null || (location!=null)) { 
            ret += "(";
            if (location!=null) { 
                ret += location.getDisplay();
                if (date != null) 
                    ret = StringUtils.stripEnd(ret, ".,;:?") + " " + date.getDisplay();
            } else {
                ret += date.getDisplay();
            }
            ret += ")";
        }
        return ret;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        Meeting other = (Meeting)obj;
        if (subdivision == null) {
            if (other.subdivision != null) return false;
        } else if (!subdivision.equals(other.subdivision)) return false;
        if (date == null) {
            if (other.date != null) return false;
        } else if (!date.equals(other.date)) return false;
        return true;
    }

}
