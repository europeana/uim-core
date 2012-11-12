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

import eu.europeana.uim.gui.cp.client.services.RepositoryService;
import eu.europeana.uim.gui.cp.server.engine.ExternalServiceEngine;
import eu.europeana.uim.gui.cp.shared.CollectionDTO;
import eu.europeana.uim.gui.cp.shared.ProviderDTO;
import eu.europeana.uim.gui.cp.shared.StepStatusDTO;
import eu.europeana.uim.gui.cp.shared.WorkflowDTO;
import eu.europeana.uim.repox.RepoxControlledVocabulary;
import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.RepoxService;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.StandardControlledVocabulary;
import eu.europeana.uim.sugar.SugarException;
import eu.europeana.uim.sugar.SugarService;
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
    public Boolean synchronizeExternalServices() {
        boolean success = false;
        if (getEngine() instanceof ExternalServiceEngine) {
            RepoxService repoxService = ((ExternalServiceEngine)getEngine()).getRepoxService();
            SugarService sugarService = ((ExternalServiceEngine)getEngine()).getSugarService();
            if (repoxService != null || sugarService != null) {
                StorageEngine<Serializable> storage = getStorageEngine();
                storage.command("repository.clearcache");
                storage.checkpoint();

                try {
                    List<Provider<Serializable>> providers = storage.getAllProviders();
                    for (Provider<Serializable> provider : providers) {
                        boolean update = repoxService != null ? synchronizeProviderWithRepox(
                                repoxService, provider) : false;
                        update = sugarService != null ? synchronizeProviderWithSugar(sugarService,
                                provider) : false;
                        if (update) {
                            storage.updateProvider(provider);
                        }
                    }

                    List<Collection<Serializable>> collections = storage.getAllCollections();
                    for (Collection<Serializable> collection : collections) {
                        boolean update = repoxService != null ? synchronizeCollectionWithRepox(
                                repoxService, collection) : false;
                        update = sugarService != null ? synchronizeCollectionWithSugar(
                                sugarService, collection) : false;
                        if (update) {
                            storage.updateCollection(collection);
                        }
                    }

                    storage.checkpoint();
                } catch (Exception e) {
                    log.severe("Could not synchronize providers and collections with repox/sugar! " +
                               e);
                    throw new RuntimeException(
                            "Could not synchronize providers and collections with repox/sugar!", e);
                }
            }
        }
        return success;
    }

    @Override
    public List<WorkflowDTO> getWorkflows() {
        List<WorkflowDTO> res = new ArrayList<WorkflowDTO>();
        res.add(new WorkflowDTO("ALONG", "ALONG", "ALONG"));

        try {
            if (getEngine() != null) {
                res.add(new WorkflowDTO("ENGINE", "ENGINE", "ENGINE"));
            }
        } catch (Throwable t) {
            res.add(new WorkflowDTO("ENGINE FAILED", "ENGINE FAILED", t.getLocalizedMessage()));
            return res;
        }

        try {
            if (getEngine().getRegistry() != null) {
                res.add(new WorkflowDTO("REGISTRY", "REGISTRY", "REGISTRY"));
            }
        } catch (Throwable t) {
            res.add(new WorkflowDTO("REGISTRY FAILED", "REGISTRY FAILED", t.getLocalizedMessage()));
            return res;
        }

        try {
            if (getEngine().getRegistry().getWorkflows() != null) {
                res.add(new WorkflowDTO("WORKFLOW", "WORKFLOW", "WORKFLOW"));
            }
        } catch (Throwable t) {
            res.add(new WorkflowDTO("WORKFLOW FAILED", "WORKFLOW FAILED", t.getLocalizedMessage()));
            return res;
        }
       

        List<Workflow<?, ?>> workflows = getEngine().getRegistry().getWorkflows();
        if (workflows != null) {
            if (getEngine().getRegistry().getWorkflows().size() > 0) {
                res.add(new WorkflowDTO("SOMETHING", "SOMETHING", "SOMETHING"));
            } else {
                res.add(new WorkflowDTO("NOTHING", "NOTHING", "NOTHING"));
            }
            if (true) {
                return res;
            }
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
            for (Workflow<?, ?> w : workflows) {
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
            res.add(new WorkflowDTO("NULL", "NULL", "NULL"));
        }
        return res;
    }

    @Override
    public List<ProviderDTO> getProviders() {
        List<ProviderDTO> res = new ArrayList<ProviderDTO>();

        StorageEngine<Serializable> storage = getStorageEngine();
        if (storage == null) { return res; }

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
                    if (provider != null) res.add(provider);
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

        StorageEngine<Serializable> storage = getStorageEngine();
        if (storage == null) { return res; }

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
                    if (collDTO != null) res.add(collDTO);
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

        String providercountry = pro.getValue(StandardControlledVocabulary.COUNTRY);
        // Check for a europeana specific value
        if (providercountry == null) {
            providercountry = pro.getValue("providerCountry");
        }
        provDTO.setCountry(providercountry);

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
        StorageEngine<Serializable> storage = getStorageEngine();
        if (storage == null) { return 0; }

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
        StorageEngine<Serializable> storage = getStorageEngine();
        if (storage == null) { return false; }

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
        StorageEngine<Serializable> storage = getStorageEngine();
        if (storage == null) { return false; }

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
        StorageEngine<Serializable> storage = getStorageEngine();
        if (storage == null) { return false; }

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
    public ProviderDTO synchronizeProviderExternalServices(Serializable providerId) {
        ProviderDTO prov = null;
        if (getEngine() instanceof ExternalServiceEngine) {
            RepoxService repoxService = ((ExternalServiceEngine)getEngine()).getRepoxService();
            SugarService sugarService = ((ExternalServiceEngine)getEngine()).getSugarService();
            if (repoxService != null || sugarService != null) {
                StorageEngine<Serializable> storage = (StorageEngine<Serializable>)getEngine().getRegistry().getStorageEngine();
                try {
                    Provider<Serializable> provider = storage.getProvider(providerId);

                    boolean update = repoxService != null ? synchronizeProviderWithRepox(
                            repoxService, provider) : false;
                    update = sugarService != null ? synchronizeProviderWithSugar(sugarService,
                            provider) : false;

                    if (update) {
                        storage.updateProvider(provider);
                    }

                    prov = getWrappedProviderDTO(provider);
                } catch (StorageEngineException e) {
                    log.severe("Could not synchronize provider '" + providerId +
                               "' with repox/sugar! " + e);
                    throw new RuntimeException("Could not read/write provider '" + providerId +
                                               "'!", e);
                }
            }
        }
        return prov;
    }

    private boolean synchronizeProviderWithRepox(RepoxService repoxService,
            Provider<Serializable> provider) {
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
                if (!(beforeValue == null ? entry.getValue() == null
                        : beforeValue.equals(entry.getValue()))) {
                    update = true;
                    break;
                }
            }
        }
        return update;
    }

    private boolean synchronizeProviderWithSugar(SugarService sugarService,
            Provider<Serializable> provider) {
        boolean update = false;
        try {
            sugarService.updateProvider(provider);
        } catch (SugarException e) {
            throw new RuntimeException("Could not update provider to repox!", e);
        }
        try {
            update = sugarService.synchronizeProvider(provider);
        } catch (SugarException e) {
            throw new RuntimeException("Could not synchronize provider to repox!", e);
        }
        return update;
    }

    @Override
    public CollectionDTO synchronizeCollectionExternalServices(Serializable collectionId) {
        log.info("Synchronization with external services for collection '" + collectionId +
                 "' was triggered!");

        CollectionDTO coll = null;
        if (getEngine() instanceof ExternalServiceEngine) {
            RepoxService repoxService = ((ExternalServiceEngine)getEngine()).getRepoxService();
            SugarService sugarService = ((ExternalServiceEngine)getEngine()).getSugarService();
            if (repoxService != null || sugarService != null) {
                StorageEngine<Serializable> storage = (StorageEngine<Serializable>)getEngine().getRegistry().getStorageEngine();
                try {
                    Collection<Serializable> collection = storage.getCollection(collectionId);

                    boolean update = repoxService != null ? synchronizeCollectionWithRepox(
                            repoxService, collection) : false;
                    log.info("Synchronization with repox lead to update '" + update + "'!");

                    update = sugarService != null ? synchronizeCollectionWithSugar(sugarService,
                            collection) : false;

                    if (update) {
                        storage.updateCollection(collection);
                    }

                    coll = getWrappedCollectionDTO(collection);
                } catch (StorageEngineException e) {
                    log.severe("Could not synchronize collection '" + collectionId +
                               "' with repox/sugar! " + e);
                    throw new RuntimeException("Could not read/write collection '" + collectionId +
                                               "'!", e);
                }
            }
        }
        return coll;
    }

    private boolean synchronizeCollectionWithRepox(RepoxService repoxService,
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
                if (!(beforeValue == null ? entry.getValue() == null
                        : beforeValue.equals(entry.getValue()))) {
                    update = true;
                    break;
                }
            }
        }

        return update;
    }

    private boolean synchronizeCollectionWithSugar(SugarService sugarService,
            Collection<Serializable> collection) {
        boolean update = false;
        try {
            sugarService.updateCollection(collection);
        } catch (SugarException e) {
            log.severe("Could not update collection to sugar! " + e);
            throw new RuntimeException("Could not update collection to sugar!", e);
        }
        try {
            update = sugarService.synchronizeCollection(collection);
        } catch (SugarException e) {
            log.severe("Could not synchronize collection to sugar! " + e);
            throw new RuntimeException("Could not synchronize collection to sugar!", e);
        }
        return update;
    }

    private StorageEngine<Serializable> getStorageEngine() {
        StorageEngine<Serializable> storage = (StorageEngine<Serializable>)getEngine().getRegistry().getStorageEngine();
        if (storage == null) {
            log.log(Level.SEVERE, "Storage connection is null!");
        }
// else {
// RepoxService repoxService = ((ExternalServiceEngine)getEngine()).getRepoxService();
// SugarService sugarService = ((ExternalServiceEngine)getEngine()).getSugarService();
// if (repoxService == null && sugarService == null) {
// storage.command("repository.clearcache");
// }
// }
        return storage;
    }
}
