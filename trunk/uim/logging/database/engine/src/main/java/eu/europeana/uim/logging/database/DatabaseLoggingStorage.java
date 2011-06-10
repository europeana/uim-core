/* dLoggingStorage.java - created on Apr 27, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.logging.database;

import org.springframework.beans.factory.annotation.Autowired;

import eu.europeana.uim.logging.database.model.TDurationDatabaseEntryHome;
import eu.europeana.uim.logging.database.model.TObjectDatabaseLogEntryHome;
import eu.europeana.uim.logging.database.model.TStringDatabaseLogEntryHome;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Apr 27, 2011
 */
public class DatabaseLoggingStorage {
    @Autowired
    private TStringDatabaseLogEntryHome stringHome;

    @Autowired
    private TObjectDatabaseLogEntryHome objectHome;

    @Autowired
    private TDurationDatabaseEntryHome  durationHome;

    /**
     * Creates a new instance of this class.
     */
    public DatabaseLoggingStorage() {
    }

    /**
     * @return the dao for database log entries
     */
    public TStringDatabaseLogEntryHome getStringHome() {
        return stringHome;
    }

    public void setStringHome(TStringDatabaseLogEntryHome stringHome) {
        this.stringHome = stringHome;
    }

    public TObjectDatabaseLogEntryHome getObjectHome() {
        return objectHome;
    }

    public void setObjectHome(TObjectDatabaseLogEntryHome objectHome) {
        this.objectHome = objectHome;
    }

    public TDurationDatabaseEntryHome getDurationHome() {
        return durationHome;
    }

    public void setDurationHome(TDurationDatabaseEntryHome durationHome) {
        this.durationHome = durationHome;
    }
}
