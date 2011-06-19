/* UIMWorkflowTest.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.command;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import org.apache.felix.service.command.CommandSession;
import org.junit.Test;

import eu.europeana.uim.UIMRegistry;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
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

    
    
    @Test
    public void testCreateProvider() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);

        ProviderBean<Long> provider = new ProviderBean<Long>(1L);

        UIMRegistry registry = new UIMRegistry();
        
        StorageEngine storage = mock(StorageEngine.class);
        when(storage.getIdentifier()).thenReturn("identifier");
        when(storage.createProvider()).thenReturn(provider);

        registry.addStorageEngine(storage);
        
        
        CommandSession session = mock(CommandSession.class);
        when(session.getConsole()).thenReturn(out);

        UIMStore command = new UIMStore(registry);
        command.operation = UIMStore.Operation.createProvider;
        command.argument0 = "mnemonic";
        command.argument1 = "name";

        command.execute(session);
        
        assertEquals("mnemonic", provider.getMnemonic());
        
        command.operation = UIMStore.Operation.checkpoint;
        command.execute(session);
    }
    
    
    @Test
    public void testUpdateProvider() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);

        ProviderBean<Long> provider = new ProviderBean<Long>(1L);

        UIMRegistry registry = new UIMRegistry();
        
        StorageEngine storage = mock(StorageEngine.class);
        when(storage.getIdentifier()).thenReturn("identifier");
        when(storage.findProvider((String)any())).thenReturn(provider);

        registry.addStorageEngine(storage);
        
        
        CommandSession session = mock(CommandSession.class);
        when(session.getConsole()).thenReturn(out);

        UIMStore command = new UIMStore(registry);
        command.operation = UIMStore.Operation.updateProvider;
        command.argument0 = "p";
        command.argument1 = "oaiBaseUrl";
        command.argument2 = "url";

        command.execute(session);
        assertEquals("url", provider.getOaiBaseUrl());
        
        command.operation = UIMStore.Operation.checkpoint;
        command.execute(session);
    }
    
    
    @Test
    public void testCreateCollection() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);

        ProviderBean<Long> provider = new ProviderBean<Long>(1L);
        Collection collection = new CollectionBean<Long>(1L, provider);

        UIMRegistry registry = new UIMRegistry();
        
        StorageEngine storage = mock(StorageEngine.class);
        when(storage.getIdentifier()).thenReturn("identifier");
        when(storage.findProvider((String)any())).thenReturn(provider);
        when(storage.createCollection((Provider)any())).thenReturn(collection);

        registry.addStorageEngine(storage);
        
        
        CommandSession session = mock(CommandSession.class);
        when(session.getConsole()).thenReturn(out);

        UIMStore command = new UIMStore(registry);
        command.operation = UIMStore.Operation.createCollection;
        command.argument0 = "mnemonic";
        command.argument1 = "name";
        command.parent = "p";

        command.execute(session);
        
        assertEquals("mnemonic", collection.getMnemonic());
        
        command.operation = UIMStore.Operation.checkpoint;
        command.execute(session);
    }
    
    

    @Test
    public void testUpdateCollection() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);

        Collection collection = new CollectionBean<Long>(1L, null);

        UIMRegistry registry = new UIMRegistry();
        
        StorageEngine storage = mock(StorageEngine.class);
        when(storage.getIdentifier()).thenReturn("identifier");
        when(storage.findCollection((String)any())).thenReturn(collection);

        registry.addStorageEngine(storage);
        
        
        CommandSession session = mock(CommandSession.class);
        when(session.getConsole()).thenReturn(out);

        UIMStore command = new UIMStore(registry);
        command.operation = UIMStore.Operation.updateCollection;
        command.argument0 = "p";
        command.argument1 = "oaiBaseUrl";
        command.argument2 = "url";

        command.execute(session);
        assertEquals("url", collection.getOaiBaseUrl(false));
        
        command.operation = UIMStore.Operation.checkpoint;
        command.execute(session);
    }
    
}
