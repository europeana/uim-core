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
package eu.europeana.uim.gui.cp.server;

import java.util.List;

import eu.europeana.uim.gui.cp.client.services.IntegrationSeviceProxy;
import eu.europeana.uim.gui.cp.shared.ImportResultDTO;
import eu.europeana.uim.gui.cp.shared.SugarCRMRecordDTO;

/**
 * @author georgiosmarkakis
 *
 */
public class IntegrationSeviceProxyImpl extends IntegrationServicesProviderServlet implements IntegrationSeviceProxy {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see eu.europeana.uim.gui.cp.client.services.IntegrationSeviceProxy#processSelectedRecord(eu.europeana.uim.gui.cp.shared.SugarCRMRecordDTO)
	 */
	@Override
	public ImportResultDTO processSelectedRecord(SugarCRMRecordDTO record) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.gui.cp.client.services.IntegrationSeviceProxy#executeSugarCRMQuery(java.lang.String)
	 */
	@Override
	public List<SugarCRMRecordDTO> executeSugarCRMQuery(String query) {
		// TODO Auto-generated method stub
		return null;
	}

}
