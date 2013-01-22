/* GuardedMetaDataRecordUrlTest.java - created on Aug 10, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.weblink.http;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;

import org.junit.Test;

import eu.europeana.uim.orchestration.ActiveExecution;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.bean.ExecutionBean;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Aug 10, 2011
 */
@SuppressWarnings("unchecked")
public class GuardedMetaDataRecordUrlTest {
    /**
     * @throws MalformedURLException
     */
    @Test
    public void addSingleHostRecords() throws MalformedURLException {
        Execution<Long> execution = new ExecutionBean<Long>(1L);
        ActiveExecution<MetaDataRecord<Long>, Long> context = mock(ActiveExecution.class);
        when(context.getExecution()).thenReturn(execution);

        GuardedMetaDataRecordUrl<Long> url1 = new GuardedMetaDataRecordUrl<Long>(
                execution,
                0,
                "http://search.theeuropeanlibrary.org/images/expothumbs/93670/120x120/cf51c021cf4dedc83b972b29c9cd0e4b.png") {
        };
        GuardedMetaDataRecordUrl<Long> url2 = new GuardedMetaDataRecordUrl<Long>(
                execution,
                0,
                "http://search.theeuropeanlibrary.org/images/expothumbs/93670/120x120/640070dc80531ad9c79da5c033e99fa5.jpg") {
        };
        GuardedMetaDataRecordUrl<Long> url3 = new GuardedMetaDataRecordUrl<Long>(
                execution,
                0,
                "http://search.theeuropeanlibrary.org/images/expothumbs/93670/120x120/90df442bc28b6521094338850cb51632.jpg") {
        };
        GuardedMetaDataRecordUrl<Long> url4 = new GuardedMetaDataRecordUrl<Long>(
                execution,
                0,
                "http://search.theeuropeanlibrary.org/images/expothumbs/93670/120x120/dd6ef05cb8dcd91325bf9f2ed66c5ad0.jpg") {
        };
        GuardedMetaDataRecordUrl<Long> url5 = new GuardedMetaDataRecordUrl<Long>(
                execution,
                0,
                "http://search.theeuropeanlibrary.org/images/expothumbs/93670/120x120/854854df646ddd79fb35fa82791cb192.png") {
        };

        WeblinkLinkchecker linkchecker = new WeblinkLinkchecker();
        linkchecker.offer(url1, context);
        linkchecker.offer(url2, context);
        linkchecker.offer(url3, context);
        linkchecker.offer(url4, context);
        linkchecker.offer(url5, context);

        Submission submission = linkchecker.getSubmission(execution);
        submission.waitUntilFinished();

        assertEquals(5, submission.getProcessed());
    }

    /**
     * @throws MalformedURLException
     */
    @Test
    public void addTwoHostRecords() throws MalformedURLException {
        Execution<Long> execution2 = new ExecutionBean<Long>(2L);
        Execution<Long> execution3 = new ExecutionBean<Long>(3L);
        ActiveExecution<MetaDataRecord<Long>, Long> context2 = mock(ActiveExecution.class);

        when(context2.getExecution()).thenReturn(execution2);

        ActiveExecution<MetaDataRecord<Long>, Long> context3 = mock(ActiveExecution.class);
        when(context3.getExecution()).thenReturn(execution3);

        GuardedMetaDataRecordUrl<Long> url1 = new GuardedMetaDataRecordUrl<Long>(
                execution2,
                0,
                "http://www.theeuropeanlibrary.org/images/expothumbs/93670/120x120/cf51c021cf4dedc83b972b29c9cd0e4b.png") {
        };
        GuardedMetaDataRecordUrl<Long> url2 = new GuardedMetaDataRecordUrl<Long>(
                execution3,
                0,
                "http://search.theeuropeanlibrary.org/images/expothumbs/93670/120x120/640070dc80531ad9c79da5c033e99fa5.jpg") {
        };
        GuardedMetaDataRecordUrl<Long> url3 = new GuardedMetaDataRecordUrl<Long>(
                execution2,
                0,
                "http://www.theeuropeanlibrary.org/images/expothumbs/93670/120x120/90df442bc28b6521094338850cb51632.jpg") {
        };
        GuardedMetaDataRecordUrl<Long> url4 = new GuardedMetaDataRecordUrl<Long>(
                execution3,
                0,
                "http://search.theeuropeanlibrary.org/images/expothumbs/93670/120x120/dd6ef05cb8dcd91325bf9f2ed66c5ad0.jpg") {
        };
        GuardedMetaDataRecordUrl<Long> url5 = new GuardedMetaDataRecordUrl<Long>(
                execution2,
                0,
                "http://www.theeuropeanlibrary.org/images/expothumbs/93670/120x120/854854df646ddd79fb35fa82791cb192.png") {
        };
        GuardedMetaDataRecordUrl<Long> url6 = new GuardedMetaDataRecordUrl<Long>(
                execution3,
                0,
                "http://search.theeuropeanlibrary.org/images/expothumbs/93670/120x120/cf51c021cf4dedc83b972b29c9cd0e4b.png") {
        };
        GuardedMetaDataRecordUrl<Long> url7 = new GuardedMetaDataRecordUrl<Long>(
                execution2,
                0,
                "http://www.theeuropeanlibrary.org/images/expothumbs/93670/120x120/640070dc80531ad9c79da5c033e99fa5.jpg") {
        };
        GuardedMetaDataRecordUrl<Long> url8 = new GuardedMetaDataRecordUrl<Long>(
                execution3,
                0,
                "http://search.theeuropeanlibrary.org/images/expothumbs/93670/120x120/90df442bc28b6521094338850cb51632.jpg") {
        };
        GuardedMetaDataRecordUrl<Long> url9 = new GuardedMetaDataRecordUrl<Long>(
                execution2,
                0,
                "http://www.theeuropeanlibrary.org/images/expothumbs/93670/120x120/dd6ef05cb8dcd91325bf9f2ed66c5ad0.jpg") {
        };
        GuardedMetaDataRecordUrl<Long> url10 = new GuardedMetaDataRecordUrl<Long>(
                execution3,
                0,
                "http://search.theeuropeanlibrary.org/images/expothumbs/93670/120x120/854854df646ddd79fb35fa82791cb192.png") {
        };

        WeblinkLinkchecker linkchecker = new WeblinkLinkchecker();
        linkchecker.offer(url1, context2);
        linkchecker.offer(url3, context2);
        linkchecker.offer(url5, context2);
        linkchecker.offer(url7, context2);
        linkchecker.offer(url9, context2);

        linkchecker.offer(url2, context3);
        linkchecker.offer(url4, context3);
        linkchecker.offer(url6, context3);
        linkchecker.offer(url8, context3);
        linkchecker.offer(url10, context3);

        Submission submission2 = linkchecker.getSubmission(execution2);
        submission2.waitUntilFinished();
        Submission submission3 = linkchecker.getSubmission(execution3);
        submission3.waitUntilFinished();

        assertEquals(5, submission2.getProcessed());
        assertEquals(5, submission3.getProcessed());
    }
}
