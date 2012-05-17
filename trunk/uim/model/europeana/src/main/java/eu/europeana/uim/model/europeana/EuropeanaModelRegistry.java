package eu.europeana.uim.model.europeana;
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


import eu.europeana.uim.common.TKey;


/**
 * TKeys Definitions for the Europeana Data Model
 * 
 * @author Georgios Markakis
 */
public final class EuropeanaModelRegistry {

    /** The key for all sorts of concepts.
     */
    public static final TKey<EuropeanaModelRegistry, String>  UNCLASSIFIED  = TKey.register(
    		                                                                 EuropeanaModelRegistry.class,
                                                                             "unclassified",
                                                                             String.class);
    /** The key for a full EDM Record representation
     */
    public static final TKey<EuropeanaModelRegistry, String>  EDMRECORD  = TKey.register(
    		                                                                 EuropeanaModelRegistry.class,
                                                                             "edmrecord",
                                                                             String.class);
    
    /** The key for a full EDM Record representation
     */
    public static final TKey<EuropeanaModelRegistry, String>  UIMINGESTIONDATE  = TKey.register(
    		                                                                 EuropeanaModelRegistry.class,
                                                                             "uimingestiondate",
                                                                             String.class);
    

    
    
	
}
