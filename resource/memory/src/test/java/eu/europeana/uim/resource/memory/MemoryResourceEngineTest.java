/* MemoryResourceEngineTest.java - created on May 9, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.resource.memory;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import eu.europeana.uim.resource.AbstractResourceEngineTest;
import eu.europeana.uim.resource.ResourceEngine;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.bean.CollectionBean;
import eu.europeana.uim.store.bean.ProviderBean;
import eu.europeana.uim.workflow.Workflow;

/**
 * Implementation of test cases for memory based resource engine.
 *
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date May 9, 2011
 */
@RunWith(JUnit4.class)
public class MemoryResourceEngineTest extends AbstractResourceEngineTest<Long> {

    @Override
    protected ResourceEngine getResourceEngine() {
        return new MemoryResourceEngine();
    }

    private static final AtomicLong id = new AtomicLong();

    @Override
    protected Long nextID() {
        return id.incrementAndGet();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Workflow testGenerateWorkflow() {
        Workflow workflow = mock(Workflow.class);

        when(workflow.getIdentifier()).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "Workflow";
            }
        });

        return workflow;
    }

    @Override
    public Provider<Long> testGenerateProvider() {
        Provider<Long> provider = new ProviderBean<>(nextID());
        provider.setMnemonic("pro");
        return provider;
    }

    @Override
    public Collection<Long> testGenerateCollection(Provider<Long> provider) {
        Collection<Long> collection = new CollectionBean<>(nextID(), provider);
        collection.setMnemonic("col");
        return collection;
    }
}
