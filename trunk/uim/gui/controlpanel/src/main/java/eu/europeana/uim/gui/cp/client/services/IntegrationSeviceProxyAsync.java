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
package eu.europeana.uim.gui.cp.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;


import eu.europeana.uim.gui.cp.client.utils.RepoxOperationType;
import eu.europeana.uim.gui.cp.shared.ImportResultDTO;
import eu.europeana.uim.gui.cp.shared.IntegrationStatusDTO;
import eu.europeana.uim.gui.cp.shared.RepoxExecutionStatusDTO;
import eu.europeana.uim.gui.cp.shared.SugarCRMRecordDTO;

/**
 * Remote Integration Service Interface (Async)
 * 
 * @author Georgios Markakis
 */
public interface IntegrationSeviceProxyAsync {

	public void processSelectedRecord(SugarCRMRecordDTO record, AsyncCallback<ImportResultDTO> async);
	
	public void executeSugarCRMQuery(String query, AsyncCallback<List<SugarCRMRecordDTO>> async);
	
	public void retrieveIntegrationInfo(String provider, String collection, AsyncCallback<IntegrationStatusDTO> async);

	public void performRepoxRemoteOperation(RepoxOperationType operationType, String repoxResourceID, AsyncCallback<RepoxExecutionStatusDTO> async);

}
