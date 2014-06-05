/* SugarCrmToUIMServlet.java - created on Aug 8, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.servlet;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.Registry;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.sugar.SugarControlledVocabulary;
import eu.europeana.uim.sugar.SugarException;
import eu.europeana.uim.sugar.SugarService;
import eu.europeana.uim.sugar.tel.TELProviderFields;

/**
 * Servlet as a callback for SugarCRM
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date Aug 8, 2011
 */
public class SugarServlet extends AbstractSugarServlet {
    private static final Logger log = Logger.getLogger(SugarServlet.class.getName());

    private Registry            registry;

    private SugarService        sugarService;

    /**
     * Creates a new instance of this class.
     * 
     * @param registry
     */
    public SugarServlet(Registry registry) {
        this.registry = registry;
    }

    @Override
    public boolean updateProvider(String mnemonic, Map<String, String> provider)
            throws SugarException {
        try {
            @SuppressWarnings("unchecked")
            StorageEngine<Serializable> engine = (StorageEngine<Serializable>)registry.getStorageEngine();
            Provider<Serializable> prov = engine.findProvider(mnemonic);
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
                    Provider<Serializable> consortia = engine.findProvider(consMnemonic);
                    boolean add = true;
                    if (consortia == null) {
                        consortia = engine.createProvider();
                        consortia.setMnemonic(consMnemonic);
                        add = getSugarService().synchronizeProvider(prov);
                    }
                    if (add &&
                        (!consortia.getRelatedIn().contains(prov) || !prov.getRelatedOut().contains(
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
//                if (getRepoxService() != null) {
//                    try {
//                        getRepoxService().updateProvider(prov);
//                    } catch (RepoxException e) {
//                        log.log(Level.WARNING, "Failed to update repox from sugar servlet.", e);
//                    }
//                }
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
            StorageEngine<Serializable> engine = (StorageEngine<Serializable>)registry.getStorageEngine();
            Collection<Serializable> coll = engine.findCollection(mnemonic);
            if (coll == null) {
                String providerMnemonic = getSugarService().getProviderForCollection(mnemonic);
                if (providerMnemonic != null) {
                    Provider<Serializable> provider = engine.findProvider(providerMnemonic);
                    if (provider != null) {
                        coll = engine.createCollection(provider);
                        coll.setMnemonic(mnemonic);
                    } else {
                        throw new IllegalStateException("Provider <" + providerMnemonic +
                                                        "> does not exist for collection <" +
                                                        mnemonic + ">");
                    }
                } else {
                    throw new IllegalStateException("Provider not found for collection <" +
                                                    mnemonic + ">");
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
        if (!sugarService.hasActiveSession()) sugarService.login();
        return sugarService;
    }

    /**
     * Sets the sugarService to the given value.
     * 
     * @param sugarService
     *            the sugarService to set
     */
    public void setSugarService(SugarService sugarService) {
        this.sugarService = sugarService;
    }

//    /**
//     * Returns the repoxService.
//     * 
//     * @return the repoxService
//     */
//    public RepoxService getRepoxService() {
//        return repoxService;
//    }
//
//    /**
//     * Sets the repoxService to the given value.
//     * 
//     * @param repoxService
//     *            the repoxService to set
//     */
//    public void setRepoxService(RepoxService repoxService) {
//        this.repoxService = repoxService;
//        log.info("REpox service SET for sugar servlet.");
//    }
//
//    /**
//     * Sets the repoxService to the given value.
//     * 
//     * @param repoxService
//     *            the repoxService to set
//     */
//    public void unsetRepoxService(RepoxService repoxService) {
//        this.repoxService = null;
//        log.info("Repox service UNSET for repox servlet.");
//    }

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
