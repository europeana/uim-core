/* SoapSugarCRMManager.java - created on Jul 29, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.ServiceFactory;

import org.apache.axis.AxisFault;
import org.sugarcrm.soap.Entry_value;
import org.sugarcrm.soap.Get_entry_list_result;
import org.sugarcrm.soap.Get_entry_result;
import org.sugarcrm.soap.Get_relationships_result;
import org.sugarcrm.soap.Id_mod;
import org.sugarcrm.soap.Module_list;
import org.sugarcrm.soap.Name_value;
import org.sugarcrm.soap.Set_entry_result;
import org.sugarcrm.soap.SugarsoapBindingStub;
import org.sugarcrm.soap.SugarsoapLocator;
import org.sugarcrm.soap.User_auth;

import eu.europeana.uim.sugar.utils.SugarUtil;
import eu.europeana.uim.sugarcrm.SugarException;

/**
 * This manages all the connection to the TEL SugarCRM and delivers the result back using SOAP
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date Jul 29, 2011
 */
public class SugarSoapClientImpl implements SugarClient {

    private static final Logger  log     = Logger.getLogger(SugarSoapClientImpl.class.getName());

    private static final Integer TIMEOUT = 20000;

    private String               endPointUrl;
    private String               username;
    private String               password;

    private String               providerModule;
    private String               providerMnemonicField;

    private String               collectionModule;
    private String               collectionMnemonicField;

    private String               contactModule;
    private String               contactMnemonicField;

    private int                  timeout = TIMEOUT;


    private SugarsoapBindingStub binding;

    /**
     * Creates a new instance of this class.
     * 
     * @param endPointUrl
     * 
     * @param username
     * @param password
     * @param providerModule
     * @param providerMnemonic
     * @param collectionModule
     * @param collectionMnemonic
     * @param contactModule
     */
    public SugarSoapClientImpl(String endPointUrl, String username, String password,
                               String providerModule, String providerMnemonic,
                               String collectionModule, String collectionMnemonic,
                               String contactModule) {
        setEndPointUrl(endPointUrl);
        setUsername(username);
        setPassword(password);
        setProviderModule(providerModule);
        setProviderMnemonicField(providerMnemonic);
        setCollectionModule(collectionModule);
        setCollectionMnemonicField(collectionMnemonic);
        setContactModule(contactModule);
    }

    private void initializeWSDLBinding() {
        URL wsdlUrl = null;
        try {
            if (getEndPointUrl().isEmpty()) {
                wsdlUrl = new URL(new SugarsoapLocator().getsugarsoapPortAddress() + "?wsdl");
            } else {
                wsdlUrl = new URL(getEndPointUrl() + "?wsdl");
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not generate WSDL url from " + getEndPointUrl(), e);
        }

        URL endPointURL;
        try {
            endPointURL = new URL(getEndPointUrl());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not generate end point url from " + getEndPointUrl(),
                    e);
        }

        log.finest("URL endpoint created successfully!");

        // Create Service for test configuration
        ServiceFactory serviceFactory;
        try {
            serviceFactory = ServiceFactory.newInstance();
        } catch (ServiceException e) {
            throw new RuntimeException("Could not get a service factory", e);
        }
        Service service;
        try {
            service = serviceFactory.createService(wsdlUrl, new SugarsoapLocator().getServiceName());
        } catch (ServiceException e) {
            throw new RuntimeException("could not generate service from the service factory: " +
                                       wsdlUrl, e);
        }

        log.finest("SugarCRM service created successfully");
        log.finest("Service Name:" + service.getServiceName().toString());
        log.finest("Service WSDL:" + service.getWSDLDocumentLocation().toString());

        try {
            binding = new SugarsoapBindingStub(endPointURL, service);
        } catch (AxisFault e) {
            throw new RuntimeException("Could not create a binding stub via AXIS for " +
                                       service.getServiceName(), e);
        }

        binding.setTimeout(TIMEOUT);
        log.finest("Stub created successfully!");
    }

    /**
     * Login to SugarCRM and return the session ID.
     * 
     * @return the session id
     * @throws SugarException
     *             if login was not successful
     */
    @Override
    public String login() throws SugarException {
        return login(getUsername(), getPassword());
    }

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
    @Override
    public String login(String username, String password) throws SugarException {
        if (binding == null) {
            initializeWSDLBinding();
        }

        User_auth userAuthInfo = new User_auth();
        userAuthInfo.setUser_name(username);
        userAuthInfo.setPassword(SugarUtil.generateMD5HexString(password));
        userAuthInfo.setVersion("5.5.4");

        try {

            Set_entry_result loginResult = binding.login(userAuthInfo, SugarClient.class.getName());
            if ("-1".equals(loginResult.getId())) {
                log.severe("Login failed. " + loginResult.getError().getName() +
                           loginResult.getError().getDescription());
                throw new SugarException("Login failed for the credentials for user:" + username);
            }
            log.info("SugarCRM Login successful for " + username + " SessionID: " +
                     loginResult.getId());
            String sessionID = loginResult.getId();
            return sessionID;
        } catch (RemoteException ex) {
            log.log(Level.SEVERE, "Could not login.", ex);
            throw new SugarException("General server error during login:" + username, ex);
        }
    }

    /**
     * Logout from SugarCRM
     * 
     * @param session
     *            the session id
     */
    @Override
    public void logout(String session) {
        try {
            binding.logout(session);
            log.info("Logout Successfully for session " + session);
        } catch (RemoteException ex) {
            log.severe("Logout failed. Message: " + ex.getMessage());
        }
    }

    /**
     * Get the full record for a single collection
     * 
     * @param session
     *            the session id
     * @param id
     *            the record id
     * @return the full record: null if none could be retrieved
     */
    @Override
    public Map<String, String> getCollection(String session, String id) {
        return getSingleEntry(session, getCollectionModule(), id, getCollectionMnemonicField());
    }

    @Override
    public List<Map<String, String>> getContacts(String session, String query,
            int maxContactsToFetch) {
        return getModuleList(session, getContactModule(), query, null, maxContactsToFetch, 0);
    }

    @Override
    public Map<String, String> getProvider(String session, String id) {
        return getSingleEntry(session, getProviderModule(), id, getProviderMnemonicField());
    }

    @Override
    public Map<String, String> getContact(String session, String id) {
        return getSingleEntryFromInternalId(session, getContactModule(), id);
        // return getSingleEntry(session, getContactModule(), id, CONTACT_ID_FIELD);
    }

    @Override
    public List<Map<String, String>> getCollections(String session, String query, int maxResults) {
        return getModuleList(session, getCollectionModule(), query, null, maxResults, 0);
    }

    @Override
    public List<Map<String, String>> getProviders(String session, String query, int maxResults) {
        return getModuleList(session, getProviderModule(), query, null, maxResults, 0);
    }

    @Override
    public String getProviderForCollection(String session, String mnemonic) {
        Map<String, String> collection = getCollection(session, mnemonic);
        
        List<String> relationsships = getRelationsships(session, getCollectionModule(),
                collection.get("id"), getProviderModule(), "");

        if (!relationsships.isEmpty()) {
            // just get the provider record for the first entry in the relation
            Map<String, String> singleEntry = getSingleEntryFromInternalId(session,
                    getProviderModule(), relationsships.iterator().next());
            if (singleEntry == null) { return null; }
            String providerID = singleEntry.get(getProviderMnemonicUnqualified());
            return providerID;
        } else {
            // could not find any relevant provider for this.
            return null;
        }
    }

    @Override
    public boolean updateCollection(String session, String id, Map<String, String> values) {
        return updateEntry(session, getCollectionModule(), id, getCollectionMnemonicField(), values);
    }

    @Override
    public boolean updateProvider(String session, String id, Map<String, String> values) {
        return updateEntry(session, getProviderModule(), id, getProviderMnemonicField(), values);
    }

    @Override
    public boolean updateEntry(String session, String module, String id, String idfield,
            Map<String, String> values) {

        Map<String, String> singleEntry = getSingleEntry(session, module, id, idfield);

        if (singleEntry == null) {
            log.severe("Could not get record to update: module: " + module + " id: " + id +
                       " idfield: " + idfield);
            return false;
        }

        String sugarid = singleEntry.get("id");
        if (sugarid == null) {
            log.severe("Could not get internal SugarCRM id to update: module: " + module + " id: " +
                       id + " idfield: " + idfield);
            return false;
        }

        ArrayList<Name_value> nameValues = new ArrayList<Name_value>();

        nameValues.add(new Name_value("id", sugarid));
        for (Entry<String, String> entry : values.entrySet()) {
            nameValues.add(new Name_value(entry.getKey(), entry.getValue()));
        }

        try {
            binding.set_entry(session, module, nameValues.toArray(new Name_value[0]));
        } catch (RemoteException e) {
            log.log(Level.SEVERE, "Could not update the record. Module " + module + " id: " + id +
                                  " idfield: " + idfield, e);
            return false;
        }
        return true;
    }

    /**
     * Returns a list of all available SugarCRM modules
     * 
     * @param session
     *            the session id
     * @return all standard and custom modules in Sugarcrm
     */

    @Override
    public List<String> getAvailableModules(String session) {
        Module_list get_available_modules;
        try {
            get_available_modules = binding.get_available_modules(session);
        } catch (RemoteException e) {
            throw new RuntimeException("Could not get the available modules", e);
        }

        return Arrays.asList(get_available_modules.getModules());
    }

    /**
     * Returns a list of all entries with the complete record. This call gets first a list of all
     * ids, and then queries again for each entry.
     * 
     * @param session
     *            the session id
     * @param module
     *            the module to be queried
     * @param query
     *            the query to perform. Empty string for all records.
     * @param maxresults
     *            the maximum number of results needed. 0 for all
     * @return a list of full records
     */
    @Override
    public List<Map<String, String>> getModuleList(String session, String module, String query,
            String orderBy, int maxresults, int offset) {
        String[] select_fields = null;

        Get_entry_list_result getEntryListResponse = null;
        // Trying to get entry
        try {
            getEntryListResponse = binding.get_entry_list(session, module, query, orderBy, offset,
                    select_fields, maxresults, 0);
        } catch (RemoteException e) {
            log.log(Level.SEVERE, "Could not get entry list.", e);
            return null;
        }

        // Getting the fields for entry we got.
        LinkedList<Map<String, String>> result = new LinkedList<Map<String, String>>();
        Entry_value[] entryList = getEntryListResponse.getEntry_list();
        for (int k = 0; k < entryList.length; k++) {
            Entry_value entry = entryList[k];
            Name_value[] entryNameValueList = entry.getName_value_list();
            HashMap<String, String> recordMap = new HashMap<String, String>();
            for (int j = 0; j < entryNameValueList.length; j++) {
                Name_value entryNameValue = entryNameValueList[j];
                // Outputting only non empty fields
                if (!entryNameValue.getValue().isEmpty()) {
                    recordMap.put(entryNameValue.getName(), entryNameValue.getValue());
                }
            }
            result.add(recordMap);
        }
        return result;
    }

    @Override
    public HashMap<String, String> getSingleEntryFromInternalId(final String session,
            final String module, final String id) {
        // try to get the full record
        Get_entry_result entryResult;
        try {
            entryResult = binding.get_entry(session, module, id, null);
        } catch (RemoteException e) {
            log.log(Level.SEVERE, "Could not get single entry.", e);
            return null;
        }

        // the real data are stored in a list inside the result
        Entry_value[] entry_list = entryResult.getEntry_list();
        if (entry_list.length != 1) {
            log.severe("Could not get a single collection with id " + id + ". Found " +
                       entry_list.length + " results instead.");
            return null;
        }

        // convert to a hashmap
        Entry_value entry = entry_list[0];
        Name_value[] entryNameValueList = entry.getName_value_list();

        HashMap<String, String> recordMap = new HashMap<String, String>();
        for (int j = 0; j < entryNameValueList.length; j++) {
            Name_value entryNameValue = entryNameValueList[j];
            // Outputting only non empty fields
            if (!entryNameValue.getValue().isEmpty()) {
                recordMap.put(entryNameValue.getName(), entryNameValue.getValue());
            }
        }

        if ("1".equals(recordMap.get("deleted"))) { return null; }
        return recordMap;
    }

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

    @Override
    public Map<String, String> getSingleEntry(String session, String module, String id,
            String idfield) {
        String query = idfield + " = \"" + id + "\"";

        List<Map<String, String>> list = getModuleList(session, module, query, "", 1, 0);
        if (list != null && !list.isEmpty()) {
            Map<String, String> resultInListView = list.iterator().next();

            // first determine the internal id
            String internalid = resultInListView.get("id");

            if (internalid == null) {
                log.severe("Record does not have an internal SugarCRM id!");
                return null;
            }

            return resultInListView;

        } else {
            return null;
        }
    }

    /**
     * Get the full records of the contact belonging to an organization
     * 
     * @param session
     * @param id
     * @return teh full records, null if an error happenend
     */
    @Override
    public List<Map<String, String>> getProviderContacts(String session, String id) {
        List<Map<String, String>> allContactsInListview = getContacts(session, "",
                Integer.MAX_VALUE);
        if (allContactsInListview == null || allContactsInListview.size() == 0) {
            log.severe("Could not get contacts in Listview!");
            return null;
        }

        List<Map<String, String>> result = new LinkedList<Map<String, String>>();
        for (Map<String, String> contact : allContactsInListview) {

            String organizationID = contact.get(getProviderMnemonicUnqualified());
            if (organizationID != null && id.equals(organizationID)) {
                // get full record
                result.add(contact);
            }
        }
        return result;
    }

    /**
     * Get all relationsships between a single entry in a module and entries in another
     * 
     * @param session
     * @param module
     * @param moduleid
     * @param relatedModule
     * @param relatedModuleQuery
     * @return a list of SugarCRM internal ids corresponding to the relationsships: null if an error
     *         happened.
     */
    @Override
    public List<String> getRelationsships(String session, String module, String moduleid,
            String relatedModule, String relatedModuleQuery) {
        try {

            Get_relationships_result get_relationships = binding.get_relationships(session, module,
                    moduleid, relatedModule, relatedModuleQuery, 0);
            Id_mod[] ids = get_relationships.getIds();

            log.info("Got " + ids.length + " entries from " + relatedModule + " for the record " +
                     moduleid + " in module " + module);
            LinkedList<String> result = new LinkedList<String>();
            for (Id_mod idmod : ids) {
                if (idmod.getDeleted() == -1) {
                    continue;
                }
                result.add(idmod.getId());
            }
            return result;
        } catch (RemoteException e) {
            log.log(Level.SEVERE, "Could not get relationsships.", e);
            return null;
        }

    }

    @Override
    public String getURI() {
        return getEndPointUrl();
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the providerModule.
     * 
     * @return the providerModule
     */
    public String getProviderModule() {
        return providerModule;
    }

    /**
     * Sets the providerModule to the given value.
     * 
     * @param providerModule
     *            the providerModule to set
     */
    public void setProviderModule(String providerModule) {
        this.providerModule = providerModule;
    }

    /**
     * Returns the collectionModule.
     * 
     * @return the collectionModule
     */
    public String getCollectionModule() {
        return collectionModule;
    }

    /**
     * Sets the collectionModule to the given value.
     * 
     * @param collectionModule
     *            the collectionModule to set
     */
    public void setCollectionModule(String collectionModule) {
        this.collectionModule = collectionModule;
    }

    /**
     * Returns the contactModule.
     * 
     * @return the contactModule
     */
    public String getContactModule() {
        return contactModule;
    }

    /**
     * Sets the contactModule to the given value.
     * 
     * @param contactModule
     *            the contactModule to set
     */
    public void setContactModule(String contactModule) {
        this.contactModule = contactModule;
    }

    /**
     * Returns the timeout.
     * 
     * @return the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Sets the timeout to the given value.
     * 
     * @param timeout
     *            the timeout to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * Returns the providerMnemonicField.
     * 
     * @return the providerMnemonicField
     */
    public String getProviderMnemonicField() {
        return providerMnemonicField;
    }

    /**
     * @return the provider mnemonic field unqualified
     */
    public String getProviderMnemonicUnqualified() {
        if (getProviderMnemonicField().contains(".")) { return getProviderMnemonicField().substring(
                getProviderMnemonicField().lastIndexOf(".") + 1); }
        return getProviderMnemonicField();
    }

    /**
     * Sets the providerMnemonicField to the given value.
     * 
     * @param providerMnemonicField
     *            the providerMnemonicField to set
     */
    public void setProviderMnemonicField(String providerMnemonicField) {
        this.providerMnemonicField = providerMnemonicField;
    }

    /**
     * Returns the collectionMnemonicField.
     * 
     * @return the collectionMnemonicField
     */
    public String getCollectionMnemonicField() {
        return collectionMnemonicField;
    }

    /**
     * @return the collection mnemonic field unqualified
     */
    public String getCollectionMnemonicUnqualified() {
        if (getCollectionMnemonicField().contains(".")) { return getCollectionMnemonicField().substring(
                getCollectionMnemonicField().lastIndexOf(".") + 1); }
        return getCollectionMnemonicField();
    }

    /**
     * Sets the collectionMnemonicField to the given value.
     * 
     * @param collectionMnemonicField
     *            the collectionMnemonicField to set
     */
    public void setCollectionMnemonicField(String collectionMnemonicField) {
        this.collectionMnemonicField = collectionMnemonicField;
    }

    /**
     * Returns the contactMnemonicField.
     * 
     * @return the contactMnemonicField
     */
    public String getContactMnemonicField() {
        return contactMnemonicField;
    }

    /**
     * Returns the contactMnemonicField.
     * 
     * @return the contactMnemonicField
     */
    public String getContactMnemonicUnqualified() {
        if (getContactMnemonicField().contains(".")) { return getContactMnemonicField().substring(
                getContactMnemonicField().lastIndexOf(".") + 1); }
        return getContactMnemonicField();
    }

    /**
     * Sets the contactMnemonicField to the given value.
     * 
     * @param contactMnemonicField
     *            the contactMnemonicField to set
     */
    public void setContactMnemonicField(String contactMnemonicField) {
        this.contactMnemonicField = contactMnemonicField;
    }

    /**
     * Returns the endPointUrl.
     * 
     * @return the endPointUrl
     */
    public String getEndPointUrl() {
        return endPointUrl;
    }

    /**
     * Sets the endPointUrl to the given value.
     * 
     * @param endPointUrl
     *            the endPointUrl to set
     */
    public void setEndPointUrl(String endPointUrl) {
        this.endPointUrl = endPointUrl;
    }

}
