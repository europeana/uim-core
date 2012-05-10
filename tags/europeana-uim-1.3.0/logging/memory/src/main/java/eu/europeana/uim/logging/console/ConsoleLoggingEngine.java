/* LoggingEngineAdapter.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.logging.console;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;


/**
 * Implementation of logging engine to std err.
 * Used during test and development.
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 26 de Jan de 2012
 */
public class ConsoleLoggingEngine<I> implements LoggingEngine<I> {

    @Override
    public String getIdentifier() {
        return ConsoleLoggingEngine.class.getSimpleName();
    }

    @Override
    public void log(Level level, String modul, String... message) {
    	print(level+"-"+modul, message);
    }

    @Override
    public void log(Level level, IngestionPlugin plugin, String... message) {
    	print(level+"-"+plugin.getIdentifier(), message);
    }

    @Override
    public void log(Execution<I> execution, Level level, String modul, String... message) {
    	print(execution.getId()+"-"+level+"-"+modul, message);
    }

    @Override
    public void log(Execution<I> execution, Level level, IngestionPlugin plugin, String... message) {
    	print(execution.getId()+"-"+level+"-"+plugin.getIdentifier(), message);
    }

    @Override
    public void logFailed(Level level, String modul, Throwable throwable, String... message) {
    	print("Failed "+level+"-"+modul, throwable, message);
    }


	@Override
    public void logFailed(Execution<I> execution, Level level, String modul, Throwable throwable,
            MetaDataRecord<I> mdr, String... message) {
    	print("Failed "+level+"-"+modul, throwable, message);
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, IngestionPlugin plugin,
            Throwable throwable, MetaDataRecord<I> mdr, String... message) {
    	print("Failed "+level+"-"+plugin.getIdentifier(), throwable, message);
    }

    @Override
    public void logFailed(Level level, IngestionPlugin plugin, Throwable throwable,
            String... message) {
    	print("Failed "+level+"-"+plugin.getIdentifier(), throwable, message);
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, String modul, Throwable throwable,
            String... message) {
    	print("Failed "+level+"-"+modul, throwable, message);
    }

    @Override
    public void logFailed(Execution<I> execution, Level level, IngestionPlugin plugin,
            Throwable throwable, String... message) {
    	print("Failed "+level+"-"+plugin.getIdentifier(), throwable, message);
    }

    @Override
    public void logLink(String modul, String link, int status, String... message) {
    	print("Link "+link+":"+status+"-"+modul, message);
    }

    @Override
    public void logLink(Execution<I> execution, String modul, MetaDataRecord<I> mdr, String link,
            int status, String... message) {
    	print("Link "+link+":"+status+"-"+modul, message);
    }

    @Override
    public void logLink(Execution<I> execution, IngestionPlugin plugin, MetaDataRecord<I> mdr,
            String link, int status, String... message) {
    	print("Link "+link+":"+status+"-"+plugin.getIdentifier(), message);
    }

    @Override
    public void logField(String modul, String field, String qualifier, int status,
            String... message) {
    	print("Field "+field+":"+qualifier+"-"+status, message);
    }

    @Override
    public void logField(Execution<I> execution, String modul, MetaDataRecord<I> mdr, String field,
            String qualifier, int status, String... message) {
    	print("Field "+field+":"+qualifier+"-"+status, message);
    }

    @Override
    public void logField(Execution<I> execution, IngestionPlugin plugin, MetaDataRecord<I> mdr,
            String field, String qualifier, int status, String... message) {
    	print("Field "+field+":"+qualifier+"-"+status, message);
    }

    @Override
    public void logDuration(Execution<I> execution, String module, Long duration) {
    	print("Duration "+execution.getId()+":"+module, duration.toString());
    }

    @Override
    public void logDuration(Execution<I> execution, IngestionPlugin plugin, Long duration) {
    	print("Duration "+execution.getId()+":"+plugin.getIdentifier(), duration.toString());
    }

    @Override
    public List<eu.europeana.uim.api.LoggingEngine.LogEntry<I>> getLogs(Execution<I> execution) {
        return Collections.emptyList();
    }

    @Override
    public List<eu.europeana.uim.api.LoggingEngine.LogEntryFailed<I>> getFailedLogs(
            Execution<I> execution) {
        return Collections.emptyList();
    }

    @Override
    public List<eu.europeana.uim.api.LoggingEngine.LogEntryLink<I>> getLinkLogs(
            Execution<I> execution) {
        return Collections.emptyList();
    }

    private void print(String prefix, String... message) {
    	for(String s:message)
    		System.err.printf("%s: %s\n", prefix, s);
    }

	private void print(String prefix, Throwable throwable, String... message) {
		print(prefix, message);
	   	System.err.printf("%s\n",getStackTrace(throwable));
    }

    
    /**
     * utility method to serialize a throwable to a string
     * 
     * @param throwable
     * @return the whole stacktrace in string representation
     */
    public static String getStackTrace(Throwable throwable) {
        if (throwable == null) return "";

        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        throwable.printStackTrace(printWriter);
        return result.toString();
    }

    @Override
    public void completed(ExecutionContext<I> execution) {
    }
}
