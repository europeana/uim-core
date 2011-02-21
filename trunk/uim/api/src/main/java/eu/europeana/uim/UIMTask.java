package eu.europeana.uim;

import java.util.Queue;

import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.workflow.Task;
import eu.europeana.uim.workflow.TaskStatus;
import eu.europeana.uim.workflow.WorkflowStep;

/** A uim standard implementation of a workflow task. This task
 * is passed through the workflow and setup before each workflow step by
 * the framework.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public class UIMTask implements Task {

	private TaskStatus status = TaskStatus.NEW;
	private Throwable throwable;
	
	private Queue<Task> success = null;
	private Queue<Task> failure = null;
	
	private WorkflowStep step;
	
	private final MetaDataRecord record;
    private final StorageEngine engine;
    private final ExecutionContext context;

	/**
	 * Creates a new instance of this class.
	 * @param record
	 * @param engine
	 * @param context
	 */
	public UIMTask(MetaDataRecord record, StorageEngine engine, ExecutionContext context) {
		super();
		this.record = record;
        this.engine = engine;
        this.context = context;
	}

	
	@Override
	public void run() {
		step.processRecord(record, context);
	}

	@Override
	public TaskStatus getStatus() {
		return status;
	}

	@Override
	public void setStatus(TaskStatus status) {
		this.status = status;
	}


	@Override
	public void setUp() {
	}
	
	

	@Override
	public void tearDown() {
	}
	
	
	


	@Override
	public void save() throws StorageEngineException {
		engine.updateMetaDataRecord(record);
	}


	@Override
	public WorkflowStep getStep() {
		return step;
	}


	@Override
	public void setStep(WorkflowStep step) {
		this.step = step;
	}

	@Override
	public void setOnSuccess(Queue<Task> success) {
		this.success = success;
	}

	@Override
	public Queue<Task> getOnSuccess() {
		return success;
	}

	@Override
	public void setOnFailure(Queue<Task> failure) {
		this.failure = failure;
	}

	@Override
	public Queue<Task> getOnFailure() {
		return failure;
	}

	@Override
	public void setThrowable(Throwable throwable) {
		this.throwable =throwable;
	}

	@Override
	public Throwable getThrowable() {
		return throwable;
	}

	@Override
	public MetaDataRecord getMetaDataRecord() {
		return record;
	}
	
}
