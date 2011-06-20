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
package eu.europeana.uim.repoxclient.objects;

/**
 * This Enumeration holds the default values for the type 
 * of an Ingestion
 * 
 * @author Georgios Markakis
 */
public enum HarvestingType {
  INCREMENTAL("Incremental Harvesting"),
  FULL("Full Harvesting"),
  SAMPLE("Sample Harvesting");


private final String description;

/**
 * The Enum Constructor
 * @param description
 */
HarvestingType(String description){
	this.description = description;
}


//Getter
public String getDescription() {
	return description;
}


}