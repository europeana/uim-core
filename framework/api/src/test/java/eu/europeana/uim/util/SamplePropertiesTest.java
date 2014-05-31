/* TestSampleProperties.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.util;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.UimDataSet;

/**
 * Tests {@link SampleProperties} using mocks of {@link UimDataSet}s and
 * {@link StorageEngine}.
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 16, 2011
 */
public class SamplePropertiesTest {

    /**
     * Tests loading of samples.
     *
     * @throws StorageEngineException
     * @throws IOException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testLoadSamples() throws StorageEngineException, IOException {
        StorageEngine engine = mock(StorageEngine.class);
        Provider provider = mock(Provider.class);
        Collection collection = mock(Collection.class);

        when(engine.createProvider()).thenReturn(provider);
        when(engine.findProvider(anyString())).thenReturn(provider);

        when(engine.createCollection((Provider) any())).thenReturn(collection);
        when(engine.findCollection(anyString())).thenReturn(collection);

        SampleProperties sample = new SampleProperties();
        sample.loadSampleData(engine);
    }
}
