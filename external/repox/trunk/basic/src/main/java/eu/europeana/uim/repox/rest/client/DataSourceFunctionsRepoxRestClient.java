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
package eu.europeana.uim.repox.rest.client;

import eu.europeana.uim.repox.RepoxException;

/**
 * Interface declaration of the Repox REST client OSGI service for record specifics.
 * 
 * @author Georgios Markakis
 * @author Yorgos Mamakis
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since 10.02.2013
 */
public interface DataSourceFunctionsRepoxRestClient {
    /**
     * Initializes the export of records
     * 
     * <code>
     *    /rest/dataSources/startExport?id=bmfinancas&recordsPerFile=1000
     *  </code>
     * 
     * @param datasourceId
     *            the DataSource reference
     * @param records
     *            no of records per file
     * @return successful?
     * @throws RepoxException
     */
    String initializeExport(String datasourceId, int records) throws RepoxException;

    /**
     * Retrieves number of records for a specific dataset.
     * 
     * <code>
     * /rest/dataSources/countRecords?id=bmfinancas
     *  </code>
     * 
     * @param datasourceId
     *            identifier of data source
     * @return number of records
     * @throws RepoxException
     */
    String retrieveRecordCount(String datasourceId) throws RepoxException;

    /**
     * Retrieves last ingestion date for a specific dataset.
     * 
     * <code>
     *     /rest/dataSources/lastIngestionDate?id=bmfinancas
     *  </code>
     * 
     * @param datasourceId
     * @return date of last ingestion
     * @throws RepoxException
     */
    String retrieveLastIngestionDate(String datasourceId) throws RepoxException;
}
