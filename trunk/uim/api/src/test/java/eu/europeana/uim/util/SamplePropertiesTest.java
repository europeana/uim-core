/* TestSampleProperties.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.util;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public class SamplePropertiesTest {

	
	@Test
	public void testLoadSamples() throws StorageEngineException, IOException {
		StorageEngine engine = mock(StorageEngine.class);
		Provider provider = mock(Provider.class);
		Collection collection = mock(Collection.class);
		
		when(engine.createProvider()).thenReturn(provider);
		when(engine.findProvider(anyString())).thenReturn(provider);
		
		when(engine.createCollection((Provider)any())).thenReturn(collection);
		when(engine.findCollection(anyString())).thenReturn(collection);

		SampleProperties sample = new SampleProperties();
		sample.loadSampleData(engine);
		
	}
}
