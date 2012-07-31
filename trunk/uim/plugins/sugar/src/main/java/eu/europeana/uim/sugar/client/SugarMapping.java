/* SugarMapping.java - created on Feb 5, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.client;

import eu.europeana.uim.sugarcrm.model.RetrievableField;
import eu.europeana.uim.sugarcrm.model.UpdatableField;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 5, 2012
 */
public interface SugarMapping {
    /**
     * @return the retrievable fields for providers
     */
    RetrievableField[] getProviderRetrievableFields();

    /**
     * @return the updateable fields for providers
     */
    UpdatableField[] getProviderUpdateableFields();

    /**
     * @return the retrievalbe fields for collections
     */
    RetrievableField[] getCollectionRetrievableFields();

    /**
     * @return the updateable fields for collections
     */
    UpdatableField[] getCollectionUpdateableFields();
}
