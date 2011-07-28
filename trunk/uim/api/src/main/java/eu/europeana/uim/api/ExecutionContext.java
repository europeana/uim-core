package eu.europeana.uim.api;

import java.io.File;
import java.io.Serializable;
import java.util.Properties;

import eu.europeana.uim.common.RevisableProgressMonitor;
import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.workflow.Workflow;

/**
 * Context of a running execution holding execution dependent variables for the plugins etc.
 * 
 * @param <I>
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface ExecutionContext<I> {
    /**
     * @return execution for which this context is for
     */
    Execution<I> getExecution();

    /**
     * @return workflow running in the holding execution
     */
    Workflow getWorkflow();

    /**
     * @param throwable
     *            holding heavy errors
     */
    void setThrowable(Throwable throwable);

    /**
     * @return throwable with errors
     */
    Throwable getThrowable();

    /**
     * @return DataSet for this execution (provider, collection, ...)
     */
    UimDataSet<I> getDataSet();

    /**
     * @return progress monitor
     */
    RevisableProgressMonitor getMonitor();

    /**
     * @return logging engine
     */
    LoggingEngine<I> getLoggingEngine();

    /**
     * @return the execution specific properties
     */
    public Properties getProperties();

    /**
     * @param plugin
     * @return a working directory, which is specific for this execution
     */
    public File getWorkingDirectory(IngestionPlugin plugin);

    /**
     * @param plugin
     * @return a temporary directory, which is only valid for this execution and deleted after the
     *         workflow is over (if not overridden by configuration)
     */
    public File getTmpDirectory(IngestionPlugin plugin);

    /**
     * Transforms file related properties into the actual file references.
     * 
     * @param fileReference
     * @return a reference to the resource to access, null if not successful
     */
    public File getFileResource(String fileReference);

    /**
     * @param <NS>
     *            the namespace (type) in which the field is defined
     * @param <T>
     *            the runtime type of the values for this field
     * @param key
     *            typed key which holds namespace, name and type information
     * @param value
     *            object typed using the type specified in the key
     * @return the previous object with this key (@see Map.put())
     */
    <NS, T extends Serializable> Object putValue(TKey<NS, T> key, T value);

    /**
     * @param <NS>
     *            the namespace (type) in which the field is defined
     * @param <T>
     *            the runtime type of the values for this field
     * @param key
     *            typed key which holds namespace, name and type information
     * @return value object typed using the type specified in the key
     */
    <NS, T extends Serializable> T getValue(TKey<NS, T> key);
}
