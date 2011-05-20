package eu.europeana.uim.workflow;

import java.util.List;

import eu.europeana.uim.api.IngestionPlugin;

/**
 * Definition of a Unified Ingestion Management workflow consisting of a defined
 * {@link WorkflowStart}, followed by an arbitrary number of {@link IngestionPlugin}. Each workflow
 * provides an identifier (fully class name for example) to recognize them, furthermore a more
 * meaningful name together with a detailled description.
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 20, 2011
 */
public interface Workflow {
// /**
// * @return unique identifier of this workflow (suggestions is full class name)
// */
// String getIdentifier();

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
    WorkflowStart getStart();

    /**
     * @return plugins as steps in this workflow
     */
    List<IngestionPlugin> getSteps();

    /**
     * @param pluginName
     * @return Is this a save point plugin?
     */
    boolean isSavepoint(String pluginName);

    /**
     * @param pluginName
     * @return Is this a mandatory plugin, so unsuccesful processing is a failure?
     */
    boolean isMandatory(String pluginName);
}
