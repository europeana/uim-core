/* SugarCrmToUIMServlet.java - created on Aug 8, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.repox.rest.servlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.repox.RepoxException;
import eu.europeana.uim.repox.RepoxService;
import eu.europeana.uim.store.Collection;

/**
 * Servlet as a callback for SugarCRM
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date Aug 8, 2011
 */
public class RepoxServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(RepoxServlet.class.getName());

    private Registry            registry;
    private RepoxService        repoxService;

    /**
     * Creates a new instance of this class.
     * 
     * @param registry
     */
    public RepoxServlet(Registry registry) {
        this.registry = registry;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        resp.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
        resp.setHeader("Pragma", "no-cache"); // HTTP 1.0
        resp.setDateHeader("Expires", 0); // prevents caching at the proxy server

        String action = req.getParameter("action");
        String id = req.getParameter("id");

        if ("update".equals(action)) {
            try {
                StorageEngine<Serializable> storageEngine = (StorageEngine<Serializable>)registry.getStorageEngine();

                StringBuilder builder = new StringBuilder();
                if ("*".equals(id)) {
                    List<Collection<Serializable>> collections = storageEngine.getAllCollections();
                    for (Collection<Serializable> coll : collections) {
                        boolean updated = synchronizeCollection(coll);
                        builder.append(coll.getMnemonic());
                        if (updated) {
                            storageEngine.updateCollection(coll);
                            builder.append(" upd, ");
                        } else {
                            builder.append(" same, ");
                        }
                    }
                    resp.setStatus(200);
                    resp.getWriter().write(builder.toString());
                    resp.getWriter().write(" DONE:" + collections.size());
                } else {
                    Collection<Serializable> coll = storageEngine.getCollection(id);
                    boolean updated = synchronizeCollection(coll);
                    builder.append(id + ": " + (updated ? "upd" : "same"));
                    resp.setStatus(200);
                    resp.getWriter().write(" DONE");
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

    private boolean synchronizeCollection(Collection<Serializable> collection) {
        Map<String, String> beforeValues = new HashMap<String, String>(collection.values());

        try {
            repoxService.updateCollection(collection);
        } catch (RepoxException e) {
            throw new RuntimeException("Could not update collection to repox!", e);
        }
        try {
            repoxService.synchronizeCollection(collection);
        } catch (RepoxException e) {
            throw new RuntimeException("Could not synchronize collection to repox!", e);
        }

        boolean update = false;
        Map<String, String> afterValues = collection.values();
        for (Entry<String, String> entry : afterValues.entrySet()) {
            String beforeValue = beforeValues.remove(entry.getKey());
            if (!entry.getValue().equals(beforeValue)) {
                update = true;
                break;
            }
        }
        if (!update && !beforeValues.isEmpty()) {
            update = true;
        }

        return update;
    }

    /**
     * @return repox service
     */
    public RepoxService getRepoxService() {
        return repoxService;
    }

    /**
     * @param repoxService
     *            repox service
     */
    public void setRepoxService(RepoxService repoxService) {
        this.repoxService = repoxService;
    }
}
