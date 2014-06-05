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
package eu.europeana.uim.deprecated.model;

/**
 * Container Class for Harvesting Status Info
 * 
 * @author Georgios MarkakisS
 * @since Jan 20, 2012
 */
public class RepoxHarvestingStatus {
    private HarvestingState status;
    private String          timeLeft;
    private String          percentage;
    private String          records;

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(HarvestingState status) {
        this.status = status;
    }

    /**
     * @return the status
     */
    public HarvestingState getStatus() {
        return status;
    }

    /**
     * @param timeLeft
     *            the timeLeft to set
     */
    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }

    /**
     * @return the timeLeft
     */
    public String getTimeLeft() {
        return timeLeft;
    }

    /**
     * @param percentage
     *            the percentage to set
     */
    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    /**
     * @return the percentage
     */
    public String getPercentage() {
        return percentage;
    }

    /**
     * @param records
     *            the records to set
     */
    public void setRecords(String records) {
        this.records = records;
    }

    /**
     * @return the records
     */
    public String getRecords() {
        return records;
    }
}
