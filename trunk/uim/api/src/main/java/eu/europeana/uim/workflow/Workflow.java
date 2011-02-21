package eu.europeana.uim.workflow;

import java.util.List;


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

    public List<WorkflowStep> getSteps();

}
