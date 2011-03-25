package eu.europeana.uim.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.ArrayUtils;

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
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BatchWorkflowStart implements WorkflowStart {

    /** String BATCH_SUBSET */
    public static final String                    BATCH_SUBSET  = "batch.subset";

    /** String BATCH_SHUFFLE */
    public static final String                    BATCH_SHUFFLE = "batch.shuffle";

    /**
     * Key to retrieve own data from context.
     */
    private static TKey<BatchWorkflowStart, Data> DATA_KEY      = TKey.register(
                                                                        BatchWorkflowStart.class,
                                                                        "data", Data.class);

    /**
     * default batch size
     */
    public static int                             BATCH_SIZE    = 250;

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
        return 5;
    }

    @Override
    public int getMaximumThreadCount() {
        return 10;
    }

    @Override
    public List<String> getParameters() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public void initialize(ExecutionContext context, StorageEngine<?> storage)
            throws WorkflowStartFailedException {
        try {
            Object[] ids;

            DataSet dataSet = context.getDataSet();
            if (dataSet instanceof Provider) {
                try {
                    ids = storage.getByProvider((Provider)dataSet, false);
                } catch (StorageEngineException e) {
                    throw new WorkflowStartFailedException("Provider '" + dataSet.getId() +
                                                           "' could not be retrieved!", e);
                }
            } else if (dataSet instanceof Collection) {
                try {
                    ids = storage.getByCollection((Collection)dataSet);
                } catch (StorageEngineException e) {
                    throw new RuntimeException("Collection '" + dataSet.getId() +
                                               "' could not be retrieved!", e);
                }
            } else if (dataSet instanceof Request) {
                try {
                    ids = storage.getByRequest((Request)dataSet);
                } catch (StorageEngineException e) {
                    throw new RuntimeException("Request '" + dataSet.getId() +
                                               "' could not be retrieved!", e);
                }
            } else if (dataSet instanceof MetaDataRecord) {
                ids = new Object[] { ((MetaDataRecord)dataSet).getId() };
            } else {
                throw new WorkflowStartFailedException("Unsupported dataset <" +
                                                       context.getDataSet() + ">");
            }

            boolean shuffle = Boolean.parseBoolean(context.getProperties().getProperty(
                    BATCH_SHUFFLE, "false"));
            if (shuffle) {
                List<Object> list = Arrays.asList(ids);
                Collections.shuffle(list);
                ids = list.toArray(new Object[list.size()]);
            }
            if (context.getProperties().getProperty(BATCH_SUBSET) != null) {
                int subset = Integer.parseInt(context.getProperties().getProperty(BATCH_SUBSET));
                ids = ArrayUtils.subarray(ids, 0, Math.min(subset, ids.length - 1));
            }

            Data data = new Data();
            data.total = ids.length;
            context.putValue(DATA_KEY, data);
            if (ids.length > BATCH_SIZE) {
                int batches = (int)Math.ceil(1.0 * ids.length / BATCH_SIZE);
                for (int i = 0; i < batches; i++) {
                    int end = Math.min(ids.length, (i + 1) * BATCH_SIZE);
                    int start = i * BATCH_SIZE;

                    Object[] batch = new Object[end - start];
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
            data.initialized = true;
        } finally {
        }
    }

    @Override
    public TaskCreator createLoader(final ExecutionContext context, final StorageEngine storage) {
        if (!isFinished(context, storage)) { return new TaskCreator() {
            @Override
            public void run() {
                try {
                    Object[] poll = context.getValue(DATA_KEY).batches.poll(500,
                            TimeUnit.MILLISECONDS);
                    if (poll != null) {
                        MetaDataRecord[] mdrs = new MetaDataRecord[poll.length];
                        for (int i = 0; i < poll.length; i++) {
                            mdrs[i] = storage.getMetaDataRecord(poll[i]);
                        }

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
    public boolean isFinished(ExecutionContext context, StorageEngine<?> storage) {
        Data value = context.getValue(DATA_KEY);
        return value.initialized && value.batches.isEmpty();
    }

    /**
     * container for runtime information.
     * 
     * @author Andreas Juffinger (andreas.juffinger@kb.nl)
     * @date Feb 28, 2011
     */
    final static class Data implements Serializable {
        public int                     total       = 0;
        public boolean                 initialized = false;
        public BlockingQueue<Object[]> batches     = new LinkedBlockingQueue<Object[]>();
    }

    @Override
    public void completed(ExecutionContext context) throws WorkflowStartFailedException {
        context.getValue(DATA_KEY).batches.clear();
    }
}
