/* SugarCRMServiceImpl.java - created on Aug 15, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.soap;

import java.util.List;
import java.util.Map;

import org.theeuropeanlibrary.model.common.qualifier.Language;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.sugar.soap.client.SugarClient;
import eu.europeana.uim.sugarcrm.SugarException;
import eu.europeana.uim.sugarcrm.SugarService;

/**
 * TEL specific implementation of the SugarCRM service
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date Aug 15, 2011
 */
public class SugarServiceImpl implements SugarService {

    private String            sessionID = null;

    private final Registry    registry;
    private final SugarClient manager;

    /**
     * Creates a new instance of this class.
     * 
     * @param registry
     * @param manager
     */
    public SugarServiceImpl(Registry registry, SugarClient manager) {
        this.registry = registry;
        this.manager = manager;
    }

    /**
     * lazy loading of sugar crm manager.
     * 
     * @return
     */
    private SugarClient getManager() {
        return manager;
    }

    @Override
    public boolean hasActiveSession() throws SugarException {
        return sessionID != null;
    }

    @Override
    public String login(String username, String password) throws SugarException {
        sessionID = null;
        try {
            sessionID = getManager().login(username, password);
            return sessionID;
        } catch (SugarException e) {
            throw new SugarException("Could not login to SugarCRM" + e);
        }
    }

    @Override
    public void updateProvider(Provider<?> provider) throws SugarException {
        SugarClient client = validateConnection();

        
        String mnemonic = provider.getMnemonic();
        Map<String, String> sugarProvider = client.getProvider(sessionID, mnemonic);
        
        
        //
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public void synchronizeProvider(Provider<?> provider) throws SugarException {
        //
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public void updateCollection(Collection<?> collection) throws SugarException {
        //
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }

    @Override
    public void synchronizeCollection(Collection<?> collection) throws SugarException {
        //
        throw new UnsupportedOperationException("Sorry, not implemented.");
    }
    
    
    @Override
    public List<Map<String, String>> listProviders() throws SugarException {
        SugarClient client = validateConnection();
        
        List<Map<String,String>> providers = client.getProviders(sessionID, "",  Integer.MAX_VALUE);
        return providers;
    }

    
    
    @Override
    public List<Map<String, String>> listCollections() throws SugarException {
        SugarClient client = validateConnection();
        
        List<Map<String,String>> providers = client.getCollections(sessionID, "", Integer.MAX_VALUE);
        return providers;
    }

    
    
    private SugarClient validateConnection() throws SugarException {
        if (sessionID == null) { throw new SugarException("Could not find a valid session!"); }

        SugarClient sugarCRMManager = getManager();
        if (sugarCRMManager == null) { throw new SugarException(
                "Could not find SugarCRMManager!"); }
        return sugarCRMManager;
    }

    
    
    /**
     * Converts the SugarCrm language field content to simple Iso3 code.
     * 
     * @param code
     *            the full field entry from SugarCRM
     * @return the Iso3 code, "und" if not known
     */
    public static Language normalizeSugarLanguage(String code) {
        if (code == null || code.isEmpty()) return Language.UND;
        String[] split = code.split("-", 2);
        String isocode = split[0].trim();

        Language language = Language.lookupLanguage(isocode);
        if (language == null) {
            if (split.length > 1) {
                language = Language.lookupLanguage(split[1]);
            }
            if (language == null) {
                language = Language.UND;
            }
        }

        return language;
    }
}
