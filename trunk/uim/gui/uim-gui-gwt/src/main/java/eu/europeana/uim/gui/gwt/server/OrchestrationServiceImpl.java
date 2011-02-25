package eu.europeana.uim.gui.gwt.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import eu.europeana.uim.api.ActiveExecution;
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
public class OrchestrationServiceImpl extends AbstractOSGIRemoteServiceServlet implements OrchestrationService {

	private static Logger log = Logger.getLogger(OrchestrationServiceImpl.class.getName());

	public OrchestrationServiceImpl() {
		super();
	}

	private Map<Long, Provider> wrappedProviders = new HashMap<Long, Provider>();

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
			List<eu.europeana.uim.store.Provider> providers = getEngine().getRegistry().getStorage().getAllProviders();
			if (providers != null) {
				for (eu.europeana.uim.store.Provider p : providers) {
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
			eu.europeana.uim.store.Provider p = getEngine().getRegistry().getStorage().getProvider(provider);
			List<eu.europeana.uim.store.Collection> cols = getEngine().getRegistry().getStorage().getCollections(p);
			for (eu.europeana.uim.store.Collection col : cols) {
				res.add(new Collection(col.getId(), col.getName(), getWrappedProvider(provider), getEngine().getRegistry().getStorage().getTotalByCollection(col)));
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
			List<eu.europeana.uim.store.Collection> cols = getEngine().getRegistry().getStorage().getAllCollections();
			for (eu.europeana.uim.store.Collection col : cols) {
				res.add(new Collection(col.getId(), col.getName(), getWrappedProvider(col.getProvider().getId()), getEngine().getRegistry().getStorage().getTotalByCollection(col)));
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
			for (eu.europeana.uim.store.Execution execution : getEngine().getRegistry().getStorage().getAllExecutions()) {
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
			for (eu.europeana.uim.store.Execution execution : getEngine().getRegistry().getStorage().getAllExecutions()) {
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
			eu.europeana.uim.store.Collection c = getEngine().getRegistry().getStorage().getCollection(collection);
			if (c == null) {
				throw new RuntimeException("Error: cannot find collection " + collection);
			}
			eu.europeana.uim.workflow.Workflow w = getWorkflow(workflow);
			Execution execution = new Execution();

			GWTProgressMonitor monitor = new GWTProgressMonitor(execution);
			ActiveExecution ae = getEngine().getOrchestrator().executeWorkflow(w, c, monitor);
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
			eu.europeana.uim.store.Provider p = getEngine().getRegistry().getStorage().getProvider(provider);
			if (p == null) {
				throw new RuntimeException("Error: cannot find provider " + provider);
			}
			eu.europeana.uim.workflow.Workflow w = getWorkflow(workflow);
			Execution execution = new Execution();
			GWTProgressMonitor monitor = new GWTProgressMonitor(execution);
			ActiveExecution ae = getEngine().getOrchestrator().executeWorkflow(w, p, monitor);
			populateWrappedExecution(execution, ae, w, p);
			return execution;
		} catch (StorageEngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void populateWrappedExecution(Execution execution, ActiveExecution ae, eu.europeana.uim.workflow.Workflow w, UimEntity dataset) {
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
		Provider wrapped = wrappedProviders.get(provider);
		if (wrapped == null) {
			try {
				eu.europeana.uim.store.Provider p = getEngine().getRegistry().getStorage().getProvider(provider);
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
			wrapped.setDataSet(e.getDataSet().getIdentifier());
			wrapped.setName(e.getWorkflowName() + "/" + e.getDataSet().getIdentifier());
			wrappedExecutions.put(execution, wrapped);
		} else {
			// update what may have changed
			wrapped.setActive(e.isActive());
			wrapped.setEndTime(e.getEndTime());
            if(e.isActive() && e instanceof ActiveExecution) {
                ActiveExecution ae = (ActiveExecution) e;
                wrapped.setScheduled(ae.getScheduledSize());
                wrapped.setCompleted(ae.getCompletedSize());
                wrapped.setFailure(ae.getFailureSize());
            }
		}
		return wrapped;
	}

	private eu.europeana.uim.workflow.Workflow getWorkflow(String name) {
		eu.europeana.uim.workflow.Workflow workflow = getEngine().getRegistry().getWorkflow(name);
		if (workflow == null) {
			throw new RuntimeException("Error: cannot find workflow " + workflow);
		}
		return workflow;
	}

	@Override
	public Integer getCollectionTotal(Long collection) {
		try {
			return getEngine().getRegistry().getStorage().getTotalByCollection(getEngine().getRegistry().getStorage().getCollection(collection));
		} catch (StorageEngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<StepStatus> getStatus(String workflow) {
		List<StepStatus> res = new ArrayList<StepStatus>();
        /* FIXME re-implement this
		List<WorkflowStepStatus> runtimeStatus = getEngine().getOrchestrator().getRuntimeStatus(getWorkflow(workflow));
		for (WorkflowStepStatus wss : runtimeStatus) {
			StepStatus ss = new StepStatus(wss.getStep().getIdentifier(), (wss.getParent() != null ? wss.getParent().getIdentifier() : null), wss.queueSize(), wss.successes(), wss.failures());
			res.add(ss);
		}
		*/
		return res;
	}

}

