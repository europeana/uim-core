package eu.europeana.uim.api;

import java.io.Serializable;
import java.util.Properties;

import eu.europeana.uim.TKey;
import eu.europeana.uim.common.RevisableProgressMonitor;
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
    RevisableProgressMonitor getMonitor();

    /** logging engine **/
    LoggingEngine<?> getLoggingEngine();

    /** the execution specific properties */
    public Properties getProperties();
    
    <NS,T extends Serializable> void putValue(TKey<NS, T> key, T value);
	<NS,T extends Serializable> T getValue(TKey<NS, T> key);


}
