package eu.europeana.uim.gui.gwt.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.gui.gwt.client.OrchestrationService;
import eu.europeana.uim.gui.gwt.shared.Collection;
import eu.europeana.uim.gui.gwt.shared.Execution;
import eu.europeana.uim.gui.gwt.shared.Provider;
import eu.europeana.uim.gui.gwt.shared.StepStatus;
import eu.europeana.uim.gui.gwt.shared.Workflow;
import eu.europeana.uim.store.UimEntity;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class OrchestrationServiceImpl extends AbstractOSGIRemoteServiceServlet implements
        OrchestrationService {

    private static Logger log = Logger.getLogger(OrchestrationServiceImpl.class.getName());

    public OrchestrationServiceImpl() {
        super();
    }

    private Map<Long, Provider>  wrappedProviders  = new HashMap<Long, Provider>();

    private Map<Long, Execution> wrappedExecutions = new HashMap<Long, Execution>();

    @Override
    public List<Workflow> getWorkflows() {
        List<Workflow> res = new ArrayList<Workflow>();
        List<eu.europeana.uim.workflow.Workflow> workflows = getEngine().getRegistry().getWorkflows();
        if (workflows != null) {
            for (eu.europeana.uim.workflow.Workflow w : workflows) {
                res.add(new Workflow(w.getName(), w.getDescription()));
            }
        }
        return res;
    }

    @Override
    public List<Provider> getProviders() {
        List<Provider> res = new ArrayList<Provider>();
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorage();
            List<eu.europeana.uim.store.Provider<Long>> providers = storage.getAllProviders();
            if (providers != null) {
                for (eu.europeana.uim.store.Provider<Long> p : providers) {
                    Provider provider = getWrappedProvider(p.getId());
                    res.add(provider);
                }
            }
        } catch (StorageEngineException e) {
            res.add(new Provider(0l, "Failed to load provider. Notify system administrator."));
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public List<Collection> getCollections(Long provider) {
        List<Collection> res = new ArrayList<Collection>();
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorage();
            eu.europeana.uim.store.Provider<Long> p = storage.getProvider(provider);
            List<eu.europeana.uim.store.Collection<Long>> cols = storage.getCollections(p);
            for (eu.europeana.uim.store.Collection<Long> col : cols) {
                res.add(new Collection(col.getId(), col.getName(), getWrappedProvider(provider),
                        storage.getTotalByCollection(col)));
            }
        } catch (StorageEngineException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<Collection> getAllCollections() {
        List<Collection> res = new ArrayList<Collection>();
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorage();
            List<eu.europeana.uim.store.Collection<Long>> cols = storage.getAllCollections();
            for (eu.europeana.uim.store.Collection<Long> col : cols) {
                res.add(new Collection(col.getId(), col.getName(),
                        getWrappedProvider(col.getProvider().getId()),
                        storage.getTotalByCollection(col)));
            }
        } catch (StorageEngineException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<Execution> getActiveExecutions() {
        List<Execution> r = new ArrayList<Execution>();
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorage();
            for (eu.europeana.uim.store.Execution<Long> execution : storage.getAllExecutions()) {
                if (execution.isActive()) {
                    r.add(getWrappedExecution(execution.getId(), execution));
                }
            }
        } catch (StorageEngineException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public List<Execution> getPastExecutions() {
        List<Execution> r = new ArrayList<Execution>();
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorage();
            for (eu.europeana.uim.store.Execution<Long> execution : storage.getAllExecutions()) {
                if (!execution.isActive()) {
                    r.add(getWrappedExecution(execution.getId(), execution));
                }
            }
        } catch (StorageEngineException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public Execution startCollection(String workflow, Long collection) {
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorage();
            eu.europeana.uim.store.Collection<Long> c = storage.getCollection(collection);
            if (c == null) { throw new RuntimeException("Error: cannot find collection " +
                                                        collection); }
            eu.europeana.uim.workflow.Workflow w = getWorkflow(workflow);
            Execution execution = new Execution();

            GWTProgressMonitor monitor = new GWTProgressMonitor(execution);

            ActiveExecution ae = getEngine().getOrchestrator().executeWorkflow(w, c);
            ae.getMonitor().addListener(monitor);
            populateWrappedExecution(execution, ae, w, c);

            return execution;
        } catch (StorageEngineException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Execution startProvider(String workflow, Long provider) {
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorage();
            eu.europeana.uim.store.Provider<Long> p = storage.getProvider(provider);
            if (p == null) { throw new RuntimeException("Error: cannot find provider " + provider); }
            eu.europeana.uim.workflow.Workflow w = getWorkflow(workflow);
            Execution execution = new Execution();
            GWTProgressMonitor monitor = new GWTProgressMonitor(execution);
            ActiveExecution<Long> ae = (ActiveExecution<Long>)getEngine().getOrchestrator().executeWorkflow(w, p);
            ae.getMonitor().addListener(monitor);

            populateWrappedExecution(execution, ae, w, p);
            return execution;
        } catch (StorageEngineException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private void populateWrappedExecution(Execution execution, ActiveExecution<Long> ae,
            eu.europeana.uim.workflow.Workflow w, UimEntity dataset) {
        execution.setId(ae.getId());
        execution.setName(w.getName() + "/" + dataset.toString());
        execution.setWorkflow(w.getName());
        execution.setCompleted(ae.getCompletedSize());
        execution.setFailure(ae.getFailureSize());
        execution.setScheduled(ae.getScheduledSize());
        execution.setDone(ae.isFinished());
        execution.setStartTime(ae.getStartTime());
        wrappedExecutions.put(ae.getId(), execution);
    }

    @Override
    public Execution getExecution(Long id) {
        return wrappedExecutions.get(id);
    }

    private Provider getWrappedProvider(Long provider) {
        StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorage();
        Provider wrapped = wrappedProviders.get(provider);
        if (wrapped == null) {
            try {
                eu.europeana.uim.store.Provider<Long> p = storage.getProvider(provider);
                wrapped = new Provider(p.getId(), p.getName());
                wrappedProviders.put(provider, wrapped);
            } catch (StorageEngineException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return wrapped;
    }

    private Execution getWrappedExecution(Long execution, eu.europeana.uim.store.Execution e) {
        Execution wrapped = wrappedExecutions.get(execution);
        if (wrapped == null) {
            wrapped = new Execution();
            wrapped.setId(execution);
            wrapped.setActive(e.isActive());
            wrapped.setStartTime(e.getStartTime());
            wrapped.setEndTime(e.getEndTime());
            wrapped.setDataSet(e.getDataSet().getId().toString());
            wrapped.setName(e.getWorkflowName() + "/" + e.getDataSet().getId().toString());
            wrappedExecutions.put(execution, wrapped);
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
    public List<StepStatus> getStatus(String workflow) {
        List<StepStatus> res = new ArrayList<StepStatus>();
        /*
         * FIXME re-implement this List<WorkflowStepStatus> runtimeStatus =
         * getEngine().getOrchestrator().getRuntimeStatus(getWorkflow(workflow)); for
         * (WorkflowStepStatus wss : runtimeStatus) { StepStatus ss = new
         * StepStatus(wss.getStep().getIdentifier(), (wss.getParent() != null ?
         * wss.getParent().getIdentifier() : null), wss.queueSize(), wss.successes(),
         * wss.failures()); res.add(ss); }
         */
        return res;
    }
}
