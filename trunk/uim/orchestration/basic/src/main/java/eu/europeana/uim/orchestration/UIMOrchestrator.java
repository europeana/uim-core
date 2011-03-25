package eu.europeana.uim.orchestration;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.Orchestrator;
import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.common.RevisableProgressMonitor;
import eu.europeana.uim.orchestration.processing.TaskExecutorRegistry;
import eu.europeana.uim.store.DataSet;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.workflow.Workflow;

/**
 * Orchestrates the ingestion job execution. The orchestrator keeps a map of WorkflowProcessors, one
 * for each different workflow. When a new request for workflow execution comes in, the
 * WorkflowProcessor for the Workflow is retrieved, or created if it does not exist.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class UIMOrchestrator implements Orchestrator {
    private static Logger              log = Logger.getLogger(UIMOrchestrator.class.getName());

// private static final int BATCH_SIZE = 100;

    private final Registry             registry;
    private final UIMWorkflowProcessor processor;

    /**
     * Creates a new instance of this class.
     * 
     * @param registry
     * @param processor
     */
    public UIMOrchestrator(Registry registry, UIMWorkflowProcessor processor) {
        this.registry = registry;
        this.processor = processor;

        processor.startup();
    }

    @Override
    public String getIdentifier() {
        return UIMOrchestrator.class.getSimpleName();
    }

    /**
     * Executes a given workflow. A new Execution is created and a WorkflowProcessor created if none
     * exists for this workflow
     * 
     * @param w
     *            the workflow to execute
     * @param dataset
     *            the data set on which this Execution runs
     * @return a new ActiveExecution for this execution request
     */
    @Override
    public ActiveExecution<?> executeWorkflow(Workflow w, DataSet<?> dataset) {
        return executeWorkflow(w, dataset, new Properties());
    }

    /**
     * Executes a given workflow. A new Execution is created and a WorkflowProcessor created if none
     * exists for this workflow
     * 
     * @param w
     *            the workflow to execute
     * @param dataset
     *            the data set on which this Execution runs
     * @return a new ActiveExecution for this execution request
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public ActiveExecution<?> executeWorkflow(Workflow w, DataSet dataset, Properties properties) {
        RevisableProgressMonitor monitor = new RevisableProgressMonitor();
        monitor.beginTask(w.getName(), 1);

        try {
            Execution e = registry.getStorage().createExecution(dataset, w.getName());
            e.setActive(true);
            e.setStartTime(new Date());
            registry.getStorage().updateExecution(e);

            try {
                UIMActiveExecution activeExecution = new UIMActiveExecution(e, w,
                        registry.getStorage(), registry.getLoggingEngine(), properties, monitor);
                processor.schedule(activeExecution);

                return activeExecution;
            } catch (Throwable t) {
                log.log(Level.SEVERE, "Could not update execution details: " + t.getMessage(), t);
            }
        } catch (StorageEngineException e1) {
            log.log(Level.SEVERE, "Could not update execution details: " + e1.getMessage(), e1);
            e1.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ActiveExecution<?>> getActiveExecutions() {
        return processor.getExecutions();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <I> ActiveExecution<I> getActiveExecution(I id) {
        for (ActiveExecution<?> ae : processor.getExecutions()) {
            if (ae.getId() == id) { return (ActiveExecution<I>)ae; }
        }
        return null;
    }

    /**
     * @param execution
     */
    public void pause(ActiveExecution<?> execution) {
        execution.setPaused(true);
    }

    /**
     * @param execution
     */
    public void resume(ActiveExecution<?> execution) {
        execution.setPaused(false);
    }

    /**
     * @param execution
     */
    public void cancel(ActiveExecution<?> execution) {
        execution.getMonitor().setCancelled(true);
    }

    @Override
    public void shutdown() {
        processor.shutdown();
        TaskExecutorRegistry.getInstance().shutdown();
    }
}
