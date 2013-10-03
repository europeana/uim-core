/* Period.java - created on 22 de Mar de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.time;

import org.theeuropeanlibrary.model.common.FieldId;

/**
 * An interval of time defined by a start instant and end instant
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 22 de Mar de 2011
 */
public class Period extends Temporal {
    /**
     * The start of the period. May be null if it is an open start period If the period has a start
     * instant, but the start instant is unknown, then this field should have a Instant with the
     * flag unknown
     */
    @FieldId(1)
    private Instant start;

    /**
     * The end of the period. May be null if the period has not ended. If the period has ended but
     * the end instant is unknown, then this field should have a Instant with the flag unknown
     */
    @FieldId(2)
    private Instant end;

    /**
     * Creates a new instance of this class.
     */
    public Period() {
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param start
     *            The start of the period. May be null if it is an open start period
     * @param end
     *            The end of the period. May be null if the period has not ended.
     */
    public Period(Instant start, Instant end) {
        super();
        if (start == null && end == null) { throw new IllegalArgumentException(
                "Arguments 'start' and 'end' should not be both null!"); }
        this.start = start;
        this.end = end;
    }

    /**
     * @return start The start of the period.
     */
    public Instant getStart() {
        return start;
    }

    /**
     * @return end The end of the period.
     */
    public Instant getEnd() {
        return end;
    }

    /**
     * @param start
     *            The start of the period.
     */
    public void setStart(Instant start) {
        this.start = start;
    }

    /**
     * @param end
     *            The end of the period.
     */
    public void setEnd(Instant end) {
        this.end = end;
    }

    @Override
    public String getDisplay() {
        String ret;
        if (start != null) {
            ret = start.getDisplay();
        } else {
            ret = "";
        }
        ret += " ";
        if (end != null) {
            ret += end.getDisplay();
        }
        return ret;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((end == null) ? 0 : end.hashCode());
        result = prime * result + ((start == null) ? 0 : start.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        Period other = (Period)obj;
        if (end == null) {
            if (other.end != null) return false;
        } else if (!end.equals(other.end)) return false;
        if (start == null) {
            if (other.start != null) return false;
        } else if (!start.equals(other.start)) return false;
        return true;
    }
}
