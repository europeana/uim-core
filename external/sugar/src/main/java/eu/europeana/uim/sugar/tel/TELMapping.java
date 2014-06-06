/* TheEuropeanLibraryMapping.java - created on Feb 5, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.tel;

import eu.europeana.uim.sugar.soap.client.SugarMapping;
import eu.europeana.uim.sugar.model.RetrievableField;
import eu.europeana.uim.sugar.model.UpdatableField;

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
                TELProviderFields.CONSORTIA_NAME,
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
                TELCollectionFields.LAST_LOADED_DATE,
                TELCollectionFields.COUNTRY,
                TELCollectionFields.LANGUAGE,
                TELCollectionFields.REPOX_SERVER,
                TELCollectionFields.REPOX_PREFIX,
                TELCollectionFields.TEL_ACTIVE_UIM,
                TELCollectionFields.HARVESTING_METHOD,
                TELCollectionFields.METADATA_PROFILE,
                TELCollectionFields.STATUS
        };
    }

    @Override
    public UpdatableField[] getCollectionUpdateableFields() {
        return new UpdatableField[]{
                TELCollectionFields.HARVESTED_RECORDS,
                TELCollectionFields.HARVESTING_STATUS,
                TELCollectionFields.HARVESTING_DATE,
                TELCollectionFields.HARVESTING_UPDATE,
                
                TELCollectionFields.LOAD_RECORDS,
                TELCollectionFields.INDEXED_RECORDS,
                TELCollectionFields.FULLTEXT_OBJECTS,
                TELCollectionFields.DIGITAL_OBJECTS,
                TELCollectionFields.DIGITAL_STORED_OBJECTS,
                TELCollectionFields.LOD_RECORDS,
                
                TELCollectionFields.LAST_LOADED_DATE,
                TELCollectionFields.LAST_LOADED_RECORDS,
                
                TELCollectionFields.LAST_TRANSFORM_DATE,
                TELCollectionFields.LAST_TRANSFORM_RECORDS,
                
                TELCollectionFields.LAST_CLUSTER_DATE,
                TELCollectionFields.LAST_CLUSTER_RECORDS,

                TELCollectionFields.LAST_ENRICH_DATE,
                TELCollectionFields.LAST_ENRICH_RECORDS,

                TELCollectionFields.LAST_ACCEPTANCE_DATE,
                TELCollectionFields.LAST_ACCEPTANCE_RECORDS,
                
                TELCollectionFields.LAST_INDEXED_DATE,
                TELCollectionFields.LAST_INDEXED_RECORDS,
                
                TELCollectionFields.LAST_LOD_DATE,
                TELCollectionFields.LAST_LOD_RECORDS,
                
                TELCollectionFields.LAST_NEWSPAPER_DATE,
                TELCollectionFields.LAST_NEWSPAPER_RECORDS,
                
                TELCollectionFields.LINKCHECK_EXECUTION,
                TELCollectionFields.FIELDCHECK_EXECUTION,
                TELCollectionFields.EDMCHECK_EXECUTION
        };
    }
}
