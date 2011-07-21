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

import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.gui.cp.client.services.IntegrationSeviceProxy;
import eu.europeana.uim.gui.cp.server.engine.ExpandedOsgiEngine;
import eu.europeana.uim.gui.cp.shared.ImportResultDTO;
import eu.europeana.uim.gui.cp.shared.SugarCRMRecordDTO;
import eu.europeana.uim.repoxclient.plugin.RepoxUIMService;
import eu.europeana.uim.repoxclient.rest.exceptions.AggregatorOperationException;
import eu.europeana.uim.repoxclient.rest.exceptions.DataSourceOperationException;
import eu.europeana.uim.repoxclient.rest.exceptions.ProviderOperationException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.sugarcrmclient.plugin.SugarCRMService;
import eu.europeana.uim.sugarcrmclient.plugin.objects.SugarCrmRecord;
import eu.europeana.uim.sugarcrmclient.plugin.objects.data.DatasetStates;
import eu.europeana.uim.sugarcrmclient.plugin.objects.data.RetrievableField;
import eu.europeana.uim.sugarcrmclient.plugin.objects.data.UpdatableField;
import eu.europeana.uim.sugarcrmclient.plugin.objects.queries.CustomSugarCrmQuery;
import eu.europeana.uim.sugarcrmclient.plugin.objects.queries.SimpleSugarCrmQuery;
import eu.europeana.uim.sugarcrmclient.plugin.objects.queries.SugarCrmQuery;
import eu.europeana.uim.sugarcrmclient.ws.exceptions.QueryResultException;

/**
 * @author Georgios Markakis
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

		ExpandedOsgiEngine engine =  getEngine();
		SugarCRMService sugService = engine.getSugarCrmService();
		
		RepoxUIMService repoxService = engine.getRepoxService();

		ImportResultDTO result = new ImportResultDTO();
		result.setResult("ok");
		
		String id = record.getId();
		
		try {
			SugarCrmRecord originalRec = sugService.retrieveRecord(id);
			Provider prov = sugService.createProviderFromRecord(originalRec);
			Collection coll = sugService.createCollectionFromRecord(originalRec, prov);
			
			if(prov.isAggregator()){
				if(repoxService.aggregatorExists(prov)){
					repoxService.updateAggregatorfromUIMObj(prov);
				}
				else
				{
					repoxService.createAggregatorfromUIMObj(prov,false);	
				}
			}
			else{
				//if(repoxService.providerExists(prov)){
				//	repoxService.updateProviderfromUIMObj(prov);
				//}
				//else
				//{
				 repoxService.createProviderfromUIMObj(prov,false);	
				//}
			}
			
			repoxService.createDatasourcefromUIMObj(coll, prov);
			
		} catch (QueryResultException e) {			
			result.setDescription("Import failed while accessing SugarCRM.");
			result.setCause(e.getMessage());
			result.setResult("fail");
		} catch (StorageEngineException e) {
			result.setDescription("Import failed while storing in UIM.");
			result.setCause(e.getMessage());
			result.setResult("fail");
		} catch (AggregatorOperationException e) {
			result.setDescription("Import failed while creating an Aggregator in Repox.");
			result.setCause(e.getMessage());
			result.setResult("fail");
		} catch (ProviderOperationException e) {
			result.setDescription("Import failed while creating an Provider in Repox.");
			result.setCause(e.getMessage());
			result.setResult("fail");
		} catch (DataSourceOperationException e) {
			result.setDescription("Import failed while creating a DataSource in Repox.");
			result.setCause(e.getMessage());
			result.setResult("fail");
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
		SugarCRMService sugService = engine.getSugarCrmService();
		
		//CustomSugarCrmQuery queryObj = new CustomSugarCrmQuery(query);
		SimpleSugarCrmQuery queryObj = new  SimpleSugarCrmQuery(DatasetStates.MAPPING_AND_NORMALIZATION);
		
		
		try {
			ArrayList<SugarCrmRecord> results =  (ArrayList<SugarCrmRecord>) sugService.retrieveRecords(queryObj);
			guiobjs =  (ArrayList<SugarCRMRecordDTO>) convertSugarObj2GuiObj(results);
			results = null;
		} catch (QueryResultException e) {

			e.printStackTrace();
		}
		
		
		return guiobjs;
	}
	
	
	
	
	/**
	 * Converts a 
	 * @param toconvert
	 * @return
	 */
	private List<SugarCRMRecordDTO> convertSugarObj2GuiObj(ArrayList<SugarCrmRecord> toconvert){
		ArrayList<SugarCRMRecordDTO> converted = new  ArrayList<SugarCRMRecordDTO>();
		
		for (SugarCrmRecord originalrecord: toconvert){
			SugarCRMRecordDTO guirecord = new SugarCRMRecordDTO();
			
			guirecord.setId(originalrecord.getItemValue(RetrievableField.ID));
			guirecord.setAccess_to_content_checker(originalrecord.getItemValue(RetrievableField.ACCESS_TO_CONTENT_CHECKER));
			guirecord.setActual_ingestion_date_(originalrecord.getItemValue(RetrievableField.DATE_OF_REPLICATION));
			guirecord.setAssigned_user_id(originalrecord.getItemValue(RetrievableField.ASSIGNED_USER_ID));
			guirecord.setAssigned_user_name(originalrecord.getItemValue(RetrievableField.ASSIGNED_USER_NAME));
			guirecord.setCampaign_name(originalrecord.getItemValue(RetrievableField.CAMPAIGN_NAME));
			guirecord.setCountry_c(originalrecord.getItemValue(RetrievableField.COUNTRY));
			guirecord.setCreated_by(originalrecord.getItemValue(RetrievableField.CREATED_BY_USER));
			guirecord.setDataset_country(originalrecord.getItemValue(RetrievableField.DATASET_COUNTRY));
			guirecord.setDate_entered(originalrecord.getItemValue(RetrievableField.DATE_ENTERED));
			guirecord.setDate_modified(originalrecord.getItemValue(RetrievableField.DATE_MODIFIED));
			guirecord.setDeleted(originalrecord.getItemValue(RetrievableField.DELETED));
			guirecord.setDescription(originalrecord.getItemValue(RetrievableField.DESCRIPTION));
			guirecord.setEnabled_c(originalrecord.getItemValue(RetrievableField.ENABLED));
			guirecord.setExpected_ingestion_date(originalrecord.getItemValue(RetrievableField.EXPECTED_INGESTION_DATE));
			guirecord.setHarvest_url(originalrecord.getItemValue(RetrievableField.HARVEST_URL));
			guirecord.setIdentifier(originalrecord.getItemValue(RetrievableField.IDENTIFIER));
			guirecord.setIngested_image_c(originalrecord.getItemValue(UpdatableField.INGESTED_IMAGE));
			guirecord.setIngested_sound_c(originalrecord.getItemValue(UpdatableField.INGESTED_SOUND));
			guirecord.setIngested_text_c(originalrecord.getItemValue(UpdatableField.INGESTED_TEXT));
			guirecord.setIngested_total_c(originalrecord.getItemValue(UpdatableField.TOTAL_INGESTED));
			guirecord.setIngested_video_c(originalrecord.getItemValue(UpdatableField.INGESTED_VIDEO));
			guirecord.setModified_by_user(originalrecord.getItemValue(RetrievableField.MODIFIED_BY_USER));
			guirecord.setName(originalrecord.getItemValue(RetrievableField.NAME));
			guirecord.setName_acronym_c(originalrecord.getItemValue(RetrievableField.ACRONYM));
			guirecord.setNext_step(originalrecord.getItemValue(UpdatableField.NEXT_STEP));
			guirecord.setNotes(originalrecord.getItemValue(RetrievableField.NOTES));
			guirecord.setOrganization_name(originalrecord.getItemValue(RetrievableField.ORGANIZATION_NAME));
			guirecord.setPlanned_image_c(originalrecord.getItemValue(RetrievableField.PLANNED_IMAGE));
			guirecord.setPlanned_sound_c(originalrecord.getItemValue(RetrievableField.PLANNED_SOUND));
			guirecord.setPlanned_text_c(originalrecord.getItemValue(RetrievableField.PLANNED_TEXT));
			guirecord.setPlanned_total_c(originalrecord.getItemValue(RetrievableField.PLANNED_TOTAL));
			guirecord.setPlanned_video_c(originalrecord.getItemValue(RetrievableField.PLANNED_VIDEO));
			guirecord.setSetspec_c(originalrecord.getItemValue(RetrievableField.SETSPEC));
			guirecord.setStatus(originalrecord.getItemValue(UpdatableField.STATUS));
			guirecord.setType(originalrecord.getItemValue(UpdatableField.TYPE));
			
			
			converted.add(guirecord);
		}
		
		
		return converted;
		
	}

}
