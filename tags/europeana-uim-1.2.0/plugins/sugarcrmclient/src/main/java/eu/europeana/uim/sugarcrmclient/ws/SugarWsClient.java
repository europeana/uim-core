/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package eu.europeana.uim.sugarcrmclient.ws;


import eu.europeana.uim.sugarcrmclient.jibxbindings.GetAvailableModules;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetAvailableModulesResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetEntries;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetEntriesResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetEntry;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetEntryList;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetEntryListResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetEntryResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetModuleFields;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetModuleFieldsResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetNoteAttachment;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetNoteAttachmentResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetRelationships;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetRelationshipsResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetUserId;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetUserIdResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.IsUserAdmin;
import eu.europeana.uim.sugarcrmclient.jibxbindings.IsUserAdminResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.Login;
import eu.europeana.uim.sugarcrmclient.jibxbindings.LoginResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.Logout;
import eu.europeana.uim.sugarcrmclient.jibxbindings.LogoutResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.SetEntry;
import eu.europeana.uim.sugarcrmclient.jibxbindings.SetEntryResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.SetNoteAttachment;
import eu.europeana.uim.sugarcrmclient.jibxbindings.SetNoteAttachmentResponse;
import eu.europeana.uim.sugarcrmclient.ws.exceptions.FileAttachmentException;
import eu.europeana.uim.sugarcrmclient.ws.exceptions.GenericSugarCRMException;
import eu.europeana.uim.sugarcrmclient.ws.exceptions.LoginFailureException;
import eu.europeana.uim.sugarcrmclient.ws.exceptions.LogoutFailureException;
import eu.europeana.uim.sugarcrmclient.ws.exceptions.QueryResultException;

/**
 *  Interface describing the available SOAP based sugarCRM operations 
 *  
 * @author Georgios Markakis
 */
public interface SugarWsClient {


	/**
	 * Public method for performing Login operations (see Junit test for usage example) 
	 * 
	 * @param login the Login object
	 * @return a String containing the current Session id
	 * @throws LoginFailureException when login credentials are incorrect
	 * @throws GenericSugarCRMException 
	 */
	public String login(Login login) throws LoginFailureException;
	
	
	/**
	 * Public method for performing Login operations (see JUnit test for usage example) 
	 * 
	 * @param login
	 * @return a LoginResponse object
	 * @throws LoginFailureException when login credentials are incorrect
	 * @throws GenericSugarCRMException
	 */
	public LoginResponse login2(Login login) throws LoginFailureException;
	
	
	/**
	 * Public method for performing Logout operations (see Junit test for usage example) 
	 * 
	 * @param a logout request object
	 * @return a LogoutResponse object
	 * @throws LogoutFailureException when logout fails
	 * @throws GenericSugarCRMException
	 */
	public LogoutResponse logout(Logout request) throws LogoutFailureException;
	
	/**
	 * This method returns an object indicating that the current user has admin privileges or not.
	 * @param request
	 * @return
	 * @throws GenericSugarCRMException 
	 */
	public IsUserAdminResponse is_user_admin(IsUserAdmin request) throws GenericSugarCRMException;
	
	/**
	 * This method gives the user name of the user who "owns" the specific session 
	 * 
	 * @param request
	 * @return
	 * @throws GenericSugarCRMException 
	 */
	public GetUserIdResponse get_user_id(GetUserId request) throws GenericSugarCRMException;
	
	/**
	 * Shows all the available module names in SugarCRM 
	 * 
	 * @param request
	 * @return
	 * @throws QueryResultException 
	 * @throws GenericSugarCRMException 
	 */
	public GetAvailableModulesResponse get_available_modules(GetAvailableModules request) throws QueryResultException ;
	
	/**
	 * Get the fields for a specific module 
	 * 
	 * @param request 
	 * @return a GetModuleFieldsResponse containing a list of module fields
	 * @throws QueryResultException 
	 */
	public GetModuleFieldsResponse get_module_fields(GetModuleFields request) throws QueryResultException;
	
	/**
	 * Performs a query on the records contained in sugarCRM
	 *  
	 * @param request
	 * @return
	 * @throws QueryResultException 
	 */
	public GetEntryListResponse get_entry_list(GetEntryList request) throws QueryResultException;
	
	/**
	 * Gets a specific entry
	 * @param request
	 * @return
	 * @throws QueryResultException 
	 */
	public GetEntryResponse get_entry(GetEntry request) throws QueryResultException;
	
	/**
	 * Creates/Updates an entry in SugarCRM
	 * 
	 * @param request
	 * @return 
	 * @throws QueryResultException 
	 */
	public SetEntryResponse set_entry(SetEntry request) throws QueryResultException;
	
	/**
	 * Gets the entries for a request
	 * 
	 * @param request
	 * @return
	 * @throws QueryResultException 
	 */
	public GetEntriesResponse get_entries(GetEntries request) throws QueryResultException;
	
	
	/**
	 * Sets a note attachment to a record
	 * 
	 * @param request
	 * @return
	 * @throws FileAttachmentException 
	 */
	public SetNoteAttachmentResponse set_note_attachment(SetNoteAttachment request) throws FileAttachmentException;
	
	/**
	 * Gets a note attachment from a record
	 * 
	 * @param request
	 * @return
	 * @throws FileAttachmentException 
	 */
	public GetNoteAttachmentResponse get_note_attachment(GetNoteAttachment request) throws FileAttachmentException;
	
	/**
	 * Gets the Relationships for a specific module
	 * @param request
	 * @return
	 * @throws QueryResultException
	 */
	public GetRelationshipsResponse get_relationships(GetRelationships request) throws QueryResultException;
	
}
