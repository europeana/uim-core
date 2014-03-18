package eu.europeana.uim.orchestration;

import java.io.File;
import java.io.Serializable;
import java.util.Properties;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.common.progress.RevisableProgressMonitor;
import eu.europeana.uim.logging.LoggingEngine;
import eu.europeana.uim.plugin.ingestion.IngestionPlugin;
import eu.europeana.uim.resource.ResourceEngine;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.workflow.Workflow;

/**
 * Context of a running execution holding execution dependent variables for the plugins etc.
 * 
 * @param <U>
 *            uim data set type
 * @param <I>
 *            generic ID
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface ExecutionContext<U extends UimDataSet<I>, I> {
    /**
     * @return execution for which this context is for
     */
    Execution<I> getExecution();

    /**
     * @return workflow running in the holding execution
     */
    Workflow<U, I> getWorkflow();

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
     * @return the collection iff the dataset is mdr, request, or collection. NULL for provider dataset.
     */
    Collection<I> getDataSetCollection();

    /**
     * @return progress monitor
     */
    RevisableProgressMonitor getMonitor();

    /**
     * @return logging engine
     */
    LoggingEngine<I> getLoggingEngine();

    /**
     * @return storage engine for this execution
     */
    StorageEngine<I> getStorageEngine();

    /**
     * @return resource engine for this execution
     */
    ResourceEngine getResourceEngine();

    /**
     * @return the execution specific properties
     */
    Properties getProperties();

    /**
     * @param plugin
     * @return a working directory, which is specific for this execution
     */
    File getWorkingDirectory(IngestionPlugin<U, I> plugin);

    /**
     * @param plugin
     * @return a temporary directory, which is only valid for this execution and deleted after the
     *         workflow is over (if not overridden by configuration)
     */
    File getTemporaryDirectory(IngestionPlugin<U, I> plugin);

    /**
     * Transforms file related properties into the actual file references.
     * 
     * @param fileReference
     * @return a reference to the resource to access, null if not successful
     */
    File getFileResource(String fileReference);

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
