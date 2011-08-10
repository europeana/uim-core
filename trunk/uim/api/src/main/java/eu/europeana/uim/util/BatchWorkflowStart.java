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
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.store.bean.MetaDataRecordBean;
import eu.europeana.uim.workflow.AbstractWorkflowStart;
import eu.europeana.uim.workflow.Task;
import eu.europeana.uim.workflow.TaskCreator;
import eu.europeana.uim.workflow.WorkflowStartFailedException;

/**
 * Loads batches from the storage and pulls them into as tasks.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 14, 2011
 */
public class BatchWorkflowStart extends AbstractWorkflowStart {
    private static final Logger                   log                  = Logger.getLogger(BatchWorkflowStart.class.getName());

    /** String BATCH_SUBSET */
    public static final String                    BATCH_SUBSET_HEAD    = "batch.subset.head";

    /** String BATCH_SUBSET */
    public static final String                    BATCH_SUBSET_SHUFFLE = "batch.subset.shuffle";

    /** String BATCH_SHUFFLE */
    public static final String                    BATCH_SHUFFLE        = "batch.shuffle";

    /**
     * Key to retrieve own data from context.
     */
    @SuppressWarnings("rawtypes")
    private static TKey<BatchWorkflowStart, Data> DATA_KEY             = TKey.register(
                                                                               BatchWorkflowStart.class,
                                                                               "data", Data.class);

    /**
     * default batch size
     */
    public static int                             BATCH_SIZE           = 250;

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

    @SuppressWarnings("unchecked")
    @Override
    public <I> void initialize(ExecutionContext<I> context, StorageEngine<I> storage)
            throws WorkflowStartFailedException {
        try {
            long start = System.currentTimeMillis();
            Collection<I> coll = null;
            I[] records = null;

            UimDataSet<I> dataSet = context.getDataSet();
            if (dataSet instanceof Collection) {
                try {
                    coll = (Collection<I>)dataSet;
                    records = storage.getByCollection((Collection<I>)dataSet);
                } catch (StorageEngineException e) {
                    throw new RuntimeException("Collection '" + dataSet.getId() +
                                               "' could not be retrieved!", e);
                }
            } else if (dataSet instanceof Request) {
                try {
                    coll = ((Request<I>)dataSet).getCollection();
                    records = storage.getByRequest((Request<I>)dataSet);
                } catch (StorageEngineException e) {
                    throw new RuntimeException("Request '" + dataSet.getId() +
                                               "' could not be retrieved!", e);
                }
            } else if (dataSet instanceof MetaDataRecord) {
                MetaDataRecord<I> record = (MetaDataRecord<I>)dataSet;
                records = (I[])Array.newInstance(record.getId().getClass(), 1);
                records[0] = record.getId();
                coll = record.getCollection();
            } else {
                throw new WorkflowStartFailedException("Unsupported dataset <" +
                                                       context.getDataSet() + ">");
            }

            // this is for testing to allow injection of a data object
            Data<?> data = context.getValue(DATA_KEY);
            if (data == null) {
                data = new Data<I>();
                context.putValue(DATA_KEY, data);
            }

            boolean shuffle = Boolean.parseBoolean(context.getProperties().getProperty(
                    BATCH_SHUFFLE, "false"));

            if (shuffle) {
                List<I> allids = Arrays.asList(records);
                Collections.shuffle(allids);

                Object[] ids = allids.toArray(new Object[allids.size()]);
                data.total = ids.length;
                addArray(context, ids);

                log.info(String.format("Loaded %d records in %.3f sec created shuffled.",
                        ids.length, (System.currentTimeMillis() - start) / 1000.0));

            } else if (context.getProperties().getProperty(BATCH_SUBSET_HEAD) != null) {
                int subset = Integer.parseInt(context.getProperties().getProperty(BATCH_SUBSET_HEAD));

                List<I> allids = Arrays.asList(records);
                allids = allids.subList(0, Math.min(subset, allids.size() -1));
                Object[] ids = allids.toArray(new Object[allids.size()]);
                data.total = ids.length;
                addArray(context, ids);

                log.info(String.format("Loaded %d records in %.3f sec created subset of size:" +
                                       subset, ids.length,
                        (System.currentTimeMillis() - start) / 1000.0));
            } else if (context.getProperties().getProperty(BATCH_SUBSET_SHUFFLE) != null) {
                int subset = Integer.parseInt(context.getProperties().getProperty(
                        BATCH_SUBSET_SHUFFLE));

                List<I> allids = Arrays.asList(records);
                Collections.shuffle(allids);

                allids = allids.subList(0, Math.min(subset, allids.size() -1));
                Object[] ids = allids.toArray(new Object[allids.size()]);
                data.total = ids.length;
                addArray(context, ids);

                log.info(String.format("Loaded %d records in %.3f sec created subset of size:" +
                                       subset, ids.length,
                        (System.currentTimeMillis() - start) / 1000.0));
            } else {
                addArray(context, records);
                data.total = records.length;

                log.info(String.format("Created %d batches in %.3f sec", data.batches.size(),
                        (System.currentTimeMillis() - start) / 1000.0));
            }

            data.collection = coll;
            data.initialized = true;
        } finally {
        }
    }

    @SuppressWarnings("unchecked")
    private <I> void addArray(ExecutionContext<I> context, Object[] ids) {
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
    public <I> TaskCreator<I> createLoader(final ExecutionContext<I> context,
            final StorageEngine<I> storage) {
        if (!isFinished(context, storage)) { return new TaskCreator<I>() {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            @Override
            public void run() {
                try {
                    Data container = context.getValue(DATA_KEY);
                    I[] poll = (I[])container.batches.poll(500, TimeUnit.MILLISECONDS);
                    if (poll != null) {
                        List<MetaDataRecord<I>> metaDataRecords = storage.getMetaDataRecords(Arrays.asList(poll));
                        MetaDataRecord<I>[] mdrs = metaDataRecords.toArray(new MetaDataRecord[metaDataRecords.size()]);

                        for (int i = 0; i < mdrs.length; i++) {
                            MetaDataRecord<I> mdr = mdrs[i];

                            if (mdr instanceof MetaDataRecordBean) {
                                ((MetaDataRecordBean)mdr).setCollection(container.collection);
                            }

                            Task<I> task = new Task<I>(mdr, storage, context);
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
    public <I> int getTotalSize(ExecutionContext<I> context) {
        @SuppressWarnings("unchecked")
        Data<I> value = context.getValue(DATA_KEY);
        if (value != null) {
            return context.getValue(DATA_KEY).total;
        } else {
            return 0;
        }
    }

    @Override
    public <I> boolean isFinished(ExecutionContext<I> context, StorageEngine<I> storage) {
        @SuppressWarnings("unchecked")
        Data<I> value = context.getValue(DATA_KEY);
        return value.initialized && value.batches.isEmpty();
    }

    @Override
    public <I> void completed(ExecutionContext<I> context) throws WorkflowStartFailedException {
        context.getValue(DATA_KEY).batches.clear();
    }

    /**
     * container for runtime information.
     * 
     * @param <T>
     * 
     * @author Andreas Juffinger (andreas.juffinger@kb.nl)
     * @since Feb 28, 2011
     */
    protected final static class Data<T> implements Serializable {
        /** total records */
        public int                total       = 0;
        /** initialized yes/no */
        public boolean            initialized = false;

        /** batches */
        public BlockingQueue<T[]> batches     = new LinkedBlockingQueue<T[]>();

        /** collection */
        public Collection<?>      collection  = null;
    }
}
