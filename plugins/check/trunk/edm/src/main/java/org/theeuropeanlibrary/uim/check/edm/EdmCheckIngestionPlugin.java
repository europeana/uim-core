/* LinkcheckIngestionPlugin.java - created on Mar 20, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.edm;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Validator;

import org.theeuropeanlibrary.commons.export.edm.EdmXmlSerializer;
import org.theeuropeanlibrary.commons.export.edm.model.ResourceMap;
import org.theeuropeanlibrary.model.common.Link;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import eu.europeana.uim.orchestration.ActiveExecution;
import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.plugin.ingestion.CorruptedDatasetException;
import eu.europeana.uim.plugin.ingestion.IngestionPluginFailedException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.sugar.SugarControlledVocabulary;
import eu.europeana.uim.sugar.SugarService;

/**
 * This plugin check links and adds/updates status information onto the {@link Link} object.
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 20, 2011
 */
public class EdmCheckIngestionPlugin<I> extends AbstractEdmIngestionPlugin<I> {
    /**
     * Set the Logging variable to use logging within this class
     */
    private static final Logger           log        = Logger.getLogger(EdmCheckIngestionPlugin.class.getName());

    /** Stop validating if maxerrors is reached */
    public static final String            MAX_ERRORS = "edmcheck.maxerrors";

    private static final List<String>     PARAMETER  = new ArrayList<String>() {
                                                         {
                                                             add(MAX_ERRORS);
                                                         }
                                                     };

    private final static SimpleDateFormat df         = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static SugarService           sugarService;

    /**
     * Creates a new instance of this class.
     */
    public EdmCheckIngestionPlugin() {
        super("EDM Checking Plugin",
                "Plugin which checks if the records are valid for conversion to EDM.");
    }

    @Override
    public void initialize() {
    }

    @Override
    public void shutdown() {
    }

    @Override
    public List<String> getParameters() {
        return PARAMETER;
    }

    @Override
    public void initialize(ExecutionContext<MetaDataRecord<I>, I> context)
            throws IngestionPluginFailedException {

        ContextRunningData value = new ContextRunningData(
                context.getFileResource("check/xsd/edm/EDM_tel.xsd"));

        context.putValue(DATA, value);

        Collection<?> collection = null;
        UimDataSet<?> dataset = context.getDataSet();
        if (dataset instanceof Collection) {
            collection = (Collection<?>)dataset;
        } else if (dataset instanceof Request<?>) {
            collection = ((Request<?>)dataset).getCollection();
        }

        String time = df.format(new Date());
        String mnem = collection != null ? collection.getMnemonic() : "NULL";
        String name = collection != null ? collection.getName() : "No collection";

        context.getLoggingEngine().log(context.getExecution(), Level.INFO, "edmcheck",
                "initialize", mnem, name, time);
    }

    @Override
    public void completed(ExecutionContext<MetaDataRecord<I>, I> context)
            throws IngestionPluginFailedException {
        ContextRunningData value = context.getValue(DATA);

        Collection<I> collection = null;
        UimDataSet<I> dataset = context.getDataSet();
        if (dataset instanceof Collection) {
            collection = (Collection<I>)dataset;
        } else if (dataset instanceof Request<?>) {
            collection = ((Request<I>)dataset).getCollection();
        }

        String time = df.format(new Date());
        String mnem = collection != null ? collection.getMnemonic() : "NULL";
        String name = collection != null ? collection.getName() : "No collection";
        
        context.getLoggingEngine().log(context.getExecution(), Level.INFO, "edmcheck", 
                "completed",
                mnem, 
                name, 
                String.valueOf(value.submitted), 
                String.valueOf(value.ignored), 
                String.valueOf(value.report.getValidRecords()), 
                String.valueOf(value.report.getInvalidRecords()),
                String.valueOf(value.report.getValidRecordsPercent()), 
                (value.ignored > 0 ? "(partial report - error limit reached)" : ""),
                time);

        context.getExecution().putValue("edmcheck.ignored", "" + value.ignored);
        context.getExecution().putValue("edmcheck.submitted", "" + value.submitted);
        context.getExecution().putValue("edmcheck.invalid", "" + value.report.getInvalidRecords());
        context.getExecution().putValue("edmcheck.valid", "" + value.report.getValidRecords());

        try {
            if (collection != null) {

                collection.putValue(SugarControlledVocabulary.COLLECTION_EDM_VALIDATION,
                        "" + context.getExecution().getId());

                ((ActiveExecution<MetaDataRecord<I>, I>)context).getStorageEngine().updateCollection(
                        collection);

                if (getSugarService() != null) {
                    getSugarService().updateCollection(collection);
                }
            }
        } catch (Throwable t) {
            context.getLoggingEngine().logFailed(Level.INFO, this, t,
                    "Update collection or sugar service on " + collection + " failed");
            log.log(Level.WARNING, "Failed to update collection or call sugar service: " +
                                   collection, t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean process(MetaDataRecord<I> mdr, ExecutionContext<MetaDataRecord<I>, I> context)
            throws IngestionPluginFailedException, CorruptedDatasetException {
        ContextRunningData value = context.getValue(DATA);

        if (value.maxErrors > 0 && value.report.getInvalidRecords() >= value.maxErrors) {
            value.ignored++;
            return true;
        }
        value.submitted++;

        ResourceMap edm = value.edmConverter.convert((MetaDataRecord<Long>)mdr);

        EdmXmlSerializer edmXmlSerializer = new EdmXmlSerializer();

        Document edmDom = edmXmlSerializer.toDom(edm);

        final ArrayList<String> validationError = new ArrayList<String>();
        Validator validator = value.edmSchema.newValidator();

        // this was being done in Europeana Libraries, but I do not know if it is really necessary
// edmDom = XmlUtil.parseDom(new StringReader(XmlUtil.writeDomToString(edmDom)));
        Source source = new DOMSource(edmDom);
        validator.setErrorHandler(new ErrorHandler() {

            @Override
            public void warning(SAXParseException e) throws SAXException {
                validationError.add(e.getMessage());
            }

            @Override
            public void fatalError(SAXParseException e) throws SAXException {
                validationError.add(e.getMessage());
            }

            @Override
            public void error(SAXParseException e) throws SAXException {
                validationError.add(e.getMessage());
            }

        });

        try {
            validator.validate(source);
        } catch (SAXException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        if (!validationError.isEmpty()) {
            value.report.addInvalidRecord(validationError);
            // store the validation errors in the uim logging engine
            for (String valMsg : new HashSet<String>(validationError))
                context.getLoggingEngine().logEdmCheck(context.getExecution(), "edmcheck", mdr,
                        valMsg);
        } else
            value.report.addValidRecord();

        return true;
    }

    /**
     * @param sugarService
     */
    public void setSugarService(SugarService sugarService) {
        EdmCheckIngestionPlugin.sugarService = sugarService;
    }

    /**
     * @param sugarService
     */
    public void unsetSugarService(SugarService sugarService) {
        EdmCheckIngestionPlugin.sugarService = null;
    }

    /**
     * @return the sugar service
     */
    public SugarService getSugarService() {
        return EdmCheckIngestionPlugin.sugarService;
    }
}
