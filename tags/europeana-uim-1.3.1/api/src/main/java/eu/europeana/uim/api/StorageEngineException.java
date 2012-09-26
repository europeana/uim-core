package eu.europeana.uim.api;

/**
 * This exception is thrown by all public methods in the {@link StorageEngine}.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public class StorageEngineException extends Exception {
    /**
     * For inheritance reasons, pipes through to the super constructor.
     * 
     * @param message
     *            description of the error
     */
    public StorageEngineException(String message) {
        super(message);
    }

    /**
     * For inheritance reasons, pipes through to the super constructor.
     * 
     * @param message
     *            description of the error
     * @param cause
     *            root cause of the error
     */
    public StorageEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
