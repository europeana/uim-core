package eu.europeana.uim.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;
import eu.europeana.uim.UIMTask;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.DataSet;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.workflow.AbstractWorkflowStart;
import eu.europeana.uim.workflow.Task;
import eu.europeana.uim.workflow.TaskCreator;
import eu.europeana.uim.workflow.WorkflowStart;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 14, 2011
 */
public class BatchWorkflowStart extends AbstractWorkflowStart implements WorkflowStart {

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
    public void initialize(ExecutionContext context, StorageEngine storage)
    throws StorageEngineException {
        try {
            long[] ids;

            DataSet dataSet = context.getDataSet();
            if (dataSet instanceof Provider) {
                ids = storage.getByProvider((Provider)dataSet, false);
            } else if (dataSet instanceof Collection) {
                ids = storage.getByCollection((Collection)dataSet);
            } else if (dataSet instanceof Request) {
                ids = storage.getByRequest((Request)dataSet);
            } else if (dataSet instanceof MetaDataRecord) {
                ids = new long[] { ((MetaDataRecord)dataSet).getId() };
            } else {
                throw new IllegalStateException("Unsupported dataset <" + context.getDataSet() +
                ">");
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
    public void completed(ExecutionContext context) {
    }

    @Override
    public TaskCreator createLoader(final ExecutionContext context, final StorageEngine storage) {
        if (!isFinished(context, storage)) {
            return new TaskCreator() {
                public void run() {
                    setDone(false);
                    try {
                        long[] poll = context.getValue(DATA_KEY).batches.poll(500, TimeUnit.MILLISECONDS);
                        if (poll != null) {
                            MetaDataRecord[] mdrs = storage.getMetaDataRecords(poll);

                            for (int i = 0; i < mdrs.length; i++) {
                                MetaDataRecord mdr = mdrs[i];
                                Task task = new UIMTask(mdr, storage, context);
                                getQueue().offer(task);
                            }
                        }
                    } catch (Throwable t) {
                        throw new RuntimeException("Failed to retrieve MDRs from storage. " + context.getExecution().toString(), t);
                    } finally {
                        setDone(true);
                    }
                }
            };
        }
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

    @Override
    public String getDescription() {
        return "Workflow start which loads batches from the storage and provides them in batches of 250 to the system.";
    }

    /**
     * container for runtime information.
     * 
     * @author Andreas Juffinger (andreas.juffinger@kb.nl)
     * @date Feb 28, 2011
     */
    final static class Data implements Serializable {
        public int total = 0;
        public BlockingQueue<long[]> batches = new LinkedBlockingQueue<long[]>();
    }


}
