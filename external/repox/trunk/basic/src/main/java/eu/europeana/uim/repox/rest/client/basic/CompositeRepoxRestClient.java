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
package eu.europeana.uim.repox.rest.client.basic;

import java.util.Date;

import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.model.IngestFrequency;
import eu.europeana.uim.repox.rest.client.AggregatorRepoxRestClient;
import eu.europeana.uim.repox.rest.client.DataSourceFunctionsRepoxRestClient;
import eu.europeana.uim.repox.rest.client.DataSourceRepoxRestClient;
import eu.europeana.uim.repox.rest.client.HarvestingRepoxRestClient;
import eu.europeana.uim.repox.rest.client.ProviderRepoxRestClient;
import eu.europeana.uim.repox.rest.client.RecordRepoxRestClient;
import eu.europeana.uim.repox.rest.client.RepoxRestClient;
import eu.europeana.uim.repox.rest.client.xml.Aggregator;
import eu.europeana.uim.repox.rest.client.xml.Aggregators;
import eu.europeana.uim.repox.rest.client.xml.DataProviders;
import eu.europeana.uim.repox.rest.client.xml.DataSources;
import eu.europeana.uim.repox.rest.client.xml.HarvestingStatus;
import eu.europeana.uim.repox.rest.client.xml.Log;
import eu.europeana.uim.repox.rest.client.xml.Provider;
import eu.europeana.uim.repox.rest.client.xml.RecordResult;
import eu.europeana.uim.repox.rest.client.xml.RunningTasks;
import eu.europeana.uim.repox.rest.client.xml.ScheduleTasks;
import eu.europeana.uim.repox.rest.client.xml.Source;

/**
 * Class implementing REST functionality for accessing the REPOX
 * repository.uikjul.kcv bnm o
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since 10.02.2013
 */
public class CompositeRepoxRestClient implements RepoxRestClient {
	/**
	 * base uri specifying the repox installation
	 */
	private final String uri;

	private final AggregatorRepoxRestClient aggClient;
	private final ProviderRepoxRestClient provClient;
	private final DataSourceRepoxRestClient dsClient;
	private final RecordRepoxRestClient recClient;
	private final HarvestingRepoxRestClient harvClient;
	private final DataSourceFunctionsRepoxRestClient dsFuncClient;

	/**
	 * Creates a new instance of this class.
	 */
	public CompositeRepoxRestClient() {
		this(null);
	}

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param uri
	 *            base uri specifying the repox installation
	 */
	public CompositeRepoxRestClient(String uri) {
		this.uri = uri;

		aggClient = new BasicAggregatorRepoxRestClient(uri);
		provClient = new BasicProviderRepoxRestClient(uri);
		dsClient = new BasicDataSourceRepoxRestClient(uri);
		recClient = new BasicRecordRepoxRestClient(uri);
		harvClient = new BasicHarvestingRepoxRestClient(uri);
		dsFuncClient = new BasicDataSourceFunctionsRepoxRestClient(uri);
	}

	@Override
	public String getUri() {
		return uri;
	}

	@Override
	public Aggregator createAggregator(Aggregator aggregator)
			throws RepoxException {
		return aggClient.createAggregator(aggregator);
	}

	@Override
	public String deleteAggregator(String aggregatorId) throws RepoxException {
		return aggClient.deleteAggregator(aggregatorId);
	}

	@Override
	public Aggregator updateAggregator(Aggregator aggregator)
			throws RepoxException {
		return aggClient.updateAggregator(aggregator);
	}

	@Override
	public Aggregators retrieveAggregators() throws RepoxException {
		return aggClient.retrieveAggregators();
	}

	@Override
	public Aggregator retrieveAggregator(String aggregatorId)
			throws RepoxException {
		return aggClient.retrieveAggregator(aggregatorId);
	}

	@Override
	public Aggregator retrieveAggregatorByMetadata(String mnemonic)
			throws RepoxException {
		return aggClient.retrieveAggregatorByMetadata(mnemonic);
	}

	@Override
	public Provider createProvider(Provider prov, Aggregator agr)
			throws RepoxException {
		return provClient.createProvider(prov, agr);
	}

	@Override
	public String deleteProvider(String provID) throws RepoxException {
		return provClient.deleteProvider(provID);
	}

	@Override
	public Provider updateProvider(Provider prov) throws RepoxException {
		return provClient.updateProvider(prov);
	}

	@Override
	public Provider moveProvider(String providerId, String aggregatorId)
			throws RepoxException {
		return provClient.moveProvider(providerId, aggregatorId);
	}

	@Override
	public DataProviders retrieveProviders() throws RepoxException {
		return provClient.retrieveProviders();
	}

	@Override
	public DataProviders retrieveAggregatorProviders(Aggregator agr)
			throws RepoxException {
		return provClient.retrieveAggregatorProviders(agr);
	}

	@Override
	public Provider retrieveProvider(String providerId) throws RepoxException {
		return provClient.retrieveProvider(providerId);
	}

	@Override
	public Provider retrieveProviderByMetadata(String mnemonic)
			throws RepoxException {
		return provClient.retrieveProviderByMetadata(mnemonic);
	}

	@Override
	public DataSources retrieveDataSources() throws RepoxException {
		return dsClient.retrieveDataSources();
	}

	@Override
	public Source retrieveDataSource(String sourceId) throws RepoxException {
		return dsClient.retrieveDataSource(sourceId);
	}

	@Override
	public Source retrieveDataSourceByMetadata(String mnemonic)
			throws RepoxException {
		return dsClient.retrieveDataSourceByMetadata(mnemonic);
	}

	@Override
	public Source createDatasourceOAI(Source ds, Provider prov)
			throws RepoxException {
		return dsClient.createDatasourceOAI(ds, prov);
	}

	@Override
	public Source createDatasourceZ3950Timestamp(Source ds, Provider prov)
			throws RepoxException {
		return dsClient.createDatasourceZ3950Timestamp(ds, prov);
	}

	@Override
	public Source createDatasourceZ3950IdFile(Source ds, Provider prov)
			throws RepoxException {
		return dsClient.createDatasourceZ3950IdFile(ds, prov);
	}

	@Override
	public Source createDatasourceZ3950IdSequence(Source ds, Provider prov)
			throws RepoxException {
		return dsClient.createDatasourceZ3950IdSequence(ds, prov);
	}

	@Override
	public Source createDatasourceFtp(Source ds, Provider prov)
			throws RepoxException {
		return dsClient.createDatasourceFtp(ds, prov);
	}

	@Override
	public Source createDatasourceHttp(Source ds, Provider prov)
			throws RepoxException {
		return dsClient.createDatasourceHttp(ds, prov);
	}

	@Override
	public Source createDatasourceFolder(Source ds, Provider prov)
			throws RepoxException {
		return dsClient.createDatasourceFolder(ds, prov);
	}

	@Override
	public Source updateDatasourceOAI(Source ds) throws RepoxException {
		return dsClient.updateDatasourceOAI(ds);
	}

	@Override
	public Source updateDatasourceZ3950Timestamp(Source ds)
			throws RepoxException {
		return dsClient.updateDatasourceZ3950Timestamp(ds);
	}

	@Override
	public Source updateDatasourceZ3950IdFile(Source ds) throws RepoxException {
		return dsClient.updateDatasourceZ3950IdFile(ds);
	}

	@Override
	public Source updateDatasourceZ3950IdSequence(Source ds)
			throws RepoxException {
		return dsClient.updateDatasourceZ3950IdSequence(ds);
	}

	@Override
	public Source updateDatasourceFtp(Source ds) throws RepoxException {
		return dsClient.updateDatasourceFtp(ds);
	}

	@Override
	public Source updateDatasourceHttp(Source ds) throws RepoxException {
		return dsClient.updateDatasourceHttp(ds);
	}

	@Override
	public Source updateDatasourceFolder(Source ds) throws RepoxException {
		return dsClient.updateDatasourceFolder(ds);
	}

	@Override
	public String deleteDatasource(String dsID) throws RepoxException {
		return dsClient.deleteDatasource(dsID);
	}

	@Override
	public RecordResult retrieveRecord(String recordID) throws RepoxException {
		return recClient.retrieveRecord(recordID);
	}

	@Override
	public String saveRecord(String recordID, Source ds, String recordXML)
			throws RepoxException {
		return recClient.saveRecord(recordID, ds, recordXML);
	}

	@Override
	public String deleteRecord(String recordID) throws RepoxException {
		return recClient.deleteRecord(recordID);
	}

	@Override
	public String eraseRecord(String recordID) throws RepoxException {
		return recClient.eraseRecord(recordID);
	}

	@Override
	public String initiateHarvesting(String dsID, boolean isfull)
			throws RepoxException {
		return harvClient.initiateHarvesting(dsID, isfull);
	}

	@Override
	public String scheduleHarvesting(String dsID, Date ingestionDate,
			IngestFrequency frequency, boolean isfull) throws RepoxException {
		return harvClient.scheduleHarvesting(dsID, ingestionDate, frequency,
				isfull);
	}

	@Override
	public String cancelHarvesting(String dsID) throws RepoxException {
		return harvClient.cancelHarvesting(dsID);
	}

	@Override
	public HarvestingStatus getHarvestingStatus(String dsID)
			throws RepoxException {
		return harvClient.getHarvestingStatus(dsID);
	}

	@Override
	public RunningTasks getActiveHarvestingSessions() throws RepoxException {
		return harvClient.getActiveHarvestingSessions();
	}

	@Override
	public ScheduleTasks getScheduledHarvestingSessions(String dsID)
			throws RepoxException {
		return harvClient.getScheduledHarvestingSessions(dsID);
	}

	@Override
	public Log getHarvestLog(String dsID) throws RepoxException {
		return harvClient.getHarvestLog(dsID);
	}

	@Override
	public DataSources retrieveProviderDataSources(String providerId)
			throws RepoxException {
		return dsClient.retrieveProviderDataSources(providerId);
	}

	@Override
	public String initializeExport(String datasourceId, int records)
			throws RepoxException {
		return dsFuncClient.initializeExport(datasourceId, records);
	}

	@Override
	public String retrieveRecordCount(String datasourceId)
			throws RepoxException {
		return dsFuncClient.retrieveRecordCount(datasourceId);
	}

	@Override
	public String retrieveLastIngestionDate(String datasourceId)
			throws RepoxException {
		return dsFuncClient.retrieveLastIngestionDate(datasourceId);
	}
}
