/* LinkcheckIngestionPluginTest.java - created on Mar 20, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.weblink;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.theeuropeanlibrary.model.common.Link;
import org.theeuropeanlibrary.model.common.qualifier.LinkTarget;
import org.theeuropeanlibrary.model.tel.ObjectModelRegistry;
import org.theeuropeanlibrary.uim.check.weblink.http.Submission;
import org.theeuropeanlibrary.uim.check.weblink.http.WeblinkLinkchecker;

import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.orchestration.AbstractBatchWorkflowTest;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * Tests the link checking functionality.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 20, 2011
 */
public class LinkcheckWorkflowTest extends AbstractBatchWorkflowTest {
    /**
     * Tests a simple runthrough of the link checking plugin against live (and not-supposed to be)
     * live data.
     * 
     * @throws StorageEngineException
     * @throws InterruptedException
     */
    @Test
    public void testSimpleCheck() throws InterruptedException, StorageEngineException {
        LinkedHashMap<String, List<String>> map = new LinkedHashMap<String, List<String>>();
        map.put(LinkCheckIngestionPlugin.TOC, new ArrayList<String>() {
            {
                add("true");
            }
        });
        registry.getResourceEngine().setGlobalResources(map);

        Execution<Long> execution = executeWorkflow(new LinkCheckWorkflow(), 1);
        
        Submission submission = WeblinkLinkchecker.getShared().getSubmission(execution);
        submission.waitUntilFinished();

        Assert.assertEquals(8, submission.getProcessed());
        Assert.assertEquals(0, submission.getExceptions());
        Assert.assertEquals(7, submission.getStatus().get(200).intValue());
        Assert.assertNull(submission.getStatus().get(304));
        Assert.assertEquals(1, submission.getStatus().get(400).intValue());
    }

    @Override
    protected void fillRecord(MetaDataRecord<Long> mdr, int count) {
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

        mdr.addValue(
                ObjectModelRegistry.LINK,
                new Link(
                        "http://www.theeuropeanlibrary.org/exhibition-reading-europe/object.html?id=97923"),
                linkTypeQualifier);

        mdr.addValue(
                ObjectModelRegistry.LINK,
                new Link(
                        "http://search.theeuropeanlibrary.org/images/expothumbs/93670/779b591c0fb51202eaa73606bfa49b41.jpg"),
                linkTypeQualifier);
    }
}
