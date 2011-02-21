package eu.europeana.uim.workflow;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractWorkflow implements Workflow {

    private String name;
    private String description;
    
    private WorkflowStart start;
    
    private List<WorkflowStep> steps = new LinkedList<WorkflowStep>();

    public AbstractWorkflow(String name, String description) {
    	this.name = name;
    	this.description = description;
    }

    public AbstractWorkflow(String name, String description, WorkflowStart start) {
    	this.name = name;
    	this.description = description;
    	this.start = start;
    }

    
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public List<WorkflowStep> getSteps() {
        return this.steps;
    }

	@Override
	public WorkflowStart getStart() {
		return start;
	}    

	@Override
	public void setStart(WorkflowStart start) {
		this.start = start;
	}
	
	
	protected void setName(String name) {
		this.name = name;
	}


	protected void setDescription(String description) {
		this.description = description;
	}


	
	protected void addStep(WorkflowStep step) {
		steps.add(step);
	}


	
}
