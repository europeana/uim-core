package eu.europeana.uim.api;

import java.util.List;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;

/**
 * The interface for general ingestion plugins. An ingestion plugin is a single processing step
 * within a workflow. An ingestion plugin needs further to be "stateless" with respect to the
 * processed meta data records, but also with respect to the configuration according to a execution
 * context.
 * 
 * For instance: When the framework processes more than one execution at a time with two workflows
 * which both have the same plugin, the framework presents (@see processRecord()) records in
 * arbitrary order.
 * 
 * Another possibility would be that a workflow is started on two different datasets. In this case
 * the plugin developer also needs to keep in mind, that the framework ensures, that only one
 * instance of the plugin exists (singleton). This instance is then presented with records from the
 * one and the other dataset in an arbitrary order.
 * 
 * THEREFORE:
 * <ul>
 * <li>Member variables are not allowed in an plugin. This is also checked and an
 * IllegalArgumentException is thrown if a plugin is part of an executed workflow.</li>
 * <li>Execution/Dataset specific information needs to be stored/retrieved with putValue/getValue
 * into the execution context.</li>
 * </ul>
 * 
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 25, 2011
 */
public interface IngestionPlugin {

    /**
     * getter for the list of fields this plugin wants to operate on.
     * 
     * @return a list of fields this plugin requires
     */
    public TKey<?, ?>[] getInputFields();

    /**
     * getter for the list of output fields. Note that the metadata record might not allow
     * "manipulation" of fields in the future.
     * 
     * @return a list of fields this plugin creates
     */
    public TKey<?, ?>[] getOutputFields();

    /**
     * list of configuration parameters this plugin can take from the execution context to be
     * configured for a specific execution. NOTE: any execution related configuration/information
     * needs to be stored into the context and retrieved from the context in the processRecord
     * method.
     * 
     * @return list of configuration parameters.
     */
    public List<String> getParameters();

    /**
     * get the description of the plugin which is provided to the operators when starting analysing
     * workflows.
     * 
     * @return the description
     */
    public String getDescription();

    /**
     * A plugin is always executed within a thread pool, this parameter defines the preferred size
     * of the pool. Plugins should know best, what's a good level of parallelism.
     * 
     * @return number of threads this plugin should usually be processed.
     */
    int getPreferredThreadCount();

    /**
     * Number of maximum threads. The plugin might specify here one (1) if it is not thread safe.
     * 
     * @return the number of maximal threads.
     */
    int getMaximumThreadCount();

    /**
     * initialization method for an execution context. The context holds the properties specific for
     * this execution.
     * 
     * @param context
     */
    void initialize(ExecutionContext context);

    /**
     * finalization method (tear down) for an execution. At the end of each execution this method is
     * called to allow the plugin to clean up memory or external resources.
     * 
     * @param context
     */
    void completed(ExecutionContext context);

    /**
     * Process a single meta data record within a given execution context
     * 
     * @param mdr
     *            the {@link MetaDataRecord} to process
     * @param context
     *            the {@link ExecutionContext} for this processing call. This context can change for
     *            each call, so references to it have to be handled carefully.
     * @throws IngestionPluginFailedException
     */
    public void processRecord(MetaDataRecord mdr, ExecutionContext context)
            throws IngestionPluginFailedException;

}
