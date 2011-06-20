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
package eu.europeana.uim.sugarcrmclient.internal.helpers;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import eu.europeana.uim.sugarcrmclient.jibxbindings.Array;
import eu.europeana.uim.sugarcrmclient.jibxbindings.ArrayAttributes;
import eu.europeana.uim.sugarcrmclient.jibxbindings.ArrayAttributes.ArrayType;
import eu.europeana.uim.sugarcrmclient.jibxbindings.CommonAttributes;
import eu.europeana.uim.sugarcrmclient.jibxbindings.GetEntries;
import eu.europeana.uim.sugarcrmclient.jibxbindings.Login;
import eu.europeana.uim.sugarcrmclient.jibxbindings.SelectFields;
import eu.europeana.uim.sugarcrmclient.jibxbindings.UserAuth;
import eu.europeana.uim.sugarcrmclient.jibxbindings.NameValueList;
import eu.europeana.uim.sugarcrmclient.jibxbindings.NameValue;


import org.apache.log4j.Logger;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;

/**
 * This Class provides auxiliary methods that can be used for the creation & instantiation
 * of SugarWSClient instances (see provided Junit tests for usage examples). 
 * 
 * @author Georgios Markakis
 */
public class ClientUtils {

	private static org.apache.log4j.Logger LOGGER = Logger.getLogger(ClientUtils.class);

	
	
	/**
	 * This method marshals the contents of a  JAXB Element and outputs the results to the
	 * Logger.  
	 * @param jaxbObject A JIBX representation of a SugarCRM SOAP Element. 
	 */
	public static void logMarshalledObject(Object jibxObject){		

		try {

			String xmlContent = unmarshallObject(jibxObject);
			
			LOGGER.info("===========================================");
			StringBuffer sb = new StringBuffer("Soap Ouput for Class: ");
			sb.append(jibxObject.getClass().getSimpleName());
			LOGGER.info(sb.toString());
			LOGGER.info(xmlContent);
			LOGGER.info("===========================================");
		} catch (JiBXException e) {

			e.printStackTrace();
		}
		
	}
	
	
	
	/**
	 * This method marshals the contents of a  JAXB Element and outputs the results to the
	 * Karaf output console.
	 *   
	 * @param out the (Karaf console) printwriter
	 * @param jibxObject
	 */
	public static void logMarshalledObjectOsgi(PrintStream out, Object jibxObject){

		try {

			String xmlContent = unmarshallObject(jibxObject);
			
			out.println("===========================================");
			StringBuffer sb = new StringBuffer("Soap Ouput for Class: ");
			sb.append(jibxObject.getClass().getSimpleName());
			out.println(sb.toString());
			out.println(xmlContent);
			out.println("===========================================");
		} catch (JiBXException e) {

			e.printStackTrace();
		}
		
	}
	
	
	
	/**
	 * Private auxiliary method that performs the unmarshalling of the object
	 * 
	 * @param jibxObject
	 * @return
	 * @throws JiBXException
	 */
	private static String unmarshallObject(Object jibxObject) throws JiBXException{
		IBindingFactory context;
		context = BindingDirectory.getFactory(jibxObject.getClass());

		IMarshallingContext mctx = context.createMarshallingContext();
		mctx.setIndent(2);
		StringWriter stringWriter = new StringWriter();
		mctx.setOutput(stringWriter);
		mctx.marshalDocument(jibxObject);		
		
		String xmlContents = stringWriter.toString();
		
		return xmlContents;
	}
	
	
	/**
	 * Encrypts a given String into a MD5 format.
	 * @param value The string to be encrypted
	 * @return the encrypted String
	 */
	public static String md5(String value){
		
		StringBuffer md5Password = new StringBuffer("0");
		
		MessageDigest mdEnc;
		try {
			mdEnc = MessageDigest.getInstance("MD5");
			mdEnc.update(value.getBytes(), 0, value.length());
			md5Password.append(new BigInteger(1, mdEnc.digest()).toString(16)); 
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		
		return md5Password.toString();
	}
	
	
	
	/**
	 * Creates a Login Soap Request given the user credentials.
	 * @param username 
	 * @param passwrd
	 * @return  a Login Soap Request Jaxb representation
	 */
	public static Login createStandardLoginObject(String username, String passwrd){
		
		
		Login login = new Login();
		UserAuth user = new UserAuth();

		user.setUserName(username);
		user.setPassword(md5(passwrd));
		user.setVersion("1.0");
		
		login.setApplicationName("EuropeanaSugarCRMClient");
		login.setUserAuth(user);
		
		return login;
	}
	
	
	
	
	/**
	 * Creates a SelectFields Soap Object given a List<String> fieldnames object which sets the 
	 * fields to be retrieved.
	 * @param fieldnames
	 * @return
	 */
	public static SelectFields generatePopulatedSelectFields(List<String> fieldnames){
		
		SelectFields selfields = new SelectFields();
		StringBuffer arrayType = new StringBuffer();
		arrayType.append("string[");
		arrayType.append(fieldnames.size());
		arrayType.append("]");
		CommonAttributes commonAttributes = new CommonAttributes();
		commonAttributes.setHref(arrayType.toString());
		selfields.setCommonAttributes(commonAttributes);
		
		Element rootElement = null;
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
	        Document document = documentBuilder.newDocument();
	        
	        for( String fieldname : fieldnames){
	        	Element element = document.createElement("string");
	        	
	        	Array array =  new Array();
	        	array.getAnyList();
				selfields.setArray(array );
	        	
	    		//selfields.getAnies().add(rootElement);
	    		element.appendChild(document.createTextNode(fieldname));
	    		//selfields.getAnies().add(element);
	    		
	        }

            
	        
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
		
		
		return selfields;
		
	}
	
	/**
	 * Creates a NameValueList Soap Element given a List<String> namevalues object which sets the 
	 * fields to be retrieved.
	 * @param fieldnames
	 * @return
	 */
	public static NameValueList generatePopulatedNameValueList(List<NameValue> namevalues){

		NameValueList namevalueList = new NameValueList();
		StringBuffer arrayType = new StringBuffer();
		arrayType.append("name_value[");
		arrayType.append(namevalues.size());
		arrayType.append("]");		
		Array array = new Array();
		ArrayType arrTypeObj = new ArrayType();
		arrTypeObj.setArrayType(arrayType.toString());
		
		ArrayAttributes atts = new ArrayAttributes();
		atts.setArrayType(arrTypeObj);
		
		namevalueList.setArrayAttributes(atts);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
	        Document document = documentBuilder.newDocument();
	        
	        for( NameValue namevalue : namevalues){
	        	Element name_value = document.createElement("name_value");
	        	
	        	Element name = document.createElement("name");
	        	Element value = document.createElement("value");
	        	
	        	name.appendChild(document.createTextNode(namevalue.getName()));
	        	value.appendChild(document.createTextNode(namevalue.getValue()));
	        	name_value.appendChild(name);
	        	name_value.appendChild(value);
	        	
	    		array.getAnyList().add(name_value);
	        		    		
	        }
	        
	        namevalueList.setArray(array);
	        
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}

		return namevalueList;

	}
	
	
	
	
	
	/**
	 * @param responseString
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 */
	public static HashMap <String,HashMap<String,String>>  responseFactory(String responseString) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		
		HashMap <String,HashMap<String,String>> returnMap = new HashMap<String,HashMap<String,String>>();
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
	    DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();

	    Document document = documentBuilder.parse(new InputSource(new StringReader(responseString)));
		
	    String messageName = document.getFirstChild().getNodeName();
	
	    XPathFactory factory = XPathFactory.newInstance();
	    XPath xpath = factory.newXPath();
	    
	    xpath.setNamespaceContext(new SugarCRMNamespaceContext());
	    XPathExpression expr = xpath.compile("//ns1:get_entry_listResponse/return/entry_list/item");

	    Object result = expr.evaluate(document, XPathConstants.NODESET);
	    NodeList nodes = (NodeList) result;
	    
	    
	    for (int i = 0; i < nodes.getLength(); i++) {
	    	
	    	NodeList innerNodes = nodes.item(i).getChildNodes();
	    	    	
	    	String id =  innerNodes.item(0).getTextContent();
	    	
	    	HashMap <String,String> elementData = new HashMap <String,String>();
	    	
	    	NodeList infoNodes = innerNodes.item(2).getChildNodes();
	    	
	    	for (int z=0; z < infoNodes.getLength(); z++){
	    		String name = infoNodes.item(z).getFirstChild().getTextContent();
	    		String value = infoNodes.item(z).getLastChild().getTextContent();
	    		
	    		elementData.put(name,value);
	    		
	    	}
	    		    	
	    	returnMap.put(id, elementData);

	    }
	    
	    
		return returnMap;
	}
	
	
	
	
	/**
	 * @param responseString
	 * @return
	 */
	public static String extractSimpleResponse(String responseString){
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
	    DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		    Document document = documentBuilder.parse(new InputSource(new StringReader(responseString)));
			
		    String sessionID  = null;
		    String messageName = document.getFirstChild().getNodeName();
		    
		    
		    NodeList nl = document.getElementsByTagName("id");
		    
		    if (nl.getLength() > 0){
		    	sessionID = nl.item(0).getTextContent();
		    }
		    
		    return sessionID;
		    
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return null;
	}
	
	
	public static String extractFromElement(String value, Element el){
		
		NodeList nl =el.getElementsByTagName(value);
		
		if(nl.getLength() != 0){
			
			return nl.item(0).getTextContent();
		}
		
		
		return null;
	}
	
		
}
