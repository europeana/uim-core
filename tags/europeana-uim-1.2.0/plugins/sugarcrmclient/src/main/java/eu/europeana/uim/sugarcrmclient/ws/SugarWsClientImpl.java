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

import org.springframework.ws.client.core.WebServiceTemplate;

import eu.europeana.uim.sugarcrmclient.internal.helpers.ClientUtils;
import eu.europeana.uim.sugarcrmclient.jibxbindings.IsUserAdmin;
import eu.europeana.uim.sugarcrmclient.jibxbindings.IsUserAdminResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetEntryList;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetEntryListResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetEntry;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetEntryResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetEntries;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetEntriesResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.SetEntry;
import eu.europeana.uim.sugarcrmclient.jibxbindings.SetEntryResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.Logout;
import eu.europeana.uim.sugarcrmclient.jibxbindings.LogoutResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetModuleFields;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetModuleFieldsResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetAvailableModules;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetAvailableModulesResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetUserId;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetUserIdResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.Login;
import eu.europeana.uim.sugarcrmclient.jibxbindings.LoginResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.ContactByEmail;
import eu.europeana.uim.sugarcrmclient.jibxbindings.ContactByEmailResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.SetNoteAttachment;
import eu.europeana.uim.sugarcrmclient.jibxbindings.SetNoteAttachmentResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetNoteAttachment;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetNoteAttachmentResponse;
import eu.europeana.uim.sugarcrmclient.ws.exceptions.GenericSugarCRMException;
import eu.europeana.uim.sugarcrmclient.ws.exceptions.QueryResultException;
import eu.europeana.uim.sugarcrmclient.ws.exceptions.*;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetRelationships;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetRelationshipsResponse;

/**
 * The core class for performing SOAP based sugarCRM operations  
 * 
 * @author Georgios Markakis
 */
public class SugarWsClientImpl implements SugarWsClient{

	
	private WebServiceTemplate webServiceTemplate;

	private String sessionID;
		
	
	/**
	 * Generic auxiliary method for marshalling and unmarshalling requests
	 * and responses via Spring-WS
	 * 
	 * @param <T> Class of the request Object
	 * @param <S> Class of the response object
	 * @param wsOperation the instance of the request operation
	 * @return the unmarshalled response object 
	 */
	private <T,S> S invokeWSTemplate( T wsOperation, Class<S> responseClass){

		@SuppressWarnings("unchecked")
		S wsResponse = (S)webServiceTemplate.marshalSendAndReceive(wsOperation);
		
		return wsResponse;
	}
	
	

	
	
	
	/**
	 * Public method for performing Login operations (see Junit test for usage example) 
	 * 
	 * @param login the Login object
	 * @return a String containing the current Session id
	 * @throws LoginFailureException when login credentials are incorrect
	 * @throws GenericSugarCRMException 
	 */
	public String login(Login login) throws LoginFailureException{
		

		LoginResponse response =  invokeWSTemplate(login,LoginResponse.class);
		String sessionID = response.getReturn().getId();
		if("-1".equals(sessionID)){			
			throw new LoginFailureException(response.getReturn().getError());
		}
		  return sessionID;
	}
	


	/**
	 * Public method for performing Login operations (see JUnit test for usage example) 
	 * 
	 * @param login
	 * @return a LoginResponse object
	 * @throws LoginFailureException when login credentials are incorrect
	 * @throws GenericSugarCRMException
	 */
	public LoginResponse login2(Login login) throws LoginFailureException{
		
		LoginResponse response =  invokeWSTemplate(login,LoginResponse.class);
		
		if("-1".equals(response.getReturn().getId())){			
			throw new LoginFailureException(response.getReturn().getError());
		}
		return response;

	}
	
	
	
	
	/**
	 * Public method for performing Logout operations (see Junit test for usage example) 
	 * 
	 * @param a logout request object
	 * @return a LogoutResponse object
	 * @throws LogoutFailureException when logout fails
	 * @throws GenericSugarCRMException
	 */
	public LogoutResponse logout(Logout request) throws LogoutFailureException{

		LogoutResponse response =  invokeWSTemplate(request,LogoutResponse.class);
		
		String returnvalue = response.getReturn().getNumber();
		
		if (!"0".equals(returnvalue)){
			
			throw new LogoutFailureException(response.getReturn().getDescription());
		}
		return response;
	}
	
	
	/**
	 * This method returns an object indicating that the current user has admin privileges or not.
	 * @param request
	 * @return
	 * @throws GenericSugarCRMException 
	 */
	public IsUserAdminResponse is_user_admin(IsUserAdmin request) throws GenericSugarCRMException{

		try{
		IsUserAdminResponse response = invokeWSTemplate(request,IsUserAdminResponse.class);
	
		return response;
		}
		catch (Exception e){
			throw new GenericSugarCRMException();
		}
	}
	
	
	/**
	 * This method gives the user name of the user who "owns" the specific session 
	 * 
	 * @param request
	 * @return
	 * @throws GenericSugarCRMException 
	 */
	public GetUserIdResponse get_user_id(GetUserId request) throws GenericSugarCRMException{
		
		try{
		GetUserIdResponse response = invokeWSTemplate(request,GetUserIdResponse.class);
		
		return response;
		}
		catch (Exception e){
			throw new GenericSugarCRMException();
		}
	}
	
	/**
	 * Shows all the available module names in SugarCRM 
	 * 
	 * @param request
	 * @return
	 * @throws QueryResultException 
	 * @throws GenericSugarCRMException 
	 */
	public GetAvailableModulesResponse get_available_modules(GetAvailableModules request) throws QueryResultException  {

		GetAvailableModulesResponse response = invokeWSTemplate(request,GetAvailableModulesResponse.class);
		
		if(!"0".equals(response.getReturn().getError().getNumber())){			
			throw new QueryResultException(response.getReturn().getError());
		}
		return response;
	}


	
	/**
	 * Get the fields for a specific module 
	 * 
	 * @param request 
	 * @return a GetModuleFieldsResponse containing a list of module fields
	 * @throws QueryResultException 
	 */
	public GetModuleFieldsResponse get_module_fields(GetModuleFields request) throws QueryResultException{

		GetModuleFieldsResponse response = invokeWSTemplate(request,GetModuleFieldsResponse.class);
		/*
		if(!"0".equals(response.getReturn().getError().getNumber())){			
			throw new QueryResultException(response.getReturn().getError());
		}
		*/
		return response;
	}

	
	/**
	 * Performs a query on the records contained in sugarCRM
	 *  
	 * @param request
	 * @return
	 * @throws QueryResultException 
	 */
	public GetEntryListResponse get_entry_list(GetEntryList request) throws QueryResultException{
		
		GetEntryListResponse response = invokeWSTemplate(request,GetEntryListResponse.class);
		
		if(!"0".equals(response.getReturn().getError().getNumber())){			
			throw new QueryResultException(response.getReturn().getError());
		}
		
		return response;
	}
	
	
	/**
	 * Gets a specific entry
	 * @param request
	 * @return
	 * @throws QueryResultException 
	 */
	public GetEntryResponse get_entry(GetEntry request) throws QueryResultException{
		
		GetEntryResponse response = invokeWSTemplate(request,GetEntryResponse.class);
		
		if(!"0".equals(response.getReturn().getError().getNumber())){			
			throw new QueryResultException(response.getReturn().getError());
		}
		
		return response;
	}
	
	
	/**
	 * Creates/Updates an entry in SugarCRM
	 * 
	 * @param request
	 * @return 
	 * @throws QueryResultException 
	 */
	public SetEntryResponse set_entry(SetEntry request) throws QueryResultException{
		
		SetEntryResponse response = invokeWSTemplate(request,SetEntryResponse.class);
		
		if(!"0".equals(response.getReturn().getError().getNumber())){			
			throw new QueryResultException(response.getReturn().getError());
		}
		
		return response;
	}
	
	
	
	/**
	 * Gets the entries for a request
	 * 
	 * @param request
	 * @return
	 * @throws QueryResultException 
	 */
	public GetEntriesResponse get_entries(GetEntries request) throws QueryResultException{
		
		GetEntriesResponse response = invokeWSTemplate(request,GetEntriesResponse.class);
		
		if(!"0".equals(response.getReturn().getError().getNumber())){			
			throw new QueryResultException(response.getReturn().getError());
		}
		
		return response;
	}
	
	
	/**
	 * Contacts a user directly via e-mail
	 * 
	 * @param request
	 * @return
	 */
	public ContactByEmailResponse contact_by_email(ContactByEmail request) throws FileAttachmentException{
		
		ContactByEmailResponse response = invokeWSTemplate(request,ContactByEmailResponse.class);
		
		return response;
	}
	
	
	
	/**
	 * Sets a note attachment to a record
	 * 
	 * @param request
	 * @return
	 */
	public SetNoteAttachmentResponse set_note_attachment(SetNoteAttachment request) throws FileAttachmentException{
		
		SetNoteAttachmentResponse response = invokeWSTemplate(request,SetNoteAttachmentResponse.class);
	
		if(!"0".equals(response.getReturn().getError().getNumber())){			
			throw new FileAttachmentException(response.getReturn().getError());
		}
		
		return response;
	}
	
	

	/**
	 * Gets a note attachment from a record
	 * 
	 * @param request
	 * @return
	 */
	public GetNoteAttachmentResponse get_note_attachment(GetNoteAttachment request) throws FileAttachmentException{
		
		GetNoteAttachmentResponse response = invokeWSTemplate(request,GetNoteAttachmentResponse.class);
		
		if(!"0".equals(response.getReturn().getError().getNumber())){			
			throw new FileAttachmentException(response.getReturn().getError());
		}
		
		return response;
	}
	
	
	
	
	
	/**
	 * Gets the Relationships for a specific module
	 * @param request
	 * @return
	 * @throws QueryResultException
	 */
	public GetRelationshipsResponse get_relationships(GetRelationships request) throws QueryResultException{

		GetRelationshipsResponse response = invokeWSTemplate(request,GetRelationshipsResponse.class);
		
		if(!"0".equals(response.getReturn().getError().getNumber())){			
			throw new QueryResultException(response.getReturn().getError());
		}
		
		return response;
		
	}
	
	
	
	
	/* Getters & Setters */
	
	/**
	 * @return
	 */
	public WebServiceTemplate getWebServiceTemplate(){
		return this.webServiceTemplate;
	}
	
	
	
	/**
	 * @param webServiceTemplate
	 */
	public void setWebServiceTemplate(WebServiceTemplate webServiceTemplate){
		this.webServiceTemplate = webServiceTemplate;
	}
	
	
	
	/**
	 * @param defaultUri
	 */
	public void setDefaultUri(String defaultUri) {
		
		webServiceTemplate.setDefaultUri(defaultUri);
	}

	
	/**
	 * @return
	 */
	public String getDefaultUri() {
		return webServiceTemplate.getDefaultUri();
	}
	
	
	/**
	 * @param sessionID
	 */
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	
	/**
	 * @return
	 */
	public String getSessionID() {
		return sessionID;
	}

}
