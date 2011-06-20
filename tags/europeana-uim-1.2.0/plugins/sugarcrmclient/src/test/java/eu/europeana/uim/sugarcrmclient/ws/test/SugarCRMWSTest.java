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
package eu.europeana.uim.sugarcrmclient.ws.test;

import static org.junit.Assert.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.europeana.uim.sugarcrmclient.ws.SugarWsClientImpl;
import eu.europeana.uim.sugarcrmclient.ws.exceptions.FileAttachmentException;
import eu.europeana.uim.sugarcrmclient.ws.exceptions.GenericSugarCRMException;
import eu.europeana.uim.sugarcrmclient.ws.exceptions.LoginFailureException;
import eu.europeana.uim.sugarcrmclient.ws.exceptions.LogoutFailureException;
import eu.europeana.uim.sugarcrmclient.ws.exceptions.QueryResultException;

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
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetRelationships;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetRelationshipsResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetUserId;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetUserIdResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.Login;
import eu.europeana.uim.sugarcrmclient.jibxbindings.Logout;
import eu.europeana.uim.sugarcrmclient.jibxbindings.LogoutResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.NameValue;
import eu.europeana.uim.sugarcrmclient.jibxbindings.NameValueList;
import eu.europeana.uim.sugarcrmclient.jibxbindings.NoteAttachment;
import eu.europeana.uim.sugarcrmclient.jibxbindings.SelectFields;
import eu.europeana.uim.sugarcrmclient.jibxbindings.SetEntry;
import eu.europeana.uim.sugarcrmclient.jibxbindings.SetEntryResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.SetNoteAttachment;
import eu.europeana.uim.sugarcrmclient.jibxbindings.SetNoteAttachmentResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.UserAuth;
import eu.europeana.uim.sugarcrmclient.jibxbindings.LoginResponse;
import eu.europeana.uim.sugarcrmclient.jibxbindings.IsUserAdmin;
import eu.europeana.uim.sugarcrmclient.jibxbindings.IsUserAdminResponse;


import eu.europeana.uim.sugarcrmclient.internal.helpers.ClientUtils;
import org.apache.log4j.Logger;


/**
 * Class implementing Unit Tests for SugarWsClient
 * 
 * @author Georgios Markakis
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-context.xml"})
public final class SugarCRMWSTest {

	@Resource
	private SugarWsClientImpl sugarWsClientImpl; 
	
	private String sessionID;

	
	private static org.apache.log4j.Logger LOGGER = Logger.getLogger(SugarCRMWSTest.class);
	
	
	/**
	 *  Method invoked before each test execution. It performs the initial login in order for allow permission to the 
	 *  subsequent web service calls. It also sets the session id for this test run. 
	 * @throws LoginFailureException 
	 */
	@Before
	public void setupSession() throws LoginFailureException{
		LoginResponse lresponse;
		lresponse = sugarWsClientImpl.login2(ClientUtils.createStandardLoginObject("test", "test"));
		assertNotNull(lresponse);
		sessionID = lresponse.getReturn().getId();
	}
	
	
	/**
	 * Method invoked after each test has been executed. It destroys the current session. 
	 * @throws LogoutFailureException 
	 */
	@After
	public void destroySession() throws LogoutFailureException{
		
		Logout request = new Logout();
		request.setSession(sessionID);
		LogoutResponse lgresponse;
		lgresponse = sugarWsClientImpl.logout(request );
		assertNotNull(lgresponse);

		
	}


	/**
	 * User Login Test (make sure that the user has been created in advance in in the configured SCRM installation)
	 */
	@Test
	public void testLogin() throws Exception{
		LoginResponse response;
		Login login = ClientUtils.createStandardLoginObject("test", "test");			
		ClientUtils.logMarshalledObject(login);
		response = sugarWsClientImpl.login2(login);		
		assertNotNull(response);
		LOGGER.info(response.getReturn().getId());
		ClientUtils.logMarshalledObject(response);
	}
	
	
	/**
	 * Is User Admin Test: Checks if the user has admin rights.
	 * @throws GenericSugarCRMException 
	 */
	@Test
	public void testIsUserAdmin() throws GenericSugarCRMException{	
		IsUserAdmin user = new IsUserAdmin();		
		user.setSession(sessionID);
		IsUserAdminResponse response;
		response = sugarWsClientImpl.is_user_admin(user);
		assertNotNull(response);
		ClientUtils.logMarshalledObject(response);
	}
	
	
	/**
	 * Get User ID Test: Retrieves a user id for a session.
	 * @throws GenericSugarCRMException 
	 */
	@Test
	public void testGetUserID() throws GenericSugarCRMException{	 
		GetUserId request = new GetUserId();
		request.setSession(sessionID);
		ClientUtils.logMarshalledObject(request);
		GetUserIdResponse response;
		response = sugarWsClientImpl.get_user_id(request);
		assertNotNull(response);
		ClientUtils.logMarshalledObject(response);
	}
	
	
	/**
	 * Get Available Modules: Gets the available modules for a specific SugarCRM installation.
	 * @throws QueryResultException 
	 */
	@Test
	public void testGetAvailableModules() throws QueryResultException{	 	
		GetAvailableModules request = new GetAvailableModules();
		request.setSession(sessionID);
		ClientUtils.logMarshalledObject(request);
		GetAvailableModulesResponse response;
		response = sugarWsClientImpl.get_available_modules(request);
		assertNotNull(response);
		ClientUtils.logMarshalledObject(response);
	}

	
	
	/**
	 * Get Module Fields Test: Get the fields for a specific modules.
	 * @throws Exception 
	 */
	@Test
	public void testGetModuleFields() throws Exception{	 		
		GetModuleFields request = new GetModuleFields();
		request.setSession(sessionID);
		request.setModuleName("Contacts");
		ClientUtils.logMarshalledObject(request);
		GetModuleFieldsResponse response;
		response = sugarWsClientImpl.get_module_fields(request);
		assertNotNull(response);		
		ClientUtils.logMarshalledObject(response);
	}
	
	
	
	
	/**
	 * Get Entry List Test: Retrieves all fields from a specific module that match a 
	 * specific query String. 
	 */
	@Test
	public void testGetEntryList(){		
		GetEntryList request = new GetEntryList();
		
		ArrayList <String> fieldnames = new  ArrayList<String>();
		
		fieldnames.add("id");
		fieldnames.add("first_name");
		fieldnames.add("last_name");
		fieldnames.add("salutation");
		
		SelectFields fields = ClientUtils.generatePopulatedSelectFields(fieldnames);
		
  		request.setModuleName("Contacts");	
		request.setSelectFields(fields);
		request.setSession(sessionID);
		request.setOrderBy("last_name");
		request.setMaxResults(10);
		request.setOffset(0);
		//request.setQuery("(contacts.salutation = 'Mr.' AND contacts.title LIKE 'doctor appointment%')");
		request.setQuery("(contacts.first_name LIKE '%M%')");
		
		ClientUtils.logMarshalledObject(request);
		GetEntryListResponse response;
		try {
			response = sugarWsClientImpl.get_entry_list(request);
			assertNotNull(response);
			ClientUtils.logMarshalledObject(response);
		} catch (QueryResultException e) {
			e.printStackTrace();
		}

		
	}
	
	
	/**
	 * Get Entry Test: Retrieves all fields from a specific module. 
	 * @throws QueryResultException 
	 */
	@Test
	public void testGetEntries() throws QueryResultException{	 		
		GetEntries request = new GetEntries();
		ArrayList <String> fieldnames = new  ArrayList<String>();
		fieldnames.add("id");
		fieldnames.add("first_name");
		fieldnames.add("last_name");
		fieldnames.add("salutation");
		SelectFields fields = ClientUtils.generatePopulatedSelectFields(fieldnames);
  		request.setModuleName("Contacts");	
		request.setSelectFields(fields);
		request.setSession(sessionID);
		request.setIds(fields);
		ClientUtils.logMarshalledObject(request);
		GetEntriesResponse response;
		response = sugarWsClientImpl.get_entries(request);
		ClientUtils.logMarshalledObject(response);
	}
	
	
	
	
	
	
	/**
	 * Get Entry Test: Get a test entry for a specific ID.
	 * @throws QueryResultException 
	 */
	@Test
	public void testGetEntry() throws QueryResultException{
		GetEntry request = new GetEntry();
		request.setId("99d2439f-05f3-af5b-0d4c-4d53f081ba9b");
		request.setModuleName("Accounts");
		request.setSession(sessionID);	
		SelectFields selectFields = new SelectFields();
		request.setSelectFields(selectFields );
		ClientUtils.logMarshalledObject(request);
		GetEntryResponse response;
		response = sugarWsClientImpl.get_entry(request);
		ClientUtils.logMarshalledObject(response);
	}
	
	
	
	/**
	 * Set Entry Test: Create an entry with the specified ID and the declared 
	 * name-value pairs and update it if it already exists. 
	 * @throws QueryResultException 
	 */
	@Test
	public void testSetEntry() throws QueryResultException{
		SetEntry request = new SetEntry();
		
		NameValue nv1 = new NameValue();
		nv1.setName("id");
		nv1.setValue("ac3f140f-ef12-ffab-6c97-4df512d4a9d8");
		
		NameValue nv0 = new NameValue();
		nv0.setName("first_name");
		nv0.setValue("JohnZXs");

		NameValue nv2 = new NameValue();
		nv2.setName("last_name");
		nv2.setValue("SmithZ");		

		ArrayList <NameValue> nvList = new  ArrayList <NameValue>();
		
		nvList.add(nv0);
		nvList.add(nv2);
		
		NameValueList valueList = ClientUtils.generatePopulatedNameValueList(nvList);
		
		request.setNameValueList(valueList);
		request.setModuleName("Contacts");
		request.setSession(sessionID);	
		ClientUtils.logMarshalledObject(request);
		SetEntryResponse response;
		response = sugarWsClientImpl.set_entry(request);
		ClientUtils.logMarshalledObject(response);
	}
	
	/**
	 * Set Attachment Test: Create an attachment for a specific entry 
	 * @throws FileAttachmentException 
	 * 
	 */
	@Test
	public void testSetAttachment() throws FileAttachmentException{
		
		SetNoteAttachment request = new SetNoteAttachment();
	    NoteAttachment note = new NoteAttachment();
	    
	    note.setId("ac3f140f-ef12-ffab-6c97-4df512d4a9d8");
	    note.setFile("asdasdsadsadsadsad");
	    note.setFilename("test.txt");
	    
		request.setNote(note);
		request.setSession(sugarWsClientImpl.getSessionID());
		ClientUtils.logMarshalledObject(request);
		SetNoteAttachmentResponse resp;
		resp = sugarWsClientImpl.set_note_attachment(request );
		ClientUtils.logMarshalledObject(resp);

	}
	
	
	
	
	/**
	 * Get Relationships test: Gets the Related Organizations modules for a specific Dataset Record
	 * @throws Exception
	 */
	@Test
	public void testGetRelationships() throws Exception{
		
		GetRelationships request =  new GetRelationships();
		
		request.setDeleted(0);
		request.setModuleId("49357412-f6b0-9a19-78f0-4cffa2f08cd8");
		request.setModuleName("Opportunities");
		request.setRelatedModule("Accounts");
		request.setRelatedModuleQuery("");
		request.setSession(sessionID);
		
		ClientUtils.logMarshalledObject(request);
		
		GetRelationshipsResponse resp = sugarWsClientImpl.get_relationships(request);
		
		ClientUtils.logMarshalledObject(resp);
		
	}
	
		
	}

