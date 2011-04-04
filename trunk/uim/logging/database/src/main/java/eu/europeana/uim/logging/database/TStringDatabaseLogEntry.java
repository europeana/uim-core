/* StringDatabaseLogEntry.java - created on Apr 4, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.logging.database;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Implementation of log entries for messages of simple Strings.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 4, 2011
 */
@Entity
@Table(name = "uim_stringlogentry")
@DiscriminatorValue("1")
public class TStringDatabaseLogEntry extends TDatabaseLogEntry<String> {
    @Column
    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            generic message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
