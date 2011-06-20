package eu.europeana.uim.sugarcrmclient.ws;

import org.springframework.ws.client.core.WebServiceTemplate;

import eu.europeana.uim.sugarcrmclient.internal.helpers.ClientUtils;
import eu.europeana.uim.sugarcrmclient.ws.exceptions.LoginFailureException;

/**
 * 
 * @author Georgios Markakis
 */
public class ClientFactory {

	
	private WebServiceTemplate webServiceTemplate;
	

	/**
	 * Internal factory method used by Spring 
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public  SugarWsClientImpl createInstance(String userName, String password){
		
		SugarWsClientImpl client = new SugarWsClientImpl();
		client.setWebServiceTemplate(webServiceTemplate);
		
		try {
			client.setSessionID(client.login(ClientUtils.createStandardLoginObject(userName,password)));
		} catch (LoginFailureException e) {
			client.setSessionID("-1");
			e.printStackTrace();
		}
		
		return client;
	}
	
	
	
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
	
}
