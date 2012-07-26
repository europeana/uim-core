package eu.europeana.uim.gui.cp.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.gui.cp.client.services.RepositoryService;
import eu.europeana.uim.gui.cp.server.engine.RepoxEngine;
import eu.europeana.uim.gui.cp.shared.CollectionDTO;
import eu.europeana.uim.gui.cp.shared.ProviderDTO;
import eu.europeana.uim.gui.cp.shared.StepStatusDTO;
import eu.europeana.uim.gui.cp.shared.WorkflowDTO;
import eu.europeana.uim.repox.RepoxControlledVocabulary;
import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.RepoxService;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.StandardControlledVocabulary;
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
    private final static Logger log = Logger.getLogger(RepositoryServiceImpl.class.getName());

    /**
     * Creates a new instance of this class.
     */
    public RepositoryServiceImpl() {
        super();
    }

    @Override
    public Boolean synchronizeRepox() {
        boolean success = false;
        if (getEngine() instanceof RepoxEngine) {
            RepoxService repoxService = ((RepoxEngine)getEngine()).getRepoxService();
            if (repoxService != null) {
                StorageEngine<Serializable> storage = (StorageEngine<Serializable>)getEngine().getRegistry().getStorageEngine();

                try {
                    List<Provider<Serializable>> providers = storage.getAllProviders();
                    for (Provider<Serializable> provider : providers) {
                        boolean update = synchronizeProvider(repoxService, provider);
                        if (update) {
                            storage.updateProvider(provider);
                        }
                    }

                    List<Collection<Serializable>> collections = storage.getAllCollections();
                    for (Collection<Serializable> collection : collections) {
                        boolean update = synchronizeCollection(repoxService, collection);
                        if (update) {
                            storage.updateCollection(collection);
                        }
                    }
                } catch (StorageEngineException e) {
                    throw new RuntimeException(
                            "Could not synchronize providers and collections with repox!", e);
                }
            }
        }
        return success;
    }

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
        } else {
            log.log(Level.WARNING, "Workflows are null!");
        }
        return res;
    }

    @Override
    public List<ProviderDTO> getProviders() {
        List<ProviderDTO> res = new ArrayList<ProviderDTO>();

        StorageEngine<Serializable> storage = (StorageEngine<Serializable>)getEngine().getRegistry().getStorageEngine();
        if (storage == null) {
            log.log(Level.SEVERE, "Storage connection is null!");
            return res;
        }

        List<Provider<Serializable>> providers = null;
        try {
            providers = storage.getAllProviders();
        } catch (Throwable t) {
            log.log(Level.WARNING, "Could not retrieve providers!", t);
        }

        if (providers != null) {
            for (Provider<Serializable> p : providers) {
                try {
                    ProviderDTO provider = getWrappedProviderDTO(p);
                    if (provider != null)
                        res.add(provider);
                } catch (Throwable t) {
                    log.log(Level.WARNING, "Error in copy data to DTO of provider!", t);
                }
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

        return res;
    }

    @Override
    public List<CollectionDTO> getCollections(Serializable provider) {
        List<CollectionDTO> res = new ArrayList<CollectionDTO>();

        StorageEngine<Serializable> storage = (StorageEngine<Serializable>)getEngine().getRegistry().getStorageEngine();
        if (storage == null) {
            log.log(Level.SEVERE, "Storage connection is null!");
            return res;
        }

        Provider<Serializable> p = null;
        try {
            p = storage.getProvider(provider);
        } catch (Throwable t) {
            log.log(Level.WARNING, "Could not retrieve provider '" + provider + "'!", t);
        }

        if (p != null) {
            List<Collection<Serializable>> cols = null;
            try {
                cols = storage.getCollections(p);
            } catch (Throwable t) {
                log.log(Level.WARNING, "Could not retrieve collections for provider '" + provider +
                                       "'!", t);
            }

            if (cols != null) {
                for (Collection<Serializable> col : cols) {
                    CollectionDTO collDTO = getWrappedCollectionDTO(col);
                    if (collDTO != null)
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
            }
        }

        return res;
    }

    private ProviderDTO getWrappedProviderDTO(Provider<Serializable> pro) {
        if (pro == null) return null;
        
        ProviderDTO provDTO = new ProviderDTO(pro.getId());
        provDTO.setName(pro.getName());
        provDTO.setMnemonic(pro.getMnemonic());
        provDTO.setOaiBaseUrl(pro.getOaiBaseUrl());
        provDTO.setOaiMetadataPrefix(pro.getOaiMetadataPrefix());
        provDTO.setCountry(pro.getValue(StandardControlledVocabulary.COUNTRY));
        return provDTO;
    }

    private CollectionDTO getWrappedCollectionDTO(Collection<Serializable> col) {
        if (col == null) return null;
        
        CollectionDTO collDTO = new CollectionDTO(col.getId());
        collDTO.setName(col.getName());
        collDTO.setMnemonic(col.getMnemonic());
        collDTO.setProvider(getWrappedProviderDTO(col.getProvider()));
        collDTO.setLanguage(col.getLanguage());
        collDTO.setOaiBaseUrl(col.getOaiBaseUrl(false));
        collDTO.setOaiMetadataPrefix(col.getOaiMetadataPrefix(false));
        collDTO.setOaiSet(col.getOaiSet());
        collDTO.setCountry(col.getValue(StandardControlledVocabulary.COUNTRY));
        collDTO.setUpdateDate(col.getValue(RepoxControlledVocabulary.LAST_UPDATE_DATE));
        collDTO.setHarvestStatus(col.getValue(RepoxControlledVocabulary.COLLECTION_HARVESTING_STATE));
        collDTO.setHarvestRecords(col.getValue(RepoxControlledVocabulary.COLLECTION_HARVESTED_RECORDS));
        return collDTO;
    }

    @Override
    public Integer getCollectionTotal(Serializable collection) {
        StorageEngine<Serializable> storage = (StorageEngine<Serializable>)getEngine().getRegistry().getStorageEngine();
        if (storage == null) {
            log.log(Level.SEVERE, "Storage connection is null!");
            return 0;
        }

        int num = 0;
        try {
            num = storage.getTotalByCollection(storage.getCollection(collection));
        } catch (Throwable t) {
            log.log(Level.WARNING, "Could not get number of records for collection '" + collection +
                                   "'!", t);
        }
        return num;
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
    public Boolean clearProviderValues(ProviderDTO provider) {
        StorageEngine<Serializable> storage = (StorageEngine<Serializable>)getEngine().getRegistry().getStorageEngine();
        if (storage == null) {
            log.log(Level.SEVERE, "Storage connection is null!");
            return false;
        }

        Provider<Serializable> prov;
        try {
            if (provider.getId() != null) {
                prov = storage.getProvider(provider.getId());
                prov.values().clear();
                storage.updateProvider(prov);
            }
        } catch (Throwable t) {
            log.log(Level.WARNING, "Could not retrieve provider '" + provider + "'!", t);
            return false;
        }
        return true;
    }

    @Override
    public Boolean updateProvider(ProviderDTO provider) {
        StorageEngine<Serializable> storage = (StorageEngine<Serializable>)getEngine().getRegistry().getStorageEngine();
        if (storage == null) {
            log.log(Level.SEVERE, "Storage connection is null!");
            return false;
        }

        Provider<Serializable> prov;
        try {
            if (provider.getId() == null) {
                prov = storage.createProvider();
            } else {
                prov = storage.getProvider(provider.getId());
            }
        } catch (Throwable t) {
            log.log(Level.WARNING, "Could not retrieve provider '" + provider + "'!", t);
            return false;
        }

        prov.setMnemonic(provider.getMnemonic());
        prov.setName(provider.getName());
        prov.setOaiBaseUrl(provider.getOaiBaseUrl());
        prov.setOaiMetadataPrefix(provider.getOaiMetadataPrefix());

        try {
            storage.updateProvider(prov);
        } catch (Throwable t) {
            log.log(Level.WARNING, "Could not update provider '" + provider + "'!", t);
            return false;
        }

        return true;
    }

    @Override
    public Boolean clearCollectionValues(CollectionDTO collection) {
        StorageEngine<Serializable> storage = (StorageEngine<Serializable>)getEngine().getRegistry().getStorageEngine();
        if (storage == null) {
            log.log(Level.SEVERE, "Storage connection is null!");
            return false;
        }

        Collection<Serializable> coll;
        try {
            if (collection.getId() != null) {
                coll = storage.getCollection(collection.getId());
                coll.values().clear();
                storage.updateCollection(coll);
            }
        } catch (Throwable t) {
            log.log(Level.WARNING, "Could not retrieve collection '" + collection + "'!", t);
            return false;
        }
        return true;
    }

    @Override
    public Boolean updateCollection(CollectionDTO collection) {
        StorageEngine<Serializable> storage = (StorageEngine<Serializable>)getEngine().getRegistry().getStorageEngine();
        if (storage == null) {
            log.log(Level.SEVERE, "Storage connection is null!");
            return false;
        }

        Collection<Serializable> coll;
        try {
            if (collection.getId() == null) {
                Provider<Serializable> prov = storage.getProvider(collection.getProvider().getId());
                coll = storage.createCollection(prov);
            } else {
                coll = storage.getCollection(collection.getId());
            }
        } catch (Throwable t) {
            log.log(Level.WARNING, "Could not retrieve collection '" + collection + "'!", t);
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
        } catch (Throwable t) {
            log.log(Level.WARNING, "Could not update collection '" + collection + "'!", t);
            return false;
        }

        return true;
    }

    @Override
    public ProviderDTO synchronizeRepoxProvider(Serializable providerId) {
        ProviderDTO prov = null;
        if (getEngine() instanceof RepoxEngine) {
            RepoxService repoxService = ((RepoxEngine)getEngine()).getRepoxService();
            if (repoxService != null) {
                StorageEngine<Serializable> storage = (StorageEngine<Serializable>)getEngine().getRegistry().getStorageEngine();
                try {
                    Provider<Serializable> provider = storage.getProvider(providerId);

                    boolean update = synchronizeProvider(repoxService, provider);
                    if (update) {
                        storage.updateProvider(provider);
                    }

                    prov = getWrappedProviderDTO(provider);
                } catch (StorageEngineException e) {
                    throw new RuntimeException("Could not read/write provider '" + providerId +
                                               "'!", e);
                }
            }
        }
        return prov;
    }

    private boolean synchronizeProvider(RepoxService repoxService, Provider<Serializable> provider) {
        Map<String, String> beforeValues = new HashMap<String, String>(provider.values());

        try {
            repoxService.updateProvider(provider);
        } catch (RepoxException e) {
            throw new RuntimeException("Could not update provider to repox!", e);
        }
        try {
            repoxService.synchronizeProvider(provider);
        } catch (RepoxException e) {
            throw new RuntimeException("Could not synchronize provider to repox!", e);
        }

        Map<String, String> afterValues = provider.values();
        boolean update = beforeValues.size() != afterValues.size();
        if (!update) {
            for (Entry<String, String> entry : afterValues.entrySet()) {
                String beforeValue = beforeValues.get(entry.getKey());
                if (StringUtils.equals(beforeValue, entry.getValue())) {
                    update = true;
                    break;
                }
            }
        }
        return update;
    }

    @Override
    public CollectionDTO synchronizeRepoxCollection(Serializable collectionId) {
        log.info("Synchronization with REPOX for collection '" + collectionId + "' was triggered!");
        
        CollectionDTO coll = null;
        if (getEngine() instanceof RepoxEngine) {
            RepoxService repoxService = ((RepoxEngine)getEngine()).getRepoxService();
            if (repoxService != null) {
                StorageEngine<Serializable> storage = (StorageEngine<Serializable>)getEngine().getRegistry().getStorageEngine();
                try {
                    Collection<Serializable> collection = storage.getCollection(collectionId);

                    boolean update = synchronizeCollection(repoxService, collection);
                    if (update) {
                        storage.updateCollection(collection);
                    }

                    coll = getWrappedCollectionDTO(collection);
                } catch (StorageEngineException e) {
                    throw new RuntimeException("Could not read/write collection '" + collectionId +
                                               "'!", e);
                }
            }
        }
        return coll;
    }

    private boolean synchronizeCollection(RepoxService repoxService,
            Collection<Serializable> collection) {
        Map<String, String> beforeValues = new HashMap<String, String>(collection.values());

        try {
            repoxService.updateCollection(collection);
        } catch (RepoxException e) {
            log.severe("Could not update collection to repox! " + e);
            throw new RuntimeException("Could not update collection to repox!", e);
        }
        try {
            repoxService.synchronizeCollection(collection);
        } catch (RepoxException e) {
            log.severe("Could not synchronize collection to repox! " + e);
            throw new RuntimeException("Could not synchronize collection to repox!", e);
        }

        Map<String, String> afterValues = collection.values();

        boolean update = beforeValues.size() != afterValues.size();
        if (!update) {
            for (Entry<String, String> entry : afterValues.entrySet()) {
                String beforeValue = beforeValues.get(entry.getKey());
                if (!StringUtils.equals(beforeValue, entry.getValue())) {
                    update = true;
                    break;
                }
            }
        }

        return update;
    }
}
