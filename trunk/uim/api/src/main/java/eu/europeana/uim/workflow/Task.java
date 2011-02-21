package eu.europeana.uim.workflow;

import java.util.Queue;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.StorageEngineException;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public interface Task extends Runnable {

    TaskStatus getStatus();
    void setStatus(TaskStatus status);

    void setUp();
    void tearDown();
    
    void save() throws StorageEngineException;

	void setStep(WorkflowStep step);
	WorkflowStep getStep();
	
	void setOnSuccess(Queue<Task> thisSuccess);
	Queue<Task> getOnSuccess();
	
	void setOnFailure(Queue<Task> failure);
	Queue<Task> getOnFailure();

	void setThrowable(Throwable throwable);
	Throwable getThrowable();

	public MetaDataRecord getMetaDataRecord();
	
}
