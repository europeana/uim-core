/* LinkcheckIngestionPlugin.java - created on Mar 20, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.edm;

import java.io.File;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.theeuropeanlibrary.commons.export.edm.ObjectModelToEdmConverter;
import org.theeuropeanlibrary.model.common.qualifier.LinkTarget;
import org.xml.sax.SAXException;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.plugin.ingestion.AbstractIngestionPlugin;
import eu.europeana.uim.plugin.ingestion.IngestionPluginFailedException;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * Base class for linking and thumbnail checking.
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 20, 2011
 */
public abstract class AbstractEdmIngestionPlugin<I> extends
        AbstractIngestionPlugin<MetaDataRecord<I>, I> {
    /**
     * Set the Logging variable to use logging within this class
     */
    private static final Logger                                    log  = Logger.getLogger(AbstractEdmIngestionPlugin.class.getName());

    /**
     * typed key to retrieve the container holding all execution dependent variables
     */
    @SuppressWarnings("rawtypes")
    protected static final TKey<AbstractEdmIngestionPlugin, ContextRunningData> DATA = TKey.register(
                                                                                AbstractEdmIngestionPlugin.class,
                                                                                "data", ContextRunningData.class);

    /**
     * Creates a new instance of this class and initializes members.
     * 
     * @param name
     *            meaningful name of this plugin
     * @param description
     *            meaningful description of this plugin
     */
    public AbstractEdmIngestionPlugin(String name, String description) {
        super(name, description);
    }

    @Override
    public TKey<?, ?>[] getInputFields() {
        return new TKey[0];
    }

    @Override
    public TKey<?, ?>[] getOptionalFields() {
        return new TKey[0];
    }

    @Override
    public TKey<?, ?>[] getOutputFields() {
        return new TKey[0];
    }

    @Override
    public void initialize() {
    }

    @Override
    public void shutdown() {
    }

    @Override
    public int getPreferredThreadCount() {
        return 10;
    }

    @Override
    public int getMaximumThreadCount() {
        return 10;
    }

    @Override
    public void completed(ExecutionContext<MetaDataRecord<I>, I> context)
            throws IngestionPluginFailedException {
        ContextRunningData value = context.getValue(DATA);
        log.info("Submitted:" + value.submitted + ", Ignored: " + value.ignored);
    }

    /**
     * Container holding all execution specific information for the validation plugin.
     */
    protected static class ContextRunningData implements Serializable {
        Schema edmSchema = null;
        ObjectModelToEdmConverter edmConverter;
        EdmValidationReport             report    = new EdmValidationReport();
        int             maxErrors    = 0;
        int             ignored    = 0;
        int             submitted  = 0;
        
        /**
         * Creates a new instance of this class.
         * @param edmXsdFile 
         */
        public ContextRunningData(File edmXsdFile) {
            try {
                edmSchema = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema").newSchema(edmXsdFile);
            } catch (SAXException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
            edmConverter=new ObjectModelToEdmConverter();
        }
        
    }
}
