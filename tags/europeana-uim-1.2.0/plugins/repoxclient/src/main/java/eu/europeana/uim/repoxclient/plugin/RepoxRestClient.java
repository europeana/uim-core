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
package eu.europeana.uim.repoxclient.plugin;

import org.joda.time.DateTime;


import eu.europeana.uim.repoxclient.jibxbindings.Aggregator;
import eu.europeana.uim.repoxclient.jibxbindings.Aggregators;
import eu.europeana.uim.repoxclient.jibxbindings.Provider;
import eu.europeana.uim.repoxclient.jibxbindings.Providers;
import eu.europeana.uim.repoxclient.jibxbindings.DataSources;
import eu.europeana.uim.repoxclient.jibxbindings.DataSource;
import eu.europeana.uim.repoxclient.jibxbindings.Status;
import eu.europeana.uim.repoxclient.jibxbindings.ActiveSessions;
import eu.europeana.uim.repoxclient.jibxbindings.ScheduledSessions;
import eu.europeana.uim.repoxclient.jibxbindings.Harvestlog;

import eu.europeana.uim.repoxclient.jibxbindings.RecordResult;
import eu.europeana.uim.repoxclient.objects.HarvestingType;
import eu.europeana.uim.repoxclient.rest.exceptions.AggregatorOperationException;
import eu.europeana.uim.repoxclient.rest.exceptions.DataSourceOperationException;
import eu.europeana.uim.repoxclient.rest.exceptions.HarvestingOperationException;
import eu.europeana.uim.repoxclient.rest.exceptions.ProviderOperationException;
import eu.europeana.uim.repoxclient.rest.exceptions.RecordOperationException;
import eu.europeana.uim.repoxclient.rest.exceptions.RepoxException;

/**
 * Interface declaration of the Repox REST client OSGI service 
 * 
 * @author Georgios Markakis
 */
public interface RepoxRestClient {

	
	/**
	 * Creates an Aggregator in Repox
	 * 
	 * @param aggregator an Aggregator object 
	 * @throws AggregatorOperationException
	 */
	public void createAggregator(Aggregator aggregator) throws AggregatorOperationException;
	
	
	/**
	 * Deletes an existing Aggregator from Repox
	 * @param aggregator a reference to the Aggregator object
	 * @throws AggregatorOperationException
	 */
	public void deleteAggregator(Aggregator aggregator)throws AggregatorOperationException;	
	
	
	/**
	 * Updates an existing Aggregator in Repox
	 * @param aggregator the Aggregator object to update
	 * @throws AggregatorOperationException
	 */
	public void updateAggregator(Aggregator aggregator)throws AggregatorOperationException;	
	
	
	/**
	 * Retrieves all the available Aggregators from Repox
	 * @return an object containing all available Aggregators 
	 * @throws AggregatorOperationException
	 */
	public Aggregators retrieveAggregators() throws AggregatorOperationException;	
	
	
	/**
	 * Creates a provider in Repox and assigns it to the specific Aggregator
	 * @param prov the Provider definition
	 * @param agr the Aggregator reference
	 * @throws ProviderOperationException
	 */
	public void createProvider(Provider prov,Aggregator agr) throws ProviderOperationException;
	
	
	/**
	 * Creates a provider in Repox
	 * 
	 * @param prov the Provider definition
	 * @throws ProviderOperationException
	 */
	public void createProvider(Provider prov) throws ProviderOperationException;
	
	
	/**
	 * Deletes a provider from Repox
	 * 
	 * @param prov the Provider reference
	 * @throws ProviderOperationException
	 */
	public void deleteProvider(Provider prov) throws ProviderOperationException;
	
	
	/**
	 * Updates a provider within Repox
	 * 
	 * @param the Provider object to update
	 * @throws ProviderOperationException
	 */
	public void updateProvider(Provider prov) throws ProviderOperationException;
	
	
	/**
	 * Retrieves all available providers within Repox
	 * @return an object containing all provider references
	 * @throws ProviderOperationException
	 */
	public Providers retrieveProviders() throws ProviderOperationException;	
	
	/**
	 * Retrieve all available Repox DataSources
	 * 
	 * @return a DataSources object
	 * @throws DataSourceOperationException
	 * @throws RepoxException
	 */
	public DataSources retrieveDataSources() throws DataSourceOperationException;
	
	/**
	 * Create a Repox DataSource 
	 * 
	 * @param ds a DataSource object
	 * @throws DataSourceOperationException
	 * @throws RepoxException
	 */
	public void createDatasource(DataSource ds) throws DataSourceOperationException;
	
	/**
	 * Delete a Repox DataSource
	 * 
	 * @param ds
	 * @throws DataSourceOperationException
	 * @throws RepoxException
	 */
	public void deleteDatasource(DataSource ds) throws DataSourceOperationException;

	/**
	 * Update an existing DataSource
	 * 
	 * @param ds a DataSource object
	 * @throws DataSourceOperationException
	 * @throws RepoxException
	 */
	public void updateDatasource(DataSource ds) throws DataSourceOperationException;
	

	/**
	 * Retrieve a specific Record
	 * 
	 * @param recordString
	 * @return a RecordResult object
	 * @throws RecordOperationException
	 * @throws RepoxException
	 */
	public RecordResult retrieveRecord(String recordString) throws RecordOperationException;
	
	/**
	 * Starts a remote harvesting process  
	 * 
	 * @param type the type of harvesting to perform
	 * @param ds the DataSource to be used 
	 * @return the harvesting processId 
	 * @throws HarvestingOperationException
	 * @throws RepoxException
	 */
	public String initiateHarvesting(HarvestingType type,DataSource ds) throws HarvestingOperationException;
	
	/**
	 * Starts a remote harvesting process at a specific Date (scheduling) 
	 * 
	 * @param type the type of harvesting to perform
	 * @param ds the DataSource to be used 
	 * @param ingestionDate the specific Date upon which the
	 * @return the harvesting processId 
	 * @throws HarvestingOperationException
	 * @throws RepoxException
	 */
	public String initiateHarvesting(HarvestingType type,DataSource ds,DateTime ingestionDate) throws HarvestingOperationException;
	
	
	
	/**
	 * @param ds
	 * @throws HarvestingOperationException
	 */
	public void cancelHarvesting(DataSource ds) throws HarvestingOperationException;
	
	/**
	 * Check the status of an existing harvesting job 
	 * @param ingestionProcessId the harvesting processId 
	 * @return the status
	 * @throws RepoxException
	 */
	public Status getHarvestingStatus(DataSource ds) throws HarvestingOperationException;
	


	/**
	 * Gets a list of Datasources currently being harvested
	 * @return an object containing a reference to all DataSources
	 * @throws HarvestingOperationException
	 */
	public ActiveSessions getActiveHarvestingSessions() throws HarvestingOperationException;
	
	
	/**
	 * Gets a list of Datasources scheduled for harvesting (ingestion)
	 * @return an object containing a reference to all DataSources
	 * @throws HarvestingOperationException
	 */
	public ScheduledSessions getScheduledHarvestingSessions() throws HarvestingOperationException;
	
	
	
	/**
	 * Gets the latest harvesting Log for a specific DataSource
	 * @param ds the DataSource reference
	 * @return the HarvestLog
	 * @throws HarvestingOperationException
	 */
	public Harvestlog getHarvestLog(DataSource ds) throws HarvestingOperationException;
}
