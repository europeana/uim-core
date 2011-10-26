/* ExecutionLogFileWriter.java - created on Oct 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;

import eu.europeana.uim.store.Execution;

/**
 * Simple log write to a file
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @param <I>
 *            type of execution id
 * @date Oct 19, 2011
 */
public class ExecutionLogFileWriter<I> {

    private File              baseRootPath;

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");

    /**
     * Creates a new instance of this class.
     */
    public ExecutionLogFileWriter() {

    }

    /**
     * Creates a new instance of this class.
     * 
     * @param basePath
     *            the path to store the log files in
     * @throws IOException
     */
    public ExecutionLogFileWriter(final String basePath) throws IOException {
        setRootPath(basePath);
    }

    /**
     * @param rootPath
     *            the root path to keep all log files
     * @throws IOException
     */
    public void setRootPath(String rootPath) throws IOException {
        baseRootPath = new File(rootPath);
        if (!baseRootPath.exists()) {
            if (!baseRootPath.mkdirs()) { throw new IllegalArgumentException(
                    "Could not create logging directory " + baseRootPath.getCanonicalPath()); }
        }
    }

    /**
     * @param execution
     *            the execution this log entry belongs to
     * @param level
     *            the severity of the message
     * @param message
     *            the log message
     * @throws IOException
     */
    public synchronized void log(final Execution<I> execution, final Level level,
            final String message) throws IOException {
        File logFile = getLogFile(execution);
        // we have not configured the logging path, just ignore the log request
        if (logFile == null) return;
        FileWriter fstream = null;
        BufferedWriter out = null;

        try {
            fstream = new FileWriter(logFile, true);
            out = new BufferedWriter(fstream);
            
            // we do not encode somehting here to HTML
//            String cleanMessage = message.replace("|", "&179;");
//            cleanMessage = cleanMessage.replace("<", "&lt;");
//            cleanMessage = cleanMessage.replace(">", "&gt;");
            
            String stripped = StringUtils.strip(message, " \t\n\r");
            out.write(dateFormat.format(new Date()) + "|" +
                      String.format("%1$#9s", level.getName()) + "|" + stripped + "\n");
        } finally {
            if (out != null) out.close();
            if (fstream != null) fstream.close();
        }

    }

    /**
     * 
     * @param execution
     * @return the log file for this execution
     */
    public File getLogFile(final Execution<I> execution) {
        if (baseRootPath == null) { return null; }
        return new File(baseRootPath, "execution-log-" + execution.getId());
    }
}
