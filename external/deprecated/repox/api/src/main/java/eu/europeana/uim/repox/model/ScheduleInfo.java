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
package eu.europeana.uim.repox.model;

import org.joda.time.DateTime;

/**
 * Scheduler information object
 * 
 * @author Georgios Markakis
 * @since Jan 20, 2012
 */
public class ScheduleInfo {
    private DateTime        datetime;
    private IngestFrequency frequency;
    private boolean         fullingest;

    /**
     * @param frequency
     *            the frequency to set
     */
    public void setFrequency(IngestFrequency frequency) {
        this.frequency = frequency;
    }

    /**
     * @return the frequency
     */
    public IngestFrequency getFrequency() {
        return frequency;
    }

    /**
     * @param datetime
     *            the datetime to set
     */
    public void setDatetime(DateTime datetime) {
        this.datetime = datetime;
    }

    /**
     * @return the datetime
     */
    public DateTime getDatetime() {
        return datetime;
    }

    /**
     * @param fullingest
     *            the fullingest to set
     */
    public void setFullingest(boolean fullingest) {
        this.fullingest = fullingest;
    }

    /**
     * @return the fullingest
     */
    public boolean isFullingest() {
        return fullingest;
    }
}
