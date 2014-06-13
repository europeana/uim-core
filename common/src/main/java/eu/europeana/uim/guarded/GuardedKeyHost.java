package eu.europeana.uim.guarded;

import java.net.URL;
import java.util.HashMap;

/**
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 16, 2011
 */
final class GuardedKeyHost implements GuardedKey<Long> {

    private final static HashMap<String, Long> delivered = new HashMap<>();

    private final String host;

    public GuardedKeyHost(String host) {
        this.host = host;

        if (!delivered.containsKey(host)) {
            delivered.put(host, 0L);
        }
    }

    public GuardedKeyHost(URL url) {
        this(url.getHost());
    }

    @Override
    public Long getConditionValue() {
        return delivered.get(host);
    }

    @Override
    public void delivered() {
        delivered.put(host, System.currentTimeMillis());
    }

    @Override
    public int compareTo(GuardedKey<Long> o) {
        return getConditionValue().compareTo(o.getConditionValue());
    }

    @Override
    public String toString() {
        return "Host: <" + host + "> condition:" + getConditionValue();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GuardedKeyHost other = (GuardedKeyHost) obj;
        if (host == null) {
            if (other.host != null) {
                return false;
            }
        } else if (!host.equals(other.host)) {
            return false;
        }
        return true;
    }
}
