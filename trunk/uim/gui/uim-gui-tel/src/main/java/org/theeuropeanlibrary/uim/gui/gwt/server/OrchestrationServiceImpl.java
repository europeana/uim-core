package org.theeuropeanlibrary.uim.gui.gwt.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.theeuropeanlibrary.uim.gui.gwt.client.OrchestrationService;
import org.theeuropeanlibrary.uim.gui.gwt.shared.CollectionDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ExecutionDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ParameterDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.ProviderDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.StepStatusDTO;
import org.theeuropeanlibrary.uim.gui.gwt.shared.WorkflowDTO;

import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.DataSet;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.workflow.Workflow;
import eu.europeana.uim.workflow.WorkflowStart;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class OrchestrationServiceImpl extends AbstractOSGIRemoteServiceServlet implements
        OrchestrationService {

    private static Logger log = Logger.getLogger(OrchestrationServiceImpl.class.getName());

    public OrchestrationServiceImpl() {
        super();
    }

    private Map<Long, ProviderDTO>  wrappedProviderDTOs  = new HashMap<Long, ProviderDTO>();

    private Map<Long, ExecutionDTO> wrappedExecutionDTOs = new HashMap<Long, ExecutionDTO>();

    @Override
    public List<WorkflowDTO> getWorkflows() {
        List<WorkflowDTO> res = new ArrayList<WorkflowDTO>();
        List<eu.europeana.uim.workflow.Workflow> workflows = getEngine().getRegistry().getWorkflows();
        if (workflows != null) {
            for (eu.europeana.uim.workflow.Workflow w : workflows) {
                WorkflowDTO wd = new WorkflowDTO(w.getName(), w.getDescription());
                res.add(wd);
            }
        }
        return res;
    }
    
    @Override
    public List<ParameterDTO> getParameters(Long provider, Long collection, String workflow) {
        List<ParameterDTO> res = new ArrayList<ParameterDTO>();
        if (workflow != null) {
            Workflow w = getWorkflow(workflow);
            WorkflowStart start = w.getStart();
            for (String param : start.getParameters()) {
                res.add(new ParameterDTO(param));
            }
            for (IngestionPlugin i : w.getSteps()) {
                for (String param : i.getParameters()) {
                    res.add(new ParameterDTO(param));
                }
            }
        }
        return res;
    }

    @Override
    public List<ProviderDTO> getProviders() {
        List<ProviderDTO> res = new ArrayList<ProviderDTO>();
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorage();
            List<Provider<Long>> providers = storage.getAllProviders();
            if (providers != null) {
                for (Provider<Long> p : providers) {
                    ProviderDTO provider = getWrappedProviderDTO(p.getId());
                    res.add(provider);
                }
            }
        } catch (StorageEngineException e) {
            res.add(new ProviderDTO(0l, "Failed to load provider. Notify system administrator.", null));
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public List<CollectionDTO> getCollections(Long provider) {
        List<CollectionDTO> res = new ArrayList<CollectionDTO>();
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorage();
            Provider<Long> p = storage.getProvider(provider);
            List<Collection<Long>> cols = storage.getCollections(p);
            for (Collection<Long> col : cols) {
                res.add(new CollectionDTO(col.getId(), col.getName(), col.getMnemonic(), getWrappedProviderDTO(provider),
                        storage.getTotalByCollection(col)));
            }
        } catch (StorageEngineException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<CollectionDTO> getAllCollections() {
        List<CollectionDTO> res = new ArrayList<CollectionDTO>();
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorage();
            List<Collection<Long>> cols = storage.getAllCollections();
            for (Collection<Long> col : cols) {
                res.add(new CollectionDTO(col.getId(), col.getName(), col.getMnemonic(),
                        getWrappedProviderDTO(col.getProvider().getId()),
                        storage.getTotalByCollection(col)));
            }
        } catch (StorageEngineException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<ExecutionDTO> getActiveExecutions() {
        List<ExecutionDTO> r = new ArrayList<ExecutionDTO>();
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorage();
            for (Execution<Long> execution : storage.getAllExecutions()) {
                if (execution.isActive()) {
                    r.add(getWrappedExecutionDTO(execution.getId(), execution));
                }
            }
        } catch (StorageEngineException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public List<ExecutionDTO> getPastExecutions() {
        List<ExecutionDTO> r = new ArrayList<ExecutionDTO>();
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorage();
            for (Execution<Long> execution : storage.getAllExecutions()) {
                if (!execution.isActive()) {
                    r.add(getWrappedExecutionDTO(execution.getId(), execution));
                }
            }
        } catch (StorageEngineException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public ExecutionDTO startCollection(String workflow, Long collection, String executionName) {
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorage();
            Collection<Long> c = storage.getCollection(collection);
            if (c == null) { throw new RuntimeException("Error: cannot find collection " +
                                                        collection); }
            eu.europeana.uim.workflow.Workflow w = getWorkflow(workflow);
            ExecutionDTO execution = new ExecutionDTO();

            GWTProgressMonitor monitor = new GWTProgressMonitor(execution);

            ActiveExecution ae = getEngine().getOrchestrator().executeWorkflow(w, c);
            ae.getMonitor().addListener(monitor);
            populateWrappedExecutionDTO(execution, ae, w, c, executionName);

            return execution;
        } catch (StorageEngineException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ExecutionDTO startProvider(String workflow, Long provider, String executionName) {
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorage();
            Provider<Long> p = storage.getProvider(provider);
            if (p == null) { throw new RuntimeException("Error: cannot find provider " + provider); }
            eu.europeana.uim.workflow.Workflow w = getWorkflow(workflow);
            ExecutionDTO execution = new ExecutionDTO();
            GWTProgressMonitor monitor = new GWTProgressMonitor(execution);
            ActiveExecution<Long> ae = (ActiveExecution<Long>)getEngine().getOrchestrator().executeWorkflow(w, p);
            ae.getMonitor().addListener(monitor);

            populateWrappedExecutionDTO(execution, ae, w, p, executionName);
            return execution;
        } catch (StorageEngineException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private void populateWrappedExecutionDTO(ExecutionDTO execution, ActiveExecution<Long> ae,
            eu.europeana.uim.workflow.Workflow w, DataSet<Long> dataset, String executionName) {
        execution.setId(ae.getId());
        execution.setName(executionName);
        execution.setWorkflow(w.getName());
        execution.setCompleted(ae.getCompletedSize());
        execution.setFailure(ae.getFailureSize());
        execution.setScheduled(ae.getScheduledSize());
        execution.setDone(ae.isFinished());
        execution.setStartTime(ae.getStartTime());
        execution.setDataSet(dataset.toString());
        wrappedExecutionDTOs.put(ae.getId(), execution);
    }

    @Override
    public ExecutionDTO getExecution(Long id) {
        return wrappedExecutionDTOs.get(id);
    }

    private ProviderDTO getWrappedProviderDTO(Long provider) {
        StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorage();
        ProviderDTO wrapped = wrappedProviderDTOs.get(provider);
        if (wrapped == null) {
            try {
                Provider<Long> p = storage.getProvider(provider);
                wrapped = new ProviderDTO(p.getId(), p.getName(), p.getMnemonic());
                wrappedProviderDTOs.put(provider, wrapped);
            } catch (StorageEngineException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return wrapped;
    }

    private ExecutionDTO getWrappedExecutionDTO(Long execution, Execution e) {
        ExecutionDTO wrapped = wrappedExecutionDTOs.get(execution);
        if (wrapped == null) {
            wrapped = new ExecutionDTO();
            wrapped.setId(execution);
            wrapped.setActive(e.isActive());
            wrapped.setStartTime(e.getStartTime());
            wrapped.setEndTime(e.getEndTime());
            wrapped.setDataSet(e.getDataSet().getId().toString());
            wrapped.setName(e.getWorkflowName() + "/" + e.getDataSet().getId().toString());
            wrappedExecutionDTOs.put(execution, wrapped);
        } else {
            // update what may have changed
            wrapped.setActive(e.isActive());
            wrapped.setEndTime(e.getEndTime());
            if (e.isActive() && e instanceof ActiveExecution) {
                ActiveExecution ae = (ActiveExecution)e;
                wrapped.setScheduled(ae.getScheduledSize());
                wrapped.setCompleted(ae.getCompletedSize());
                wrapped.setFailure(ae.getFailureSize());
            }
        }
        return wrapped;
    }

    private eu.europeana.uim.workflow.Workflow getWorkflow(String name) {
        eu.europeana.uim.workflow.Workflow workflow = getEngine().getRegistry().getWorkflow(name);
        if (workflow == null) { throw new RuntimeException("Error: cannot find workflow " +
                                                           workflow); }
        return workflow;
    }

    @Override
    public Integer getCollectionTotal(Long collection) {
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorage();
            return storage.getTotalByCollection(storage.getCollection(collection));
        } catch (StorageEngineException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<StepStatusDTO> getStatus(String workflow) {
        List<StepStatusDTO> res = new ArrayList<StepStatusDTO>();
        /*
         * FIXME re-implement this List<WorkflowStepStatusDTO> runtimeStatus =
         * getEngine().getOrchestrator().getRuntimeStatus(getWorkflow(workflow)); for
         * (WorkflowStepStatusDTO wss : runtimeStatus) { StepStatusDTO ss = new
         * StepStatusDTO(wss.getStep().getIdentifier(), (wss.getParent() != null ?
         * wss.getParent().getIdentifier() : null), wss.queueSize(), wss.successes(),
         * wss.failures()); res.add(ss); }
         */
        return res;
    }
}
