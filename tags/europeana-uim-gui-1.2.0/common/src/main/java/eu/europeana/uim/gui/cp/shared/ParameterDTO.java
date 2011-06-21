/* PropertyDTO.java - created on May 9, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.gui.cp.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * This class represents a key - values for specific parameters of IngestionPlugin.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 9, 2011
 */
public class ParameterDTO implements IsSerializable {
    private String   key;
    private String[] values;

    /**
     * Creates a new instance of this class.
     */
    public ParameterDTO() {
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param key
     * @param values
     */
    public ParameterDTO(String key, String[] values) {
        this.key = key;
        this.values = values;
    }

    /**
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * Sets the key to the given value.
     * 
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return values
     */
    public String[] getValues() {
        return values;
    }

    /**
     * @param values
     */
    public void setValues(String[] values) {
        this.values = values;
    }
}
