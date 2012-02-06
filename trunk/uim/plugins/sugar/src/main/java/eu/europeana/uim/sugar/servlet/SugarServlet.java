/* SugarCrmToUIMServlet.java - created on Aug 8, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
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
public class SugarServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(SugarServlet.class.getName());

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        resp.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
        resp.setHeader("Pragma", "no-cache"); // HTTP 1.0
        resp.setDateHeader("Expires", 0); // prevents caching at the proxy server

        try {
            if (!getSugarService().hasActiveSession()) getSugarService().login();
        } catch (SugarException se) {
            try {
                resp.setStatus(500);
                se.printStackTrace(resp.getWriter());
                return;
            } catch (IOException e) {
                resp.setStatus(500);
                return;
            }
        }

        String action = req.getParameter("action");
        String type =  req.getParameter("type");
        String id = req.getParameter("id");

        if ("update".equals(action)) {
            try {
                if ("organization".equals(type)) {

                    StringBuilder builder = new StringBuilder();
                    if ("*".equals(id)) {
                        List<Map<String, String>> providers = getSugarService().listProviders(true);
                        for (Map<String, String> provider : providers) {
                            String mnemonic = getSugarService().getProviderMnemonic(provider);
                            if (mnemonic != null) {
                                boolean update = updateProvider(mnemonic, provider);
                                if (builder.length() > 0) {
                                    builder.append(", \n");
                                }
                                builder.append(mnemonic + ": " + (update ? "UPD" : "NaN"));
                            }
                        }

                        resp.setStatus(200);
                        resp.getWriter().write(builder.toString());
                        resp.getWriter().write(" DONE:" + providers.size());
                    } else {
                        boolean update = updateProvider(id, null);
                        builder.append(id + ": " + (update ? "UPD" : "NaN"));
                        resp.setStatus(200);
                        resp.getWriter().write(" DONE");
                    }
                } else if ("collection".equals(type)) {
                    StringBuilder builder = new StringBuilder();
                    if ("*".equals(id)) {
                        List<Map<String, String>> collections = getSugarService().listCollections(
                                true);
                        for (Map<String, String> collection : collections) {
                            String mnemonic = getSugarService().getCollectionMnemonic(collection);
                            if (mnemonic != null) {
                                boolean update = updateCollection(mnemonic, collection);
                                if (builder.length() > 0) {
                                    builder.append(", \n");
                                }
                                builder.append(mnemonic + ": " + (update ? "UPD" : "NaN"));
                            }
                        }

                        resp.setStatus(200);
                        resp.getWriter().write(builder.toString());
                        resp.getWriter().write(" DONE:" + collections.size());
                    } else {
                        boolean update = updateCollection(id, null);
                        builder.append(id + ": " + (update ? "UPD" : "NaN"));
                        resp.setStatus(200);
                        resp.getWriter().write(" DONE");
                    }

                } else {
                    resp.sendError(400,
                            "Illegal arguments, neither collection nor provider id was given.");
                }
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Error during update", t);

                resp.setStatus(500);
                // t.printStackTrace(resp.getWriter());
                throw new ServletException(t);
            }
        } else {
            try {
                resp.getWriter().write("Action: <" + action + "> is invalid.");
            } catch (IOException e) {
                throw new RuntimeException("Caused by IOException", e);
            }
            resp.setStatus(400);
        }
    }

    private <I> boolean updateProvider(String mnemonic, Map<String, String> provider)
            throws SugarException, StorageEngineException {
        @SuppressWarnings("unchecked")
        StorageEngine<I> engine = (StorageEngine<I>)registry.getStorageEngine();
        Provider<I> prov = engine.findProvider(mnemonic);
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
        }
        return update;
    }

    private <I> boolean updateCollection(String mnemonic, Map<String, String> collection)
            throws SugarException, StorageEngineException {
        @SuppressWarnings("unchecked")
        StorageEngine<I> engine = (StorageEngine<I>)registry.getStorageEngine();
        Collection<I> coll = engine.findCollection(mnemonic);
        if (coll == null) {
            String providerMnemonic = getSugarService().getProviderForCollection(mnemonic);
            if (providerMnemonic != null) {
                Provider<I> provider = engine.findProvider(providerMnemonic);
                if (provider != null) {
                    coll = engine.createCollection(provider);
                    coll.setMnemonic(mnemonic);
                } else {
                    throw new IllegalStateException("Provider <" + providerMnemonic +
                                                    "> does not exist for collection <" + mnemonic +
                                                    ">");
                }
            } else {
                throw new IllegalStateException("Provider not found for collection <" + mnemonic +
                                                ">");
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
        }
        return update;

    }

    /**
     * Returns the sugarService.
     * 
     * @return the sugarService
     */
    public SugarService getSugarService() {
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

}
