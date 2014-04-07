/* LinkcheckIngestionPluginTest.java - created on Mar 20, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.weblink;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Properties;

import org.junit.Ignore;
import org.junit.Test;
import org.theeuropeanlibrary.model.common.Link;
import org.theeuropeanlibrary.model.common.qualifier.LinkTarget;
import org.theeuropeanlibrary.model.tel.ObjectModelRegistry;
import org.theeuropeanlibrary.uim.check.weblink.AbstractLinkIngestionPlugin.Data;
import org.theeuropeanlibrary.uim.check.weblink.http.HttpClientSetup;
import org.theeuropeanlibrary.uim.check.weblink.http.Submission;
import org.theeuropeanlibrary.uim.check.weblink.http.WeblinkLinkchecker;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.logging.LoggingEngine;
import eu.europeana.uim.logging.LoggingEngineAdapter;
import eu.europeana.uim.orchestration.ActiveExecution;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.bean.CollectionBean;
import eu.europeana.uim.store.bean.ExecutionBean;
import eu.europeana.uim.store.bean.MetaDataRecordBean;

/**
 * Tests the link checking functionality.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 20, 2011
 */
public class LinkcheckIngestionPluginTest {
    /**
     * Tests a simple runthrough of the link checking plugin against live (and not-supposed to be)
     * live data.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Ignore
    @Test
    public void testSimpleCheck() {
        LinkCheckIngestionPlugin plugin = new LinkCheckIngestionPlugin();
        plugin.initialize();

        CollectionBean collection = new CollectionBean();
        collection.setName("test");
        ExecutionBean execution = new ExecutionBean(1L);
        execution.setDataSet(collection);

        Properties properties = new Properties();

        LoggingEngine logging = LoggingEngineAdapter.LONG;

        ActiveExecution<Collection<Long>, Long> context = mock(ActiveExecution.class);
        when(context.getProperties()).thenReturn(properties);
        when(context.getExecution()).thenReturn(execution);
        when(context.getLoggingEngine()).thenReturn(logging);

        MetaDataRecord mdr = new MetaDataRecordBean();
        Enum<?>[] linkTypeQualifier = new Enum<?>[] { LinkTarget.TABLE_OF_CONTENTS };

        mdr.addValue(
                ObjectModelRegistry.LINK,
                new Link(
                        "http://www.theeuropeanlibrary.org/exhibition-reading-europe/detail.html?id=103043"),
                linkTypeQualifier);
        mdr.addValue(
                ObjectModelRegistry.LINK,
                new Link(
                        "http://www.theeuropeanlibrary.org/exhibition-reading-europe/detail.html?id=96805"),
                linkTypeQualifier);
        mdr.addValue(
                ObjectModelRegistry.LINK,
                new Link(
                        "http://www.theeuropeanlibrary.org/exhibition-reading-europe/detail.html?id=96669"),
                linkTypeQualifier);
        mdr.addValue(
                ObjectModelRegistry.LINK,
                new Link(
                        "http://www.theeuropeanlibrary.org/exhibition-reading-europe/detail.html?id=96666"),
                linkTypeQualifier);

        mdr.addValue(ObjectModelRegistry.LINK, new Link(
                "http://www.theeuropeanlibrary.org/exhibition-reading-europe/detail.html?id=XX"),
                linkTypeQualifier);

        mdr.addValue(ObjectModelRegistry.LINK, new Link(
                "http://www.theeuropeanlibrary.org/exhibition-reading-europe/detail.html?id=99"),
                linkTypeQualifier);

        mdr.addValue(ObjectModelRegistry.LINK, new Link(
                "http://www.theeuropeanlibrary.org/exhibition-reading-europe/detail.html?id=XY"),
                linkTypeQualifier);

        mdr.addValue(
                ObjectModelRegistry.LINK,
                new Link(
                        "http://www.theeuropeanlibrary.org/exhibition-reading-europe/object.html?id=97923"),
                linkTypeQualifier);

// mdr.addValue(
// ObjectModelRegistry.LINK,
// new Link(
// "http://www.theeuropeanlibrary.org/exhibition-reading-europe/object.html?id=105256"),
// linkTypeQualifier);

        mdr.addValue(
                ObjectModelRegistry.LINK,
                new Link(
                        "http://www.theeuropeanlibrary.org/exhibition-reading-europe/object.html?id=113001"),
                linkTypeQualifier);

        mdr.addValue(
                ObjectModelRegistry.LINK,
                new Link(
                        "http://www.theeuropeanlibrary.org/exhibition-reading-europe/object.html?id=113009"),
                linkTypeQualifier);

        mdr.addValue(
                ObjectModelRegistry.LINK,
                new Link(
                        "http://www.theeuropeanlibrary.org/exhibition-reading-europe/object.html?id=97859"),
                linkTypeQualifier);

        mdr.addValue(
                ObjectModelRegistry.LINK,
                new Link(
                        "http://search.theeuropeanlibrary.org/images/expothumbs/93670/779b591c0fb51202eaa73606bfa49b41.jpg"),
                linkTypeQualifier);

        Data data = new AbstractLinkIngestionPlugin.Data();
        data.checktypes.add(LinkTarget.TABLE_OF_CONTENTS);

        when(context.getValue((TKey<?, Data>)any())).thenReturn(data);

        plugin.initialize(context);
        plugin.process(collection, context);
        plugin.completed(context);

        Submission submission = WeblinkLinkchecker.getShared().getSubmission(context.getExecution());
        submission.waitUntilFinished();

        assertEquals(12, submission.getProcessed());
        assertEquals(0, submission.getExceptions());
        assertEquals(10, submission.getStatus().get(200).intValue());
        assertNull(submission.getStatus().get(304));
        assertEquals(2, submission.getStatus().get(400).intValue());

        plugin.shutdown();
        HttpClientSetup.shutdown();
    }
}
