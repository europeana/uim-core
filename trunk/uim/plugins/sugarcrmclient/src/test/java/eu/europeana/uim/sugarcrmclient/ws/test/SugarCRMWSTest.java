/**
 * 
 */
package eu.europeana.uim.sugarcrmclient.ws.test;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import java.io.StringWriter;
import java.math.*;
import java.security.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.annotation.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import eu.europeana.uim.sugarcrmclient.ws.SugarWsClient;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlType;
import com.sugarcrm.sugarcrm.GetServerVersion;

import com.sugarcrm.sugarcrm.GetAvailableModules;
import com.sugarcrm.sugarcrm.GetAvailableModulesResponse;
import com.sugarcrm.sugarcrm.GetEntryList;
import com.sugarcrm.sugarcrm.GetEntryListResponse;
import com.sugarcrm.sugarcrm.GetEntryListResult;
import com.sugarcrm.sugarcrm.GetUserId;
import com.sugarcrm.sugarcrm.GetUserIdResponse;
import com.sugarcrm.sugarcrm.Login;
import com.sugarcrm.sugarcrm.SelectFields;
import com.sugarcrm.sugarcrm.UserAuth;
import com.sugarcrm.sugarcrm.LoginResponse;
import com.sugarcrm.sugarcrm.IsUserAdmin;
import com.sugarcrm.sugarcrm.IsUserAdminResponse;

import com.sugarcrm.sugarcrm.ObjectFactory;

//import org.xmlsoap.schemas.soap.encoding.String;

/**
 * @author geomark
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:test-context.xml"})
public final class SugarCRMWSTest {

	@Resource
	private SugarWsClient sugarWsClient; 
	
	private String sessionID;

	
	
	@Before public void setupSession(){
		LoginResponse lresponse =  sugarWsClient.login2(createStandardLoginObject());
		sessionID = lresponse.getReturn().getId();
	}
	


	@Test
	public void testLogin(){
		
		LoginResponse response =  sugarWsClient.login2(createStandardLoginObject());
		
		System.out.println(response.getReturn().getId());
		
		marshallReturnedObject(response);
	}
	
	
	@Test
	public void testIsUserAdmin(){
		
		ObjectFactory factory = new ObjectFactory();
		
		IsUserAdmin user = factory.createIsUserAdmin();
		
		user.setSession(sugarWsClient.login(createStandardLoginObject()));
		
		IsUserAdminResponse response = sugarWsClient.is_user_admin(user);
		
		marshallReturnedObject(response);
	}
	
	
	@Test
	public void testGetUserID(){	 
		ObjectFactory factory = new ObjectFactory();		
		GetUserId request = factory.createGetUserId();
		request.setSession(sessionID);
		marshallReturnedObject(request);
		GetUserIdResponse response =  sugarWsClient.get_user_id(request);
		marshallReturnedObject(response);
	}
	
	//@Test
	public void simpleTest(){
		
		String result = sugarWsClient.test();
		System.out.println(result);
	}
	
	
	@Test
	public void testGetAvailableModules(){	 
		ObjectFactory factory = new ObjectFactory();		
		GetAvailableModules request = factory.createGetAvailableModules();
		request.setSession(sessionID);
		marshallReturnedObject(request);
		GetAvailableModulesResponse response =  sugarWsClient.get_available_modules(request);
		
		marshallReturnedObject(response);
	}
	
	//@Test
	public void testGetEntryList(){	 
		ObjectFactory factory = new ObjectFactory();		
		GetEntryList request = factory.createGetEntryList();
		SelectFields fields = factory.createSelectFields();
		request.setModuleName("Accounts");
		request.setOrderBy("accounts.city");
		request.setSelectFields(fields);
		request.setMaxResults(50);
		request.setSession(sessionID);
		marshallReturnedObject(request);
		GetEntryListResponse response =  sugarWsClient.get_entry_list(request);
		marshallReturnedObject(response);
	}
	
	
	
	private Login createStandardLoginObject(){
		
		ObjectFactory factory = new ObjectFactory();
		Login login = factory.createLogin();
		UserAuth user = factory.createUserAuth();

		user.setUserName("test");
		user.setPassword(md5("test"));
		user.setVersion("1.0");
		
		login.setApplicationName("sugar");
		login.setUserAuth(user);
		
		return login;
		
	}
	
	
	private void marshallReturnedObject(Object returnObject){
		
		JAXBContext context;
		try {
			context = JAXBContext.newInstance("com.sugarcrm.sugarcrm");
			Marshaller m = context.createMarshaller();
			
			StringWriter writer = new StringWriter();
			m.marshal(returnObject, writer);
			
			System.out.println(writer.toString());
			
		} catch (JAXBException e) {
	
			e.printStackTrace();
		}
		
	}
	
	
	private String md5(String value){
		
		StringBuffer md5Password = new StringBuffer("0");
		
		MessageDigest mdEnc;
		try {
			mdEnc = MessageDigest.getInstance("MD5");
			mdEnc.update(value.getBytes(), 0, value.length());
			md5Password.append(new BigInteger(1, mdEnc.digest()).toString(16)); // Encrypted string
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		
		return md5Password.toString();
	}
	
	
		
	}

