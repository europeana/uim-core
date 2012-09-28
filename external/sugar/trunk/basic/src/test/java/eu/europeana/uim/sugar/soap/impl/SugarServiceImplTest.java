/* SugarServiceImplTest.java - created on Feb 5, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.soap.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import eu.europeana.uim.store.bean.CollectionBean;
import eu.europeana.uim.store.bean.ProviderBean;
import eu.europeana.uim.sugar.SugarException;
import eu.europeana.uim.sugar.client.PropertiesSugarMapping;
import eu.europeana.uim.sugar.client.SugarClient;
import eu.europeana.uim.sugar.client.SugarSoapClientImpl;
import eu.europeana.uim.sugar.impl.SugarServiceImpl;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 5, 2012
 */
public class SugarServiceImplTest {
    private static String           username;
    private static String           password;
    private static Properties       properties;

    private static SugarClient      client;
    private static SugarServiceImpl service;

    /**
     * Setup of the connection manager
     * 
     * @throws IOException
     */
    @BeforeClass
    public static void loadConfig() throws IOException {
        username = System.getProperty("sugar.username");
        password = System.getProperty("sugar.password");

        if (username == null || password == null) { throw new IllegalStateException(
                "No credentials configured! sugar.username, sugar.password "
                        + "must be set via system property for tests."); }

        properties = new Properties();
        properties.load(SugarServiceImplTest.class.getResourceAsStream("/sugarcrm.properties"));

        String endpoint = properties.getProperty("sugar.endpoint");

        String providerModul = properties.getProperty("sugar.provider");
        String providerMnemonic = properties.getProperty("sugar.provider.mnemonic");

        String collectionModul = properties.getProperty("sugar.collection");
        String collectionMnemonic = properties.getProperty("sugar.collection.mnemonic");

        String contactModul = properties.getProperty("sugar.contact");
        
        String collectionTranslationModul=properties.getProperty("sugar.collectiontranslation");

        client = new SugarSoapClientImpl(endpoint, username, password, providerModul,
                providerMnemonic, collectionModul, collectionMnemonic, contactModul,collectionTranslationModul);

        service = new SugarServiceImpl(client, new PropertiesSugarMapping(properties));
    }

    /**
     * @throws SugarException
     * 
     */
    @AfterClass
    public static void logout() throws SugarException {
        service.logout();
    }

    /**
     * @throws SugarException
     */
    @Before
    public void login() throws SugarException {
        if (!service.hasActiveSession()) {
            service.login();
        }
    }

    /**
     * @throws SugarException
     * 
     */
    @Test
    public void testSession() throws SugarException {
        assertTrue(service.hasActiveSession());
    }

    /**
     * @throws SugarException
     * 
     */
    @Test
    public void testListCollections() throws SugarException {
        List<Map<String, String>> alllist = service.listCollections(false);
        List<Map<String, String>> active = service.listCollections(true);

        assertTrue(alllist.size() >= active.size());
    }

    /**
     * @throws SugarException
     * 
     */
    @Test
    public void testListProviders() throws SugarException {
        List<Map<String, String>> alllist = service.listProviders(false);
        List<Map<String, String>> active = service.listProviders(true);

        assertTrue(alllist.size() >= active.size());
    }

    /**
     * @throws SugarException
     * 
     */
    @Test
    @Ignore
    public void testSynchronize() throws SugarException {
        String pMne = properties.getProperty("test.provider.mnemonic");
        String cMne = properties.getProperty("test.collection.mnemonic");

        ProviderBean<Long> provider = new ProviderBean<Long>(1L);
        provider.setMnemonic(pMne);

        CollectionBean<Long> collection = new CollectionBean<Long>(2L, provider);
        collection.setMnemonic(cMne);

        boolean update = service.synchronizeProvider(provider);
        assertTrue(update);

        update = service.synchronizeCollection(collection);
        assertTrue(update);
    }

    /**
     * @throws SugarException
     * 
     */
    @Test
    @Ignore
    public void testUpdateSugarProvider() throws SugarException {
        String pMne = properties.getProperty("test.provider.mnemonic");

        ProviderBean<Long> provider = new ProviderBean<Long>(1L);
        provider.setMnemonic(pMne);

        service.synchronizeProvider(provider);

        // so, now we do have sugar values
        String oldname = provider.getName();
        String newname = "JUnit Test";

        provider.setName(newname);
        service.updateProvider(provider);

        ProviderBean<Long> provider2 = new ProviderBean<Long>(1L);
        provider2.setMnemonic(provider.getMnemonic());

        service.synchronizeProvider(provider2);

        assertEquals(provider2.getName(), provider.getName());
        assertEquals(provider2.getName(), newname);

        provider2.setName(oldname);
        service.updateProvider(provider2);

        service.synchronizeProvider(provider);
        assertEquals(provider.getName(), provider2.getName());
        assertEquals(provider.getName(), oldname);
    }

    /**
     * @throws SugarException
     * 
     */
    @Test
    @Ignore
    public void testUpdateSugarCollection() throws SugarException {
        String pMne = properties.getProperty("test.provider.mnemonic");
        String cMne = properties.getProperty("test.collection.mnemonic");

        ProviderBean<Long> provider = new ProviderBean<Long>(1L);
        provider.setMnemonic(pMne);

        CollectionBean<Long> collection = new CollectionBean<Long>(2L, provider);
        collection.setMnemonic(cMne);

        service.synchronizeProvider(provider);
        service.synchronizeCollection(collection);

        // so, now we do have sugar values
        String oldname = provider.getName();
        String newname = "JUnit Test";

        collection.setName(newname);
        service.updateCollection(collection);

        CollectionBean<Long> collection2 = new CollectionBean<Long>(2L, provider);
        collection2.setMnemonic(collection.getMnemonic());

        service.synchronizeCollection(collection2);

        assertEquals(collection2.getName(), collection.getName());
        assertEquals(collection2.getName(), newname);

        collection2.setName(oldname);
        service.updateCollection(collection2);

        service.synchronizeCollection(collection);
        assertEquals(collection.getName(), collection2.getName());
        assertEquals(collection.getName(), oldname);
    }

}
