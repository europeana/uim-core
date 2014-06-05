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

import org.joda.time.DateTime;

import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.model.IngestFrequency;
import eu.europeana.uim.repox.rest.client.HarvestingRepoxRestClient;
import eu.europeana.uim.repox.rest.client.xml.HarvestingStatus;
import eu.europeana.uim.repox.rest.client.xml.Log;
import eu.europeana.uim.repox.rest.client.xml.Response;
import eu.europeana.uim.repox.rest.client.xml.RunningTasks;
import eu.europeana.uim.repox.rest.client.xml.ScheduleTasks;

/**
 * Class implementing REST functionality for accessing record functionality
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since 10.02.2013
 */
public class BasicHarvestingRepoxRestClient extends AbstractRepoxRestClient implements
        HarvestingRepoxRestClient {
    /**
     * Creates a new instance of this class.
     */
    public BasicHarvestingRepoxRestClient() {
        this(null);
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param uri
     *            base uri specifying the repox installation
     */
    public BasicHarvestingRepoxRestClient(String uri) {
        super(uri);
    }

    @Override
    public String initiateHarvesting(String dsID, boolean isfull) throws RepoxException {
        StringBuffer id = new StringBuffer();
        id.append("id=");
        id.append(dsID);
        StringBuffer fullIngest = new StringBuffer();
        fullIngest.append("fullIngest=");
        fullIngest.append(isfull);

        Response resp = invokeRestCall("/dataSources/startIngest", Response.class, id.toString(),
                fullIngest.toString());

        if (resp.getSuccess() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not initiate harvesting! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException(
                        "Unidentified repox error during initiating of harvesting!");
            }
        } else {
            return resp.getSuccess();
        }
    }

    @Override
    public String cancelHarvesting(String dsID) throws RepoxException {
        StringBuffer id = new StringBuffer();
        id.append("id=");
        id.append(dsID);

        Response resp = invokeRestCall("/dataSources/stopIngest", Response.class, id.toString());

        if (resp.getSuccess() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not cancel harvesting! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException("Unidentified repox error during canceling of harvesting!");
            }
        } else {
            return resp.getSuccess();
        }
    }

    @Override
    public String scheduleHarvesting(String dsID, DateTime ingestionDate,
            IngestFrequency frequencyobj, boolean isfull) throws RepoxException {
        StringBuffer id = new StringBuffer();
        StringBuffer firstRunDate = new StringBuffer();
        StringBuffer firstRunHour = new StringBuffer();
        StringBuffer frequency = new StringBuffer();
        StringBuffer xmonths = new StringBuffer();
        StringBuffer fullIngest = new StringBuffer();

        id.append("id=");
        id.append(dsID);

        firstRunDate.append("firstRunDate=");
        firstRunDate.append(ingestionDate.getDayOfMonth() + "-" + ingestionDate.getMonthOfYear() +
                            "-" + ingestionDate.getYear());

        firstRunHour.append("firstRunHour=");
        firstRunHour.append(ingestionDate.getHourOfDay() + ":" + ingestionDate.getMinuteOfHour());

        frequency.append("frequency=");
        frequency.append(frequencyobj.toString());

        xmonths.append("xmonths=");
        // xmonths.append(frequencyobj.toString());

        frequency.append("fullIngest=");
        frequency.append(fullIngest.toString());

        Response resp = invokeRestCall("/dataSources/scheduleIngest", Response.class,
                id.toString(), firstRunDate.toString(), firstRunHour.toString(),
                frequency.toString(), xmonths.toString(), fullIngest.toString());

        if (resp.getSuccess() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not schedule harvesting! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException(
                        "Unidentified repox error during scheduling of harvesting!");
            }
        } else {
            return resp.getSuccess();
        }
    }

    @Override
    public HarvestingStatus getHarvestingStatus(String dsID) throws RepoxException {
        StringBuffer id = new StringBuffer();

        id.append("id=");
        id.append(dsID);

        Response resp = invokeRestCall("/dataSources/harvestStatus", Response.class, id.toString());

        if (resp.getHarvestingStatus() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not retrieve harvesting status! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException(
                        "Unidentified repox error during retrieving of harvesting status!");
            }
        } else {
            return resp.getHarvestingStatus();
        }
    }

    @Override
    public RunningTasks getActiveHarvestingSessions() throws RepoxException {
        Response resp = invokeRestCall("/dataSources/harvesting", Response.class);

        if (resp.getRunningTasks() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not retrieve active harvesting sessions! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException(
                        "Unidentified repox error during retrieving of active harvesting sessions!");
            }
        } else {
            return resp.getRunningTasks();
        }
    }

    @Override
    public ScheduleTasks getScheduledHarvestingSessions(String dsID) throws RepoxException {
        StringBuffer id = new StringBuffer();
        id.append("id=");
        id.append(dsID);

        Response resp = invokeRestCall("/dataSources/scheduleList", Response.class, id.toString());

        if (resp.getScheduleTasks() == null) {
            if (resp.getError() != null) {
                throw new RepoxException(
                        "Could not retrieve scheduled harvesting sessions! Reason: " +
                                resp.getError().getCause());
            } else {
                throw new RepoxException(
                        "Unidentified repox error during retrieving of scheduled harvesting sessions!");
            }
        } else {
            return resp.getScheduleTasks();
        }
    }

    @Override
    public Log getHarvestLog(String dsID) throws RepoxException {
        StringBuffer id = new StringBuffer();

        id.append("id=");
        id.append(dsID);

        Response resp = invokeRestCall("/dataSources/log", Response.class, id.toString());

        if (resp.getLog() == null) {
            if (resp.getError() != null) {
                throw new RepoxException("Could not retrieve harvesting log! Reason: " +
                                         resp.getError().getCause());
            } else {
                throw new RepoxException(
                        "Unidentified repox error during retrieving of harvesting log!");
            }
        } else {
            return resp.getLog();
        }
    }
}
