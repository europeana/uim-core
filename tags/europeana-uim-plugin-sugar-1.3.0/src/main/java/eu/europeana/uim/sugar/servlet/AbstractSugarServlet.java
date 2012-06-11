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

import eu.europeana.uim.sugarcrm.SugarException;

/**
 * Servlet as a callback for SugarCRM
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date Aug 8, 2011
 */
public abstract class AbstractSugarServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(AbstractSugarServlet.class.getName());

    /**
     * Creates a new instance of this class.
     */
    public AbstractSugarServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        resp.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
        resp.setHeader("Pragma", "no-cache"); // HTTP 1.0
        resp.setDateHeader("Expires", 0); // prevents caching at the proxy server

        String action = req.getParameter("action");
        String type = req.getParameter("type");
        String id = req.getParameter("id");

        log.fine("Sugar update request:" + req.getParameterMap());
        if ("true".equals(req.getParameter("async"))) {
            asynchUpdate(resp, action, type, id);
        } else {
            synchUpdate(resp, action, type, id);
        }
    }

    private void synchUpdate(HttpServletResponse resp, String action, String type, String id)
            throws ServletException {
        if ("update".equals(action)) {
            try {
                if ("organization".equals(type)) {

                    StringBuilder builder = new StringBuilder();
                    if ("*".equals(id)) {
                        List<Map<String, String>> providers = listProviders(true);
                        for (Map<String, String> provider : providers) {
                            String mnemonic = getProviderMnemonic(provider);
                            if (mnemonic != null) {
                                boolean update = updateProvider(mnemonic, provider);
                                log.info("Updated/Synched provider with sugar:" + mnemonic);

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
                        log.info("Updated/Synched provider with sugar:" + id);

                        builder.append(id + ": " + (update ? "UPD" : "NaN"));
                        resp.setStatus(200);
                        resp.getWriter().write(" DONE");
                    }
                } else if ("collection".equals(type)) {
                    StringBuilder builder = new StringBuilder();
                    if ("*".equals(id)) {
                        List<Map<String, String>> collections = listCollections(true);
                        for (Map<String, String> collection : collections) {
                            String mnemonic = getCollectionMnemonic(collection);
                            if (mnemonic != null) {
                                boolean update = updateCollection(mnemonic, collection);
                                log.info("Updated/Synched collection with sugar:" + mnemonic);

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
                        log.info("Updated/Synched collection with sugar:" + id);

                        builder.append(id + ": " + (update ? "UPD" : "NaN"));
                        resp.setStatus(200);
                        resp.getWriter().write(" DONE");
                    }

                } else {
                    resp.sendError(400,
                            "Illegal arguments, neither collection nor provider id was given.");
                }
            } catch (Throwable t) {
                log.log(Level.SEVERE, "Error during update", t);

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

    private void asynchUpdate(HttpServletResponse resp, final String action, final String type,
            final String id) throws ServletException {

        Runnable async = new Runnable() {
            public void run() {
                if ("update".equals(action)) {
                    try {
                        if ("organization".equals(type)) {

                            StringBuilder builder = new StringBuilder();
                            if ("*".equals(id)) {
                                List<Map<String, String>> providers = listProviders(true);
                                for (Map<String, String> provider : providers) {
                                    String mnemonic = getProviderMnemonic(provider);
                                    if (mnemonic != null) {
                                        boolean update = updateProvider(mnemonic, provider);
                                        log.info("Updated/Synched provider with sugar:" + mnemonic);

                                        if (builder.length() > 0) {
                                            builder.append(", \n");
                                        }
                                        builder.append(mnemonic + ": " + (update ? "UPD" : "NaN"));
                                    }
                                }

                                log.info(" DONE comlete provider update:" + providers.size());
                            } else {
                                boolean update = updateProvider(id, null);
                                log.info("Updated/Synched provider with sugar:" + id);
                            }
                        } else if ("collection".equals(type)) {
                            StringBuilder builder = new StringBuilder();
                            if ("*".equals(id)) {
                                List<Map<String, String>> collections = listCollections(true);
                                for (Map<String, String> collection : collections) {
                                    String mnemonic = getCollectionMnemonic(collection);
                                    if (mnemonic != null) {
                                        boolean update = updateCollection(mnemonic, collection);
                                        log.info("Updated/Synched collection with sugar:" +
                                                 mnemonic);

                                        if (builder.length() > 0) {
                                            builder.append(", \n");
                                        }
                                        builder.append(mnemonic + ": " + (update ? "UPD" : "NaN"));
                                    }
                                }

                                log.info(" DONE complete collection update:" + collections.size());
                            } else {
                                boolean update = updateCollection(id, null);
                                log.info("Updated/Synched collection with sugar:" + id);
                            }

                        } else {
                            log.severe("Illegal arguments, neither collection nor provider id was given.");
                        }
                    } catch (Throwable t) {
                        log.log(Level.SEVERE, "Error during update", t);
                    }
                } else {
                    log.warning("Action: <" + action + "> is invalid.");
                }
            }
        };

        new Thread(async).start();
        resp.setStatus(200);
        try {
            resp.getWriter().write("Started asynch processing.");
        } catch (IOException e) {
            log.log(Level.SEVERE, "Cannot write to response.", e);
        }
    }

    /**
     * @param mnemonic
     * @param provider
     * @return true iff the provider has been updated
     * @throws SugarException
     */
    public abstract boolean updateProvider(String mnemonic, Map<String, String> provider)
            throws SugarException;

    /**
     * @param mnemonic
     * @param collection
     * @return true iff the collection has been updated
     * @throws SugarException
     */
    public abstract boolean updateCollection(String mnemonic, Map<String, String> collection)
            throws SugarException;

    /**
     * @param activeOnly
     * @return a list of all collections in sugar
     * @throws SugarException
     */
    public abstract List<Map<String, String>> listCollections(boolean activeOnly)
            throws SugarException;

    /**
     * @param activeOnly
     * @return a list of all providers in sugar
     * @throws SugarException
     */
    public abstract List<Map<String, String>> listProviders(boolean activeOnly)
            throws SugarException;

    /**
     * @param values
     * @return the mnemonic value of the collection
     * @throws SugarException
     */
    public abstract String getCollectionMnemonic(Map<String, String> values) throws SugarException;

    /**
     * @param values
     * @return the mnemonic value of the provider
     * @throws SugarException
     */
    public abstract String getProviderMnemonic(Map<String, String> values) throws SugarException;

}
