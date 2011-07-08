package eu.europeana.uim.gui.cp.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.gui.cp.client.services.ExecutionService;
import eu.europeana.uim.gui.cp.shared.ExecutionDTO;
import eu.europeana.uim.gui.cp.shared.ParameterDTO;
import eu.europeana.uim.gui.cp.shared.ProgressDTO;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.UimDataSet;

/**
 * Orchestration service implementation.
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 11, 2011
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ExecutionServiceImpl extends AbstractOSGIRemoteServiceServlet implements
        ExecutionService {
    /**
     * Creates a new instance of this class.
     */
    public ExecutionServiceImpl() {
        super();
    }

    private Map<Long, ExecutionDTO> wrappedExecutionDTOs = new HashMap<Long, ExecutionDTO>();

    @Override
    public List<ExecutionDTO> getActiveExecutions() {
        List<ExecutionDTO> r = new ArrayList<ExecutionDTO>();
        java.util.Collection<ActiveExecution<?>> activeExecutions = getEngine().getRegistry().getOrchestrator().getActiveExecutions();
        for (ActiveExecution<?> execution : activeExecutions) {
            ExecutionDTO exec = getWrappedExecutionDTO((Long)execution.getId(), execution);
            r.add(exec);
        }
        return r;
    }

    @Override
    public List<ExecutionDTO> getPastExecutions() {
        List<ExecutionDTO> r = new ArrayList<ExecutionDTO>();
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorageEngine();
            for (Execution<Long> execution : storage.getAllExecutions()) {
                if (!execution.isActive()) {
                    r.add(getWrappedExecutionDTO(execution.getId(), execution));
                }
            }
        } catch (StorageEngineException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public ExecutionDTO startCollection(String workflow, Long collection, String executionName,
            Set<ParameterDTO> parameters) {
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorageEngine();
            Collection<Long> c = storage.getCollection(collection);
            if (c == null) { throw new RuntimeException("Error: cannot find collection " +
                                                        collection); }
            eu.europeana.uim.workflow.Workflow w = getWorkflow(workflow);
            ExecutionDTO execution = new ExecutionDTO();

            GWTProgressMonitor monitor = new GWTProgressMonitor(execution);

            ActiveExecution<Long> ae;
            if (parameters != null) {
                Properties properties = prepareProperties(parameters);
                ae = (ActiveExecution<Long>)getEngine().getRegistry().getOrchestrator().executeWorkflow(
                        w, c, properties);
            } else {
                ae = (ActiveExecution<Long>)getEngine().getRegistry().getOrchestrator().executeWorkflow(
                        w, c);
            }
            ae.getMonitor().addListener(monitor);
            ae.setName(executionName);
            populateWrappedExecutionDTO(execution, ae, w, c, executionName);

            return execution;
        } catch (StorageEngineException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Properties prepareProperties(Set<ParameterDTO> parameters) {
        Properties properties = new Properties();
        for (ParameterDTO parameter : parameters) {
            if (parameter.getValues() != null) {
                if (parameter.getValues().length > 1) {
                    StringBuilder b = new StringBuilder();
                    for (String val : parameter.getValues()) {
                        b.append(val);
                        b.append(",");
                    }
                    b.deleteCharAt(b.length() - 1);
                    properties.put(parameter.getKey(), b.toString());
                } else if (parameter.getValues().length == 1) {
                    properties.put(parameter.getKey(), parameter.getValues()[0]);
                }
            }
        }
        return properties;
    }

    @Override
    public ExecutionDTO startProvider(String workflow, Long provider, String executionName,
            Set<ParameterDTO> parameters) {
//        try {
//            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorageEngine();
//            Provider<Long> p = storage.getProvider(provider);
//            if (p == null) { throw new RuntimeException("Error: cannot find provider " + provider); }
//            eu.europeana.uim.workflow.Workflow w = getWorkflow(workflow);
            ExecutionDTO execution = new ExecutionDTO();
//            GWTProgressMonitor monitor = new GWTProgressMonitor(execution);

//FIXME            
//            ActiveExecution<Long> ae;
//            if (parameters != null) {
//                Properties properties = prepareProperties(parameters);
//                ae = (ActiveExecution<Long>)getEngine().getRegistry().getOrchestrator().executeWorkflow(
//                        w, p, properties);
//            } else {
//                ae = (ActiveExecution<Long>)getEngine().getRegistry().getOrchestrator().executeWorkflow(
//                        w, p);
//            }
//            ae.setName(executionName);
//
//            ae.getMonitor().addListener(monitor);
//
//            populateWrappedExecutionDTO(execution, ae, w, p, executionName);
            return execution;
//        } catch (StorageEngineException e) {
//            e.printStackTrace();
//        }
//        return null;
    }

    private void populateWrappedExecutionDTO(ExecutionDTO execution, ActiveExecution<Long> ae,
            eu.europeana.uim.workflow.Workflow w, UimDataSet<Long> dataset, String executionName) {
        execution.setId(ae.getId());
        execution.setName(executionName);
        execution.setWorkflow(w.getName());
        execution.setCompleted(ae.getCompletedSize());
        execution.setFailure(ae.getFailureSize());
        execution.setScheduled(ae.getScheduledSize());
        execution.setCanceled(ae.isCanceled());
        execution.setStartTime(ae.getStartTime());
        execution.setDataSet(dataset.toString());
        if (dataset instanceof Collection) {
            execution.setDataSet(((Collection)dataset).getName());
        } else if (dataset instanceof Provider) {
            execution.setDataSet(((Provider)dataset).getName());
        } else {
            execution.setDataSet(dataset.toString());
        }

        ProgressDTO progress = new ProgressDTO();
        progress.setWork(ae.getTotalSize());
        progress.setWorked(ae.getFailureSize() + ae.getCompletedSize());
        progress.setTask(ae.getMonitor().getTask());
        progress.setSubtask(ae.getMonitor().getSubtask());

        execution.setProgress(progress);

        wrappedExecutionDTOs.put(ae.getId(), execution);
    }

    @Override
    public ExecutionDTO getExecution(Long id) {
        ExecutionDTO exec = null;
        ActiveExecution<?> ae = getEngine().getRegistry().getOrchestrator().getActiveExecution(id);
        if (ae != null) {
            exec = getWrappedExecutionDTO((Long)ae.getId(), ae);
        } else {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorageEngine();
            Execution<Long> execution;
            try {
                execution = storage.getExecution(id);
                exec = getWrappedExecutionDTO(execution.getId(), execution);
            } catch (StorageEngineException e) {
                e.printStackTrace();
            }
        }
        return exec;
    }

    private ExecutionDTO getWrappedExecutionDTO(Long execution, Execution e) {
        ExecutionDTO wrapped = wrappedExecutionDTOs.get(execution);
        if (wrapped == null) {
            wrapped = new ExecutionDTO();
            wrapped.setId(execution);
            wrapped.setStartTime(e.getStartTime());
            if (e.getDataSet() instanceof Collection) {
                wrapped.setDataSet(((Collection)e.getDataSet()).getName());
            } else if (e.getDataSet() instanceof Provider) {
                wrapped.setDataSet(((Provider)e.getDataSet()).getName());
            } else {
                wrapped.setDataSet(e.getDataSet().toString());
            }
            wrapped.setName(e.getName());
            wrapped.setWorkflow(e.getWorkflowName());
            wrapped.setProgress(new ProgressDTO());
            wrappedExecutionDTOs.put(execution, wrapped);
        }
        // update what may have changed
        wrapped.setActive(e.isActive());
        if (e.getEndTime() != null) {
            wrapped.setEndTime(e.getEndTime());
        }
        wrapped.setCanceled(e.isCanceled());
        if (e.isActive() && e instanceof ActiveExecution) {
            ActiveExecution ae = (ActiveExecution)e;
            wrapped.setScheduled(ae.getScheduledSize());
            wrapped.setCompleted(ae.getCompletedSize());
            wrapped.setFailure(ae.getFailureSize());
            wrapped.setPaused(ae.isPaused());

            ProgressDTO progress = wrapped.getProgress();
            progress.setWork(ae.getTotalSize());
            progress.setWorked(ae.getFailureSize() + ae.getCompletedSize());
            progress.setTask(ae.getMonitor().getTask());
            progress.setSubtask(ae.getMonitor().getSubtask());
            progress.setDone(!e.isActive());
        } else if (!e.isActive()) {
            wrapped.setScheduled(e.getProcessedCount());
            wrapped.setCompleted(e.getSuccessCount());
            wrapped.setFailure(e.getFailureCount());
        }
        return wrapped;
    }

    private eu.europeana.uim.workflow.Workflow getWorkflow(String identifier) {
        eu.europeana.uim.workflow.Workflow workflow = getEngine().getRegistry().getWorkflow(
                identifier);
        if (workflow == null) { throw new RuntimeException("Error: cannot find workflow " +
                                                           workflow); }
        return workflow;
    }

    @Override
    public Boolean pauseExecution(Long execution) {
        ActiveExecution<Long> ae = getEngine().getRegistry().getOrchestrator().getActiveExecution(
                execution);
        if (ae != null) {
            getEngine().getRegistry().getOrchestrator().pause(ae);
            return ae.isPaused();
        } else {
            return false;
        }
    }

    @Override
    public Boolean resumeExecution(Long execution) {
        ActiveExecution<Long> ae = getEngine().getRegistry().getOrchestrator().getActiveExecution(
                execution);
        if (ae != null) {
            getEngine().getRegistry().getOrchestrator().resume(ae);
            return !ae.isPaused();
        } else {
            return false;
        }
    }

    @Override
    public Boolean cancelExecution(Long execution) {
        ActiveExecution<Long> ae = getEngine().getRegistry().getOrchestrator().getActiveExecution(
                execution);
        if (ae != null) {
            getEngine().getRegistry().getOrchestrator().cancel(ae);
            return ae.isCanceled();
        } else {
            return false;
        }
    }
}
