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

import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.rest.client.RecordRepoxRestClient;
import eu.europeana.uim.repox.rest.client.xml.RecordResult;
import eu.europeana.uim.repox.rest.client.xml.Source;

/**
 * Class implementing REST functionality for accessing record functionality
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since 10.02.2013
 */
public class BasicRecordRepoxRestClient extends AbstractRepoxRestClient implements
        RecordRepoxRestClient {
    /**
     * Creates a new instance of this class.
     */
    public BasicRecordRepoxRestClient() {
        this(null);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param uri
     *            base uri specifying the repox installation
     */
    public BasicRecordRepoxRestClient(String uri) {
        super(uri);
    }

    @Override
    public RecordResult retrieveRecord(String recordString) throws RepoxException {
        throw new UnsupportedOperationException("Not implemented yet...");
    }

    @Override
    public String saveRecord(String recordID, Source ds, String recordXML) throws RepoxException {
        throw new UnsupportedOperationException("Not implemented yet...");
    }

    @Override
    public String deleteRecord(String recordID) throws RepoxException {
        throw new UnsupportedOperationException("Not implemented yet...");
    }

    @Override
    public String eraseRecord(String recordID) throws RepoxException {
        throw new UnsupportedOperationException("Not implemented yet...");
    }
}
