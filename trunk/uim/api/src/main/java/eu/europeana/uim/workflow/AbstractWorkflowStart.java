package eu.europeana.uim.workflow;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.UIMTask;
import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.StorageEngineException;

/** Abstract implementation of a workflow start with convenient methods to manage
 * a queue of batches.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 14, 2011
 */
public abstract class AbstractWorkflowStart implements WorkflowStart {
    private final static Logger log = Logger.getLogger(AbstractWorkflowStart.class.getName());

	private final String identifier;
	private final boolean savepoint;
	
	private ThreadPoolExecutor executor;

	private boolean initialized = false;
	private boolean finished = false;

	private BlockingQueue<long[]> batches = new LinkedBlockingQueue<long[]>();

	
	/**
	 * Creates a new instance of this class. Note that a workflow start is 
	 * per default a savepoint.
	 * 
	 * @param identifier
	 */
	public AbstractWorkflowStart(String identifier){
		this.identifier = identifier;
		this.savepoint = false;
	}
	
	/**
	 * Creates a new instance of this class.
	 * @param identifier
	 * @param savepoint 
	 */
	public AbstractWorkflowStart(String identifier, boolean savepoint){
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
    public int getPreferredThreadCount() {
        return 1;
    }

    @Override
    public int getMaximumThreadCount() {
        return 1;
    }

	
	@Override
	public <T> void initialize(ActiveExecution<T> visitor)  throws StorageEngineException {
	}

	
	@Override
	public <T> boolean isFinished(ActiveExecution<T> execution) {
		return isInitialized() && isFinished();
	}
	

	
	
    @SuppressWarnings("unchecked")
	@Override
	public <T> int createWorkflowTasks(ActiveExecution<T> executor) {
        try {
        	long[] poll = poll(500, TimeUnit.MILLISECONDS);
        	if (poll == null) {
        		// nothing on the queue.
        		if (isFinished()){
        			return 0;
        		}
        	} else {
                MetaDataRecord[] mdrs = executor.getStorageEngine().getMetaDataRecords(poll);
                for (MetaDataRecord mdr : mdrs) {
                    UIMTask task = new UIMTask(mdr, executor.getStorageEngine(), executor);
                    
					executor.getSuccess(this.getIdentifier()).add((T) task);
                }
                return mdrs.length;
            }

        } catch (StorageEngineException e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        } catch (InterruptedException e) {
        	// dont care.
		}
        return 0;
    }
	
    


	/** Method to enqueue a new batch of long ids with the blocking queue.
	 * 
	 * @param e the batch to enqueue
	 * @param timeout the timeout
	 * @param unit the time unit of the timeout
	 * @return true iff sucessfully enqueued
	 * @throws InterruptedException
	 */
	protected boolean offer(long[] e, long timeout, TimeUnit unit)
			throws InterruptedException {
		return batches.offer(e, timeout, unit);
	}

	/** Method to get and remove the first element of the {@link Queue}. 
	 * 
	 * @param timeout the timeout
	 * @param unit the time unit of the timeout
	 * @return the next batch of ids.
	 * @throws InterruptedException
	 */
	protected long[] poll(long timeout, TimeUnit unit) throws InterruptedException {
		return batches.poll(timeout, unit);
	}

	
	/** Getter for the batches blocking queue
	 * @return the queue which is used to create tasks from
	 */
	public BlockingQueue<long[]> getBatches() {
		return batches;
	}

	/**
	 * @return true iff this workflow start has been initialized
	 */
	protected boolean isInitialized() {
		return initialized;
	}

	/** set weather this class is successfully initialized or not.
	 * @param initialized
	 */
	protected void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	/**
	 * @return true iff no new elements are on the queue and the finished flag has been set to true 
	 */
	protected boolean isFinished() {
		return finished && batches.isEmpty();
	}

	/** set the finish flag of this class.
	 * 
	 * @param finished
	 */
	protected void setFinished(boolean finished) {
		this.finished = finished;
	}

    @Override
    public void processRecord(MetaDataRecord mdr, ExecutionContext context) {
    }


}
