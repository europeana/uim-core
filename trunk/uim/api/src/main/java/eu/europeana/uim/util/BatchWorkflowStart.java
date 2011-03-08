package eu.europeana.uim.util;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.DataSet;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.workflow.Task;
import eu.europeana.uim.workflow.TaskCreator;
import eu.europeana.uim.workflow.WorkflowStart;
import eu.europeana.uim.workflow.WorkflowStartFailedException;

/**
 * Loads batches from the storage and pulls them into as tasks.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 14, 2011
 */
public class BatchWorkflowStart implements WorkflowStart {
    /**
     * Key to retrieve own data from context.
     */
    private static TKey<BatchWorkflowStart, Data> DATA_KEY   = TKey.register(
                                                                     BatchWorkflowStart.class,
                                                                     "data", Data.class);

    /**
     * default batch size
     */
    public static int                             BATCH_SIZE = 250;

    /**
     * Creates a new instance of this class.
     */
    public BatchWorkflowStart() {
        // nothing to do
    }

    @Override
    public String getName() {
        return BatchWorkflowStart.class.getSimpleName();
    }

    @Override
    public String getDescription() {
        return "Workflow start which loads batches from the storage and provides them in batches of 250 to the system.";
    }

    @Override
    public int getPreferredThreadCount() {
        return 2;
    }

    @Override
    public int getMaximumThreadCount() {
        return 2;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> getParameters() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void initialize(ExecutionContext context, StorageEngine storage)
            throws WorkflowStartFailedException {
        try {
            long[] ids;

            DataSet dataSet = context.getDataSet();
            if (dataSet instanceof Provider) {
                try {
                    ids = storage.getByProvider((Provider)dataSet, false);
                } catch (StorageEngineException e) {
                    throw new WorkflowStartFailedException("Provider '" + dataSet.getIdentifier() +
                                                           "' could not be retrieved!", e);
                }
            } else if (dataSet instanceof Collection) {
                try {
                    ids = storage.getByCollection((Collection)dataSet);
                } catch (StorageEngineException e) {
                    throw new RuntimeException("Collection '" + dataSet.getIdentifier() +
                                               "' could not be retrieved!", e);
                }
            } else if (dataSet instanceof Request) {
                try {
                    ids = storage.getByRequest((Request)dataSet);
                } catch (StorageEngineException e) {
                    throw new RuntimeException("Request '" + dataSet.getIdentifier() +
                                               "' could not be retrieved!", e);
                }
            } else if (dataSet instanceof MetaDataRecord) {
                ids = new long[] { ((MetaDataRecord)dataSet).getId() };
            } else {
                throw new WorkflowStartFailedException("Unsupported dataset <" +
                                                       context.getDataSet() + ">");
            }

            Data data = new Data();
            data.total = ids.length;
            context.putValue(DATA_KEY, data);
            if (ids.length > BATCH_SIZE) {
                int batches = (int)Math.ceil(1.0 * ids.length / BATCH_SIZE);
                for (int i = 0; i < batches; i++) {
                    int end = Math.min(ids.length, (i + 1) * BATCH_SIZE);
                    int start = i * BATCH_SIZE;

                    long[] batch = new long[end - start];
                    System.arraycopy(ids, start, batch, 0, end - start);

                    synchronized (context.getValue(DATA_KEY).batches) {
                        context.getValue(DATA_KEY).batches.add(batch);
                    }
                }

            } else {
                // adding this to the local queue and
                // enqueue the batch with a runnable per
                // batch "implements" the way how this api
                // is meant
                synchronized (context.getValue(DATA_KEY).batches) {
                    context.getValue(DATA_KEY).batches.add(ids);
                }
            }
        } finally {
        }
    }

    @Override
    public TaskCreator createLoader(final ExecutionContext context, final StorageEngine storage) {
        if (!isFinished(context, storage)) { return new TaskCreator() {
            @Override
            public void run() {
                setDone(false);
                try {
                    long[] poll = context.getValue(DATA_KEY).batches.poll(500,
                            TimeUnit.MILLISECONDS);
                    if (poll != null) {
                        MetaDataRecord[] mdrs = storage.getMetaDataRecords(poll);

                        for (int i = 0; i < mdrs.length; i++) {
                            MetaDataRecord mdr = mdrs[i];
                            Task task = new Task(mdr, storage, context);
                            synchronized (getQueue()) {
                                getQueue().offer(task);
                            }
                        }
                    }
                } catch (Throwable t) {
                    throw new RuntimeException("Failed to retrieve MDRs from storage. " +
                                               context.getExecution().toString(), t);
                } finally {
                    setDone(true);
                }
            }
        }; }
        return null;
    }

    @Override
    public int getTotalSize(ExecutionContext context) {
        return context.getValue(DATA_KEY).total;
    }

    @Override
    public boolean isFinished(ExecutionContext context, StorageEngine storage) {
        return context.getValue(DATA_KEY).batches.isEmpty();
    }

    /**
     * container for runtime information.
     * 
     * @author Andreas Juffinger (andreas.juffinger@kb.nl)
     * @date Feb 28, 2011
     */
    final static class Data implements Serializable {
        public int                   total   = 0;
        public BlockingQueue<long[]> batches = new LinkedBlockingQueue<long[]>();
    }

    @Override
    public void completed(ExecutionContext context) throws WorkflowStartFailedException {
        context.getValue(DATA_KEY).batches.clear();
    }
}
