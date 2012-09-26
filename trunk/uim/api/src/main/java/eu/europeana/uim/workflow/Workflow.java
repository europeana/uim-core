package eu.europeana.uim.workflow;

import java.util.List;

import eu.europeana.uim.plugin.ingestion.IngestionPlugin;
import eu.europeana.uim.plugin.source.WorkflowStart;
import eu.europeana.uim.store.UimDataSet;

/**
 * Definition of a Unified Ingestion Management workflow consisting of a defined
 * {@link WorkflowStart}, followed by an arbitrary number of {@link IngestionPlugin}. Each workflow
 * meaningful name together with a detailled description.
 * 
 * @param <U>
 *            uim data set type
 * @param <I>
 *            generic identifier
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 20, 2011
 */
public interface Workflow<U extends UimDataSet<I>, I> {
    /**
     * @return identifier of the workflow, should be simple classname
     */
    String getIdentifier();

    /**
     * @return name of the workflow, should be reasonable meaningful
     */
    String getName();

    /**
     * @return description of this specific workflow (what does it, what should be the outcome,
     *         etc.)
     */
    String getDescription();

    /**
     * @return defined start point of work flow
     */
    WorkflowStart<U, I> getStart();

    /**
     * @return plugins as steps in this workflow
     */
    List<IngestionPlugin<U, I>> getSteps();

    /**
     * @param pluginIdentifier
     *            unique identifier for ingestion plugins
     * @return plugin with the given identifier or null if not in wf.
     */
    IngestionPlugin<U, I> getStep(String pluginIdentifier);

    /**
     * @param pluginIdentifier
     *            unique identifier for ingestion plugins
     * @return Is this a save point plugin?
     */
    boolean isSavepoint(String pluginIdentifier);

    /**
     * @param pluginIdentifier
     *            unique identifier for ingestion plugins
     * @return Is this a mandatory plugin, so unsuccesful processing is a failure?
     */
    boolean isMandatory(String pluginIdentifier);
}
