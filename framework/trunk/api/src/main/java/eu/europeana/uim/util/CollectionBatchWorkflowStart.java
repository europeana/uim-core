package eu.europeana.uim.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.plugin.source.AbstractWorkflowStart;
import eu.europeana.uim.plugin.source.Task;
import eu.europeana.uim.plugin.source.TaskCreator;
import eu.europeana.uim.plugin.source.WorkflowStartFailedException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.UimDataSet;

/**
 * Loads batches from the storage and pulls them into as tasks.
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 14, 2011
 */
public class CollectionBatchWorkflowStart<I> extends AbstractWorkflowStart<Collection<I>, I> {
    /**
     * Key to retrieve own data from context.
     */
    @SuppressWarnings("rawtypes")
    private static TKey<CollectionBatchWorkflowStart, Data> DATA_KEY = TKey.register(
                                                                             CollectionBatchWorkflowStart.class,
                                                                             "data", Data.class);

    /**
     * Creates a new instance of this class.
     */
    public CollectionBatchWorkflowStart() {
        super(
                "Batch Loading Workflow Start",
                "Workflow start which loads batches from the storage and provides them in batches of 250 to the system.");
    }

    @Override
    public int getPreferredThreadCount() {
        return 2;
    }

    @Override
    public int getMaximumThreadCount() {
        return 4;
    }

    @Override
    public List<String> getParameters() {
        return new ArrayList<String>();
    }

    @Override
    public void initialize(ExecutionContext<Collection<I>, I> context)
            throws WorkflowStartFailedException {
        Collection<I> coll = null;

        UimDataSet<I> dataSet = context.getDataSet();
        if (dataSet instanceof Collection) {
            coll = (Collection<I>)dataSet;
        } else {
            throw new WorkflowStartFailedException("Unsupported dataset <" + context.getDataSet() +
                                                   ">");
        }

        // this is for testing to allow injection of a data object
        Data data = context.getValue(DATA_KEY);
        if (data == null) {
            data = new Data();
            context.putValue(DATA_KEY, data);
        }
        data.collection = coll;
        data.initialized = true;
    }

    @Override
    public TaskCreator<Collection<I>, I> createLoader(
            final ExecutionContext<Collection<I>, I> context) throws WorkflowStartFailedException {
        if (!isFinished(context)) { return new TaskCreator<Collection<I>, I>() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                try {
                    Data container = context.getValue(DATA_KEY);
                    if (!container.finished) {
                        Task<Collection<I>, I> task = new Task<Collection<I>, I>(
                                (Collection<I>)container.collection, context);
                        synchronized (getQueue()) {
                            getQueue().offer(task);
                        }
                        container.finished = true;
                    }
                } catch (Throwable t) {
                    throw new RuntimeException("Failed to prepare task for collection. " +
                                               context.getExecution().toString(), t);
                } finally {
                    setDone(true);
                }
            }
        }; }
        return null;
    }

    @Override
    public int getTotalSize(ExecutionContext<Collection<I>, I> context) {
        Data value = context.getValue(DATA_KEY);
        if (value != null) {
            return context.getValue(DATA_KEY).collection != null ? 1 : 0;
        } else {
            return 0;
        }
    }

    @Override
    public boolean isFinished(ExecutionContext<Collection<I>, I> context) {
        Data value = context.getValue(DATA_KEY);
        return value.finished;
    }

    @Override
    public void completed(ExecutionContext<Collection<I>, I> context)
            throws WorkflowStartFailedException {
        context.getValue(DATA_KEY).collection = null;
    }

    /**
     * container for runtime information.
     * 
     * @author Andreas Juffinger (andreas.juffinger@kb.nl)
     * @since Feb 28, 2011
     */
    protected final static class Data implements Serializable {
        /** initialized yes/no */
        public boolean       initialized = false;

        /** collection */
        public Collection<?> collection  = null;

        /** initialized yes/no */
        public boolean       finished    = false;
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
