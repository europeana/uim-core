package eu.europeana.uim.workflow;

import java.util.List;

import eu.europeana.uim.api.IngestionPlugin;


/**
 * UIM UIMWorkflow definition
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public interface Workflow {

    public String getName();

    public String getDescription();

    public WorkflowStart getStart();

    public void setStart(WorkflowStart start);

    public List<IngestionPlugin> getSteps();
    
    public boolean isSavepoint(IngestionPlugin plugin);

}
