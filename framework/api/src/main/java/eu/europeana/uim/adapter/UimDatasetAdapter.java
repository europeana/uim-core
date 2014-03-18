/* AdapterService.java - created on Sep 19, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.adapter;

import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.UimDataSet;

/**
 * This interface provides a method that request adaptions to a {@link MetaDataRecord} for specific
 * plugins. Note, multiple services can be registered to fit different kinds of tasks. However,
 * limiting it to one might be faster.
 * 
 * @param <U>
 *            uim data set type
 * @param <I>
 *            generic identifier
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Sep 19, 2012
 */
public interface UimDatasetAdapter<U extends UimDataSet<I>, I> {
    /**
     * @return identifier defining generic plugin
     */
    String getPluginIdentifier();

    /**
     * Method is called to adapt a given uim data set, so that it can serve as input to the plugin
     * specified by the plugin identifier.
     * 
     * @param dataset
     *            generic uim data set that should be adapted to fit plugin
     * @return adapted {@link UimDataSet} to fit as input data for plugin specified by plugin
     *         identifier or null if not possible to adapt
     */
    U adapt(U dataset);

    /**
     * Method is called to unadapt an uim data set to be called before storage.
     * 
     * @param dataset
     *            generic uim data set that should be adapted to fit plugin
     * @return unadapted {@link UimDataSet} holding only normalized data that can be used for
     *         storage
     */
    U unadapt(U dataset);
}
