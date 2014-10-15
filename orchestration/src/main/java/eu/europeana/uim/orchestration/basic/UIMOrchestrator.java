package eu.europeana.uim.orchestration.basic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.Registry;
import eu.europeana.uim.common.progress.RevisableProgressMonitor;
import eu.europeana.uim.logging.LoggingEngine;
import eu.europeana.uim.orchestration.ActiveExecution;
import eu.europeana.uim.orchestration.Orchestrator;
import eu.europeana.uim.orchestration.basic.processing.TaskExecutorRegistry;
import eu.europeana.uim.plugin.ingestion.IngestionPlugin;
import eu.europeana.uim.plugin.source.WorkflowStart;
import eu.europeana.uim.resource.ResourceEngine;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.util.ExecutionLogFileWriter;
import eu.europeana.uim.util.LoggingFacadeEngine;
import eu.europeana.uim.workflow.Workflow;

/**
 * Orchestrates the ingestion job execution. The orchestrator keeps a map of WorkflowProcessors, one
 * for each different workflow. When a new request for workflow execution comes in, the
 * WorkflowProcessor for the Workflow is retrieved, or created if it does not exist.
 * 
 * @param <I>
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class UIMOrchestrator<I> implements Orchestrator<I> {
    private static Logger                     log     = Logger.getLogger(UIMOrchestrator.class.getName());

    private final Registry                    registry;
    private final UIMWorkflowProcessor<I>     processor;

    private final List<ActiveExecution<?, I>> startup = new ArrayList<ActiveExecution<?, I>>();

    /**
     * Creates a new instance of this class.
     * 
     * @param registry
     * @param processor
     */
    @SuppressWarnings({ "unchecked", "rawtypes", "cast" })
    public UIMOrchestrator(Registry registry, UIMWorkflowProcessor processor) {
        this.registry = registry;
        this.processor = (UIMWorkflowProcessor<I>)processor;

        processor.startup();
    }

    @Override
    public String getIdentifier() {
        return UIMOrchestrator.class.getSimpleName();
    }

    /**
     * Executes a given workflow. A new Execution is created and a WorkflowProcessor created if none
     * exists for this workflow. Note, this method queries the registered resource engine for
     * properties known to the plugins of the workflow.
     * 
     * @param workflow
     *            the workflow to execute
     * @param dataset
     *            the data set on which this Execution runs
     * @return a new ActiveExecution for this execution request
     */
    @Override
    public <U extends UimDataSet<I>> ActiveExecution<U, I> executeWorkflow(Workflow<U, I> workflow,
            UimDataSet<I> dataset) {
        return executeWorkflow(workflow, dataset, new Properties());
    }

    /**
     * Executes a given workflow. A new Execution is created and a WorkflowProcessor created if none
     * exists for this workflow. Note, this method queries the registered resource engine for
     * properties known to the plugins of the workflow for all not given properties, but necessary
     * for the plugins.
     * 
     * @param workflow
     *            the workflow to execute
     * @param dataset
     *            the data set on which this Execution runs
     * @return a new ActiveExecution for this execution request
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <U extends UimDataSet<I>> ActiveExecution<U, I> executeWorkflow(Workflow<U, I> workflow,
            UimDataSet<I> dataset, Properties properties) {
        setupProperties(workflow, dataset, properties);

        RevisableProgressMonitor monitor = new RevisableProgressMonitor();

        try {
            StorageEngine<I> storageEngine = (StorageEngine<I>)registry.getStorageEngine();
            LoggingEngine<I> loggingEngine = (LoggingEngine<I>)registry.getLoggingEngine();
            ResourceEngine resourceEngine = registry.getResourceEngine();

            String workingDirectory = null;
            if (resourceEngine == null || resourceEngine.getResourceDirectory() == null) {
                // if we don't have a ResourceEngine or a working directory, use the standard
                // working directory
                workingDirectory = System.getProperty("user.dir");
            } else {
                workingDirectory = resourceEngine.getWorkingDirectory().getCanonicalPath();
                workingDirectory += File.separatorChar + "logging";
                if (!new File(workingDirectory).exists()) {
                    new File(workingDirectory).mkdirs();
                }
            }

            ExecutionLogFileWriter<I> executionLogFileWriter = new ExecutionLogFileWriter<>(
                    workingDirectory);

            Execution<I> e = storageEngine.createExecution(dataset, workflow);
            e.setName(workflow.getName() +
                      "/" +
                      (dataset instanceof Collection ? ((Collection)dataset).getMnemonic()
                              : dataset.toString()));
            e.setActive(true);
            e.setStartTime(new Date());

            // must update to get the id filled.
            storageEngine.updateExecution(e);

            LoggingFacadeEngine<I> loggingFacadeEngine = new LoggingFacadeEngine<I>(e, dataset,
                    workflow, properties, loggingEngine, executionLogFileWriter);
            monitor.addListener(loggingFacadeEngine);
            monitor.beginTask(workflow.getName(), 1);

            try {
                e.setLogFile(executionLogFileWriter.getLogFile(e).getCanonicalPath());
                UIMActiveExecution<U, I> activeExecution = new UIMActiveExecution<U, I>(e,
                        workflow, storageEngine, loggingFacadeEngine, resourceEngine, properties,
                        monitor);
                synchronized (startup) {
                    startup.add(activeExecution);
                }

                storageEngine.updateExecution(e);
                processor.schedule(activeExecution);

                synchronized (startup) {
                    startup.remove(activeExecution);
                }
                return activeExecution;
            } catch (Throwable t) {
                log.log(Level.SEVERE, "Could not update execution details: " + t.getMessage(), t);
            }
        } catch (StorageEngineException e1) {
            log.log(Level.SEVERE, "Could not update execution details: " + e1.getMessage(), e1);
            e1.printStackTrace();
        } catch (IOException e1) {
            log.log(Level.SEVERE,
                    "I/O error while setting up the logging engine: " + e1.getMessage(), e1);
            e1.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void setupProperties(Workflow w, UimDataSet<?> dataset, Properties properties) {
        ResourceEngine resourceEngine = this.registry.getResourceEngine();
        if (resourceEngine != null) {
            Collection collection = null;
            Provider provider = null;
            if (dataset instanceof MetaDataRecord<?>) {
                collection = ((MetaDataRecord<?>)dataset).getCollection();
                provider = collection.getProvider();
            } else if (dataset instanceof Request<?>) {
                collection = ((Request<?>)dataset).getCollection();
                provider = collection.getProvider();
            } else if (dataset instanceof Collection<?>) {
                collection = ((Collection<?>)dataset);
                provider = collection.getProvider();
            }

            List<String> params = new ArrayList<String>();
            WorkflowStart start = w.getStart();
            params.addAll(start.getParameters());
            List<IngestionPlugin<?, I>> steps = w.getSteps();
            for (IngestionPlugin<?, I> i : steps) {
                params.addAll(i.getParameters());
            }

            LinkedHashMap<String, List<String>> globalResources = resourceEngine.getGlobalResources(params);

            LinkedHashMap<String, List<String>> workflowResources = resourceEngine.getWorkflowResources(
                    w, params);
            if (workflowResources != null && workflowResources.size() > 0) {
                for (Entry<String, List<String>> entry : workflowResources.entrySet()) {
                    if (entry.getValue() != null) {
                        globalResources.put(entry.getKey(), entry.getValue());
                    }
                }
            }

            if (provider != null) {
                LinkedHashMap<String, List<String>> providerResources = resourceEngine.getProviderResources(
                        provider, params);
                if (providerResources != null && providerResources.size() > 0) {
                    for (Entry<String, List<String>> entry : providerResources.entrySet()) {
                        if (entry.getValue() != null) {
                            globalResources.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }

            if (collection != null) {
                LinkedHashMap<String, List<String>> collectionResources = resourceEngine.getCollectionResources(
                        collection, params);
                if (collectionResources != null && collectionResources.size() > 0) {
                    for (Entry<String, List<String>> entry : collectionResources.entrySet()) {
                        if (entry.getValue() != null) {
                            globalResources.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }

            for (Entry<String, List<String>> entry : globalResources.entrySet()) {
                if (!properties.containsKey(entry.getKey())) {
                    if (entry.getValue() != null) {
                        if (entry.getValue().size() > 1) {
                            StringBuilder b = new StringBuilder();
                            for (String val : entry.getValue()) {
                                b.append(val);
                                b.append(",");
                            }
                            b.deleteCharAt(b.length() - 1);
                            properties.put(entry.getKey(), b.toString());
                        } else if (entry.getValue().size() == 1) {
                            properties.put(entry.getKey(), entry.getValue().get(0));
                        }
                    }
                }
            }
        }
    }

    @Override
    public List<ActiveExecution<?, I>> getActiveExecutions() {
        synchronized (startup) {
            List<ActiveExecution<?, I>> result = new ArrayList<ActiveExecution<?, I>>(
                    processor.getExecutions());
            result.addAll(startup);
            return result;
        }
    }

    @Override
    public ActiveExecution<?, I> getActiveExecution(I id) {
        for (ActiveExecution<?, I> ae : processor.getExecutions()) {
            if (ae.getExecution().getId().equals(id)) { return ae; }
        }
        return null;
    }

    @Override
    public void pause(ActiveExecution<?, I> execution) {
        execution.setPaused(true);
    }

    @Override
    public void resume(ActiveExecution<?, I> execution) {
        execution.setPaused(false);
    }

    @Override
    public void cancel(ActiveExecution<?, I> execution) {
        execution.getMonitor().setCancelled(true);
    }

    @Override
    public void shutdown() {
        processor.shutdown();
        TaskExecutorRegistry.getInstance().shutdown();
    }
}
