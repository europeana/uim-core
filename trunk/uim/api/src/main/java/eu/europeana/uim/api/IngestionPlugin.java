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
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @date Feb 25, 2011
 */
public interface IngestionPlugin {
    /**
     * Get the class name of the plugin which is used to register the plugin with the registry.
     * 
     * @return the name for this plugin (should be Plugin.class.getSimpleName()).
     */
    String getName();

    /**
     * Get the description of the plugin which is provided to the operators when starting analyzing
     * workflows.
     * 
     * @return the description
     */
    String getDescription();

    /**
     * Get the list of fields this plugin wants to operate on. This is used for information
     * purposes, so that it can be validated if the records hold these data.
     * 
     * @return a list of fields this plugin requires
     */
    TKey<?, ?>[] getInputFields();

    /**
     * Get the list of fields this plugin would like to operate on or can get additional information
     * for the working process. This is used for information purposes, so that it can be validated
     * if the records hold these data.
     * 
     * @return a list of fields this plugin requires
     */
    TKey<?, ?>[] getOptionalFields();

    /**
     * Get the list of output fields. NOTE: the meta data record might not allow "manipulation" of
     * fields in the future.
     * 
     * @return a list of fields this plugin creates
     */
    TKey<?, ?>[] getOutputFields();

    
    /** Initialize the plugin when it is loaded in the OSGI container and attached to the 
     * uim registry.
     * 
     */
    void initialize();

    
    /** Shutdown teh plugin when it is removed from the uim registry (due to OSGI shutdown or
     * reinstallation etc. 
     */
    void shutdown();

    
    /**
     * List of configuration parameters this plugin can take from the execution context to be
     * configured for a specific execution. NOTE: any execution related configuration/information
     * needs to be stored into the context and retrieved from the context during the processRecord
     * method.
     * 
     * @return list of configuration parameters.
     */
    List<String> getParameters();

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
     * Initialization method for an execution context. The context holds the properties specific for
     * this execution.
     * 
     * @param context
     *            holds execution depending, information the {@link ExecutionContext} for this
     *            processing call. This context can change for each call, so references to it have
     *            to be handled carefully.
     * @throws IngestionPluginFailedException
     *             is thrown if the intiliazation fails and so the plugin is not ready to take
     *             records for this {@link ExecutionContext}
     */
    void initialize(ExecutionContext context) throws IngestionPluginFailedException;

    /**
     * Finalization method (tear down) for an execution. At the end of each execution this method is
     * called to allow the plugin to clean up memory or external resources.
     * 
     * @param context
     *            holds execution depending, information the {@link ExecutionContext} for this
     *            processing call. This context can change for each call, so references to it have
     *            to be handled carefully.
     * @throws IngestionPluginFailedException
     *             is thrown if the tear down encountered a severe failure during deleting external
     *             resources, so that executing it in a new {@link ExecutionContext} will most
     *             likely fail
     */
    void completed(ExecutionContext context) throws IngestionPluginFailedException;

    /**
     * Process a single meta data record within a given execution context. It returns true, if
     * processing went well and false, if something failed. NOTE, false in this context means only
     * that the plugin could not do its work, but neither is the {@link MetaDataRecord} corrupted
     * nor is the plugin itself damaged, so that the record can further processed and this plugin
     * can take other records as well. Furthermore, additional information can be logged (
     * {@link LoggingEngine}).
     * 
     * @param mdr
     *            the {@link MetaDataRecord} to process
     * @param context
     *            holds execution depending, information the {@link ExecutionContext} for this
     *            processing call. This context can change for each call, so references to it have
     *            to be handled carefully.
     * @return true, if the plugin could do its work and false if something failed during processing
     * @throws IngestionPluginFailedException
     *             is thrown if the plugin encounters a severe problem and it is therefore
     *             impossible to process any more records for this {@link ExecutionContext}
     * @throws CorruptedMetadataRecordException
     *             the plugin encountered a severe problem for a specific {@link MetaDataRecord} so
     *             that further processing of this specific {@link MetaDataRecord} does not make
     *             sense any longer
     */
    boolean processRecord(MetaDataRecord mdr, ExecutionContext context)
            throws IngestionPluginFailedException, CorruptedMetadataRecordException;
}
