package eu.europeana.uim.util;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.workflow.AbstractWorkflowStart;
import eu.europeana.uim.workflow.Task;
import eu.europeana.uim.workflow.TaskCreator;
import eu.europeana.uim.workflow.WorkflowStartFailedException;

/**
 * Loads batches from the storage and pulls them into as tasks.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @param <I>
 *            The identifier type
 * @since Feb 14, 2011
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BatchWorkflowStart<I> extends AbstractWorkflowStart {
    private static final Logger                   log           = Logger.getLogger(BatchWorkflowStart.class.getName());

    /** String BATCH_SUBSET */
    public static final String                    BATCH_SUBSET_HEAD  = "batch.subset.head";

    /** String BATCH_SUBSET */
    public static final String                    BATCH_SUBSET_SHUFFLE  = "batch.subset.shuffle";

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
        super(
                "Batch Loading Workflow Start",
                "Workflow start which loads batches from the storage and provides them in batches of 250 to the system.");
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
        return Arrays.asList(BATCH_SUBSET_HEAD, BATCH_SUBSET_SHUFFLE, BATCH_SHUFFLE);
    }

    @Override
    public void initialize(ExecutionContext context, StorageEngine<?> storage)
            throws WorkflowStartFailedException {
        try {
            long start = System.currentTimeMillis();
            I[] records = null;
            

            UimDataSet dataSet = context.getDataSet();
            if (dataSet instanceof Provider) {
                try {
                    records = ((StorageEngine<I>)storage).getByProvider((Provider)dataSet, false);
                } catch (StorageEngineException e) {
                    throw new WorkflowStartFailedException("Provider '" + dataSet.getId() +
                                                           "' could not be retrieved!", e);
                }
            } else if (dataSet instanceof Collection) {
                try {
                    records = ((StorageEngine<I>)storage).getByCollection((Collection)dataSet);
                } catch (StorageEngineException e) {
                    throw new RuntimeException("Collection '" + dataSet.getId() +
                                               "' could not be retrieved!", e);
                }
            } else if (dataSet instanceof Request) {
                try {
                    records = ((StorageEngine<I>)storage).getByRequest((Request)dataSet);
                } catch (StorageEngineException e) {
                    throw new RuntimeException("Request '" + dataSet.getId() +
                                               "' could not be retrieved!", e);
                }
            } else if (dataSet instanceof MetaDataRecord) {
                records = (I[])Array.newInstance(((MetaDataRecord)dataSet).getId().getClass(), 1);
                records[0] = (I)((MetaDataRecord)dataSet).getId();
            } else {
                throw new WorkflowStartFailedException("Unsupported dataset <" +
                                                       context.getDataSet() + ">");
            }

            // this is for testing to allow injection of a data object
            Data data = context.getValue(DATA_KEY);
            if (data == null) {
                data = new Data();
                context.putValue(DATA_KEY, data);
            }

            boolean shuffle = Boolean.parseBoolean(context.getProperties().getProperty(BATCH_SHUFFLE, "false"));
            
            if (shuffle) {
                List<I> allids = Arrays.asList(records);
                Collections.shuffle(allids);

                Object[] ids = allids.toArray(new Object[allids.size()]);
                data.total = ids.length;
                addArray(context, ids);
                
                log.info(String.format("Loaded %d records in %.3f sec created shuffled.", ids.length,
                        (System.currentTimeMillis() - start) / 1000.0));
                
            } else if (context.getProperties().getProperty(BATCH_SUBSET_HEAD) != null) {
                int subset = Integer.parseInt(context.getProperties().getProperty(BATCH_SUBSET_HEAD));

                List<I> allids = Arrays.asList(records);
                allids = allids.subList(0, subset);
                Object[] ids = allids.toArray(new Object[allids.size()]);
                data.total = ids.length;
                addArray(context, ids);
                
                log.info(String.format("Loaded %d records in %.3f sec created subset of size:" + subset, ids.length,
                        (System.currentTimeMillis() - start) / 1000.0));
            } else if (context.getProperties().getProperty(BATCH_SUBSET_SHUFFLE) != null) {
                int subset = Integer.parseInt(context.getProperties().getProperty(BATCH_SUBSET_SHUFFLE));

                List<I> allids = Arrays.asList(records);
                Collections.shuffle(allids);

                allids = allids.subList(0, subset);
                Object[] ids = allids.toArray(new Object[allids.size()]);
                data.total = ids.length;
                addArray(context, ids);
                
                log.info(String.format("Loaded %d records in %.3f sec created subset of size:" + subset, ids.length,
                        (System.currentTimeMillis() - start) / 1000.0));
            } else {
                addArray(context, records);
                data.total = records.length;

                log.info(String.format("Created %d batches in %.3f sec", data.batches.size(),
                        (System.currentTimeMillis() - start) / 1000.0));
            }

            data.initialized = true;
        } finally {
        }
    }

    private void addArray(ExecutionContext context, Object[] ids) {
        if (ids.length > BATCH_SIZE) {
            int batches = (int)Math.ceil(1.0 * ids.length / BATCH_SIZE);
            for (int i = 0; i < batches; i++) {
                int end = Math.min(ids.length, (i + 1) * BATCH_SIZE);
                int sta = i * BATCH_SIZE;

                Object[] batch = new Object[end - sta];
                System.arraycopy(ids, sta, batch, 0, end - sta);

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
    }

    @Override
    public TaskCreator createLoader(final ExecutionContext context, final StorageEngine storage) {
        if (!isFinished(context, storage)) { return new TaskCreator() {
            @Override
            public void run() {
                try {
                    I[] poll = (I[])context.getValue(DATA_KEY).batches.poll(500,
                            TimeUnit.MILLISECONDS);
                    if (poll != null) {
                        List<MetaDataRecord> metaDataRecords = storage.getMetaDataRecords(Arrays.asList(poll));
                        MetaDataRecord[] mdrs = metaDataRecords.toArray(new MetaDataRecord[metaDataRecords.size()]);

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
        Data value = context.getValue(DATA_KEY);
        if (value != null) {
            return context.getValue(DATA_KEY).total;
        } else {
            return 0;
        }
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
     * @since Feb 28, 2011
     */
    final static class Data<I> implements Serializable {
        public int                total       = 0;
        public boolean            initialized = false;
        public BlockingQueue<I[]> batches     = new LinkedBlockingQueue<I[]>();
    }

    @Override
    public void completed(ExecutionContext context) throws WorkflowStartFailedException {
        context.getValue(DATA_KEY).batches.clear();
    }
}
