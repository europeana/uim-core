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
package eu.europeana.uim.repox;

import java.util.Set;

import eu.europeana.uim.repox.model.RepoxConnectionStatus;
import eu.europeana.uim.repox.model.RepoxHarvestingStatus;
import eu.europeana.uim.repox.model.ScheduleInfo;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;

/**
 * Interface declaration of the Repox REST based OSGI service for UIM. This is basically a wrapper
 * over the previous UIM service which provides UIM specific functionality.
 * 
 * @author Georgios Markakis
 * @since Jan 20, 2012
 */
public interface RepoxUIMService {
    /**
     * This method shows the current connection status of the plugin instance.
     * 
     * @return a ConnectionStatus object
     */
    RepoxConnectionStatus showConnectionStatus();

    /**
     * Checks if an aggregator instance in UIM also exists in Repox
     * 
     * @param countrycode
     *            code of a country
     * @return Exists?
     * @throws AggregatorOperationException
     */
    boolean aggregatorExists(String countrycode) throws AggregatorOperationException;

    /**
     * Creates an Aggregator in Repox
     * 
     * @param countryCode
     *            code of a country
     * @param url
     * @throws AggregatorOperationException
     */
    void createAggregator(String countryCode, String url) throws AggregatorOperationException;

    /**
     * Deletes an existing Aggregator from Repox
     * 
     * @param countryCode
     *            code of a country
     * @throws AggregatorOperationException
     */
    void deleteAggregator(String countryCode) throws AggregatorOperationException;

    /**
     * Updates an existing Aggregator in Repox
     * 
     * @param countryCode
     *            code of a country
     * @param name
     * @param nameCode
     * @param url
     * @throws AggregatorOperationException
     */
    void updateAggregator(String countryCode, String name, String nameCode, String url)
            throws AggregatorOperationException;

    /**
     * Checks if a UIM provider instance already exists in Repox
     * 
     * @param prov
     *            a UIM Provider Object
     * @return exists?
     * @throws ProviderOperationException
     */
    boolean providerExists(Provider<?> prov) throws ProviderOperationException;

    /**
     * Creates a provider in Repox
     * 
     * @param prov
     *            a UIM Provider Object
     * @throws ProviderOperationException
     */
    void createProviderfromUIMObj(Provider<?> prov) throws ProviderOperationException;

    /**
     * Deletes a provider from Repox (the Collection Objects belonging to this provider are deleted
     * as well)
     * 
     * @param prov
     *            a UIM Provider Object
     * @throws ProviderOperationException
     */
    void deleteProviderfromUIMObj(Provider<?> prov) throws ProviderOperationException;

    /**
     * Updates a provider within Repox
     * 
     * @param prov
     *            a UIM Provider Object
     * @throws ProviderOperationException
     */
    void updateProviderfromUIMObj(Provider<?> prov) throws ProviderOperationException;

    /**
     * Retrieves all the available Aggregators from Repox
     * 
     * @return an object containing all available Aggregators
     * @throws AggregatorOperationException
     */
    Set<Provider<?>> retrieveAggregators() throws AggregatorOperationException;

    /**
     * Retrieves all available providers within Repox
     * 
     * @return a List of UIM Provider objects
     * @throws ProviderOperationException
     */
    Set<Provider<?>> retrieveProviders() throws ProviderOperationException;

    /**
     * Retrieve all available Repox DataSources
     * 
     * @return a DataSources object
     * @throws DataSourceOperationException
     */
    Set<Collection<?>> retrieveDataSources() throws DataSourceOperationException;

    /**
     * Checks if a collection is represented as a datasource in Repox
     * 
     * @param col
     *            which collection
     * @return Exists?
     * @throws DataSourceOperationException
     */
    boolean datasourceExists(Collection<?> col) throws DataSourceOperationException;

    /**
     * Create a Repox DataSource from an UIM object. The uim object is also assigned a "repoxid"
     * attribute
     * 
     * @param col
     *            which collection
     * @param prov
     *            which provider
     * @throws DataSourceOperationException
     */
    void createDatasourcefromUIMObj(Collection<?> col, Provider<?> prov)
            throws DataSourceOperationException;

    /**
     * Delete a Repox DataSource
     * 
     * @param col
     *            a UIM Collection object
     * @throws DataSourceOperationException
     */
    void deleteDatasourcefromUIMObj(Collection<?> col) throws DataSourceOperationException;

    /**
     * Update an existing DataSource
     * 
     * @param col
     *            a UIM Collection object
     * @throws DataSourceOperationException
     */
    void updateDatasourcefromUIMObj(Collection<?> col) throws DataSourceOperationException;

    /**
     * Retrieve a specific Record
     * 
     * @param recordString
     * @return a RecordResult object
     * @throws RecordOperationException
     */
    String retrieveRecord(String recordString) throws RecordOperationException;

    /**
     * Starts a remote harvesting process
     * 
     * @param col
     *            Collection to harvest
     * @param isfull
     *            full?
     * @throws HarvestingOperationException
     */
    void initiateHarvestingfromUIMObj(Collection<?> col, boolean isfull)
            throws HarvestingOperationException;

    /**
     * Schedules a remote harvesting process at a specific Date (scheduling)
     * 
     * @param col
     * @param info
     * @throws HarvestingOperationException
     */
    void scheduleHarvestingfromUIMObj(Collection<?> col, ScheduleInfo info)
            throws HarvestingOperationException;

    /**
     * Cancels the active execution of a harvesting session
     * 
     * @param col
     * @throws HarvestingOperationException
     */
    void cancelHarvesting(Collection<?> col) throws HarvestingOperationException;

    /**
     * Check the status of an existing harvesting job
     * 
     * @param col
     *            which collection
     * @return status
     * @throws HarvestingOperationException
     */
    RepoxHarvestingStatus getHarvestingStatus(Collection<?> col)
            throws HarvestingOperationException;

    /**
     * Gets a list of  active Datasources that are currently being harvested
     * 
     * @return an object containing a reference to all DataSources
     * @throws HarvestingOperationException
     */
    Set<Collection<?>> getActiveHarvestingSessions() throws HarvestingOperationException;

    /**
     * Gets a list of UIM Collections scheduled for harvesting (ingestion)
     * 
     * @param coll
     * @return a List of UIM Collection object references
     * @throws HarvestingOperationException
     */
    Set<ScheduleInfo> getScheduledHarvestingSessions(Collection<?> coll)
            throws HarvestingOperationException;

    /**
     * Gets the latest harvesting Log for a specific DataSource
     * 
     * @param col
     *            a UIM Collection object reference
     * @return the HarvestLog
     * @throws HarvestingOperationException
     */
    String getHarvestLog(Collection<?> col) throws HarvestingOperationException;

    /**
     * Initialize the export of Repox files
     * 
     * @param col
     *            the UIM collection
     * @param numberOfRecords
     *            the number of records per file
     * @throws HarvestingOperationException
     */
    void initializeExport(Collection<?> col, int numberOfRecords)
            throws HarvestingOperationException;
    
    
    /**
     * An auxiliary method indicating whether the type of the datasource has been changed or not
     * @param col
     * @return the UIM collection
     * @throws DataSourceOperationException
     */
    boolean hasHarvestingTypeChanged(Collection<?> col) throws DataSourceOperationException;
    
}
