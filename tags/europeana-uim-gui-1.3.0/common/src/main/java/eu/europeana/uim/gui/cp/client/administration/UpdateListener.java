/* ResourceSettingCallback.java - created on May 10, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.gui.cp.client.administration;

import eu.europeana.uim.gui.cp.shared.DataSourceDTO;

/**
 * Callback function for successfull updating a data source.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 10, 2011
 */
public interface UpdateListener {
    /**
     * success function
     * 
     * @param dataSource
     */
    void updated(final DataSourceDTO dataSource);
}
