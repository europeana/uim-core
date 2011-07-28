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
 * SugarCRMRecordDTO represents a client side (translatable to javascript)
 * object in GWT. It represents and contains information about a SugarCRM record.
 * 
 * @author Georgios Markakis
 */
public class SugarCRMRecordDTO implements IsSerializable,Comparable<SugarCRMRecordDTO> {


	private String id;
	private String importedIMG;
	private String name;
	private String date_entered;
	private String date_modified;
	private String modified_by_user;
	private String created_by;
	private String description;
	private String deleted;
	private String assigned_user_id;
	private String assigned_user_name;
	private String organization_name;
	private String campaign_name;
	private String expected_ingestion_date;
	private String notes;
	private String harvest_url;
	private String planned_total_c;
	private String planned_sound_c;
	private String planned_video_c;
	private String planned_text_c;
	private String planned_image_c;
	private String setspec_c;
	private String country_c;
	private String name_acronym_c;
	private String identifier;
	private String enabled_c;
	private String dataset_country;
	private String access_to_content_checker;
	private String actual_ingestion_date_;
	private String ingested_total_c;
	private String ingested_sound_c;
	private String ingested_video_c;
	private String ingested_text_c;
	private String ingested_image_c;
	private String next_step;
	private String status;
	private String type;
	

	
	
	@Override
	public int compareTo(SugarCRMRecordDTO arg0) {

		return 0;
	}




	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}




	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}




	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}




	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}




	/**
	 * @param date_entered the date_entered to set
	 */
	public void setDate_entered(String date_entered) {
		this.date_entered = date_entered;
	}




	/**
	 * @return the date_entered
	 */
	public String getDate_entered() {
		return date_entered;
	}




	/**
	 * @param date_modified the date_modified to set
	 */
	public void setDate_modified(String date_modified) {
		this.date_modified = date_modified;
	}




	/**
	 * @return the date_modified
	 */
	public String getDate_modified() {
		return date_modified;
	}




	/**
	 * @param modified_by_user the modified_by_user to set
	 */
	public void setModified_by_user(String modified_by_user) {
		this.modified_by_user = modified_by_user;
	}




	/**
	 * @return the modified_by_user
	 */
	public String getModified_by_user() {
		return modified_by_user;
	}




	/**
	 * @param created_by the created_by to set
	 */
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}




	/**
	 * @return the created_by
	 */
	public String getCreated_by() {
		return created_by;
	}




	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}




	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}




	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}




	/**
	 * @return the deleted
	 */
	public String getDeleted() {
		return deleted;
	}




	/**
	 * @param assigned_user_id the assigned_user_id to set
	 */
	public void setAssigned_user_id(String assigned_user_id) {
		this.assigned_user_id = assigned_user_id;
	}




	/**
	 * @return the assigned_user_id
	 */
	public String getAssigned_user_id() {
		return assigned_user_id;
	}




	/**
	 * @param assigned_user_name the assigned_user_name to set
	 */
	public void setAssigned_user_name(String assigned_user_name) {
		this.assigned_user_name = assigned_user_name;
	}




	/**
	 * @return the assigned_user_name
	 */
	public String getAssigned_user_name() {
		return assigned_user_name;
	}




	/**
	 * @param organization_name the organization_name to set
	 */
	public void setOrganization_name(String organization_name) {
		this.organization_name = organization_name;
	}




	/**
	 * @return the organization_name
	 */
	public String getOrganization_name() {
		return organization_name;
	}




	/**
	 * @param campaign_name the campaign_name to set
	 */
	public void setCampaign_name(String campaign_name) {
		this.campaign_name = campaign_name;
	}




	/**
	 * @return the campaign_name
	 */
	public String getCampaign_name() {
		return campaign_name;
	}




	/**
	 * @param expected_ingestion_date the expected_ingestion_date to set
	 */
	public void setExpected_ingestion_date(String expected_ingestion_date) {
		this.expected_ingestion_date = expected_ingestion_date;
	}




	/**
	 * @return the expected_ingestion_date
	 */
	public String getExpected_ingestion_date() {
		return expected_ingestion_date;
	}




	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}




	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}




	/**
	 * @param harvest_url the harvest_url to set
	 */
	public void setHarvest_url(String harvest_url) {
		this.harvest_url = harvest_url;
	}




	/**
	 * @return the harvest_url
	 */
	public String getHarvest_url() {
		return harvest_url;
	}




	/**
	 * @param planned_total_c the planned_total_c to set
	 */
	public void setPlanned_total_c(String planned_total_c) {
		this.planned_total_c = planned_total_c;
	}




	/**
	 * @return the planned_total_c
	 */
	public String getPlanned_total_c() {
		return planned_total_c;
	}




	/**
	 * @param planned_sound_c the planned_sound_c to set
	 */
	public void setPlanned_sound_c(String planned_sound_c) {
		this.planned_sound_c = planned_sound_c;
	}




	/**
	 * @return the planned_sound_c
	 */
	public String getPlanned_sound_c() {
		return planned_sound_c;
	}




	/**
	 * @param planned_video_c the planned_video_c to set
	 */
	public void setPlanned_video_c(String planned_video_c) {
		this.planned_video_c = planned_video_c;
	}




	/**
	 * @return the planned_video_c
	 */
	public String getPlanned_video_c() {
		return planned_video_c;
	}




	/**
	 * @param planned_text_c the planned_text_c to set
	 */
	public void setPlanned_text_c(String planned_text_c) {
		this.planned_text_c = planned_text_c;
	}




	/**
	 * @return the planned_text_c
	 */
	public String getPlanned_text_c() {
		return planned_text_c;
	}




	/**
	 * @param planned_image_c the planned_image_c to set
	 */
	public void setPlanned_image_c(String planned_image_c) {
		this.planned_image_c = planned_image_c;
	}




	/**
	 * @return the planned_image_c
	 */
	public String getPlanned_image_c() {
		return planned_image_c;
	}




	/**
	 * @param setspec_c the setspec_c to set
	 */
	public void setSetspec_c(String setspec_c) {
		this.setspec_c = setspec_c;
	}




	/**
	 * @return the setspec_c
	 */
	public String getSetspec_c() {
		return setspec_c;
	}




	/**
	 * @param country_c the country_c to set
	 */
	public void setCountry_c(String country_c) {
		this.country_c = country_c;
	}




	/**
	 * @return the country_c
	 */
	public String getCountry_c() {
		return country_c;
	}




	/**
	 * @param name_acronym_c the name_acronym_c to set
	 */
	public void setName_acronym_c(String name_acronym_c) {
		this.name_acronym_c = name_acronym_c;
	}




	/**
	 * @return the name_acronym_c
	 */
	public String getName_acronym_c() {
		return name_acronym_c;
	}




	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}




	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}




	/**
	 * @param enabled_c the enabled_c to set
	 */
	public void setEnabled_c(String enabled_c) {
		this.enabled_c = enabled_c;
	}




	/**
	 * @return the enabled_c
	 */
	public String getEnabled_c() {
		return enabled_c;
	}




	/**
	 * @param dataset_country the dataset_country to set
	 */
	public void setDataset_country(String dataset_country) {
		this.dataset_country = dataset_country;
	}




	/**
	 * @return the dataset_country
	 */
	public String getDataset_country() {
		return dataset_country;
	}




	/**
	 * @param access_to_content_checker the access_to_content_checker to set
	 */
	public void setAccess_to_content_checker(String access_to_content_checker) {
		this.access_to_content_checker = access_to_content_checker;
	}




	/**
	 * @return the access_to_content_checker
	 */
	public String getAccess_to_content_checker() {
		return access_to_content_checker;
	}




	/**
	 * @param actual_ingestion_date_ the actual_ingestion_date_ to set
	 */
	public void setActual_ingestion_date_(String actual_ingestion_date_) {
		this.actual_ingestion_date_ = actual_ingestion_date_;
	}




	/**
	 * @return the actual_ingestion_date_
	 */
	public String getActual_ingestion_date_() {
		return actual_ingestion_date_;
	}




	/**
	 * @param ingested_total_c the ingested_total_c to set
	 */
	public void setIngested_total_c(String ingested_total_c) {
		this.ingested_total_c = ingested_total_c;
	}




	/**
	 * @return the ingested_total_c
	 */
	public String getIngested_total_c() {
		return ingested_total_c;
	}




	/**
	 * @param ingested_sound_c the ingested_sound_c to set
	 */
	public void setIngested_sound_c(String ingested_sound_c) {
		this.ingested_sound_c = ingested_sound_c;
	}




	/**
	 * @return the ingested_sound_c
	 */
	public String getIngested_sound_c() {
		return ingested_sound_c;
	}




	/**
	 * @param ingested_video_c the ingested_video_c to set
	 */
	public void setIngested_video_c(String ingested_video_c) {
		this.ingested_video_c = ingested_video_c;
	}




	/**
	 * @return the ingested_video_c
	 */
	public String getIngested_video_c() {
		return ingested_video_c;
	}




	/**
	 * @param ingested_text_c the ingested_text_c to set
	 */
	public void setIngested_text_c(String ingested_text_c) {
		this.ingested_text_c = ingested_text_c;
	}




	/**
	 * @return the ingested_text_c
	 */
	public String getIngested_text_c() {
		return ingested_text_c;
	}




	/**
	 * @param ingested_image_c the ingested_image_c to set
	 */
	public void setIngested_image_c(String ingested_image_c) {
		this.ingested_image_c = ingested_image_c;
	}




	/**
	 * @return the ingested_image_c
	 */
	public String getIngested_image_c() {
		return ingested_image_c;
	}




	/**
	 * @param next_step the next_step to set
	 */
	public void setNext_step(String next_step) {
		this.next_step = next_step;
	}




	/**
	 * @return the next_step
	 */
	public String getNext_step() {
		return next_step;
	}




	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}




	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}




	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}




	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}




	/**
	 * @param importedIMG the importedIMG to set
	 */
	public void setImportedIMG(String importedIMG) {
		this.importedIMG = importedIMG;
	}




	/**
	 * @return the importedIMG
	 */
	public String getImportedIMG() {
		return importedIMG;
	}













}
