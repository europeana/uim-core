/* IngestionPluginFailedException.java - created on Feb 25, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.plugin.source;

import eu.europeana.uim.orchestration.ExecutionContext;

/**
 * This exception is thrown when the {@link WorkflowStart} encounters a major error for this
 * specific {@link ExecutionContext}. In other words, necessary information to fulfill the task of
 * the plugin are corrupted or missing and it is therefore not possible to proceed with the work on
 * any record at all. This exception can also be thrown during initialization and completion of the
 * plugin.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Feb 25, 2011
 */
public class WorkflowStartFailedException extends RuntimeException {
    /**
     * For inheritance reasons, pipes through to the super constructor.
     * 
     * @param message
     *            description of the error
     */
    public WorkflowStartFailedException(String message) {
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
    public WorkflowStartFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
