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
package eu.europeana.uim.sugarcrmclient.plugin.objects.data;


/**
 *  This enumeration indicates the Fields that can be updated
 *  by the SugarCRM client. 
 *  
 * @author Georgios Markakis
 */
public enum UpdatableField implements SugarCrmField{
	AMOUNT("amount","Amount"),
	TOTAL_INGESTED("ingested_total_c","Ingested Total"),
	INGESTED_SOUND("ingested_sound_c","Ingested Sound"),
	INGESTED_VIDEO("ingested_video_c","Ingested Video"),
	INGESTED_TEXT("ingested_text_c","Ingested Text"),
	INGESTED_IMAGE("ingested_image_c","Ingested Images"),
	NEXT_STEP("next_step","Next Step"),
	STATUS("sales_stage","sales_stage"),
	TYPE("opportunity_type","Type"),
	;
	
	private final String fieldId;
	private final String description;	
	
	UpdatableField(String fieldId,String description ){
		this.fieldId = fieldId;
		this.description = description;
	}

	public String getFieldId() {
		return fieldId;
	}

	public String getDescription() {
		return description;
	}
}
