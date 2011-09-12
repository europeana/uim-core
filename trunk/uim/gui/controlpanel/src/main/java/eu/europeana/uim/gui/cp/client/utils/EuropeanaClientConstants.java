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
package eu.europeana.uim.gui.cp.client.utils;

/**
 * String constants for GUI labels.
 * 
 * @author Georgios Markakis
 */

public class EuropeanaClientConstants {

	public final static String PANELLABEL = "Import Resources" ;
	public final static String PANELDESCRIPTION = "This view allows to import resources into UIM." ;
	
	public final static String ERRORIMAGELOC = "images/no.png" ;
	public final static String SUCCESSIMAGELOC = "images/ok.png" ;
	public final static String QUERYIMAGELOC = "images/network.gif"; 
	public final static String SEARCHDIALOGMSG = "Searching for SugarCRM entries"; 
	
	//Search related labels
	public final static String DSNAMESEARCHLABEL = "DataSet Name:" ;
	public final static String IDSEARCHLABEL = "Identifier:" ;
	public final static String ORGANIZATIONSEARCHLABEL = "Organization:" ;	
	public final static String ACRONYMSEARCHLABEL = "Acronym:" ;	
	public final static String TYPESEARCHLABEL = "Type:" ;
	public final static String STATUSSEARCHLABEL = "SugarCRM Status:" ;
	public final static String ENABLEDSEARCHLABEL = "Enabled:" ;
	public final static String INGESTDATESEARCHLABEL = "Expected Ingestion Date:" ;
	public final static String AMOUNTSEARCHLABEL = "Amount of ingested items:" ;
	public final static String COUNTRYSEARCHLABEL = "Country:" ;
	public final static String USERSEARCHLABEL = "User:" ;
	public final static String SEARCHBUTTONLABEL = "Search" ;
	public final static String SEARCHBUTTONTITLE = "Search SugarCRM for Records" ;	
	
	//Import related labels
	public final static String IMPORTMENULABEL = "Importing to UIM & Repox" ;
	public final static String IMPORTBUTTONLABEL = "Import Selected" ;
	public final static String IMPORTBUTTONTITLE = "Populate UIM and Repox with Data from SugarCrm" ;
	
	//Misc
	public final static String UIMSTATELABEL = "State:" ;
}
