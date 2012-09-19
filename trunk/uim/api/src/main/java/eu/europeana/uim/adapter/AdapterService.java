/* AdapterService.java - created on Sep 19, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.adapter;

import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.UimDataSet;

/**
 * This interface provides a method that request adaptions to a {@link MetaDataRecord} for specific
 * plugins. Note, multiple services can be registered to fit different kinds of tasks. However,
 * limiting it to one might be faster.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Sep 19, 2012
 */
public interface AdapterService {
    /**
     * Method is called to adapt a given uim data set, so that it can serve as input to the plugin
     * specified by the plugin identifier.
     * 
     * @param dataset
     *            generic uim data set that should be adapted to fit plugin
     * @param pluginIdentifier
     *            identifier defining generic plugin
     * @return adapted {@link UimDataSet} to fit as input data for plugin specified by plugin
     *         identifier or null if not possible to adapt
     */
    UimDataSet<?> adapt(UimDataSet<?> dataset, String pluginIdentifier);
}
