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

import org.joda.time.DateTime;

import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.model.IngestFrequency;
import eu.europeana.uim.repox.rest.client.xml.HarvestingStatus;
import eu.europeana.uim.repox.rest.client.xml.Log;
import eu.europeana.uim.repox.rest.client.xml.RunningTasks;
import eu.europeana.uim.repox.rest.client.xml.ScheduleTasks;

/**
 * Interface declaration of the Repox REST client OSGI service for harvesting specifics.
 * 
 * @author Georgios Markakis
 * @author Yorgos Mamakis
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since 10.02.2013
 */
public interface HarvestingRepoxRestClient {
    /**
     * Initializes a Harvesting Session. It accesses the following REST Interface:
     * 
     * <code>
     *   /rest/dataSources/startIngest?id=bmfinancas
     *  </code>
     * 
     * @param dsID
     *            the DataSource to be used
     * @param isfull
     *            full?
     * @return the harvesting processId
     * @throws RepoxException
     * @throws RepoxException
     */
    String initiateHarvesting(String dsID, boolean isfull) throws RepoxException;

    /**
     * Schedule a Harvesting Session. It accesses the following REST Interface:
     * 
     * <code>
     *      /rest/dataSources/scheduleIngest?id=bmfinancas&
     *      firstRunDate=06-07-2011&
     *      firstRunHour=17:43&
     *      frequency=Daily&
     *      xmonths=&
     *      fullIngest=true
     *  </code>
     * 
     * @param dsID
     *            the DataSource to be used
     * @param ingestionDate
     *            the specific Date upon which the
     * @param frequency
     *            frequency
     * @param isfull
     *            full?
     * @return the harvesting processId
     * @throws RepoxException
     */
    String scheduleHarvesting(String dsID, DateTime ingestionDate, IngestFrequency frequency,
            boolean isfull) throws RepoxException;

    /**
     * Cancels an existing Harvesting Session. It accesses the following REST Interface:
     * 
     * <code>
     *   /rest/dataSources/stopIngest?id=bmfinancas
     *  </code>
     * 
     * @param dsID
     *            the DataSource to be used
     * @return successful?
     * @throws RepoxException
     */
    String cancelHarvesting(String dsID) throws RepoxException;

    /**
     * Gets the status for a Harvesting Session. It accesses the following REST Interface:
     * 
     * <code>
     *    /rest/dataSources/harvestStatus?id=httpTest
     *  </code>
     * 
     * @param dsID
     *            the harvesting processId
     * @return the status
     * @throws RepoxException
     */
    HarvestingStatus getHarvestingStatus(String dsID) throws RepoxException;

    /**
     * Gets the Active Harvesting Sessions. It accesses the following REST Interface:
     * 
     * <code>
     *    /rest/dataSources/harvesting
     *  </code>
     * 
     * @return an object containing a reference to all DataSources
     * @throws RepoxException
     */
    RunningTasks getActiveHarvestingSessions() throws RepoxException;

    /**
     * Gets the Scheduled Harvesting Sessions for a DataSource. It accesses the following REST
     * Interface:
     * 
     * <code>
     *    /rest/dataSources/scheduleList?id=bmfinancas
     *  </code>
     * 
     * @param dsID
     * @return an object containing a reference to all DataSources
     * @throws RepoxException
     */
    ScheduleTasks getScheduledHarvestingSessions(String dsID) throws RepoxException;

    /**
     * Gets the latest Harvesting Log record for a DataSource. It accesses the following REST
     * Interface:
     * 
     * <code>
     *    /rest/dataSources/log?id=httpTest
     *  </code>
     * 
     * @param dsID
     *            the DataSource reference
     * @return the HarvestLog
     * @throws RepoxException
     */
    Log getHarvestLog(String dsID) throws RepoxException;
}
