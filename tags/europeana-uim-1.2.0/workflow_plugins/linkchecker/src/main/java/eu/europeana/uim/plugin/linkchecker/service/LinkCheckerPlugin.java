/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package eu.europeana.uim.plugin.linkchecker.service;

import java.util.Collections;
import java.util.List;

import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.api.CorruptedMetadataRecordException;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.IngestionPluginFailedException;
import eu.europeana.uim.common.TKey;

/**
 * This is the main class implementing the UIM functionality for 
 * the linkchecker plugin exposed as an OSGI service.
 * 
 * @author Georgios Markakis
 */
public class LinkCheckerPlugin implements IngestionPlugin {

	
	public boolean processRecord(MetaDataRecord<?> mdr, ExecutionContext context)
	throws IngestionPluginFailedException,
	CorruptedMetadataRecordException {
		
		//First check if link exists
		
		//Then try to save file locally and create 3 different versions of thumbnails
		
		
		//If everything was ok then Log the outcome 
		
       return false;
    }
	
	
	
	public String getName() {
		return null;
	}

	public String getDescription() {
		return "Linck Checker Plugin: an OSGI plugin whose main purpose " +
				"is to check the integrity of http links contained in records";
	}

	public TKey<?, ?>[] getInputFields() {
		// TODO Auto-generated method stub
		return null;
	}

	public TKey<?, ?>[] getOptionalFields() {
	     return new TKey[0];
	}

	public TKey<?, ?>[] getOutputFields() {
	     return new TKey[0];
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	public List<String> getParameters() {
        return Collections.EMPTY_LIST;
	}

	public int getPreferredThreadCount() {
		return 5;
	}

	public int getMaximumThreadCount() {
		return 10;
	}

	public void initialize(ExecutionContext context)
			throws IngestionPluginFailedException {
		// TODO Put initialization code here (Sugarcrm Plugin change state)
		
	}

	public void completed(ExecutionContext context)
			throws IngestionPluginFailedException {
		// TODO Put finalization code here (Sugarcrm Plugin change state)
		
	}



	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}



}
