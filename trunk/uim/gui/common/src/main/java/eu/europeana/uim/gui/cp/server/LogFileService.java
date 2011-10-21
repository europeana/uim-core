/* LogFileService.java - created on Oct 20, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.gui.cp.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gwt.core.client.GWT;

import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.api.Orchestrator;
import eu.europeana.uim.gui.cp.server.engine.Engine;
import eu.europeana.uim.store.bean.ExecutionBean;

/**
 * Servlet to deliver the logfile for an execution. Allows a head command to skip the beginning of a
 * file
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

        ExecutionBean<Long> executionBean = new ExecutionBean<Long>();

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

        int head = 0;
        if (request.getParameter("head") != null) {
            try {
                head = Integer.valueOf(request.getParameter("head")).intValue();
            } catch (NumberFormatException e) {
                response.sendError(501,
                        "Wrong head parameter (parameter head=" + request.getParameter("head") +
                                "\n");
                return;
            }
        }

        boolean htmlOutput = false;
        if ("html".equalsIgnoreCase(request.getParameter("format"))) {
            htmlOutput = true;
        }

        executionBean.setId(id);
        @SuppressWarnings("unchecked")
        String logFile = loggingEngine.getLogFile(executionBean);
        System.out.println("called!");
        if (logFile == null) {
            // the logging engine does not support logging
            // send demo data
            File tmpFile = File.createTempFile("demouim", "log");
            writeDemoData(executionBean, tmpFile);
            logFile = tmpFile.getCanonicalPath();
// response.sendError(501, "LoggingEngine " + loggingEngine.getIdentifier() +
// " does not support log file writing");
// return;
        }

        File logFileHandler = new File(logFile);

        if (!logFileHandler.exists()) {
            response.sendError(404, "Could not find log file " + logFileHandler.getCanonicalPath());
            return;
        }

        // this is bound to long identifiers

        PrintWriter out = null;
        try {
            out = response.getWriter();

            FileReader fileReader = new FileReader(logFileHandler);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(logFileHandler));
            response.addHeader("X-Last-File-Pos-Sent", String.valueOf(logFileHandler.length()));
            // set response header
            if (htmlOutput) {
                response.setContentType("text/html");
                sendHtmlHeader(out);
            } else {
                response.setContentType("text/plain");
            }

            bufferedReader.skip(head);
            String thisLine;
            while ((thisLine = bufferedReader.readLine()) != null) {
                if (htmlOutput) {
                    colorizedLogEntry(out, thisLine);
                } else {
                    out.write(thisLine + "\n");
                }
            }

            if (htmlOutput) {
                sendHtmlFooter(out);
            }
        } catch (IOException ioe) {
            response.sendError(501,
                    "Error while reading logfile " + logFileHandler.getCanonicalPath());
            return;
        } finally {
            if (out != null) out.close();
        }

//
// ServletFileUpload upload = new ServletFileUpload();
// try {
// FileItemIterator iter = upload.getItemIterator(request);
//
// while (iter.hasNext()) {
// FileItemStream item = iter.next();
// InputStream stream = item.openStream();
//
// FileOutputStream ofile = new FileOutputStream(new File(
// resourceEngine.getResourceDirectory() + File.separator + item.getName()));
//
// // Process the input stream
// int len;
// byte[] buffer = new byte[8192];
// while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
// ofile.write(buffer, 0, len);
// }
//
// ofile.close();
// stream.close();
// }
// } catch (Exception e) {
// e.printStackTrace();
// }
    }

    /**
     * @param out
     * @param thisLine
     */
    private void colorizedLogEntry(PrintWriter out, String thisLine) {
        String color=null;
        if (thisLine.contains(Level.WARNING.getName())) {
            color = "#0000FF";
        } else if (thisLine.contains(Level.SEVERE.getName())) {
            color = "#FF0000";
        }
        if (color==null) {
            out.write("<div><code>" + thisLine +
            "</code></div>"); 
        } else {
        out.write("<div style=\"color:" + color + ";\"><code>" + thisLine +
                  "</code></div>");
        }
    }

    /**
     * @param executionBean
     * @param tmpFile
     * @throws IOException
     */
    private void writeDemoData(ExecutionBean<Long> executionBean, File tmpFile) throws IOException {
        FileWriter fstream = new FileWriter(tmpFile);
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(new Date() + "|" + Level.SEVERE.getName() + "|Dummy log for execution " +
                  executionBean.getId() + "\n");
        out.write(new Date() + "|" + Level.INFO.getName() + "|Dummy log for execution " +
                  executionBean.getId() + "\n");
        out.write(new Date() + "|" + Level.FINE.getName() + "|Dummy log for execution " +
                  executionBean.getId() + "\n");
        out.close();
    }

    /**
     * @param out
     */
    private void sendHtmlFooter(PrintWriter out) {
        out.write("</body></html>");
    }

    /**
     * @param out
     */
    private void sendHtmlHeader(PrintWriter out) {
        out.write("<!DOCTYPE html>  \n" + "<html lang=\"en\">  \n" + "  <head>  \n"
                  + "    <meta charset=\"utf-8\">  \n" + "    <title>Logfile</title>  \n"
                  + "  </head>  \n" + "  <body>  ");
    }
}
