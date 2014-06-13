/* SugarCrmToUIMServlet.java - created on Aug 8, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.servlet;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import eu.europeana.uim.Registry;
import eu.europeana.uim.external.ExternalService;
import eu.europeana.uim.external.ExternalServiceException;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.sugar.tel.SugarControlledVocabulary;
import eu.europeana.uim.sugar.SugarException;
import eu.europeana.uim.sugar.SugarService;
import eu.europeana.uim.sugar.tel.TELProviderFields;
import java.util.logging.Level;

/**
 * Servlet as a callback for SugarCRM
 *
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date Aug 8, 2011
 */
public class SugarServlet extends AbstractSugarServlet {

    private static final Logger logger = Logger.getLogger(SugarServlet.class.getName());

    private final Registry registry;

    private final SugarService sugarService;

    /**
     * Creates a new instance of this class.
     *
     * @param registry
     */
    public SugarServlet(Registry registry) {
        this.registry = registry;

        SugarService sugar = null;
        List<ExternalService> externalServices = registry.getExternalServices();
        for (ExternalService externalService : externalServices) {
            if (externalService instanceof SugarService) {
                sugar = (SugarService) externalService;
            }
        }
        this.sugarService = sugar;
    }

    @Override
    public boolean updateProvider(String mnemonic, Map<String, String> provider)
            throws SugarException {
        try {
            @SuppressWarnings("unchecked")
            StorageEngine<Serializable> engine = (StorageEngine<Serializable>) registry.getStorageEngine();
            Serializable uimId = engine.getUimId(mnemonic);
            Provider<Serializable> prov = engine.getProvider(uimId);
            if (prov == null) {
                prov = engine.createProvider();
                prov.setMnemonic(mnemonic);
            }

            boolean update;
            if (provider != null) {
                update = getSugarService().synchronizeProvider(prov, provider);
            } else {
                update = getSugarService().synchronizeProvider(prov);
            }

            String consortiaName = prov.getValue(SugarControlledVocabulary.PROVIDER_CONSORTIA_NAME);
            if (consortiaName != null) {
                List<Map<String, String>> providers = getSugarService().filterProviders(true,
                        "accounts.name='" + consortiaName + "'");
                for (Map<String, String> pr : providers) {
                    String consMnemonic = pr.get(TELProviderFields.MNEMONIC.getFieldId());
                    Serializable consUimId = engine.getUimId(consMnemonic);
                    Provider<Serializable> consortia = engine.getProvider(consUimId);
                    boolean add = true;
                    if (consortia == null) {
                        consortia = engine.createProvider();
                        consortia.setMnemonic(consMnemonic);
                        add = getSugarService().synchronizeProvider(prov);
                    }
                    if (add
                            && (!consortia.getRelatedIn().contains(prov) || !prov.getRelatedOut().contains(
                                    consortia))) {
                        consortia.getRelatedIn().add(prov);
                        prov.getRelatedOut().add(consortia);
                        engine.updateProvider(consortia);
                    }
                }
            }

            // update is also false for inactive element
            if (update) {
                engine.updateProvider(prov);

                List<ExternalService> externalServices = registry.getExternalServices();
                for (ExternalService externalService : externalServices) {
                    if (!(externalService instanceof SugarService)) {
                        try {
                            externalService.synchronize(prov, false);
                        } catch (ExternalServiceException ex) {
                            logger.log(Level.SEVERE, "Synchronize with external service failed!", ex);
                        }
                    }
                }
            }
            return update;
        } catch (StorageEngineException e) {
            throw new SugarException("Cannot update provider <" + mnemonic + ">", e);
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean updateCollection(String mnemonic, Map<String, String> collection)
            throws SugarException {
        try {
            StorageEngine<Serializable> engine = (StorageEngine<Serializable>) registry.getStorageEngine();
            Serializable uimId = engine.getUimId(mnemonic);
            Collection<Serializable> coll = engine.getCollection(uimId);
            if (coll == null) {
                String providerMnemonic = getSugarService().getProviderForCollection(mnemonic);
                if (providerMnemonic != null) {
                    Serializable provUimId = engine.getUimId(providerMnemonic);
                    Provider<Serializable> provider = engine.getProvider(provUimId);
                    if (provider != null) {
                        coll = engine.createCollection(provider);
                        coll.setMnemonic(mnemonic);
                    } else {
                        throw new IllegalStateException("Provider <" + providerMnemonic
                                + "> does not exist for collection <"
                                + mnemonic + ">");
                    }
                } else {
                    throw new IllegalStateException("Provider not found for collection <"
                            + mnemonic + ">");
                }
            }

            boolean update;
            if (collection != null) {
                update = getSugarService().synchronizeCollection(coll, collection);
            } else {
                update = getSugarService().synchronizeCollection(coll);
            }

            // update is also false for inactive element
            if (update) {
                engine.updateCollection(coll);
//                if (getRepoxService() != null) {
//                    try {
//                        getRepoxService().updateCollection(coll);
//                    } catch (RepoxException e) {
//                        log.log(Level.WARNING, "Failed to update repox from sugar servlet.", e);
//                    }
//                }
            }
            return update;
        } catch (StorageEngineException e) {
            throw new SugarException("Cannot update collection <" + mnemonic + ">", e);
        }
    }

    /**
     * Returns the sugarService.
     *
     * @return the sugarService
     * @throws SugarException
     */
    public SugarService getSugarService() throws SugarException {
        if (!sugarService.hasActiveSession()) {
            sugarService.login();
        }
        return sugarService;
    }

    @Override
    public List<Map<String, String>> listCollections(boolean activeOnly) throws SugarException {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public List<Map<String, String>> listProviders(boolean activeOnly) throws SugarException {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public String getCollectionMnemonic(Map<String, String> values) throws SugarException {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public String getProviderMnemonic(Map<String, String> values) throws SugarException {
        // return null;
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

}
