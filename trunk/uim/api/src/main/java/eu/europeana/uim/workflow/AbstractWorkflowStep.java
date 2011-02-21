package eu.europeana.uim.workflow;

import java.util.concurrent.ThreadPoolExecutor;

import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.StorageEngineException;

public abstract class AbstractWorkflowStep implements WorkflowStep {

	private final String identifier;
	private final boolean savepoint;
	
	private ThreadPoolExecutor executor;
	
	public AbstractWorkflowStep(String identifier){
		this.identifier = identifier;
		this.savepoint = false;
	}
	
	public AbstractWorkflowStep(String identifier, boolean savepoint){
		this.identifier = identifier;
		this.savepoint = savepoint;
	}
	
	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public boolean isSavepoint() {
		return savepoint;
	}
	

	@Override
	public void setThreadPoolExecutor(ThreadPoolExecutor executor) {
		this.executor = executor;
	}

	@Override
	public ThreadPoolExecutor getThreadPoolExecutor() {
		return executor;
	}

	@Override
	public <T> void initialize(ActiveExecution<T> visitor)  throws StorageEngineException {
	}



}
