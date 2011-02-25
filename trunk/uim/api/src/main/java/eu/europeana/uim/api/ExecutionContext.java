package eu.europeana.uim.api;

import java.util.Properties;

import eu.europeana.uim.common.ProgressMonitor;
import eu.europeana.uim.store.DataSet;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.workflow.Workflow;

/**
 * Context of a running execution
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public interface ExecutionContext {

    /** the execution **/
    Execution getExecution();

    /** workflow for this execution **/
    Workflow getWorkflow();

    /** DataSet for this execution (provider, collection, ...) **/
    DataSet getDataSet();

    /** progress monitor **/
    ProgressMonitor getMonitor();

    /** logging engine **/
    LoggingEngine<?> getLoggingEngine();

    /** the execution specific properties */
    public Properties getProperties();
    
	void putValue(IngestionPlugin plugin, String key, Object value);
	boolean hasValue(IngestionPlugin plugin, String key);
	Object getValue(IngestionPlugin plugin, String key);


}
