package eu.europeana.uim.gui.cp.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.gui.cp.client.services.RepositoryService;
import eu.europeana.uim.gui.cp.shared.CollectionDTO;
import eu.europeana.uim.gui.cp.shared.ProviderDTO;
import eu.europeana.uim.gui.cp.shared.StepStatusDTO;
import eu.europeana.uim.gui.cp.shared.WorkflowDTO;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.workflow.Workflow;

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
        List<Workflow> workflows = getEngine().getRegistry().getWorkflows();
        if (workflows != null) {
            List<String> blackListKey = new ArrayList<String>() {
                {
                    add("Workflow Blacklist");
                }
            };

            LinkedHashMap<String, List<String>> globalResources = getEngine().getRegistry().getResourceEngine().getGlobalResources(
                    blackListKey);
            List<String> blacklist = globalResources.get(blackListKey.get(0));
            Set<String> blackSet = blacklist != null && blacklist.size() > 0 ? new HashSet<String>(
                    blacklist) : null;
            for (Workflow w : workflows) {
                if (blackSet != null && blackSet.contains(w.getIdentifier())) {
                    continue;
                }
                WorkflowDTO wd = new WorkflowDTO(w.getIdentifier(), w.getName(), w.getDescription());
                res.add(wd);
            }

            Collections.sort(res, new Comparator<WorkflowDTO>() {
                @Override
                public int compare(WorkflowDTO o1, WorkflowDTO o2) {
                    String name1 = o1.getName() != null ? o1.getName() : "";
                    String name2 = o2.getName() != null ? o2.getName() : "";
                    return name1.compareTo(name2);
                }
            });
        }
        return res;
    }

    @Override
    public List<ProviderDTO> getProviders() {
        List<ProviderDTO> res = new ArrayList<ProviderDTO>();
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorageEngine();
            List<Provider<Long>> providers = storage.getAllProviders();
            if (providers != null) {
                for (Provider<Long> p : providers) {
                    ProviderDTO provider = getWrappedProviderDTO(p.getId());
                    res.add(provider);
                }

                Collections.sort(res, new Comparator<ProviderDTO>() {
                    @Override
                    public int compare(ProviderDTO o1, ProviderDTO o2) {
                        String name1 = o1.getName() != null ? o1.getName() : "";
                        String name2 = o2.getName() != null ? o2.getName() : "";
                        return name1.compareTo(name2);
                    }
                });
            }
        } catch (StorageEngineException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public List<CollectionDTO> getCollections(Long provider) {
        List<CollectionDTO> res = new ArrayList<CollectionDTO>();
        try {
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorageEngine();
            Provider<Long> p = storage.getProvider(provider);
            List<Collection<Long>> cols = storage.getCollections(p);
            for (Collection<Long> col : cols) {
                CollectionDTO collDTO = new CollectionDTO(col.getId());
                collDTO.setName(col.getName());
                collDTO.setMnemonic(col.getMnemonic());
                collDTO.setProvider(getWrappedProviderDTO(provider));
                collDTO.setLanguage(col.getLanguage());
                collDTO.setOaiBaseUrl(col.getOaiBaseUrl(false));
                collDTO.setOaiMetadataPrefix(col.getOaiMetadataPrefix(false));
                collDTO.setOaiSet(col.getOaiSet());
                res.add(collDTO);
            }

            Collections.sort(res, new Comparator<CollectionDTO>() {
                @Override
                public int compare(CollectionDTO o1, CollectionDTO o2) {
                    String name1 = o1.getName() != null ? o1.getName() : "";
                    String name2 = o2.getName() != null ? o2.getName() : "";
                    return name1.compareTo(name2);
                }
            });
        } catch (StorageEngineException e) {
            e.printStackTrace();
        }
        return res;
    }

    private ProviderDTO getWrappedProviderDTO(Long provider) {
        StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorageEngine();
        ProviderDTO wrapped = wrappedProviderDTOs.get(provider);
        if (wrapped == null) {
            try {
                Provider<Long> p = storage.getProvider(provider);
                wrapped = new ProviderDTO(p.getId());
                wrapped.setName(p.getName());
                wrapped.setMnemonic(p.getMnemonic());
                wrapped.setOaiBaseUrl(p.getOaiBaseUrl());
                wrapped.setOaiMetadataPrefix(p.getOaiMetadataPrefix());
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
            StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorageEngine();
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

    @Override
    public Boolean updateProvider(ProviderDTO provider) {
        StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorageEngine();

        Provider<Long> prov;
        try {
            if (provider.getId() == null) {
                prov = storage.createProvider();
            } else {
                prov = storage.getProvider(provider.getId());
            }
        } catch (StorageEngineException e) {
            e.printStackTrace();
            return false;
        }

        prov.setMnemonic(provider.getMnemonic());
        prov.setName(provider.getName());
        prov.setOaiBaseUrl(provider.getOaiBaseUrl());
        prov.setOaiMetadataPrefix(provider.getOaiMetadataPrefix());

        try {
            storage.updateProvider(prov);
        } catch (StorageEngineException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public Boolean updateCollection(CollectionDTO collection) {
        StorageEngine<Long> storage = (StorageEngine<Long>)getEngine().getRegistry().getStorageEngine();

        Collection<Long> coll;
        try {
            if (collection.getId() == null) {
                Provider<Long> prov = storage.getProvider(collection.getProvider().getId());
                coll = storage.createCollection(prov);
            } else {
                coll = storage.getCollection(collection.getId());
            }
        } catch (StorageEngineException e) {
            e.printStackTrace();
            return false;
        }

        coll.setMnemonic(collection.getMnemonic());
        coll.setName(collection.getName());
        coll.setLanguage(collection.getLanguage());
        coll.setOaiBaseUrl(collection.getOaiBaseUrl(false));
        coll.setOaiMetadataPrefix(collection.getOaiMetadataPrefix(false));
        coll.setOaiSet(collection.getOaiSet());

        try {
            storage.updateCollection(coll);
        } catch (StorageEngineException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
