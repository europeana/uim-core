/* dLoggingStorage.java - created on Apr 27, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.logging.database;

import org.springframework.beans.factory.annotation.Autowired;

import eu.europeana.uim.logging.database.model.TLogEntryDurationHome;
import eu.europeana.uim.logging.database.model.TLogEntryEdmCheckHome;
import eu.europeana.uim.logging.database.model.TLogEntryFailedHome;
import eu.europeana.uim.logging.database.model.TLogEntryHome;
import eu.europeana.uim.logging.database.model.TLogEntryLinkHome;
import eu.europeana.uim.logging.database.model.TLogEntryFieldHome;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Apr 27, 2011
 */
public class DatabaseLoggingStorage {
    @Autowired
    private TLogEntryHome         logHome;

    @Autowired
    private TLogEntryFailedHome   logFailedHome;

    @Autowired
    private TLogEntryLinkHome     logLinkHome;

    @Autowired
    private TLogEntryFieldHome    logFieldHome;

    @Autowired
    private TLogEntryDurationHome logDurationHome;

    @Autowired
    private TLogEntryEdmCheckHome logEdmCheckHome;

    /**
     * Creates a new instance of this class.
     */
    public DatabaseLoggingStorage() {
    }

    /**
     * Returns the logHome.
     * 
     * @return the logHome
     */
    public TLogEntryHome getLogHome() {
        return logHome;
    }

    /**
     * Sets the logHome to the given value.
     * 
     * @param logHome
     *            the logHome to set
     */
    public void setLogHome(TLogEntryHome logHome) {
        this.logHome = logHome;
    }

    /**
     * Returns the logFailedHome.
     * 
     * @return the logFailedHome
     */
    public TLogEntryFailedHome getLogFailedHome() {
        return logFailedHome;
    }

    /**
     * Sets the logFailedHome to the given value.
     * 
     * @param logFailedHome
     *            the logFailedHome to set
     */
    public void setLogFailedHome(TLogEntryFailedHome logFailedHome) {
        this.logFailedHome = logFailedHome;
    }

    /**
     * Returns the logLinkHome.
     * 
     * @return the logLinkHome
     */
    public TLogEntryLinkHome getLogLinkHome() {
        return logLinkHome;
    }

    /**
     * Returns the logLinkHome.
     * 
     * @return the logLinkHome
     */
    public TLogEntryFieldHome getLogFieldHome() {
        return logFieldHome;
    }

    /**
     * Sets the logLinkHome to the given value.
     * 
     * @param logLinkHome
     *            the logLinkHome to set
     */
    public void setLogLinkHome(TLogEntryLinkHome logLinkHome) {
        this.logLinkHome = logLinkHome;
    }

    /**
     * Returns the logDurationHome.
     * 
     * @return the logDurationHome
     */
    public TLogEntryDurationHome getLogDurationHome() {
        return logDurationHome;
    }

    /**
     * Sets the logDurationHome to the given value.
     * 
     * @param logDurationHome
     *            the logDurationHome to set
     */
    public void setLogDurationHome(TLogEntryDurationHome logDurationHome) {
        this.logDurationHome = logDurationHome;
    }

    /**
     * Returns the logEdmCheckHome.
     * 
     * @return the logEdmCheckHome
     */
    public final TLogEntryEdmCheckHome getLogEdmCheckHome() {
        return logEdmCheckHome;
    }

    /**
     * Sets the logEdmCheckHome
     * 
     * @param logEdmCheckHome
     *            the logEdmCheckHome to set
     */
    public final void setLogEdmCheckHome(TLogEntryEdmCheckHome logEdmCheckHome) {
        this.logEdmCheckHome = logEdmCheckHome;
    }

}
