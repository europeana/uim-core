/* ResourceSettingCallback.java - created on May 10, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.gui.gwt.client.content;

import org.theeuropeanlibrary.uim.gui.gwt.shared.ParameterDTO;

/**
 * Callback function for successfull setting of a resource
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 10, 2011
 */
public interface ResourceSettingCallback {
    /**
     * success function
     * 
     * @param parameter
     */
    void changed(final ParameterDTO parameter);
}
