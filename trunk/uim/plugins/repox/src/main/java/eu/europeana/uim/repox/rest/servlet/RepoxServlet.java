/* SugarCrmToUIMServlet.java - created on Aug 8, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.repox.rest.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.repox.RepoxService;

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        resp.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
        resp.setHeader("Pragma", "no-cache"); // HTTP 1.0
        resp.setDateHeader("Expires", 0); // prevents caching at the proxy server

//        try {
//            if (!getSugarService().hasActiveSession()) getSugarService().login();
//        } catch (SugarException se) {
//            try {
//                resp.setStatus(500);
//                se.printStackTrace(resp.getWriter());
//                return;
//            } catch (IOException e) {
//                resp.setStatus(500);
//                return;
//            }
//        }

        String action = req.getParameter("action");
        String type =  req.getParameter("type");
        String id = req.getParameter("id");

        if ("update".equals(action)) {
            try {
                if ("organization".equals(type)) {

                    StringBuilder builder = new StringBuilder();
                    if ("*".equals(id)) {
//                        List<Map<String, String>> providers = getSugarService().listProviders(true);
//                        for (Map<String, String> provider : providers) {
//                            String mnemonic = getSugarService().getProviderMnemonic(provider);
//                            if (mnemonic != null) {
//                                boolean update = updateProvider(mnemonic, provider);
//                                if (builder.length() > 0) {
//                                    builder.append(", \n");
//                                }
//                                builder.append(mnemonic + ": " + (update ? "UPD" : "NaN"));
//                            }
//                        }
//
//                        resp.setStatus(200);
//                        resp.getWriter().write(builder.toString());
//                        resp.getWriter().write(" DONE:" + providers.size());
                    } else {
//                        boolean update = updateProvider(id, null);
//                        builder.append(id + ": " + (update ? "UPD" : "NaN"));
//                        resp.setStatus(200);
//                        resp.getWriter().write(" DONE");
                    }
                } else if ("collection".equals(type)) {
                    StringBuilder builder = new StringBuilder();
                    if ("*".equals(id)) {
//                        List<Map<String, String>> collections = getSugarService().listCollections(
//                                true);
//                        for (Map<String, String> collection : collections) {
//                            String mnemonic = getSugarService().getCollectionMnemonic(collection);
//                            if (mnemonic != null) {
//                                boolean update = updateCollection(mnemonic, collection);
//                                if (builder.length() > 0) {
//                                    builder.append(", \n");
//                                }
//                                builder.append(mnemonic + ": " + (update ? "UPD" : "NaN"));
//                            }
//                        }
//
//                        resp.setStatus(200);
//                        resp.getWriter().write(builder.toString());
//                        resp.getWriter().write(" DONE:" + collections.size());
                    } else {
//                        boolean update = updateCollection(id, null);
//                        builder.append(id + ": " + (update ? "UPD" : "NaN"));
//                        resp.setStatus(200);
//                        resp.getWriter().write(" DONE");
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


    /**
     * Returns the repoxService.
     * 
     * @return the repoxService
     */
    public RepoxService getRepoxService() {
        return repoxService;
    }

    /**
     * Sets the sugarService to the given value.
     * 
     * @param repoxService
     *            the repoxService to set
     */
    public void setRepoxService(RepoxService repoxService) {
        this.repoxService = repoxService;
    }

}
