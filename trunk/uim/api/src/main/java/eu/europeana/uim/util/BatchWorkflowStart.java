package eu.europeana.uim.util;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.DataSet;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.workflow.AbstractWorkflowStart;
import eu.europeana.uim.workflow.WorkflowStart;



/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 14, 2011
 */
public class BatchWorkflowStart extends AbstractWorkflowStart implements WorkflowStart {

	/** default batch size 
	 */
	private int batchSize = 250;
	
	/** having a local list of batches is "overhead" - it duplicates
	 * the parent class bath variable. Here it is only used to show
	 * the combination of loader runnable and task runnable.
	 */
	private LinkedList<long[]> idchunks = new LinkedList<long[]>();


	/**
	 * Creates a new instance of this class.
	 */
	public BatchWorkflowStart() {
		super(BatchWorkflowStart.class.getName());
	}

	/**
	 * Creates a new instance of this class.
	 * @param batchSize the size of each batch which is put into the workflow at once.
	 */
	public BatchWorkflowStart(int batchSize) {
		super(BatchWorkflowStart.class.getName());
		this.batchSize = batchSize;
	}


	@Override
	public int getPreferredThreadCount() {
		return 2;
	}

	@Override
	public int getMaximumThreadCount() {
		return 2;
	}


	@Override
	public <T> void initialize(ActiveExecution<T> visitor) throws StorageEngineException {
		try {
			long[] ids;

			DataSet dataSet = visitor.getDataSet();
			if (dataSet instanceof Provider) {
				ids = visitor.getStorageEngine().getByProvider((Provider)dataSet, false);
			} else if (dataSet instanceof Collection) {
				ids = visitor.getStorageEngine().getByCollection((Collection)dataSet);
			} else if (dataSet instanceof Request) {
				ids = visitor.getStorageEngine().getByRequest((Request)dataSet);
			} else if (dataSet instanceof MetaDataRecord) {
				ids = new long[]{((MetaDataRecord)dataSet).getId()};
			} else {
				throw new IllegalStateException("Unsupported dataset <" + visitor.getDataSet() + ">");
			}

			if (ids.length > batchSize) {
				int batches = (int)Math.ceil(1.0 * ids.length / batchSize);
				for (int i = 0; i < batches; i++) {
					int end = Math.min(ids.length, (i + 1) * batchSize);
					int start = i * batchSize;

					long[] batch = new long[end - start];
					System.arraycopy(ids, start, batch, 0, end-start);

					synchronized(this.idchunks) {
						this.idchunks.add(batch);
					}
				}

			} else {
				// adding this to the local queue and 
				// enqueue the batch with a runnable per
				// batch "implements" the way how this api
				// is meant 
				synchronized(this.idchunks) {
					this.idchunks.add(ids);
				}
			}
		} finally {
			setInitialized(true);
		}
	}



	@Override
	public <T> Runnable createLoader(ActiveExecution<T> execution) {
		if (idchunks.isEmpty()) {
			setFinished(true);
			return null;
		}

		return new Runnable(){
			@Override
			public void run() {
				synchronized(idchunks){
					if (!idchunks.isEmpty()) {
						try {
							@SuppressWarnings("unused")
							boolean offer = offer(idchunks.poll(), 500,  TimeUnit.MILLISECONDS);
						} catch (InterruptedException e) {
							// don't really care.
						}
					} else {
						setFinished(true);
					}
				}
			}
		};
	}


    @Override
    public void processRecord(MetaDataRecord mdr, ExecutionContext context) {
    }
}
