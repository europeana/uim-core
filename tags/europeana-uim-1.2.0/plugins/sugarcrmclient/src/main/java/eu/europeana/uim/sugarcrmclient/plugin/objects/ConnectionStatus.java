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

package eu.europeana.uim.sugarcrmclient.plugin.objects;

/**
 * Hold connection status information  
 * 
 * @author Georgios Markakis
 */
public class ConnectionStatus {

	private String defaultURI;
	private String sessionID;
	
	
		
	public void setDefaultURI(String defaultURI) {
		this.defaultURI = defaultURI;
	}

	public String getDefaultURI() {
		return defaultURI;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getSessionID() {
		return sessionID;
	}
	
	@Override
	public String toString(){
		
		StringBuffer connectionInfo = new StringBuffer();
		connectionInfo.append("Pointing at:");
		connectionInfo.append(defaultURI);
		connectionInfo.append("\n");
		connectionInfo.append("Session Id:");
		connectionInfo.append(sessionID);
		
		return connectionInfo.toString();		
	}
}
