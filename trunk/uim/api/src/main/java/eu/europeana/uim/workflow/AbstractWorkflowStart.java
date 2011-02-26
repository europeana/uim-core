package eu.europeana.uim.workflow;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.MDRFieldRegistry;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;
import eu.europeana.uim.UIMTask;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;

/** Abstract implementation of a workflow start with convenient methods to manage
 * a queue of batches.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 14, 2011
 */
public abstract class AbstractWorkflowStart implements WorkflowStart {
    private final static Logger log = Logger.getLogger(AbstractWorkflowStart.class.getName());

    private static TKey<AbstractWorkflowStart, LinkedBlockingQueue> BATCHES = TKey.register(AbstractWorkflowStart.class, "batches", LinkedBlockingQueue.class);

    @Override
    public int getPreferredThreadCount() {
        return 1;
    }

    @Override
    public int getMaximumThreadCount() {
        return 1;
    }

    
    @Override
    public void initialize(ExecutionContext context)  throws StorageEngineException {
    }

    
    @Override
    public void initialize(ExecutionContext context, StorageEngine storage)  throws StorageEngineException {
        context.putValue(BATCHES, new LinkedBlockingQueue<long[]>());
    }

	
	@Override
	public boolean isFinished(ExecutionContext context, StorageEngine storage) {
	    BlockingQueue<long[]> batches = context.getValue(BATCHES);
	    // not finished because not initialized yet
	    if (batches == null) return false;

		return batches.isEmpty();
	}
	

	
	
    @SuppressWarnings("unchecked")
	@Override
	public Task[] createWorkflowTasks(ExecutionContext context, StorageEngine storage) {
        try {
        	long[] poll = getBatches(context).poll(500, TimeUnit.MILLISECONDS);
        	if (poll == null) {
        		// nothing on the queue.
        		if (isFinished(context, storage)){
        			return new Task[0];
        		}
        	} else {
                try {
                    MetaDataRecord[] mdrs = storage.getMetaDataRecords(poll);

                    Task[] tasks = new Task[mdrs.length];
                    for (int i = 0; i < mdrs.length; i++) {
                        MetaDataRecord mdr = mdrs[i];
                        tasks[i] = new UIMTask(mdr, storage, context);
                    }
                    return tasks;
                } catch (Throwable e) {
                    log.log(Level.SEVERE, "Failed to create uim task.", e);
                }
            }

        } catch (InterruptedException e) {
        	// dont care.
		}
        return new Task[0];
    }
	
    
    @Override
    public Runnable createLoader(ExecutionContext context, StorageEngine storage) {
        // all done
        return null;
    }


    @Override
    public List<String> getParameters() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public TKey<MDRFieldRegistry, ?>[] getInputFields() {
        return new TKey[]{};
    }


    @Override
    public TKey<MDRFieldRegistry, ?>[] getOutputFields() {
        return new TKey[]{};
    }

	
    @Override
    public void processRecord(MetaDataRecord mdr, ExecutionContext context) {
    }

    /** Getter for the batches blocking queue
     * @return the queue which is used to create tasks from
     */
    public BlockingQueue<long[]> getBatches(ExecutionContext context) {
        BlockingQueue<long[]> batches = context.getValue(BATCHES);
        return batches;
    }

}
