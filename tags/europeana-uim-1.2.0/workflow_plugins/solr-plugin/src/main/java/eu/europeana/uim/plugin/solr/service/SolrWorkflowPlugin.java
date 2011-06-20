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
package eu.europeana.uim.plugin.solr.service;

import java.util.List;

import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.api.CorruptedMetadataRecordException;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.IngestionPluginFailedException;
import eu.europeana.uim.common.TKey;

/**
 * @author georgiosmarkakis
 *
 */
public class SolrWorkflowPlugin implements IngestionPlugin {

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.IngestionPlugin#getIdentifier()
	 */
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.IngestionPlugin#getName()
	 */
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.IngestionPlugin#getDescription()
	 */
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.IngestionPlugin#getInputFields()
	 */
	public TKey<?, ?>[] getInputFields() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.IngestionPlugin#getOptionalFields()
	 */
	public TKey<?, ?>[] getOptionalFields() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.IngestionPlugin#getOutputFields()
	 */
	public TKey<?, ?>[] getOutputFields() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.IngestionPlugin#initialize()
	 */
	public void initialize() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.IngestionPlugin#shutdown()
	 */
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.IngestionPlugin#getParameters()
	 */
	public List<String> getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.IngestionPlugin#getPreferredThreadCount()
	 */
	public int getPreferredThreadCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.IngestionPlugin#getMaximumThreadCount()
	 */
	public int getMaximumThreadCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.IngestionPlugin#initialize(eu.europeana.uim.api.ExecutionContext)
	 */
	public void initialize(ExecutionContext context)
			throws IngestionPluginFailedException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.IngestionPlugin#completed(eu.europeana.uim.api.ExecutionContext)
	 */
	public void completed(ExecutionContext context)
			throws IngestionPluginFailedException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.IngestionPlugin#processRecord(eu.europeana.uim.MetaDataRecord, eu.europeana.uim.api.ExecutionContext)
	 */
	public boolean processRecord(MetaDataRecord<?> mdr, ExecutionContext context)
			throws IngestionPluginFailedException,
			CorruptedMetadataRecordException {
		// TODO Auto-generated method stub
		return false;
	}

}
