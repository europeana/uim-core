/* FullTextBlockPart.java - created on Jul 23, 2014, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Part on a specific page.
 * 
 * @author Markus Muhr (markus.muhr@theeuropeanlibrary.org)
 * @since Jul 23, 2014
 */
public class FullTextBlockPart {
    @FieldId(1)
    private int          page;

    @FieldId(2)
    private List<String> coordinates;

    @FieldId(3)
    private List<String> blockIds;

    /**
     * Creates a new instance of this class.
     */
    public FullTextBlockPart() {
        super();
        this.coordinates = new ArrayList<String>();
        this.blockIds = new ArrayList<String>();
    }

    /**
     * Returns the page.
     * 
     * @return the page
     */
    public int getPage() {
        return page;
    }

    /**
     * Sets the page to the given value.
     * 
     * @param page
     *            the page to set
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * Returns the coordinates.
     * 
     * @return the coordinates
     */
    public List<String> getCoordinates() {
        return coordinates;
    }

    /**
     * Sets the coordinates to the given value.
     * 
     * @param coordinates
     *            the coordinates to set
     */
    public void setCoordinates(List<String> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Returns the blockIds.
     * 
     * @return the blockIds
     */
    public List<String> getBlockIds() {
        return blockIds;
    }

    /**
     * Sets the blockIds to the given value.
     * 
     * @param blockIds
     *            the blockIds to set
     */
    public void setBlockIds(List<String> blockIds) {
        this.blockIds = blockIds;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((blockIds == null) ? 0 : blockIds.hashCode());
        result = prime * result + ((coordinates == null) ? 0 : coordinates.hashCode());
        result = prime * result + page;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        FullTextBlockPart other = (FullTextBlockPart)obj;
        if (blockIds == null) {
            if (other.blockIds != null) return false;
        } else if (!blockIds.equals(other.blockIds)) return false;
        if (coordinates == null) {
            if (other.coordinates != null) return false;
        } else if (!coordinates.equals(other.coordinates)) return false;
        if (page != other.page) return false;
        return true;
    }

    @Override
    public String toString() {
        return "FullTextBlockPart [page=" + page + ", coordinates=" + coordinates + ", blockIds=" +
               blockIds + "]";
    }
}
