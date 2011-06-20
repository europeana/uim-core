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
package eu.europeana.uim.repoxclient.rest;

import java.util.List;

import org.joda.time.DateTime;

import eu.europeana.uim.api.Orchestrator;
import eu.europeana.uim.api.Registry;
import eu.europeana.uim.repoxclient.jibxbindings.Harvestlog;
import eu.europeana.uim.repoxclient.jibxbindings.RecordResult;
import eu.europeana.uim.repoxclient.jibxbindings.Status;
import eu.europeana.uim.repoxclient.objects.HarvestingType;
import eu.europeana.uim.repoxclient.plugin.RepoxRestClient;
import eu.europeana.uim.repoxclient.plugin.RepoxUIMService;
import eu.europeana.uim.repoxclient.rest.exceptions.AggregatorOperationException;
import eu.europeana.uim.repoxclient.rest.exceptions.DataSourceOperationException;
import eu.europeana.uim.repoxclient.rest.exceptions.HarvestingOperationException;
import eu.europeana.uim.repoxclient.rest.exceptions.ProviderOperationException;
import eu.europeana.uim.repoxclient.rest.exceptions.RecordOperationException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;

/**
 * 
 * 
 * @author Georgios Markakis
 */
public class RepoxUIMServiceImpl implements RepoxUIMService {

	private RepoxRestClient repoxRestClient;
	private Orchestrator orchestrator;
	private Registry registry;
	
	@Override
	public boolean aggregatorExists(Provider provider)
			throws AggregatorOperationException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createAggregatorfromUIMObj(Provider provider,
			boolean isRecursive) throws AggregatorOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAggregatorfromUIMObj(Provider provider)
			throws AggregatorOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAggregatorfromUIMObj(Provider provider)
			throws AggregatorOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Provider> retrieveAggregators()
			throws AggregatorOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void providerExists(Provider prov) throws ProviderOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createProviderfromUIMObj(Provider prov, Provider agr,
			boolean isRecursive) throws ProviderOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createProviderfromUIMObj(Provider prov, boolean isRecursive)
			throws ProviderOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteProviderfromUIMObj(Provider prov)
			throws ProviderOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateProviderfromUIMObj(Provider prov)
			throws ProviderOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Provider> retrieveProviders() throws ProviderOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Collection> retrieveDataSources()
			throws DataSourceOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void datasourceExists(Collection col)
			throws DataSourceOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createDatasourcefromUIMObj(Collection col, Provider prov)
			throws DataSourceOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteDatasourcefromUIMObj(Collection col)
			throws DataSourceOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateDatasourcefromUIMObj(Collection col)
			throws DataSourceOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RecordResult retrieveRecord(String recordString)
			throws RecordOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String initiateHarvestingfromUIMObj(HarvestingType type,
			Collection col) throws HarvestingOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String initiateHarvestingfromUIMObj(HarvestingType type,
			Collection col, DateTime ingestionDate)
			throws HarvestingOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancelHarvesting(Collection col)
			throws HarvestingOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Status getHarvestingStatus(Collection col)
			throws HarvestingOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Collection> getActiveHarvestingSessions()
			throws HarvestingOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Collection> getScheduledHarvestingSessions()
			throws HarvestingOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Harvestlog getHarvestLog(Collection col)
			throws HarvestingOperationException {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	/*
	 * Getters & Setters 
	 */
	
	public void setRepoxRestClient(RepoxRestClient repoxRestClient) {
		this.repoxRestClient = repoxRestClient;
	}

	public RepoxRestClient getRepoxRestClient() {
		return repoxRestClient;
	}

	public void setOrchestrator(Orchestrator orchestrator) {
		this.orchestrator = orchestrator;
	}

	public Orchestrator getOrchestrator() {
		return orchestrator;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	public Registry getRegistry() {
		return registry;
	}

	

}
