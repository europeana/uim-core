package eu.europeana.uim.util;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.logging.LoggingEngine;
import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.plugin.source.AbstractWorkflowStart;
import eu.europeana.uim.plugin.source.Task;
import eu.europeana.uim.plugin.source.TaskCreator;
import eu.europeana.uim.plugin.source.WorkflowStartFailedException;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.store.bean.MetaDataRecordBean;

/**
 * Loads batches from the storage and pulls them into as tasks.
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 14, 2011
 */
@SuppressWarnings("hiding")
public class BatchWorkflowStart<I> extends AbstractWorkflowStart<MetaDataRecord<I>, I> {
    private static final Logger                        log                     = Logger.getLogger(BatchWorkflowStart.class.getName());

    /** String BATCH_SUBSET */
    public static final String                         BATCH_SUBSET_HEAD       = "batch.subset.head";

    /** String BATCH_SUBSET */
    public static final String                         BATCH_SUBSET_SHUFFLE    = "batch.subset.shuffle";

    /** String BATCH_SHUFFLE */
    public static final String                         BATCH_SHUFFLE           = "batch.shuffle";

    /** String BATCH_SUBSET */
    public static final String                         BATCH_LOAD_CONTENT      = "batch.load-content";

    /** BatchWorkflowStart COLLECTION_LAST_REQUEST */
    public static final String                         COLLECTION_LAST_REQUEST = "collection.only.lastrequest";

    /** BatchWorkflowStart COLLECTION_FROM_REQUEST */
    public static final String                         COLLECTION_FROM_REQUEST = "collection.from.requestdate";

    /**
     * parameters to be set for batch loading
     */
    private static final List<String>                  PARAMETER               = new ArrayList<String>() {
                                                                                   {
                                                                                       add(BATCH_SUBSET_HEAD);
                                                                                       add(BATCH_SUBSET_SHUFFLE);
                                                                                       add(BATCH_SHUFFLE);
                                                                                       add(COLLECTION_LAST_REQUEST);
                                                                                       add(COLLECTION_FROM_REQUEST);
                                                                                       add(BATCH_LOAD_CONTENT);
                                                                                   }
                                                                               };

    private static final ThreadLocal<SimpleDateFormat> ISO8601_DATE_FORMAT     = new ThreadLocal<SimpleDateFormat>() {
                                                                                   @Override
                                                                                   protected SimpleDateFormat initialValue() {
                                                                                       return new SimpleDateFormat(
                                                                                               "yyyy-MM-dd");
                                                                                   }
                                                                               };

    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT      = new ThreadLocal<SimpleDateFormat>() {
                                                                                   @Override
                                                                                   protected SimpleDateFormat initialValue() {
                                                                                       return new SimpleDateFormat(
                                                                                               "yyyy.MM.dd");
                                                                                   }
                                                                               };

    /**
     * Key to retrieve own data from context.
     */
    @SuppressWarnings("rawtypes")
    private static TKey<BatchWorkflowStart, Data>      DATA_KEY                = TKey.register(
                                                                                       BatchWorkflowStart.class,
                                                                                       "data",
                                                                                       Data.class);

    /**
     * default batch size
     */
    // FIXME: changed batch size of loading, need it for fulltext, but problem with metadata only
    public static int                                  BATCH_SIZE              = 50;

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
        return PARAMETER;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(ExecutionContext<MetaDataRecord<I>, I> context)
            throws WorkflowStartFailedException {
        try {
            StorageEngine<I> storage = context.getStorageEngine();

            long start = System.currentTimeMillis();
            Collection<I> coll = null;
            I[] records = null;

            UimDataSet<I> dataSet = context.getDataSet();
            if (dataSet instanceof Collection) {
                try {
                    coll = (Collection<I>)dataSet;

                    boolean lastrequest = Boolean.parseBoolean(context.getProperties().getProperty(
                            COLLECTION_LAST_REQUEST, "false"));

                    Date thisFrom = null;
                    String propFrom = context.getProperties().getProperty(COLLECTION_FROM_REQUEST);
                    if (propFrom != null) {
                        try {
                            thisFrom = ISO8601_DATE_FORMAT.get().parse(propFrom);
                        } catch (ParseException e) {
                            thisFrom = SIMPLE_DATE_FORMAT.get().parse(propFrom);
                        }
                    }

                    if (lastrequest) {
                        Request<I> request = null;

                        // retrieve the "last" request
                        List<Request<I>> requests = storage.getRequests(coll);
                        for (Request<I> candidate : requests) {
                            if (request == null || request.getDate().before(candidate.getDate())) {
                                request = candidate;
                            }
                        }

                        if (request != null) {
                            records = storage.getByRequest(request);

                            log.info("Loaded request:" + request.getId());
                            LoggingEngine<I> loggingEngine = context.getLoggingEngine();
                            if (loggingEngine != null) {
                                loggingEngine.log(Level.INFO, "BatchWorkflowStart",
                                        "Processing only last request:" + request.getId());
                            }
                        } else {
                            throw new WorkflowStartFailedException(
                                    "No request found for collection <" + coll.getMnemonic() + ">");
                        }

                    } else if (thisFrom != null) {
                        List<I> allids = new ArrayList<I>();

                        // retrieve the "last" request
                        StringBuilder logging = new StringBuilder();
                        List<Request<I>> requests = storage.getRequests(coll);
                        for (Request<I> candidate : requests) {
                            if (candidate.getDataFrom().after(thisFrom)) {
                                if (logging.length() > 0) {
                                    logging.append(";");
                                }
                                logging.append(candidate.getId());

                                allids.addAll(Arrays.asList(storage.getByRequest(candidate)));
                            }
                        }

                        if (!allids.isEmpty()) {
                            records = allids.toArray((I[])Array.newInstance(
                                    allids.get(0).getClass(), 0));

                            log.info("Loaded requests:" + logging);
                            LoggingEngine<I> loggingEngine = context.getLoggingEngine();
                            if (loggingEngine != null) {
                                loggingEngine.log(Level.INFO, "BatchWorkflowStart",
                                        "Processing requests:" + logging);
                            }
                        } else {
                            throw new WorkflowStartFailedException(
                                    "No requests found for collection <" + coll.getMnemonic() +
                                            "> after " + thisFrom);
                        }

                    } else {
                        records = storage.getByCollection((Collection<I>)dataSet);
                    }
                } catch (StorageEngineException e) {
                    throw new WorkflowStartFailedException("Collection '" + dataSet.getId() +
                                                           "' could not be retrieved!", e);
                } catch (ParseException e) {
                    throw new WorkflowStartFailedException("Caused by parse exception", e);
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

            log.info(String.format("Dataset has all together %d records!", records.length));

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
                if (allids.size() > 0) {
                    I i = allids.get(0);
                    if (i instanceof Comparable) {
                        Collections.sort(allids, new Comparator<I>() {
                            @SuppressWarnings("rawtypes")
                            @Override
                            public int compare(I o1, I o2) {
                                return ((Comparable)o1).compareTo(o2);
                            }

                        });
                    }
                }

                allids = allids.subList(0, Math.min(subset, allids.size() - 1));
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

                allids = allids.subList(0, Math.min(subset, allids.size() - 1));
                Object[] ids = allids.toArray(new Object[allids.size()]);
                data.total = ids.length;
                addArray(context, ids);

                log.info(String.format("Loaded %d records in %.3f sec created subset of size:" +
                                       subset, ids.length,
                        (System.currentTimeMillis() - start) / 1000.0));
            } else {
                if (records.length > 0) {
                    I i = records[0];
                    if (i instanceof Comparable) {
                        Arrays.sort(records, new Comparator<I>() {
                            @SuppressWarnings("rawtypes")
                            @Override
                            public int compare(I o1, I o2) {
                                return ((Comparable)o1).compareTo(o2);
                            }
                        });
                    }
                }

                addArray(context, records);
                data.total = records.length;

                log.info(String.format("Created %d batches in %.3f sec", data.batches.size(),
                        (System.currentTimeMillis() - start) / 1000.0));
            }

            data.collection = coll;
            data.initialized = true;

            boolean loadContent = Boolean.parseBoolean(context.getProperties().getProperty(
                    BATCH_LOAD_CONTENT, "false"));
            if (loadContent) {
                storage.command("load-content:" + coll.getMnemonic());
            }
        } finally {
        }
    }

    @SuppressWarnings("unchecked")
    private <I> void addArray(ExecutionContext<MetaDataRecord<I>, I> context, Object[] ids) {
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
    public TaskCreator<MetaDataRecord<I>, I> createLoader(
            final ExecutionContext<MetaDataRecord<I>, I> context)
            throws WorkflowStartFailedException {
        if (!isFinished(context)) { return new TaskCreator<MetaDataRecord<I>, I>() {
            @SuppressWarnings({ "unchecked", "rawtypes" })
            @Override
            public void run() {
                try {
                    Data container = context.getValue(DATA_KEY);
                    I[] poll = (I[])container.batches.poll(500, TimeUnit.MILLISECONDS);
                    if (poll != null) {
                        List<MetaDataRecord<I>> metaDataRecords = context.getStorageEngine().getMetaDataRecords(
                                Arrays.asList(poll));
                        MetaDataRecord<I>[] mdrs = metaDataRecords.toArray(new MetaDataRecord[metaDataRecords.size()]);

                        if (mdrs.length != poll.length) {
                            log.warning(String.format(
                                    "Requested %d records from storage backend, but got back %d records!",
                                    poll.length, mdrs.length));
                        }

                        for (int i = 0; i < mdrs.length; i++) {
                            MetaDataRecord<I> mdr = mdrs[i];

                            if (mdr != null) {
                                if (mdr instanceof MetaDataRecordBean) {
                                    ((MetaDataRecordBean)mdr).setCollection(container.collection);
                                }

                                Task<MetaDataRecord<I>, I> task = new Task<MetaDataRecord<I>, I>(
                                        mdr, context);
                                synchronized (getQueue()) {
                                    getQueue().offer(task);
                                }
                            } else {
                                log.warning("Requested '" + poll[i] + "' record is null!");
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
    public int getTotalSize(ExecutionContext<MetaDataRecord<I>, I> context) {
        @SuppressWarnings("unchecked")
        Data<I> value = context.getValue(DATA_KEY);
        if (value != null) {
            return context.getValue(DATA_KEY).total;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isFinished(ExecutionContext<MetaDataRecord<I>, I> context) {
        @SuppressWarnings("unchecked")
        Data<I> value = context.getValue(DATA_KEY);
        return value.initialized && value.batches.isEmpty();
    }

    @Override
    public void completed(ExecutionContext<MetaDataRecord<I>, I> context)
            throws WorkflowStartFailedException {
        Data<?> value = context.getValue(DATA_KEY);
        value.batches.clear();

        StorageEngine<I> storage = context.getStorageEngine();
        boolean loadContent = Boolean.parseBoolean(context.getProperties().getProperty(
                BATCH_LOAD_CONTENT, "false"));
        if (loadContent) {
            storage.command("unload-content:" + value.collection.getMnemonic());
        }
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

    @Override
    public void initialize() {
        // nothing to do
    }

    @Override
    public void shutdown() {
        // nothing to do
    }
}
