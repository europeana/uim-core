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
package eu.europeana.uim.gui.cp.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * @author Georgios Markakis
 */
public abstract class SugarCRMRecordDTO implements IsSerializable,Comparable<SugarCRMRecordDTO> {

	private String sugarCrmId;

	public void setSugarCrmId(String sugarCrmId) {
		this.sugarCrmId = sugarCrmId;
	}

	public String getSugarCrmId() {
		return sugarCrmId;
	}
	
	
}
