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
import eu.europeana.uim.repox.rest.client.xml.RecordResult;
import eu.europeana.uim.repox.rest.client.xml.Source;

/**
 * Interface declaration of the Repox REST client OSGI service for record specifics.
 * 
 * @author Georgios Markakis
 * @author Yorgos Mamakis
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since 10.02.2013
 */
public interface RecordRepoxRestClient {
    /**
     * Retrieve a specific Record given an Id
     * 
     * @param recordID
     * @return a RecordResult object
     * @throws RepoxException
     * @throws RepoxException
     */
    RecordResult retrieveRecord(String recordID) throws RepoxException;

    /**
     * Save a Record
     * 
     * @param recordID
     * @param ds
     * @param recordXML
     * @return successful?
     * @throws RepoxException
     */
    String saveRecord(String recordID, Source ds, String recordXML) throws RepoxException;

    /**
     * Marks a Record as eligible for deletion
     * 
     * @param recordID
     * @return successful?
     * @throws RepoxException
     */
    String deleteRecord(String recordID) throws RepoxException;

    /**
     * Erases a Record
     * 
     * @param recordID
     * @return successful?
     * @throws RepoxException
     */
    String eraseRecord(String recordID) throws RepoxException;
}
