/* SugarCrmToUIMServlet.java - created on Aug 8, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.servlet;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.RepoxService;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.sugarcrm.SugarException;
import eu.europeana.uim.sugarcrm.SugarService;

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
    private RepoxService        repoxService;

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

            // update is also false for inactive element
            if (update) {
                engine.updateProvider(prov);
                if (getRepoxService() != null) {
                    try {
                        getRepoxService().updateProvider(prov);
                    } catch (RepoxException e) {
                        log.log(Level.WARNING, "Failed to update repox from sugar servlet.", e);
                    }
                }
            }
            return update;
        } catch (StorageEngineException e) {
            throw new SugarException("Cannot update provider <" + mnemonic + ">", e);
        }

    }

    @Override
    public boolean updateCollection(String mnemonic, Map<String, String> collection)
            throws SugarException {

        try {
            @SuppressWarnings("unchecked")
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
                if (getRepoxService() != null) {
                    try {
                        getRepoxService().updateCollection(coll);
                    } catch (RepoxException e) {
                        log.log(Level.WARNING, "Failed to update repox from sugar servlet.", e);
                    }
                }
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

    /**
     * Returns the repoxService.
     * 
     * @return the repoxService
     */
    public RepoxService getRepoxService() {
        return repoxService;
    }

    /**
     * Sets the repoxService to the given value.
     * 
     * @param repoxService
     *            the repoxService to set
     */
    public void setRepoxService(RepoxService repoxService) {
        this.repoxService = repoxService;
        log.info("REpox service SET for sugar servlet.");
    }

    /**
     * Sets the repoxService to the given value.
     * 
     * @param repoxService
     *            the repoxService to set
     */
    public void unsetRepoxService(RepoxService repoxService) {
        this.repoxService = null;
        log.info("Repox service UNSET for repox servlet.");
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
