/* SugarServletTest.java - created on Feb 9, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.soap.servlet;

import java.io.IOException;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.europeana.uim.Registry;
import eu.europeana.uim.UIMRegistry;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.memory.MemoryStorageEngine;
import eu.europeana.uim.sugar.SugarException;
import eu.europeana.uim.sugar.SugarServiceImpl;
import eu.europeana.uim.sugar.client.PropertiesSugarMapping;
import eu.europeana.uim.sugar.client.SugarClient;
import eu.europeana.uim.sugar.client.SugarSoapClientImpl;
import eu.europeana.uim.sugar.servlet.SugarServlet;
import eu.europeana.uim.sugar.soap.SugarServiceImplTest;

/**
 * Tests sugar servlet.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 9, 2012
 */
public class SugarServletTest {
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
     * Logout after finished tests.
     * 
     * @throws SugarException
     */
    @AfterClass
    public static void logout() throws SugarException {
        service.logout();
    }

    /**
     * Login into service, necessary to be done before using it.
     * 
     * @throws SugarException
     */
    @Before
    public void login() throws SugarException {
        if (!service.hasActiveSession()) {
            service.login();
        }
    }

    /**
     * Test update functionality on collection!
     * 
     * @throws SugarException
     * @throws StorageEngineException
     */
    @Test
    public void testUpdateCollection() throws SugarException, StorageEngineException {
        Registry registry = new UIMRegistry();
        registry.addStorageEngine(new MemoryStorageEngine());
        SugarServlet servlet = new SugarServlet(registry);
        servlet.setSugarService(service);

        servlet.updateProvider("P01374", null);
        servlet.updateCollection("a1009b", null);
    }
}
