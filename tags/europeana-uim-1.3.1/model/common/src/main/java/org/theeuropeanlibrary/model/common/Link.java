/* Party.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common;

import java.util.Date;

import org.theeuropeanlibrary.model.common.qualifier.LinkStatus;

/**
 * Defining a URL link to an image, digital object, online catalog, etc. The type of link (image,
 * digital object, online catalog) is defined externally to the class in a qualifier
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
    private String     url;

    @FieldId(2)
    private Date       lastChecked;

    @FieldId(3)
    private LinkStatus linkStatus;

    @FieldId(4)
    private String     cachedPath;

    @FieldId(5)
    private String     anchorKey;

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
        if (url == null) { throw new IllegalArgumentException("Argument 'url' should not be null!"); }
        this.url = url;
        linkStatus = LinkStatus.NOT_CHECKED;
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param url
     *            uniform resource identification to get to the actual value
     * @param anchorKey
     */
    public Link(String url, String anchorKey) {
        if (url == null) { throw new IllegalArgumentException("Argument 'url' should not be null!"); }
        this.url = url;
        this.anchorKey = anchorKey;
        linkStatus = LinkStatus.NOT_CHECKED;
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
        if (url == null) throw new IllegalArgumentException("Argument 'url' should not be null!");
        this.url = url;
    }

    /**
     * @param lastChecked
     */
    public void setLastChecked(Date lastChecked) {
        this.lastChecked = lastChecked;
    }

    /**
     * @return date at which link has been last checked
     */
    public Date getLastChecked() {
        return lastChecked;
    }

    /**
     * @param linkStatus
     */
    public void setLinkStatus(LinkStatus linkStatus) {
        this.linkStatus = linkStatus;
    }

    /**
     * @return link status
     */
    public LinkStatus getLinkStatus() {
        return linkStatus;
    }

    /**
     * @param message
     */
    public void setCachedPath(String message) {
        cachedPath = message;
    }

    /**
     * @return cache Path
     */
    public String getCachedPath() {
        return cachedPath;
    }

    /**
     * @return anchor key
     */
    public String getAnchorKey() {
        return anchorKey;
    }

    /**
     * @param anchorKey
     */
    public void setAnchorKey(String anchorKey) {
        this.anchorKey = anchorKey;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cachedPath == null) ? 0 : cachedPath.hashCode());
        result = prime * result + ((lastChecked == null) ? 0 : lastChecked.hashCode());
        result = prime * result + ((linkStatus == null) ? 0 : linkStatus.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        result = prime * result + ((anchorKey == null) ? 0 : anchorKey.hashCode());
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
        if (anchorKey == null) {
            if (other.anchorKey != null) return false;
        } else if (!anchorKey.equals(other.anchorKey)) return false;
        return true;
    }
}
