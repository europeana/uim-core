package eu.europeana.uim.plugin.ingestion;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.logging.LoggingEngine;
import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.plugin.ExecutionPlugin;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.UimDataSet;

/**
 * The interface for general ingestion plugins. An ingestion plugin is a single processing step
 * within a workflow. An ingestion plugin needs further to be "stateless" with respect to the
 * processed uim dataset, but also with respect to the configuration according to a execution
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
 * @param <U>
 *            uim data set type
 * @param <I>
 *            generic identifier
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Feb 25, 2011
 */
public interface IngestionPlugin<U extends UimDataSet<I>, I> extends ExecutionPlugin<U, I> {
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

    /**
     * Process a single meta data record within a given execution context. It returns true, if
     * processing went well and false, if something failed. NOTE, false in this context means only
     * that the plugin could not do its work, but neither is the {@link MetaDataRecord} corrupted
     * nor is the plugin itself damaged, so that the record can further processed and this plugin
     * can take other records as well. Furthermore, additional information can be logged (
     * {@link LoggingEngine}).
     * 
     * @param dataset
     *            the {@link UimDataSet} to process
     * @param context
     *            holds execution depending, information the {@link ExecutionContext} for this
     *            processing call. This context can change for each call, so references to it have
     *            to be handled carefully.
     * @return true, if the plugin could do its work and false if something failed during processing
     * @throws IngestionPluginFailedException
     *             is thrown if the plugin encounters a severe problem and it is therefore
     *             impossible to process any more records for this {@link ExecutionContext}
     * @throws CorruptedDatasetException
     *             the plugin encountered a severe problem for a specific {@link MetaDataRecord} so
     *             that further processing of this specific {@link MetaDataRecord} does not make
     *             sense any longer
     */
    boolean process(U dataset, ExecutionContext<U, I> context) throws IngestionPluginFailedException,
            CorruptedDatasetException;
}
