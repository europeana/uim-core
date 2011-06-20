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
 *  This enumeration indicates the Fields that should be just retrieved
 *  by the SugarCRM client. 
 * @author Georgios Markakis
 */
public enum RetrievableField implements SugarCrmField{
	ID("id","ID"),
	NAME("name","Name"),
	DATE_ENTERED("date_entered","Date Entered"),
	DATE_MODIFIED("date_modified","Date Modified"),
	MODIFIED_BY_USER("modified_user_id","Modified by ID"),
	CREATED_BY_USER("created_by","sales_stage"),
	DESCRIPTION("description","Description"),
	DELETED("deleted","Deleted"),
	ASSIGNED_USER_ID("assigned_user_id","Assigned User Id"),
	ASSIGNED_USER_NAME("assigned_user_name","Assigned User Name"),
	ORGANIZATION_NAME("account_name","Organisation Name"),
	CAMPAIGN_NAME("campaign_name","Campaign Name"),
	EXPECTED_INGESTION_DATE("date_closed","Expected ingestion date"),
	NOTES("notes_c","Expected ingestion date"),
	HARVEST_URL("harvest_url_c","Harvest URL"),
	PLANNED_TOTAL("planned_total_c","Ingested Total"),
	PLANNED_SOUND("planned_sound_c","Ingested Sound"),
	PLANNED_VIDEO("planned_video_c","Ingested Video"),
	PLANNED_TEXT("planned_text_c","Ingested Text"),
	PLANNED_IMAGE("planned_image_c","Ingested Images"),
	SETSPEC("setspec_c","Setspec"),
	COUNTRY("country_c","Country"),
	ACRONYM("name_acronym_c","Acronym"),
	IDENTIFIER("name_id_c","Identifier"),
	ENABLED("enabled_c","Enabled"),
	DATASET_COUNTRY("language_c","Dataset Country"),
	ACCESS_TO_CONTENT_CHECKER("access_to_content_checker_c","Access To Content Checker"),
	DATE_OF_REPLICATION("actual_ingestion_date_c","Date of Replication"),
	;
	
	private final String fieldId;
	private final String description;	
	
	RetrievableField(String fieldId,String description ){
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
