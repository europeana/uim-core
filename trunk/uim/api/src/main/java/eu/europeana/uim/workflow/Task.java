package eu.europeana.uim.workflow;

import java.util.Queue;
import java.util.Set;

import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.IngestionPluginFailedException;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * Generic task to processed by the workflow pipeline. It extends Runnable to provide getter and
 * setter for all kinds of additional information.
 * 
 * @param <I>
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public class Task<I> implements Runnable {
    private TaskStatus                status    = TaskStatus.NEW;
    private Throwable                 throwable;

    private Queue<Task<I>>            success   = null;
    private Queue<Task<I>>            failure   = null;

    private Set<Task<I>>              assigned  = null;

    private boolean                   savepoint = false;
    private boolean                   mandatory = false;
    private IngestionPlugin           step;

    private final MetaDataRecord<I>   record;
    private final StorageEngine<I>    engine;
    private final ExecutionContext<I> context;
    private boolean                   successfulProcessing;

    /**
     * Creates a new instance of this class.
     * 
     * @param record
     * @param engine
     * @param context
     */
    public Task(MetaDataRecord<I> record, StorageEngine<I> engine, ExecutionContext<I> context) {
        super();
        this.record = record;
        this.engine = engine;
        this.context = context;
    }

    @Override
    public void run() {
        successfulProcessing = step.processRecord(record, context);
    }

    /**
     * @return status of the task
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * @param status
     *            status of the task
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Initialization of the task.
     */
    public void setUp() {
        // nothing to do right now
    }

    /**
     * Task is done or finished and should therefore teared down.
     */
    public void tearDown() {
        // nothing to do right now
    }

    /**
     * @return Is it a save point?
     */
    public boolean isSavepoint() {
        return savepoint;
    }

    /**
     * @param savepoint
     *            Is it a save point?
     */
    public void setSavepoint(boolean savepoint) {
        this.savepoint = savepoint;
    }

    /**
     * Save the content to the storage backend.
     * 
     * @throws StorageEngineException
     */
    public void save() throws StorageEngineException {
        engine.updateMetaDataRecord(record);
    }

    /**
     * @return plugin to be executed by this task
     */
    public IngestionPlugin getStep() {
        return step;
    }

    /**
     * @param step
     *            plugin to be executed by this task
     * @param mandatory
     *            Is this plugin mandatory, so that a unsuccessful processing of the record lead to
     *            a failure or is it optional and not processed records can still be further
     *            processed?
     */
    public void setStep(IngestionPlugin step, boolean mandatory) {
        this.step = step;
        this.mandatory = mandatory;
    }

    /**
     * @param success
     *            queue with successful handled tasks
     */
    public void setOnSuccess(Queue<Task<I>> success) {
        this.success = success;
    }

    /**
     * @return queue with successful handled tasks
     */
    public Queue<Task<I>> getOnSuccess() {
        return success;
    }

    /**
     * @param failure
     *            queue with failed tasks (but not fatal ones for the whole workflow)
     */
    public void setOnFailure(Queue<Task<I>> failure) {
        this.failure = failure;
    }

    /**
     * @return queue with failed tasks (but not fatal ones for the whole workflow)
     */
    public Queue<Task<I>> getOnFailure() {
        return failure;
    }

    /**
     * @param throwable
     *            contains a thrown exception, if it is of type
     *            {@link IngestionPluginFailedException} the workflow must be teared down as a
     *            plugin is not able to proceed work
     */
    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    /**
     * @return thrown exception, if it is of type {@link IngestionPluginFailedException} the
     *         workflow must be teared down as a plugin is not able to proceed work
     */
    public Throwable getThrowable() {
        return throwable;
    }

    /**
     * @return Is this plugin mandatory, so that a unsuccessful processing of the record lead to a
     *         failure or is it optional and not processed records can still be further processed?
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * @return true, if processing of a {@link MetaDataRecord} was successful, otherwise false
     */
    public boolean isSuccessfulProcessing() {
        return successfulProcessing;
    }

    /**
     * @return record that is processed
     */
    public MetaDataRecord<I> getMetaDataRecord() {
        return record;
    }

    /**
     * @return execution context
     */
    public ExecutionContext<I> getExecutionContext() {
        return context;
    }

    /**
     * @return set of assigned tasks
     */
    public Set<Task<I>> getAssigned() {
        return assigned;
    }

    /**
     * @param assigned
     *            set of assigned tasks
     */
    public void setAssigned(Set<Task<I>> assigned) {
        this.assigned = assigned;
    }
}
