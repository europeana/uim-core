/* LinkcheckIngestionPlugin.java - created on Mar 20, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.weblink;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.theeuropeanlibrary.model.common.Link;
import org.theeuropeanlibrary.model.common.qualifier.LinkStatus;
import org.theeuropeanlibrary.model.common.qualifier.LinkTarget;
import org.theeuropeanlibrary.model.tel.ObjectModelRegistry;
import org.theeuropeanlibrary.uim.check.weblink.http.GuardedMetaDataRecordUrl;
import org.theeuropeanlibrary.uim.check.weblink.http.Submission;
import org.theeuropeanlibrary.uim.check.weblink.http.WeblinkLinkchecker;

import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.CorruptedMetadataRecordException;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPluginFailedException;
import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.common.TKey;
import eu.europeana.uim.model.adapters.AdapterFactory;
import eu.europeana.uim.model.adapters.MetadataRecordAdapter;
import eu.europeana.uim.model.adapters.QValueAdapterStrategy;
import eu.europeana.uim.model.adapters.europeana.EuropeanaLinkAdapterStrategy;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.MetaDataRecord.QualifiedValue;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.sugarcrm.SugarControlledVocabulary;
import eu.europeana.uim.sugarcrm.SugarService;

/**
 * This plugin check links and adds/updates status information onto the {@link Link} object.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 20, 2011
 */
public class LinkCheckIngestionPlugin extends AbstractLinkIngestionPlugin {
    /**
     * Set the Logging variable to use logging within this class
     */
    private static final Logger           log       = Logger.getLogger(LinkCheckIngestionPlugin.class.getName());

    /** Use thumbnail links */
    public static final String            THUMBNAIL = "linkcheck.thumbnail";

    /** Use opac links */
    public static final String            CATALOGUE = "linkcheck.catalogue";

    /** Use content links */
    public static final String            DIGOBJECT = "linkcheck.digitalobject";

    /** Use toc links */
    public static final String            TOC       = "linkcheck.tableofcontents";

    private static final List<String>     PARAMETER = new ArrayList<String>() {
                                                        {
                                                            add(TOC);
                                                            add(THUMBNAIL);
                                                            add(CATALOGUE);
                                                            add(DIGOBJECT);
                                                        }
                                                    };

    private final static SimpleDateFormat df        = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static SugarService           sugarService;

    /**
     * Creates a new instance of this class.
     */
    public LinkCheckIngestionPlugin() {
        super("Link Checking Plugin", "Plugin which checks links for validity.");
    }

    @Override
    public void initialize() {
    }

    @Override
    public void shutdown() {
        WeblinkLinkchecker.getShared().shutdown();
    }

    @Override
    public List<String> getParameters() {
        return PARAMETER;
    }

    @Override
    public <I> void initialize(ExecutionContext<I> context) throws IngestionPluginFailedException {
        Data value = new Data();

        if (Boolean.parseBoolean(context.getProperties().getProperty(THUMBNAIL))) {
            value.checktypes.add(LinkTarget.THUMBNAIL);
        }

        if (Boolean.parseBoolean(context.getProperties().getProperty(TOC))) {
            value.checktypes.add(LinkTarget.TABLE_OF_CONTENTS);
        }

        if (Boolean.parseBoolean(context.getProperties().getProperty(DIGOBJECT))) {
            value.checktypes.add(LinkTarget.DIGITAL_OBJECT);
        }

        if (Boolean.parseBoolean(context.getProperties().getProperty(CATALOGUE))) {
            value.checktypes.add(LinkTarget.CATALOGUE_RECORD);
        }
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

        context.getLoggingEngine().log(context.getExecution(), Level.INFO, "linkcheck",
                "initialize", mnem, name, time);
    }

    @Override
    public <I> void completed(ExecutionContext<I> context) throws IngestionPluginFailedException {
        Data value = context.getValue(DATA);

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
        context.getLoggingEngine().log(context.getExecution(), Level.INFO, "linkcheck",
                "completed", mnem, name, "" + value.submitted, "" + value.ignored, time);

        context.getExecution().putValue("linkcheck.ignored", "" + value.ignored);
        context.getExecution().putValue("linkcheck.submitted", "" + value.submitted);

        Submission submission = WeblinkLinkchecker.getShared().getSubmission(context.getExecution());
        if (submission != null) {
            context.getExecution().putValue("linkcheck.processed", "" + submission.getProcessed());
        }

        try {
            if (collection != null) {

                collection.putValue(SugarControlledVocabulary.COLLECTION_LINK_VALIDATION,
                        "" + context.getExecution().getId());

                ((ActiveExecution<I>)context).getStorageEngine().updateCollection(collection);

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

    @Override
    public <I> boolean processRecord(MetaDataRecord<I> mdr, ExecutionContext<I> context)
            throws IngestionPluginFailedException, CorruptedMetadataRecordException {
        Data value = context.getValue(DATA);

        // Adapter that ensures compatibility with the europeana datamodel 
		Map<TKey<?, ?>, QValueAdapterStrategy<?, ?, ?, ?>> strategies =  new HashMap<TKey<?, ?>, QValueAdapterStrategy<?, ?, ?, ?>>();
		
		strategies.put(ObjectModelRegistry.LINK, new EuropeanaLinkAdapterStrategy());
		
		 MetadataRecordAdapter<I, QValueAdapterStrategy<?, ?, ?, ?>> mdrad = AdapterFactory.getAdapter(mdr, strategies);
        
        // get all links
        List<QualifiedValue<Link>> linkList = mdrad.getQualifiedValues(ObjectModelRegistry.LINK);
        
        
        int index = 0;
        for (QualifiedValue<Link> linkQv : linkList) {
            boolean disjoint = (linkQv.getQualifiers() == null)? false : Collections.disjoint(linkQv.getQualifiers(), value.checktypes);
            if (disjoint) {
                // link is not relevant.
                synchronized (value) {
                    value.ignored++;
                }
                continue;
            }

            synchronized (value) {
                value.submitted++;
            }

            Link link = linkQv.getValue();
            try {
                final LoggingEngine<I> loggingEngine = context.getLoggingEngine();
                WeblinkLinkchecker.getShared().offer(
                        new GuardedMetaDataRecordUrl<I>(context.getExecution(), mdrad, link, index++,
                                new URL(link.getUrl())) {
                            @SuppressWarnings("unchecked")
                            @Override
                            public void processed(int status, String message) {
                                LinkStatus state = null;
                                if (status == 0) {
                                    state = LinkStatus.FAILED_CONNECTION;
                                } else if (status < 400) {
                                    state = LinkStatus.VALID;
                                } else if (status < 500) {
                                    state = LinkStatus.FAILED_FOURHUNDRED;
                                } else {
                                    state = LinkStatus.FAILED_FIVEHUNDRED;
                                }

                                getLink().setLastChecked(new Date());
                                getLink().setLinkStatus(state);

                                String time = df.format(getLink().getLastChecked());

                                Execution<I> execution = getExecution();

                                loggingEngine.logLink(execution, "linkcheck", getMetaDataRecord(),
                                        getLink().getUrl(), status, time, message,
                                        getUrl().getHost(), getUrl().getPath());

                                Submission submission = WeblinkLinkchecker.getShared().getSubmission(
                                        execution);

                                if (submission != null) {
                                    synchronized (submission) {
                                        execution.putValue("linkcheck.processed",
                                                "" + submission.getProcessed());
                                        
                                        if (!execution.isActive()) {
                                            
                                            // need to store our own
                                            try {
                                                if (submission.getProcessed() % 500 == 0) {
                                                    if (((StorageEngine<I>)submission.getStorageEngine()) != null) {
                                                        ((StorageEngine<I>)submission.getStorageEngine()).updateExecution(execution);
                                                    }
                                                } else if (!submission.hasRemaining()) {
                                                    if (((StorageEngine<I>)submission.getStorageEngine()) != null) {
                                                        ((StorageEngine<I>)submission.getStorageEngine()).updateExecution(execution);
                                                    }
                                                }
                                            } catch (StorageEngineException e) {
                                                throw new RuntimeException(
                                                        "Caused by StorageEngineException", e);
                                            }
                                        }
                                    }
                                }

                                // TODO: deal with mdr update
                                log.info("Checked <" + getLink().getUrl() + ">" + message);
                            }
                        }, context);
            } catch (MalformedURLException e) {
                log.info("Invalid url: <" + link.getUrl() + "> for MDR:" + mdr.getId());
            }
        }// all links in mdr

        return true;
    }

    /**
     * @param sugarService
     */
    public void setSugarService(SugarService sugarService) {
        LinkCheckIngestionPlugin.sugarService = sugarService;
    }

    /**
     * @param sugarService
     */
    public void unsetSugarService(SugarService sugarService) {
        LinkCheckIngestionPlugin.sugarService = null;
    }

    /**
     * @return the sugar service
     */
    public SugarService getSugarService() {
        return LinkCheckIngestionPlugin.sugarService;
    }
}
