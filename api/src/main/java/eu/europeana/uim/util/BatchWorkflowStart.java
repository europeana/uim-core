package eu.europeana.uim.util;

import java.io.Serializable;
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
import java.text.DateFormat;

/**
 * Loads batches from the storage and pulls them into as tasks.
 *
 * @param <I> generic identifier
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 14, 2011
 */
@SuppressWarnings("hiding")
public class BatchWorkflowStart<I> extends AbstractWorkflowStart<MetaDataRecord<I>, I> {

    private static final Logger log = Logger.getLogger(BatchWorkflowStart.class.getName());

    public static final String BATCH_SUBSET_HEAD = "batch.subset.head";
    public static final String BATCH_SUBSET_SHUFFLE = "batch.subset.shuffle";
    public static final String BATCH_SHUFFLE = "batch.shuffle";
    public static final String BATCH_SIZE = "batch.size";
    public static final String COLLECTION_LAST_REQUEST = "collection.only.lastrequest";
    public static final String COLLECTION_FROM_REQUEST = "collection.from.requestdate";

    private static final ThreadLocal<DateFormat> ISO8601_DATE_FORMAT = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
    private static final ThreadLocal<DateFormat> DEFAULT_DATE_FORMAT = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy.MM.dd");
        }
    };

    //FIXME: changed batch size of loading, need it for fulltext, but problem with metadata only
    public static int DEFAULT_BATCH_SIZE = 100;

    /**
     * Key to retrieve own data from context.
     */
    @SuppressWarnings("rawtypes")
    private static final TKey<BatchWorkflowStart, Data> DATA_KEY = TKey.register(
            BatchWorkflowStart.class,
            "data",
            Data.class);

    /**
     * Creates a new instance of this class.
     */
    public BatchWorkflowStart() {
        super("Batch Loading Workflow Start",
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
        return Arrays.asList(BATCH_SUBSET_HEAD, BATCH_SUBSET_SHUFFLE, BATCH_SHUFFLE,
                COLLECTION_LAST_REQUEST, COLLECTION_FROM_REQUEST);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(ExecutionContext<MetaDataRecord<I>, I> context) throws WorkflowStartFailedException {
        long start = System.currentTimeMillis();

        Data<I> data = context.getValue(DATA_KEY);
        if (data == null) {
            data = new Data<>();
            context.putValue(DATA_KEY, data);
        }

        Collection<I> coll = extractCollection(context);
        BlockingQueue<I> recordIds = loadRecordIds(context); 

        boolean shuffle = Boolean.parseBoolean(context.getProperties().getProperty(
                BATCH_SHUFFLE, "false"));
        log.info(String.format("Dataset has all together %d records!", recordIds.size()));

        if (shuffle) {
            recordIds = shuffleRecordIds(recordIds);
            log.info(String.format("Loaded %d records in %.3f sec created shuffled.",
                    recordIds.size(), (System.currentTimeMillis() - start) / 1000.0));
        } else if (context.getProperties().getProperty(BATCH_SUBSET_HEAD) != null) {
            int subset = Integer.parseInt(context.getProperties().getProperty(BATCH_SUBSET_HEAD));

            List<I> allids = new ArrayList<>();
            for (I recordId : recordIds) {
                allids.add(recordId);
            }

            if (allids.size() > 0) {
                I i = allids.get(0);
                if (i instanceof Comparable) {
                    Collections.sort(allids, new Comparator<I>() {
                        @SuppressWarnings("rawtypes")
                        @Override
                        public int compare(I o1, I o2) {
                            return ((Comparable) o1).compareTo(o2);
                        }

                    });
                }
            }

            allids = allids.subList(0, Math.min(subset, allids.size() - 1));

            recordIds = new LinkedBlockingQueue<>(allids);

            log.info(String.format("Loaded %d records in %.3f sec created subset of size:"
                    + subset, allids.size(),
                    (System.currentTimeMillis() - start) / 1000.0));
        } else if (context.getProperties().getProperty(BATCH_SUBSET_SHUFFLE) != null) {
            int subset = Integer.parseInt(context.getProperties().getProperty(
                    BATCH_SUBSET_SHUFFLE));

            List<I> allids = new ArrayList<>();
            for (I recordId : recordIds) {
                allids.add(recordId);
            }

            Collections.shuffle(allids);

            allids = allids.subList(0, Math.min(subset, allids.size() - 1));

            recordIds = new LinkedBlockingQueue<>(allids);

            log.info(String.format("Loaded %d records in %.3f sec created subset of size:"
                    + subset, recordIds.size(),
                    (System.currentTimeMillis() - start) / 1000.0));
        }

        data.collection = coll;
        data.initialized = true;
        data.recordIds = recordIds;
        
        String batchProp = context.getProperties().getProperty(BATCH_SIZE);
        if (batchProp != null) {
            try {
                int batchSize = Integer.parseInt(batchProp);
                data.batchSize = batchSize;
            } catch (Throwable t) {
                log.warning("Given batch size is not parsable!");
            }
        }

        log.info(String.format("Setup %d record IDs for loading in %.3f sec", recordIds.size(),
                (System.currentTimeMillis() - start) / 1000.0));
    }

    private BlockingQueue<I> shuffleRecordIds(BlockingQueue<I> recordIds) {
        List<I> allids = new ArrayList<>();
        for (I recordId : recordIds) {
            allids.add(recordId);
        }
        Collections.shuffle(allids);
        return new LinkedBlockingQueue<>(allids);
    }

    @Override
    public TaskCreator<MetaDataRecord<I>, I> createLoader(final ExecutionContext<MetaDataRecord<I>, I> context)
            throws WorkflowStartFailedException {
        if (!isFinished(context)) {
            return new TaskCreator<MetaDataRecord<I>, I>() {
                @SuppressWarnings({"unchecked", "rawtypes"})
                @Override
                public void run() {
                    try {
                        Data container = context.getValue(DATA_KEY);

                        List<I> poll = new ArrayList<>();
                        while (!container.recordIds.isEmpty() && poll.size() < container.batchSize) {
                            poll.add((I) container.recordIds.poll());
                        }

                        if (!poll.isEmpty()) {
                            List<MetaDataRecord<I>> metaDataRecords = context.getStorageEngine().getMetaDataRecords(poll);
                            MetaDataRecord<I>[] mdrs = metaDataRecords.toArray(new MetaDataRecord[metaDataRecords.size()]);

                            if (mdrs.length != poll.size()) {
                                log.warning(String.format(
                                        "Requested %d records from storage backend, but got back %d records!",
                                        poll.size(), mdrs.length));
                            }

                            for (int i = 0; i < mdrs.length; i++) {
                                MetaDataRecord<I> mdr = mdrs[i];

                                if (mdr != null) {
                                    if (mdr instanceof MetaDataRecordBean) {
                                        ((MetaDataRecordBean) mdr).setCollection(container.collection);
                                    }

                                    Task<MetaDataRecord<I>, I> task = new Task<>(
                                            mdr, context);
                                    synchronized (getQueue()) {
                                        getQueue().offer(task);
                                    }
                                } else {
                                    log.log(Level.WARNING, "Requested ''{0}'' record is null!", poll.get(i));
                                }
                            }
                        }
                    } catch (StorageEngineException t) {
                        throw new WorkflowStartFailedException("Failed to retrieve MDRs from storage. "
                                + context.getExecution().toString(), t);
                    } finally {
                        setDone(true);
                    }
                }
            };
        }
        return null;
    }

    @Override
    public int getTotalSize(ExecutionContext<MetaDataRecord<I>, I> context) {
        @SuppressWarnings("unchecked")
        Data<I> value = context.getValue(DATA_KEY);
        if (value != null) {
            return context.getValue(DATA_KEY).recordIds.size();
        } else {
            return 0;
        }
    }

    @Override
    public boolean isFinished(ExecutionContext<MetaDataRecord<I>, I> context) {
        @SuppressWarnings("unchecked")
        Data<I> value = context.getValue(DATA_KEY);
        return value.initialized && value.recordIds.isEmpty();
    }

    @Override
    public void completed(ExecutionContext<MetaDataRecord<I>, I> context) throws WorkflowStartFailedException {
        context.getValue(DATA_KEY).recordIds.clear();
    }

    private Collection<I> extractCollection(ExecutionContext<MetaDataRecord<I>, I> context) {
        Collection<I> coll = null;

        UimDataSet<I> dataSet = context.getDataSet();
        if (dataSet instanceof Collection) {
            coll = (Collection<I>) dataSet;
        } else if (dataSet instanceof Request) {
            coll = ((Request<I>) dataSet).getCollection();
        } else if (dataSet instanceof MetaDataRecord) {
            MetaDataRecord<I> record = (MetaDataRecord<I>) dataSet;
            coll = record.getCollection();
        } else {
            throw new WorkflowStartFailedException("Unsupported dataset <"
                    + context.getDataSet() + ">");
        }

        return coll;
    }

    private BlockingQueue<I> loadRecordIds(ExecutionContext<MetaDataRecord<I>, I> context) {
        BlockingQueue<I> recordIds;

        StorageEngine<I> storage = context.getStorageEngine();
        LoggingEngine<I> logging = context.getLoggingEngine();

        UimDataSet<I> dataSet = context.getDataSet();
        if (dataSet instanceof Collection) {

            boolean lastrequest = Boolean.parseBoolean(context.getProperties().getProperty(
                    COLLECTION_LAST_REQUEST, "false"));

            String propFromDate = context.getProperties().getProperty(COLLECTION_FROM_REQUEST);
            Date fromDate = propFromDate != null ? parseDate(propFromDate) : null;

            Collection<I> coll = (Collection<I>) dataSet;

            if (lastrequest) {
                recordIds = loadRecordIdsFromLastRequest(coll, storage, logging);
            } else if (fromDate != null) {
                recordIds = loadRecordIdsFromLastDate(coll, fromDate, storage, logging);
            } else {
                recordIds = loadRecordIdsFromCollection(coll, storage);
            }
        } else if (dataSet instanceof Request) {
            recordIds = loadRecordIdsFromRequest(storage, (Request<I>) dataSet);
        } else if (dataSet instanceof MetaDataRecord) {
            recordIds = loadRecordIdsFromMetaDataRecord(storage, (MetaDataRecord<I>) dataSet);
        } else {
            throw new WorkflowStartFailedException("Unsupported dataset <"
                    + context.getDataSet() + ">");
        }

        return recordIds;
    }

    private BlockingQueue<I> loadRecordIdsFromMetaDataRecord(StorageEngine<I> storage, MetaDataRecord<I> record) {
        BlockingQueue<I> records = new LinkedBlockingQueue<>();
        records.add(record.getId());
        return records;
    }

    private BlockingQueue<I> loadRecordIdsFromLastDate(Collection<I> coll, Date thisFrom, StorageEngine<I> storage, LoggingEngine<I> loggingEngine) throws WorkflowStartFailedException {
        BlockingQueue<I> recordIds;

        List<I> allids = new ArrayList<>();

        // retrieve the "last" request
        StringBuilder logMessage = new StringBuilder();
        BlockingQueue<Request<I>> requests;
        try {
            requests = storage.getRequests(coll);
        } catch (StorageEngineException e) {
            throw new WorkflowStartFailedException("Requests for collection '" + coll.getId()
                    + "' could not be retrieved!", e);
        }

        for (Request<I> candidate : requests) {
            if (candidate.getDateFrom().after(thisFrom)) {
                if (logMessage.length() > 0) {
                    logMessage.append(";");
                }
                logMessage.append(candidate.getId());

                BlockingQueue<I> ids;
                try {
                    ids = storage.getMetaDataRecordIdsByRequest(candidate);
                } catch (StorageEngineException e) {
                    throw new WorkflowStartFailedException("Records for request '" + candidate.getId()
                            + "' could not be retrieved!", e);
                }

                for (I id : ids) {
                    allids.add(id);
                }
            }
        }
        if (!allids.isEmpty()) {
            recordIds = new LinkedBlockingQueue<>(allids);

            log.log(Level.INFO, "Loaded requests:{0}", logMessage);
            if (loggingEngine != null) {
                loggingEngine.log(Level.INFO, "BatchWorkflowStart",
                        "Processing requests:" + logMessage);
            }
        } else {
            throw new WorkflowStartFailedException(
                    "No requests found for collection <" + coll.getMnemonic()
                    + "> after " + thisFrom);
        }

        return recordIds;
    }

    private BlockingQueue<I> loadRecordIdsFromLastRequest(Collection<I> coll, StorageEngine<I> storage, LoggingEngine<I> loggingEngine) {
        BlockingQueue<I> records;

        Request<I> request = null;
        // retrieve the "last" request
        BlockingQueue<Request<I>> requests;
        try {
            requests = storage.getRequests(coll);
        } catch (StorageEngineException e) {
            throw new WorkflowStartFailedException("Could not load requests for Collection '" + coll.getId()
                    + "'!", e);
        }

        for (Request<I> candidate : requests) {
            if (request == null || request.getDate().before(candidate.getDate())) {
                request = candidate;
            }
        }

        if (request != null) {
            try {
                records = storage.getMetaDataRecordIdsByRequest(request);
            } catch (StorageEngineException e) {
                throw new WorkflowStartFailedException("Records for request '" + request.getId()
                        + "' could not be retrieved!", e);
            }

            log.log(Level.INFO, "Loaded request:{0}", request.getId());
            if (loggingEngine != null) {
                loggingEngine.log(Level.INFO, "BatchWorkflowStart",
                        "Processing only last request:" + request.getId());
            }
        } else {
            throw new WorkflowStartFailedException(
                    "No request found for collection <" + coll.getMnemonic() + ">");
        }

        return records;
    }

    private Date parseDate(String propFrom) {
        Date thisFrom;
        try {
            thisFrom = ISO8601_DATE_FORMAT.get().parse(propFrom);
        } catch (ParseException e) {
            try {
                thisFrom = DEFAULT_DATE_FORMAT.get().parse(propFrom);
            } catch (ParseException ex) {
                Logger.getLogger(BatchWorkflowStart.class.getName()).log(Level.SEVERE, null, ex);
                thisFrom = null;
            }
        }
        return thisFrom;
    }

    private BlockingQueue<I> loadRecordIdsFromCollection(Collection<I> coll, StorageEngine<I> storage) {
        try {
            return storage.getMetaDataRecordIdsByCollection(coll);
        } catch (StorageEngineException e) {
            throw new WorkflowStartFailedException("Records for collection '" + coll.getId()
                    + "' could not be retrieved!", e);
        }
    }

    private BlockingQueue<I> loadRecordIdsFromRequest(StorageEngine<I> storage, Request<I> request) {
        try {
            return storage.getMetaDataRecordIdsByRequest(request);
        } catch (StorageEngineException e) {
            throw new WorkflowStartFailedException("Records for request '" + request.getId()
                    + "' could not be retrieved!", e);
        }
    }

    final static class Data<I> implements Serializable {

        boolean initialized = false;
        Collection<?> collection = null;
        BlockingQueue<I> recordIds = null;
        int batchSize = DEFAULT_BATCH_SIZE;
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
