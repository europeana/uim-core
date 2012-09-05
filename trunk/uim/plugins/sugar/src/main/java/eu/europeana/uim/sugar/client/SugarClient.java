/* SugarCRMManager.java - created on Aug 5, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.client;

import java.util.List;
import java.util.Map;

import eu.europeana.uim.sugarcrm.SugarException;

/**
 * Interface for the access layer to (potentially) different ways to query SugarCRM
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date Aug 5, 2011
 */
public interface SugarClient {

    /**
     * Returs the relevant service URI
     * 
     * @return the associated service URI
     */
    public String getURI();

    /**
     * Login to SugarCRM and return the session ID.
     * 
     * @return the session id
     * @throws SugarException
     *             if login was not successful
     */
    public String login() throws SugarException;

    /**
     * Login to SugarCRM and return the session ID.
     * 
     * @param username
     *            the SugarCRM username
     * @param password
     *            the SugarCRM password
     * @return the session id
     * @throws SugarException
     *             if login was not successful
     */
    public String login(String username, String password) throws SugarException;

    /**
     * Logout from SugarCRM
     * 
     * @param session
     *            the session id
     */
    public void logout(String session);

    /**
     * Returns a list of all available SugarCRM modules
     * 
     * @param session
     *            the session id
     * @return all standard and custom modules in Sugarcrm
     */

    public List<String> getAvailableModules(String session);

    /**
     * Returns a list of the collection records. This includes only the fields visible in the
     * SugarCRM list view.
     * 
     * @param session
     * @param query
     * @param maxResults
     * @return list of records
     */
    public List<Map<String, String>> getCollections(String session, String query, int maxResults);

    /**
     * 
     * Returns a list of all providers that match the criteria. This call gets first a list of
     * all ids, and then queries again for each entry.
     * 
     * @param session
     * @param query
     * @param maxResults
     * @return list of partial records matching the criteria
     */
    public List<Map<String, String>> getProviders(String session, String query,
            int maxResults);

    /**
     * Returns a list of all collections with the complete record. This call gets first a list of
     * all ids, and then queries again for each entry.
     * 
     * @param session
     *            the session id
     * @param module
     *            the module to be queried
     * @param query
     *            the query to perform. Empty string for all records.
     * @param orderBy
     *            order criteria
     * @param maxresults
     *            the maximum number of results needed. 0 for all
     * @param offset
     *            the offset of the records to pull
     * @return a list of full records
     */
    public List<Map<String, String>> getModuleList(String session, String module,
            String query, String orderBy, int maxresults, int offset);

    /**
     * Get the full record for a single collection
     * 
     * @param session
     *            the session id
     * @param id
     *            the record id
     * @return the full record: null if none could be retrieved
     */
    public Map<String, String> getCollection(String session, String id);

    
    /**
     * @param session
     * @param id
     * @param values
     * @return true, if the update was successful
     */
    public boolean updateCollection(String session, String id, Map<String,String> values);

    
    /**
     * Get the full record for a single organization
     * 
     * @param session
     *            the session id
     * @param id
     *            the record id
     * @return the full record: null if none could be retrieved
     */
    public Map<String, String> getProvider(String session, String id);

    
    /**
     * @param session
     * @param id
     * @param values
     * @return true, if the update was successful
     */
    public boolean updateProvider(String session, String id, Map<String,String> values);

    
    /**
     * Get the full record for a single organization
     * 
     * @param session
     *            the session id
     * @param id
     *            the record id
     * @return the full record: null if none could be retrieved
     */
    public Map<String, String> getContact(String session, String id);

    /**
     * Returns a complete record from a module for a single id. The id field can be optionally
     * specified.
     * 
     * @param session
     *            the session id
     * @param module
     *            the module to get the the record from.
     * @param internalid
     *            the internal id of the record.
     * @return the full record, null if the record was not found
     */

    public Map<String, String> getSingleEntryFromInternalId(String session, String module,
            String internalid);

    /**
     * Returns a complete record from a module for a single id. The id field can be optionally
     * specified.
     * 
     * @param session
     *            the session id
     * @param module
     *            the module to get the the record from.
     * @param id
     *            the id of the record.
     * @param idfield
     *            if specified, the field to be used for the id. This field must be visible in the
     *            list view of SugarCRM if null, the standard field ("id") is used.
     * @return the full record, null if the record was not found
     */
    public Map<String, String> getSingleEntry(String session, String module, String id,
            String idfield);

    
    /**
     * @param session
     * @param module
     * @param id
     * @param idfield
     * @param values
     * @return true if update was successful
     */
    boolean updateEntry(String session, String module, String id, String idfield,
            Map<String, String> values);

    
    /**
     * Get the full records of the contact belonging to an organization
     * 
     * @param session
     * @param id
     * @return teh full records, null if an error happenend
     */
    public List<Map<String, String>> getProviderContacts(String session, String id);

    /**
     * Get the contact records in partial view
     * 
     * @param session
     *            the session id
     * @param query
     * @param maxContactsToFetch
     * @return the partial records for the contacts
     */
    public List<Map<String, String>> getContacts(String session, String query,
            int maxContactsToFetch);

    /**
     * @param session
     *            the session id
     * @param collectionid
     *            The collection identifier
     * @return the mnemonic of the provider, null if not found
     */
    public String getProviderForCollection(String session, String collectionid);

    /**
     * @param session
     * @param mnemonic
     * @return
     */
    public List<Map<String, String>> getTranslationsForCollection(String session,String mnemonic);
    
    /**
     * Get all relationsships between a single entry in a module and entries in another
     * 
     * @param session
     * @param module
     * @param moduleid
     * @param relatedModule
     * @param relatedModuleQuery
     * @return a list of ids corresponding to the relationships: null if an error happened.
     */

    public List<String> getRelationsships(String session, String module, String moduleid,
            String relatedModule, String relatedModuleQuery);

    /**
     * Create a new entry 
     * @param session
     * @param module
     * @param values
     * @return true, if the entry could be created
     */
    String createEntry(String session, String module, 
            Map<String, String> values);

    /**
     * @param session
     * @param module1
     * @param module1id
     * @param module1idfield
     * @param module2
     * @param module2id
     * @param module2idfield
     * @return
     */
    boolean createRelationsship(String session, String module1, String module1id, String module2, String module2id);
}