/* Party.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model;

import java.util.Date;

/**
 * Defining a URL link to an image, digital object, online catalog, etc. The
 * type of link (image, digital object, online catalog) is defined externally to
 * the class in a qualifier
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 18, 2011
 */
public class Link {
	/**
	 * uniform resource location
	 */
	@FieldId(1)
	private String url;

	@FieldId(2)
	private Date lastChecked;
	
	@FieldId(3)
	private LinkStatus linkStatus;
	
	@FieldId(4)
	private String cachedPath;
	
	/**
	 * Creates a new instance of this class.
	 */
	public Link() {
		super();
	}

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param url
	 *            uniform resource identification to get to the actual value
	 */
	public Link(String url) {
		if (url == null) {
			throw new IllegalArgumentException(
					"Argument 'url' should not be null!");
		}
		this.url = url;
		linkStatus=LinkStatus.NOT_CHECKED;
	}

	/**
	 * @return uniform resource identification to get to the actual value
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            uniform resource identification to get to the actual value
	 */
	public void setUrl(String url) {
		if (url == null)
			throw new IllegalArgumentException(
					"Argument 'url' should not be null!");
		this.url = url;
	}

    /**
     * Sets the lastChecked to the given value.
     * @param lastChecked the lastChecked to set
     */
    public void setLastChecked(Date lastChecked) {
        this.lastChecked = lastChecked;
    }

    /**
     * Returns the lastChecked.
     * @return the lastChecked
     */
    public Date getLastChecked() {
        return lastChecked;
    }

    /**
     * Sets the linkStatus to the given value.
     * @param linkStatus the linkStatus to set
     */
    public void setLinkStatus(LinkStatus linkStatus) {
        this.linkStatus = linkStatus;
    }

    /**
     * Returns the linkStatus.
     * @return the linkStatus
     */
    public LinkStatus getLinkStatus() {
        return linkStatus;
    }

    /**
     * @param message
     */
    public void setCachedPath(String message) {
       cachedPath=message; 
    }

    /**
     * Returns the cachedPath.
     * @return the cachedPath
     */
    public String getCachedPath() {
        return cachedPath;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cachedPath == null) ? 0 : cachedPath.hashCode());
        result = prime * result + ((lastChecked == null) ? 0 : lastChecked.hashCode());
        result = prime * result + ((linkStatus == null) ? 0 : linkStatus.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Link other = (Link)obj;
        if (cachedPath == null) {
            if (other.cachedPath != null) return false;
        } else if (!cachedPath.equals(other.cachedPath)) return false;
        if (lastChecked == null) {
            if (other.lastChecked != null) return false;
        } else if (!lastChecked.equals(other.lastChecked)) return false;
        if (linkStatus != other.linkStatus) return false;
        if (url == null) {
            if (other.url != null) return false;
        } else if (!url.equals(other.url)) return false;
        return true;
    }
    
    

}
