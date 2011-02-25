/* IngestionPluginFailedException.java - created on Feb 25, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.api;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 25, 2011
 */
public class IngestionPluginFailedException extends RuntimeException {

    /**
     * Creates a new instance of this class.
     * @param message
     */
    public IngestionPluginFailedException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of this class.
     * @param message
     * @param cause
     */
    public IngestionPluginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

}
