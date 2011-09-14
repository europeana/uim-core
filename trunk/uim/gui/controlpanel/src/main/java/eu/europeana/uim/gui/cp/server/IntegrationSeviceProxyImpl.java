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

import java.util.ArrayList;
import java.util.List;

import eu.europeana.uim.api.ResourceEngine;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.gui.cp.client.services.IntegrationSeviceProxy;
import eu.europeana.uim.gui.cp.client.utils.EuropeanaClientConstants;
import eu.europeana.uim.gui.cp.server.engine.ExpandedOsgiEngine;
import eu.europeana.uim.gui.cp.shared.HarvestingStatusDTO;
import eu.europeana.uim.gui.cp.shared.ImportResultDTO;
import eu.europeana.uim.gui.cp.shared.IntegrationStatusDTO;
import eu.europeana.uim.gui.cp.shared.IntegrationStatusDTO.TYPE;
import eu.europeana.uim.gui.cp.shared.SugarCRMRecordDTO;
import eu.europeana.uim.repoxclient.plugin.RepoxUIMService;
import eu.europeana.uim.repoxclient.rest.exceptions.AggregatorOperationException;
import eu.europeana.uim.repoxclient.rest.exceptions.DataSourceOperationException;
import eu.europeana.uim.repoxclient.rest.exceptions.ProviderOperationException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.sugarcrm.SugarCrmService;
import eu.europeana.uim.sugarcrm.SugarCrmRecord;
import eu.europeana.uim.sugarcrmclient.plugin.objects.data.EuropeanaDatasetStates;
import eu.europeana.uim.sugarcrmclient.plugin.objects.data.EuropeanaRetrievableField;
import eu.europeana.uim.sugarcrmclient.plugin.objects.data.EuropeanaUpdatableField;
import eu.europeana.uim.sugarcrmclient.plugin.objects.queries.CustomSugarCrmQuery;
import eu.europeana.uim.sugarcrmclient.plugin.objects.queries.SimpleSugarCrmQuery;
import eu.europeana.uim.sugarcrm.SugarCrmQuery;
import eu.europeana.uim.sugarcrm.QueryResultException;

/**
 * 
 * 
 * @author Georgios Markakis
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

		ExpandedOsgiEngine engine =  getEngine();
		SugarCrmService sugService = engine.getSugarCrmService();
		
		RepoxUIMService repoxService = engine.getRepoxService();

		ImportResultDTO result = new ImportResultDTO();
		result.setResult(EuropeanaClientConstants.SUCCESSIMAGELOC);
		
		result.setCollectionName(record.getName());
		
		String id = record.getId();
		
		try {
			SugarCrmRecord originalRec = sugService.retrieveRecord(id);
			Provider prov = sugService.updateProviderFromRecord(originalRec);
			
			String aggrID = prov.getValue("repoxCountry").toLowerCase();
			
			if(!repoxService.aggregatorExists(aggrID)){
				repoxService.createAggregator(aggrID);
			}
			
			Collection coll = sugService.updateCollectionFromRecord(originalRec, prov);
			

			
			if(!repoxService.providerExists(prov)){
				repoxService.createProviderfromUIMObj(prov,false);	
			}
			else
			{
				repoxService.updateProviderfromUIMObj(prov);
			}
			
			
			if(!repoxService.datasourceExists(coll)){
				repoxService.createDatasourcefromUIMObj(coll, prov);
			}
			else
			{
				repoxService.updateDatasourcefromUIMObj(coll);
			}
			
			
		} catch (QueryResultException e) {			
			result.setDescription("Import failed while accessing SugarCRM.");
			result.setCause(e.getMessage());
			result.setResult(EuropeanaClientConstants.ERRORIMAGELOC);
		} catch (StorageEngineException e) {
			result.setDescription("Import failed while storing in UIM.");
			result.setCause(e.getMessage());
			result.setResult(EuropeanaClientConstants.ERRORIMAGELOC);
		} catch (AggregatorOperationException e) {
			result.setDescription("Import failed while creating an Aggregator in Repox.");
			result.setCause(e.getMessage());
			result.setResult(EuropeanaClientConstants.ERRORIMAGELOC);
		} catch (ProviderOperationException e) {
			result.setDescription("Import failed while creating an Provider in Repox.");
			result.setCause(e.getMessage());
			result.setResult(EuropeanaClientConstants.ERRORIMAGELOC);
		} catch (DataSourceOperationException e) {
			result.setDescription("Import failed while creating a DataSource in Repox.");
			result.setCause(e.getMessage());
			result.setResult(EuropeanaClientConstants.ERRORIMAGELOC);
		}
		
		return result;
	}

	
	
	/* (non-Javadoc)
	 * @see eu.europeana.uim.gui.cp.client.services.IntegrationSeviceProxy#executeSugarCRMQuery(java.lang.String)
	 */
	@Override
	public List<SugarCRMRecordDTO> executeSugarCRMQuery(String query) {
		
		ArrayList<SugarCRMRecordDTO> guiobjs = new ArrayList<SugarCRMRecordDTO>();
		
		ExpandedOsgiEngine engine =  getEngine();
		SugarCrmService sugService = engine.getSugarCrmService();
		
		CustomSugarCrmQuery queryObj = new CustomSugarCrmQuery(query);
		queryObj.setMaxResults(100);
		queryObj.setOffset(0);
		queryObj.setOrderBy(EuropeanaRetrievableField.ID);
		
		try {
			ArrayList<SugarCrmRecord> results =  (ArrayList<SugarCrmRecord>) sugService.retrieveRecords(queryObj);
			guiobjs =  (ArrayList<SugarCRMRecordDTO>) convertSugarObj2GuiObj(results);
			results = null;
		} catch (QueryResultException e) {

			e.printStackTrace();
		} catch (StorageEngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return guiobjs;
	}
	
	
	
	
	/**
	 * Converts a SugarCRM object (query result) into an object suitable for GWT visualization purposes.
	 * 
	 * @param toconvert a  SugarCRM query result object
	 * @return a GWT object
	 * @throws StorageEngineException 
	 */
	private List<SugarCRMRecordDTO> convertSugarObj2GuiObj(ArrayList<SugarCrmRecord> toconvert) throws StorageEngineException{
		ArrayList<SugarCRMRecordDTO> converted = new  ArrayList<SugarCRMRecordDTO>();
		
		ExpandedOsgiEngine engine =  getEngine();
		
		StorageEngine<?> resengine = engine.getRegistry().getStorageEngine();
		
		
		
		for (SugarCrmRecord originalrecord: toconvert){
			SugarCRMRecordDTO guirecord = new SugarCRMRecordDTO();
			
			
			Collection colexists = resengine.findCollection(originalrecord.getItemValue(EuropeanaRetrievableField.NAME).split("_")[0]);

			if (colexists == null){
				guirecord.setImportedIMG(EuropeanaClientConstants.ERRORIMAGELOC);
			}
			else{
				guirecord.setImportedIMG(EuropeanaClientConstants.SUCCESSIMAGELOC);
			}
			
			
			guirecord.setId(originalrecord.getItemValue(EuropeanaRetrievableField.ID));
			guirecord.setAccess_to_content_checker(originalrecord.getItemValue(EuropeanaRetrievableField.ACCESS_TO_CONTENT_CHECKER));
			guirecord.setActual_ingestion_date_(originalrecord.getItemValue(EuropeanaRetrievableField.DATE_OF_REPLICATION));
			guirecord.setAssigned_user_id(originalrecord.getItemValue(EuropeanaRetrievableField.ASSIGNED_USER_ID));
			guirecord.setAssigned_user_name(originalrecord.getItemValue(EuropeanaRetrievableField.ASSIGNED_USER_NAME));
			guirecord.setCampaign_name(originalrecord.getItemValue(EuropeanaRetrievableField.CAMPAIGN_NAME));
			guirecord.setCountry_c(originalrecord.getItemValue(EuropeanaRetrievableField.COUNTRY));
			guirecord.setCreated_by(originalrecord.getItemValue(EuropeanaRetrievableField.CREATED_BY_USER));
			guirecord.setDataset_country(originalrecord.getItemValue(EuropeanaRetrievableField.DATASET_COUNTRY));
			guirecord.setDate_entered(originalrecord.getItemValue(EuropeanaRetrievableField.DATE_ENTERED));
			guirecord.setDate_modified(originalrecord.getItemValue(EuropeanaRetrievableField.DATE_MODIFIED));
			guirecord.setDeleted(originalrecord.getItemValue(EuropeanaRetrievableField.DELETED));
			guirecord.setDescription(originalrecord.getItemValue(EuropeanaRetrievableField.DESCRIPTION));
			guirecord.setEnabled_c(originalrecord.getItemValue(EuropeanaRetrievableField.ENABLED));
			guirecord.setExpected_ingestion_date(originalrecord.getItemValue(EuropeanaRetrievableField.EXPECTED_INGESTION_DATE));
			guirecord.setHarvest_url(originalrecord.getItemValue(EuropeanaRetrievableField.HARVEST_URL));
			guirecord.setIdentifier(originalrecord.getItemValue(EuropeanaRetrievableField.IDENTIFIER));
			guirecord.setIngested_image_c(originalrecord.getItemValue(EuropeanaUpdatableField.INGESTED_IMAGE));
			guirecord.setIngested_sound_c(originalrecord.getItemValue(EuropeanaUpdatableField.INGESTED_SOUND));
			guirecord.setIngested_text_c(originalrecord.getItemValue(EuropeanaUpdatableField.INGESTED_TEXT));
			guirecord.setIngested_total_c(originalrecord.getItemValue(EuropeanaUpdatableField.TOTAL_INGESTED));
			guirecord.setIngested_video_c(originalrecord.getItemValue(EuropeanaUpdatableField.INGESTED_VIDEO));
			guirecord.setModified_by_user(originalrecord.getItemValue(EuropeanaRetrievableField.MODIFIED_BY_USER));
			guirecord.setName(originalrecord.getItemValue(EuropeanaRetrievableField.NAME));
			guirecord.setName_acronym_c(originalrecord.getItemValue(EuropeanaRetrievableField.ACRONYM));
			guirecord.setNext_step(originalrecord.getItemValue(EuropeanaUpdatableField.NEXT_STEP));
			guirecord.setNotes(originalrecord.getItemValue(EuropeanaRetrievableField.NOTES));
			guirecord.setOrganization_name(originalrecord.getItemValue(EuropeanaRetrievableField.ORGANIZATION_NAME));
			guirecord.setPlanned_image_c(originalrecord.getItemValue(EuropeanaRetrievableField.PLANNED_IMAGE));
			guirecord.setPlanned_sound_c(originalrecord.getItemValue(EuropeanaRetrievableField.PLANNED_SOUND));
			guirecord.setPlanned_text_c(originalrecord.getItemValue(EuropeanaRetrievableField.PLANNED_TEXT));
			guirecord.setPlanned_total_c(originalrecord.getItemValue(EuropeanaRetrievableField.PLANNED_TOTAL));
			guirecord.setPlanned_video_c(originalrecord.getItemValue(EuropeanaRetrievableField.PLANNED_VIDEO));
			guirecord.setSetspec_c(originalrecord.getItemValue(EuropeanaRetrievableField.SETSPEC));
			guirecord.setStatus(originalrecord.getItemValue(EuropeanaUpdatableField.STATUS));
			guirecord.setType(originalrecord.getItemValue(EuropeanaUpdatableField.TYPE));
			
			
			converted.add(guirecord);
		}
		
		
		return converted;
		
	}



	/* (non-Javadoc)
	 * @see eu.europeana.uim.gui.cp.client.services.IntegrationSeviceProxy#retrieveIntegrationInfo(java.lang.Long, java.lang.Long)
	 */
	@Override
	public IntegrationStatusDTO retrieveIntegrationInfo(String provider,String collection) {
		ExpandedOsgiEngine engine =  getEngine();
		
		RepoxUIMService repoxService = engine.getRepoxService();
		
		StorageEngine<?> stengine = engine.getRegistry().getStorageEngine();
		
		IntegrationStatusDTO ret = new IntegrationStatusDTO();
		
		
		ret.setRepoxID(provider.toString());
		ret.setSugarCRMID(collection.toString());
		
		
		return ret;
		

		
		
		/*

		
		switch(type){
		
		case COLLECTION:
			try {

				Collection<?> col = stengine.findCollection(ID);
			} catch (StorageEngineException e) {

			}
		break;
		
		case PROVIDER:
			
			try {
				stengine.findProvider(ID);
			} catch (StorageEngineException e) {

			}
			
		}
		
		
		IntegrationStatusDTO ret = new IntegrationStatusDTO();
		
		String sugarCRMID = null;
		String repoxID = null;
		
		
		ret.setId(ID);
		
		HarvestingStatusDTO harvestingStatus = new HarvestingStatusDTO();
		ret.setHarvestingStatus(harvestingStatus );
		
		ret.setSugarCRMID(sugarCRMID);
		ret.setType(type);
		ret.setRepoxID(repoxID);
		//ret.s
		
		return ret;		 
		 * 
		 */
		
	}






}
