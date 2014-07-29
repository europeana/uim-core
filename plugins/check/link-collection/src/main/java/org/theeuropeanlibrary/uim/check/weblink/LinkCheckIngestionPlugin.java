/* LinkcheckIngestionPlugin.java - created on Mar 20, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.weblink;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.theeuropeanlibrary.model.common.Link;
import org.theeuropeanlibrary.model.common.qualifier.LinkStatus;
import org.theeuropeanlibrary.model.common.qualifier.LinkTarget;
import org.theeuropeanlibrary.model.tel.ObjectModelRegistry;
import org.theeuropeanlibrary.uim.check.weblink.http.GuardedMetaDataRecordUrl;
import org.theeuropeanlibrary.uim.check.weblink.http.Submission;
import org.theeuropeanlibrary.uim.check.weblink.http.WeblinkLinkchecker;

import eu.europeana.uim.Registry;
import eu.europeana.uim.adapter.UimDatasetAdapter;
import eu.europeana.uim.common.progress.MemoryProgressMonitor;
import eu.europeana.uim.common.progress.RevisableProgressMonitor;
import eu.europeana.uim.common.progress.RevisingProgressMonitor;
import eu.europeana.uim.logging.LoggingEngine;
import eu.europeana.uim.orchestration.ActiveExecution;
import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.orchestration.Orchestrator;
import eu.europeana.uim.plugin.ingestion.CorruptedDatasetException;
import eu.europeana.uim.plugin.ingestion.IngestionPluginFailedException;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.MetaDataRecord.QualifiedValue;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.sugar.SugarControlledVocabulary;
import eu.europeana.uim.sugar.SugarService;
import java.util.concurrent.BlockingQueue;

/**
 * This plugin check links and adds/updates status information onto the {@link Link} object.
 * 
 * @param <I>
 *            generic identifier
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 20, 2011
 */
public class LinkCheckIngestionPlugin<I> extends AbstractLinkIngestionPlugin<I> {
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

    private static Registry               registry;

    /**
     * Creates a new instance of this class.
     */
    public LinkCheckIngestionPlugin() {
        super("Link Checking Plugin", "Plugin which checks links for validity.");
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param registry
     */
    public LinkCheckIngestionPlugin(Registry registry) {
        super("Link Checking Plugin", "Plugin which checks links for validity.");
        LinkCheckIngestionPlugin.registry = registry;
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
    public void initialize(ExecutionContext<Collection<I>, I> context)
            throws IngestionPluginFailedException {
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
    public void completed(ExecutionContext<Collection<I>, I> context)
            throws IngestionPluginFailedException {
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

                ((ActiveExecution<Collection<I>, I>)context).getStorageEngine().updateCollection(
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
    public boolean process(Collection<I> coll, ExecutionContext<Collection<I>, I> context)
            throws IngestionPluginFailedException, CorruptedDatasetException {
        Data value = context.getValue(DATA);

        int threshold = 100;

        Orchestrator<I> orchestrator = (Orchestrator<I>)registry.getOrchestrator();
        ActiveExecution<?, Serializable> ae = (ActiveExecution<?, Serializable>)orchestrator.getActiveExecution(context.getExecution().getId());

        RevisableProgressMonitor monitor = ae.getMonitor();
        RevisingProgressMonitor revisingProgressMonitor = new MemoryProgressMonitor();
        monitor.addListener(revisingProgressMonitor);

        BlockingQueue<I> refQueue = null;
        try {
            refQueue = context.getStorageEngine().getMetaDataRecordIdsByCollection(coll);
            ae.incrementScheduled(refQueue.size());
        } catch (StorageEngineException e1) {
            e1.printStackTrace();
        }

        Submission submission = WeblinkLinkchecker.getShared().getSubmission(context.getExecution());

// int processed = 0;
        int failed = 0;

        while (!refQueue.isEmpty()) {
            if (submission == null || submission.getRemaining() < threshold) {
                try {
                    MetaDataRecord<I> mdr = context.getStorageEngine().getMetaDataRecord(
                            refQueue.remove());
                    offerRecord(mdr, context, value);
                    submission = WeblinkLinkchecker.getShared().getSubmission(
                            context.getExecution());

                } catch (MalformedURLException e) {
                    context.getExecution().setFailureCount(failed++);
                } catch (StorageEngineException e) {
                    context.getExecution().setFailureCount(failed++);
                }

                ae.incrementCompleted(1);
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }

    /**
     * @param mdr
     * @param context
     * @param data
     */
    private void offerRecord(MetaDataRecord<I> mdr, ExecutionContext<Collection<I>, I> context,
            Data value) throws MalformedURLException {

        @SuppressWarnings("unchecked")
        UimDatasetAdapter<MetaDataRecord<I>, I> adapter = (UimDatasetAdapter<MetaDataRecord<I>, I>)registry.getUimDatasetAdapter(this.getClass().getSimpleName());

        MetaDataRecord<I> localMdr;
        if (adapter != null) {
            localMdr = adapter.adapt(mdr);
        } else {
            localMdr = mdr;
        }

        List<QualifiedValue<Link>> linkList = localMdr.getQualifiedValues(ObjectModelRegistry.LINK);

// if (linkList.size() == 0) {
// // Adapter that ensures compatibility with the europeana datamodel
// Map<TKey<?, ?>, QValueAdapterStrategy<?, ?, ?, ?>> strategies = new HashMap<TKey<?, ?>,
// QValueAdapterStrategy<?, ?, ?, ?>>();
//
// strategies.put(ObjectModelRegistry.LINK, new EuropeanaLinkAdapterStrategy());
//
// MetadataRecordAdapter<I, QValueAdapterStrategy<?, ?, ?, ?>> mdrad = AdapterFactory.getAdapter(
// mdr, strategies);
//
// // get all links
// linkList = mdrad.getQualifiedValues(ObjectModelRegistry.LINK);
// }

        int index = 0;
        for (QualifiedValue<Link> linkQv : linkList) {
            boolean disjoint = (linkQv.getQualifiers() == null) ? false : Collections.disjoint(
                    linkQv.getQualifiers(), value.checktypes);
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
                        new GuardedMetaDataRecordUrl<I>(context.getExecution(), localMdr, link,
                                index++, new URL(link.getUrl())) {
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

                                                // FIXME: misuse of api
                                                loggingEngine.completed(null);
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
                log.info("Invalid url: <" + link.getUrl() + "> for MDR:" + localMdr.getId());
            }
        }// all links in mdr
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
