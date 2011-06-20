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

import org.joda.time.DateTime;
import org.springframework.web.client.RestTemplate;

import eu.europeana.uim.repoxclient.jibxbindings.ActiveSessions;
import eu.europeana.uim.repoxclient.jibxbindings.Aggregator;
import eu.europeana.uim.repoxclient.jibxbindings.Aggregators;
import eu.europeana.uim.repoxclient.jibxbindings.DataSource;
import eu.europeana.uim.repoxclient.jibxbindings.DataSources;
import eu.europeana.uim.repoxclient.jibxbindings.Harvestlog;
import eu.europeana.uim.repoxclient.jibxbindings.Provider;
import eu.europeana.uim.repoxclient.jibxbindings.Providers;
import eu.europeana.uim.repoxclient.jibxbindings.RecordResult;
import eu.europeana.uim.repoxclient.jibxbindings.Response;
import eu.europeana.uim.repoxclient.jibxbindings.ScheduledSessions;
import eu.europeana.uim.repoxclient.jibxbindings.Status;

import eu.europeana.uim.repoxclient.objects.HarvestingType;
import eu.europeana.uim.repoxclient.plugin.RepoxRestClient;
import eu.europeana.uim.repoxclient.rest.exceptions.AggregatorOperationException;
import eu.europeana.uim.repoxclient.rest.exceptions.DataSourceOperationException;
import eu.europeana.uim.repoxclient.rest.exceptions.HarvestingOperationException;
import eu.europeana.uim.repoxclient.rest.exceptions.ProviderOperationException;
import eu.europeana.uim.repoxclient.rest.exceptions.RecordOperationException;
import eu.europeana.uim.repoxclient.rest.exceptions.RepoxException;



/**
 * Class implementing REST functionality for accessing the REPOX repository.
 * 
 * @author Georgios Markakis
 */
public class RepoxRestClientImpl  implements RepoxRestClient {

	private RestTemplate restTemplate;

	private String defaultURI;

	
	/**
	 * 
	 * @return DataSources the available datasources
	 * @throws RepoxException
	 */
	public DataSources retrieveDataSources() throws DataSourceOperationException {

		Response resp = invokRestTemplate("listDataSources", "listDataSources",
				Response.class);

		if (resp.getDataSources() == null) {
			if (resp.getError() != null) {
				throw new DataSourceOperationException(resp.getError());
			} else {
				throw new DataSourceOperationException("Unidentified Repox Error");
			}
		} else {

			return resp.getDataSources();
		}
	}
	
	
	// Public methods exposed by the service
	
	public void createDatasource(DataSource ds)
			throws DataSourceOperationException {


	}

	public void deleteDatasource(DataSource ds)
			throws DataSourceOperationException {


	}

	public void updateDatasource(DataSource ds)
			throws DataSourceOperationException {


	}

	public RecordResult retrieveRecord(String recordString)
			throws RecordOperationException{

		return null;
	}

	public String initiateHarvesting(HarvestingType type, DataSource ds)
			throws HarvestingOperationException{

		return null;
	}

	public String initiateHarvesting(HarvestingType type, DataSource ds,
			DateTime ingestionDate) throws HarvestingOperationException{

		return null;
	}

	public String harvestingStatus(String ingestionProcessId)
			throws RepoxException {

		return null;
	}



	// Private Methods

	/**
	 * Auxiliary method for invoking a REST operation
	 * 
	 * @param <S>
	 *            the return type
	 * @param wsOperation
	 * @return
	 */
	private <S> S invokRestTemplate(String restOperation, String param,
			Class<S> responseClass) {

		StringBuffer operation = new StringBuffer();
		operation.append(defaultURI);
		operation.append("?operation={param}");

		String restResponseObj = restTemplate.getForObject(
				operation.toString(), String.class, restOperation);
		S restResponse = (S) restTemplate.getForObject(operation.toString(),
				responseClass, restOperation);

		return restResponse;
	}

	// Getters & Setters

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setDefaultURI(String defaultURI) {
		this.defaultURI = defaultURI;
	}

	public String getDefaultURI() {
		return defaultURI;
	}


	@Override
	public void createAggregator(Aggregator aggregator)
			throws AggregatorOperationException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deleteAggregator(Aggregator aggregator)
			throws AggregatorOperationException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void updateAggregator(Aggregator aggregator)
			throws AggregatorOperationException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Aggregators retrieveAggregators()
			throws AggregatorOperationException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void createProvider(Provider prov, Aggregator agr)
			throws ProviderOperationException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void createProvider(Provider prov) throws ProviderOperationException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void deleteProvider(Provider prov) throws ProviderOperationException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void updateProvider(Provider prov) throws ProviderOperationException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Providers retrieveProviders() throws ProviderOperationException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void cancelHarvesting(DataSource ds)
			throws HarvestingOperationException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Status getHarvestingStatus(DataSource ds)
			throws HarvestingOperationException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ActiveSessions getActiveHarvestingSessions()
			throws HarvestingOperationException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ScheduledSessions getScheduledHarvestingSessions()
			throws HarvestingOperationException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Harvestlog getHarvestLog(DataSource ds)
			throws HarvestingOperationException {
		// TODO Auto-generated method stub
		return null;
	}

}
