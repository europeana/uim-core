/* ObjectDatabaseLogEntry.java - created on Apr 4, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.logging.database;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Implementation of log entries for messages of generic objects.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 4, 2011
 */
@Entity
@Table(name = "uim_objectlogentry")
@DiscriminatorValue("2")
public class TObjectDatabaseLogEntry extends TDatabaseLogEntry<Object> {
    @Column
    private Object message;

    @Override
    public Object getMessage() {
        return message;
    }

    /**
     * @param message
     *            generic message
     */
    public void setMessage(Object message) {
        this.message = message;
    }
}
