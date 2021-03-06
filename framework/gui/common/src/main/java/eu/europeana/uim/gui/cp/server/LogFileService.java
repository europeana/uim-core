/* LogFileService.java - created on Oct 20, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.gui.cp.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.europeana.uim.common.utils.FileUtils;
import eu.europeana.uim.gui.cp.server.engine.Engine;
import eu.europeana.uim.logging.LoggingEngine;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Execution;

/**
 * Servlet to deliver the logfile for an execution. Allows a head command to skip the beginning of a
 * file
 * 
 * Make sure, that this service is added to your web.xml, e.g. {@code
 * <servlet>
 *  <servlet-name>logFileService</servlet-name>
 *  <servlet-class>eu.europeana.uim.gui.cp.server.LogFileService</servlet-class> 
 * </servlet>
 * <servlet-mapping> 
 *  <servlet-name>logFileService</servlet-name>
 *  <url-pattern>/TelIngestionControlPanel/logfile</url-pattern> 
 *  </servlet-mapping>
 * }
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date Oct 20, 2011
 */
public class LogFileService extends HttpServlet {
    private final static Logger log = Logger.getLogger(FileUploadService.class.getName());

    private final Engine        engine;

    /**
     * Creates a new instance of this class.
     */
    public LogFileService() {
        this.engine = Engine.getInstance();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        @SuppressWarnings("rawtypes")
        LoggingEngine loggingEngine = engine.getRegistry().getLoggingEngine();
        if (loggingEngine == null) {
            log.log(Level.SEVERE, "Logging engine is null!");
            response.sendError(501, "Could not access logging engine");
            return;
        }

        Long id = 0L;
        String executionParm = request.getParameter("execution");
        if (executionParm == null) {
            response.sendError(501, "Missing execution id (parameter execution=)");
            return;
        }

        // this is bound to long identifiers
        try {
            id = Long.valueOf(executionParm);
        } catch (NumberFormatException e) {
            response.sendError(501, "Wrong execution id (parameter execution=" + executionParm +
                                    "\n");
            return;
        }

        boolean htmlOutput = false;
        if ("html".equalsIgnoreCase(request.getParameter("format"))) {
            htmlOutput = true;
        }

        boolean fullResponse = false;
        if ("true".equalsIgnoreCase(request.getParameter("full"))) {
            fullResponse = true;
        }

        @SuppressWarnings("unchecked")
        StorageEngine<Long> storageEngine = (StorageEngine<Long>)engine.getRegistry().getStorageEngine();
        Execution<Long> execution;
        try {
            execution = storageEngine.getExecution(id);
        } catch (StorageEngineException e) {
            throw new RuntimeException("Could not retrieve execution from storage engine", e);
        }

        String logFile = execution.getLogFile();
        // this is bound to long identifiers

        PrintWriter out = null;
        try {
            out = response.getWriter();

            if (logFile == null) {
                // set response header
                if (htmlOutput) {
                    response.setContentType("text/html");
                    sendHtmlHeader(out, execution.getId(), execution.isActive());
                } else {
                    response.setContentType("text/plain");
                }

                out.write("No log file found.\n");
                if (htmlOutput) {
                    sendHtmlFooter(out, execution.getId(), execution.isActive());
                }

            } else {
                File logFileHandler = new File(logFile);

                if (!logFileHandler.exists()) {
                    response.sendError(404,
                            "Could not find log file " + logFileHandler.getCanonicalPath());
                    return;
                }

                response.addHeader("X-Last-File-Pos-Sent", String.valueOf(logFileHandler.length()));
                if (execution.isActive()) {
                    response.addHeader("X-More-Data", "true");
                }
                // set response header
                if (htmlOutput) {
                    response.setContentType("text/html");
                    sendHtmlHeader(out, execution.getId(), execution.isActive());
                } else {
                    response.setContentType("text/plain");
                }

                if (fullResponse) {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(
                            logFileHandler));

                    try {
                        String thisLine = bufferedReader.readLine();
                        while (thisLine != null) {
                            if (htmlOutput) {
                                colorizedLogEntry(out, thisLine);
                            } else {
                                out.write(thisLine + "\n");
                            }
                            thisLine = bufferedReader.readLine();
                        }
                    } finally {
                        bufferedReader.close();
                    }
                } else {
                    List<String> tail = FileUtils.tail(logFileHandler, 1000);
                    if (tail.size() >= 1000) {
                        colorizedLogEntry(out, "<------------------ SKIPPED ------------->\n\r");
                        colorizedLogEntry(out, "\n\r");
                    }

                    for (String thisLine : tail) {
                        if (htmlOutput) {
                            colorizedLogEntry(out, thisLine);
                        } else {
                            out.write(thisLine + "\n");
                        }
                    }
                }

                if (htmlOutput) {
                    sendHtmlFooter(out, execution.getId(), execution.isActive());
                }
            }
        } catch (IOException ioe) {
            response.sendError(501, "Error while reading logfile " + logFile);
            return;
        } finally {
            if (out != null) out.close();
        }

    }

    /**
     * @param out
     * @param thisLine
     */
    private void colorizedLogEntry(PrintWriter out, String thisLine) {
        String color = null;
        if (thisLine.contains(Level.WARNING.getName())) {
            color = "#0000FF";
        } else if (thisLine.contains(Level.SEVERE.getName())) {
            color = "#FF0000";
        }

        String reformattedStr = thisLine.replaceAll("\\n", "\n     ");
        if (color == null) {
            out.write("<div><code>" + reformattedStr + "</code></div>");
        } else {
            out.write("<div style=\"color:" + color + ";\"><code>" + reformattedStr +
                      "</code></div>");
        }
    }

    /**
     * @param out
     */
    private void sendHtmlFooter(PrintWriter out, Long execution, boolean moreData) {
        if (moreData) {
            out.write("<img src=\"../img/ajax-loader.gif\" alt=\">Loading...\"></img>");
        }

        out.write("<p><a href=\"logfile?format=html&execution=" + execution +
                  "&full=true\">Full Log</a></p>");

        out.write("</body></html>");

    }

    /**
     * @param out
     */
    private void sendHtmlHeader(PrintWriter out, Long execution, boolean moreData) {
        out.write("<!DOCTYPE html>  \n" + "<html lang=\"en\">  \n" + "  <head>  \n"
                  + "    <meta charset=\"utf-8\">  \n" + "    <title>Logfile</title>  \n");
// if (moreData) {
// out.write("<meta http-equiv=\"refresh\" content=\"30\" />");
// }
        out.write("<script>function load()\n" + "{\n"
                  + "window.scrollTo(0, document.body.scrollHeight);" + "} </script>");
        out.write("  </head>  \n" + "  <body onload=\"load()\">");

        out.write("<p><a href=\"logfile?format=html&execution=" + execution +
                  "&full=true\">Full Log</a></p>");

    }

}
