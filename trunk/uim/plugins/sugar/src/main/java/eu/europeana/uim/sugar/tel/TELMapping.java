/* TheEuropeanLibraryMapping.java - created on Feb 5, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.tel;

import eu.europeana.uim.sugar.client.SugarMapping;
import eu.europeana.uim.sugarcrm.model.RetrievableField;
import eu.europeana.uim.sugarcrm.model.UpdatableField;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 5, 2012
 */
public class TELMapping implements SugarMapping {

    @Override
    public RetrievableField[] getProviderRetrievableFields() {
        return new RetrievableField[]{
                TELProviderFields.MNEMONIC,
                TELProviderFields.NAME,
                TELProviderFields.TEL_PROVIDER_KIND,
                TELProviderFields.COUNTRY,
                TELProviderFields.REPOX_SERVER,
                TELProviderFields.REPOX_PREFIX,
                TELProviderFields.TEL_ACTIVE_UIM,
        };
    }

    @Override
    public UpdatableField[] getProviderUpdateableFields() {
        return new UpdatableField[]{
        };
    }

    @Override
    public RetrievableField[] getCollectionRetrievableFields() {
        return new RetrievableField[]{
                TELCollectionFields.MNEMONIC,
                TELCollectionFields.NAME,
                TELCollectionFields.TEL_COLLECTION_KIND,
                TELCollectionFields.COUNTRY,
                TELCollectionFields.LANGUAGE,
                TELCollectionFields.REPOX_SERVER,
                TELCollectionFields.REPOX_PREFIX,
                TELCollectionFields.TEL_ACTIVE_UIM,
                TELCollectionFields.HARVESTING_METHOD
        };
    }
    

    @Override
    public UpdatableField[] getCollectionUpdateableFields() {
        return new UpdatableField[]{
                TELCollectionFields.HARVESTED_RECORDS,
                TELCollectionFields.HARVESTING_STATUS,
                TELCollectionFields.LAST_HARVESTING_DATE,
                TELCollectionFields.LINKCHECK_EXECUTION,
                TELCollectionFields.FIELDCHECK_EXECUTION
        };
    }

}
