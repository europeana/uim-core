/* NotConsistentLabelException.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.model;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public class NotConsistentLabelException extends Exception {

    /**
     * Creates a new instance of this class.
     * @param message
     */
    public NotConsistentLabelException(String message) {
        super(message);
    }

    /**
     * Creates a new instance of this class.
     * @param message
     * @param cause
     */
    public NotConsistentLabelException(String message, Throwable cause) {
        super(message, cause);
    }

}
