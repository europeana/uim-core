package eu.europeana.uim.workflow;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.StorageEngineException;

public class WorkflowStepComposite implements WorkflowStep {

	private final boolean savepoint;
	private ThreadPoolExecutor executor = null;
	
	private List<WorkflowStep> steps = new ArrayList<WorkflowStep>();
	
	public WorkflowStepComposite() {
		this.savepoint = false;
	}
	
	public WorkflowStepComposite(boolean savepoint) {
		this.savepoint = savepoint;
	}
	
	public boolean addStep(WorkflowStep e) {
		if (e.isSavepoint()) throw new IllegalArgumentException("Subpart of composite cannot be savepoint <" + e.getIdentifier() + ">");

		return steps.add(e);
	}

	public boolean removeStep(Object o) {
		return steps.remove(o);
	}

	public void clearSteps() {
		steps.clear();
	}

	@Override
	public boolean isSavepoint() {
		return savepoint;
	}

	@Override
	public <T> void initialize(ActiveExecution<T>  visitor)  throws StorageEngineException {
        for(WorkflowStep s : steps) {
        	s.initialize(visitor);
        }
	}

	
	@Override
	public void processRecord(MetaDataRecord mdr, ExecutionContext context) {
        for(WorkflowStep s : steps) {
        	s.processRecord(mdr, context);
        }
	}

	@Override
	public String getIdentifier() {
        StringBuilder sb = new StringBuilder();
        for(WorkflowStep s : steps) {
        	if (sb.length() > 0) {
        		sb.append(", ");
        	}
            sb.append(s.getIdentifier());
        }
        return "(" + sb.toString() + ")";
	}

	
	@Override
	public int getPreferredThreadCount() {
		int min = 4;
        for(WorkflowStep step : steps) {
        	min = Math.min(step.getPreferredThreadCount(), min);
        }
        return min;
	}

	
	@Override
	public int getMaximumThreadCount() {
		int min = 4;
        for(WorkflowStep step : steps) {
        	min = Math.min(step.getMaximumThreadCount(), min);
        }
        return min;
	}

	@Override
	public void setThreadPoolExecutor(ThreadPoolExecutor executor) {
		this.executor = executor;
        for(WorkflowStep step : steps) {
        	step.setThreadPoolExecutor(executor);
        }
	}

	@Override
	public ThreadPoolExecutor getThreadPoolExecutor() {
		return executor;
	}

	
	
}
