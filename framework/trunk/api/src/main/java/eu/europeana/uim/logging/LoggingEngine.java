package eu.europeana.uim.logging;

import eu.europeana.uim.logging.modules.EdmLogging;
import eu.europeana.uim.logging.modules.FailureLogging;
import eu.europeana.uim.logging.modules.FieldLogging;
import eu.europeana.uim.logging.modules.GeneralLogging;
import eu.europeana.uim.logging.modules.LinkLogging;
import eu.europeana.uim.logging.modules.TimingLogging;
import eu.europeana.uim.orchestration.ExecutionContext;

/**
 * Service for the reporting of the processing, to be used by the orchestrator and plugins
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 21, 2011
 */
// FIXME: simplify logging eninge, backend should take care of using special tables for special
// loggings like link etc.
public interface LoggingEngine<I> extends GeneralLogging<I>, FailureLogging<I>, TimingLogging<I>,
        FieldLogging<I>, LinkLogging<I>, EdmLogging<I> {
    /**
     * Gets the identifier of this LoggingEngine implementation
     * 
     * @return a unique identifier for the logging engine
     */
    String getIdentifier();

    /**
     * method to ensure flushing to disk at the end of workflows.
     * 
     * @param context
     */
    void completed(ExecutionContext<?, I> context);
}