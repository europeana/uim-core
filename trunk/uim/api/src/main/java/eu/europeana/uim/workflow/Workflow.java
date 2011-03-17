package eu.europeana.uim.workflow;

import java.util.List;

import eu.europeana.uim.api.IngestionPlugin;

/**
 * UIM UIMWorkflow definition
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public interface Workflow {
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
