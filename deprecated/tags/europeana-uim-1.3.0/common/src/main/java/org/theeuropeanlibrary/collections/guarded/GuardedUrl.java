package org.theeuropeanlibrary.collections.guarded;

import java.net.URL;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 16, 2011
 */
public class GuardedUrl implements Guarded {
    
    private final URL                          url;

    private final GuardedKey<Long>             key;

    /**
     * Creates a new instance of this class.
     * 
     * @param url
     */
    public GuardedUrl(URL url) {
        this.url = url;
        this.key = new GuardedKeyHost(url);
    }

    /**
     * Returns the url.
     * @return the url
     */
    public URL getUrl() {
        return url;
    }

    @Override
    public GuardedKey<Long> getGuardKey() {
        return key;
    }

    @Override
    public void processed(int status, String message) {
    }

    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        GuardedUrl other = (GuardedUrl)obj;
        if (url == null) {
            if (other.url != null) return false;
        } else if (!url.equals(other.url)) return false;
        return true;
    }

    @Override
    public String toString() {
        return url.toExternalForm();
    }
}
