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
package eu.europeana.uim.repox.rest.client.base;

import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.rest.client.DataSourceFunctionsRepoxRestClient;
import eu.europeana.uim.repox.rest.client.xml.Response;

/**
 * Class implementing REST functionality for functions on top of data sources.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since 10.02.2013
 */
public class BaseDataSourceFunctionsRepoxRestClient extends AbstractRepoxRestClient implements
        DataSourceFunctionsRepoxRestClient {
    /**
     * Creates a new instance of this class.
     */
    public BaseDataSourceFunctionsRepoxRestClient() {
        this(null);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param uri
     *            base uri specifying the repox installation
     */
    public BaseDataSourceFunctionsRepoxRestClient(String uri) {
        super(uri);
    }

    @Override
    public String initializeExport(String datasourceId, int records) throws RepoxException {
        StringBuffer id = new StringBuffer();
        StringBuffer recordsPerFile = new StringBuffer();

        id.append("id=");
        id.append(datasourceId);

        recordsPerFile.append("recordsPerFile=");

        if (records == 0) {
            recordsPerFile.append("ALL");
        } else {
            recordsPerFile.append(records);
        }

        Response resp = invokeRestCall("/dataSources/startExport", Response.class, id.toString(),
                recordsPerFile.toString());

        if (resp.getSuccess() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not initialize export! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during initializing of export!");
            }
        } else {
            return resp.getSuccess();
        }
    }

    @Override
    public String retrieveRecordCount(String datasourceId) throws RepoxException {
        StringBuffer id = new StringBuffer();
        id.append("id=");
        id.append(datasourceId);

        Response resp = invokeRestCall("/dataSources/countRecords", Response.class, id.toString());

        if (resp.getRecordCount() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not retrieve last count of records! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during initializing of export!");
            }
        } else {
            return resp.getRecordCount();
        }
    }

    @Override
    public String retrieveLastIngestionDate(String datasourceId) throws RepoxException {
        StringBuffer id = new StringBuffer();
        id.append("id=");
        id.append(datasourceId);

        Response resp = invokeRestCall("/dataSources/lastIngestionDate", Response.class,
                id.toString());

        if (resp.getLastIngestionDate() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not retrieve last ingestion date! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during initializing of export!");
            }
        } else {
            return resp.getLastIngestionDate();
        }
    }
}
