/* UIMWorkflowTest.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.command;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import org.apache.felix.service.command.CommandSession;
import org.junit.Test;

import eu.europeana.uim.UIMRegistry;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.store.bean.CollectionBean;
import eu.europeana.uim.store.bean.ProviderBean;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public class UIMStoreTest {

    @Test
    public void testListProvider() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);

        UIMRegistry registry = new UIMRegistry();

        ProviderBean<Long> provider = new ProviderBean<Long>(1L);
        provider.setMnemonic("mnemonic");
        provider.setName("name");
        
        StorageEngine storage = mock(StorageEngine.class);
        when(storage.getIdentifier()).thenReturn("identifier");
        when(storage.getAllProviders()).thenReturn(Arrays.asList(provider));

        registry.addStorageEngine(storage);

        CommandSession session = mock(CommandSession.class);
        when(session.getConsole()).thenReturn(out);

        UIMStore command = new UIMStore(registry);
        command.operation = UIMStore.Operation.listProvider;

        command.execute(session);

        String msg = new String(baos.toByteArray());
        assertEquals(20, msg.length());
    }
    
    
    @Test
    public void testListCollection() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);

        UIMRegistry registry = new UIMRegistry();

        ProviderBean<Long> provider = new ProviderBean<Long>(1L);
        provider.setMnemonic("mnemonic");
        provider.setName("name");
        
        CollectionBean<Long> collection = new CollectionBean<Long>(1L, provider);
        collection.setMnemonic("mnemonic");
        collection.setName("name");
        
        StorageEngine storage = mock(StorageEngine.class);
        when(storage.getIdentifier()).thenReturn("identifier");
        when(storage.getAllProviders()).thenReturn(Arrays.asList(provider));
        when(storage.getCollections(provider)).thenReturn(Arrays.asList(collection));

        registry.addStorageEngine(storage);

        CommandSession session = mock(CommandSession.class);
        when(session.getConsole()).thenReturn(out);

        UIMStore command = new UIMStore(registry);
        command.operation = UIMStore.Operation.listCollection;

        command.execute(session);

        String msg = new String(baos.toByteArray());
        assertEquals(38, msg.length());
    }
}
