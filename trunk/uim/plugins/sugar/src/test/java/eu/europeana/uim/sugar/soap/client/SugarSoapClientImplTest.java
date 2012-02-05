/* SugarSoapClientImpl.java - created on Feb 5, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.soap.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import eu.europeana.uim.sugarcrm.LoginFailureException;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 5, 2012
 */
public class SugarSoapClientImplTest {

    private static String username;
    private static String password;
    private static Properties properties;
    
    private SugarClient client;
    private String session;
    
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
                "No credentials configured! sugar.username, sugar.password " +
                "must be set via system property for tests."); } 
        
        properties = new Properties();
        properties.load(SugarSoapClientImplTest.class.getResourceAsStream("/sugarcrm.properties"));
    }
    
    /**
     * @throws LoginFailureException
     */
    @Before
    public void login() throws LoginFailureException{
        String endpoint = properties.getProperty("sugar.endpoint");
        
        String providerModul = properties.getProperty("sugar.provider");
        String providerMnemonic = properties.getProperty("sugar.provider.mnemonic");

        String collectionModul = properties.getProperty("sugar.collection");
        String collectionMnemonic = properties.getProperty("sugar.collection.mnemonic");

        String contactModul = properties.getProperty("sugar.contact");
        
        client = new SugarSoapClientImpl(endpoint, username, password, providerModul, providerMnemonic, collectionModul, collectionMnemonic, contactModul);
        session = client.login();
    }

    
    /**
     * 
     */
    @After
    public void logout() {
        client.logout(session);
    }
    
    /**
     * 
     */
    @Test
    public void testLogin() {
        assertNotNull(session);
        
        List<String> modules = client.getAvailableModules(session);
        
        assertTrue(modules.contains(((SugarSoapClientImpl)client).getProviderModule()));
        assertTrue(modules.contains(((SugarSoapClientImpl)client).getCollectionModule()));
        assertTrue(modules.contains(((SugarSoapClientImpl)client).getContactModule()));
    }
    
    /**
     * 
     */
    @Test
    public void testListCollections() {
        List<Map<String,String>> collections = client.getCollections(session, null, 12);
        assertEquals(12 , collections.size());
    }

    /**
     * 
     */
    @Test
    public void testGetCollection() {
        String mnemonic = properties.getProperty("test.collection.mnemonic");
        Map<String,String> collection = client.getCollection(session, mnemonic);
        assertNotNull(collection.get("id"));
        
        String sugarid = collection.get(((SugarSoapClientImpl)client).getCollectionMnemonicUnqualified());
        assertEquals(mnemonic, sugarid);
    }
    
    /**
     * 
     */
    @Test
    public void testListProvider() {
        List<Map<String,String>> providers = client.getProviders(session, null, 12);
        assertEquals(12 , providers.size());
    }
    
    /**
     * 
     */
    @Test
    public void testGetProvider() {
        String mnemonic = properties.getProperty("test.provider.mnemonic");
        Map<String,String> provider = client.getProvider(session, mnemonic);
        assertNotNull(provider.get("id"));
        
        String sugarid = provider.get(((SugarSoapClientImpl)client).getProviderMnemonicUnqualified());
        assertEquals(mnemonic, sugarid);
    }
    
    /**
     * 
     */
    @Test
    public void testListContacts() {
        List<Map<String,String>> contacts = client.getContacts(session, null, 12);
        assertEquals(12 , contacts.size());
    }
    
}
