package eu.europeana.uim.gui.cp.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.gui.cp.client.services.RepositoryService;
import eu.europeana.uim.gui.cp.shared.CollectionDTO;
import eu.europeana.uim.gui.cp.shared.ProviderDTO;
import eu.europeana.uim.gui.cp.shared.StepStatusDTO;
import eu.europeana.uim.gui.cp.shared.WorkflowDTO;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;

/**
 * Orchestration service implementation.
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 11, 2011
 */
@SuppressWarnings({ "unchecked" })
public class RepositoryServiceImpl extends AbstractOSGIRemoteServiceServlet implements
        RepositoryService {

// private static Logger log = Logger.getLogger(OrchestrationServiceImpl.class.getName());

    /**
     * Creates a new instance of this class.
     */
    public RepositoryServiceImpl() {
        super();
    }

    private Map<Long, ProviderDTO> wrappedProviderDTOs = new HashMap<Long, ProviderDTO>();

    @Override
    public List<WorkflowDTO> getWorkflows() {
        List<WorkflowDTO> res = new ArrayList<WorkflowDTO>();
        List<eu.europeana.uim.workflow.Workflow> workflows = getEngine().getRegistry().getWorkflows();
        if (workflows != null) {
            for (eu.europeana.uim.workflow.Workflow w : workflows) {
                WorkflowDTO wd = new WorkflowDTO(w.getIdentifier(), w.getName(), w.getDescription());
                res.add(wd);
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
            res.add(new ProviderDTO(0l, "Failed to load provider. Notify system administrator.",
                    null));
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
                res.add(new CollectionDTO(col.getId(), col.getName(), col.getMnemonic(),
                        getWrappedProviderDTO(provider)));
            }
        } catch (StorageEngineException e) {
            e.printStackTrace();
        }
        return res;
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
                e.printStackTrace();
            }
        }
        return wrapped;
    }

    @Override
    public Integer getCollectionTotal(Long collection) {
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorage();
            return storage.getTotalByCollection(storage.getCollection(collection));
        } catch (StorageEngineException e) {
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
