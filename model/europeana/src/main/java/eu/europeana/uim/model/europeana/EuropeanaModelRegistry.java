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

import org.theeuropeanlibrary.model.common.qualifier.Status;

import eu.europeana.uim.common.TKey;

/**
 * TKeys Definitions for the Europeana Data Model
 * 
 * @author Georgios Markakis
 */
public final class EuropeanaModelRegistry {

	/**
	 * The key for all sorts of concepts.
	 */
	public static final TKey<EuropeanaModelRegistry, String> UNCLASSIFIED = TKey
			.register(EuropeanaModelRegistry.class, "unclassified",
					String.class);
	/**
	 * The key for a full EDM Record representation
	 */
	public static final TKey<EuropeanaModelRegistry, String> EDMRECORD = TKey
			.register(EuropeanaModelRegistry.class, "edmrecord", String.class);
	/**
	 * The key for a dereferenced EDM Record
	 */
	public static final TKey<EuropeanaModelRegistry, String> EDMDEREFERENCEDRECORD = TKey
			.register(EuropeanaModelRegistry.class, "edmdereferencedrecord",
					String.class);
	/**
     * The key for id redirects
     */
    public static final TKey<EuropeanaModelRegistry, EuropeanaRedirectId> EDMRECORDREDIRECT = TKey
            .register(EuropeanaModelRegistry.class, "edmrecordredirect",
                    EuropeanaRedirectId.class);
	/**
	 * The key date of dereference
	 */
	public static final TKey<EuropeanaModelRegistry, String> UIMDEREFERENCE = TKey
			.register(EuropeanaModelRegistry.class, "uimdereferencet",
					String.class);

	/**
	 * The key indicating the actual ingestion/update datde for a specific
	 * record
	 */
	public static final TKey<EuropeanaModelRegistry, String> UIMINGESTIONDATE = TKey
			.register(EuropeanaModelRegistry.class, "uimingestiondate",
					String.class);

	/**
	 * The key indicating the actual ingestion/update datde for a specific
	 * record
	 */
	public static final TKey<EuropeanaModelRegistry, String> UIMUPDATEDDATE = TKey
			.register(EuropeanaModelRegistry.class, "uimupdateddate",
					String.class);

	/**
	 * The key for a full EDM Record representation
	 */
	public static final TKey<EuropeanaModelRegistry, EuropeanaLink> EUROPEANALINK = TKey
			.register(EuropeanaModelRegistry.class, "europeanalink",
					EuropeanaLink.class);

	/**
	 * A key indicating the status for a specific record
	 */
	public static final TKey<EuropeanaModelRegistry, Status> STATUS = TKey
			.register(EuropeanaModelRegistry.class, "status", Status.class);

	/**
	 * A key indicating that a record has been removed from production and when
	 */
	public static final TKey<EuropeanaModelRegistry, Long> REMOVED = TKey
			.register(EuropeanaModelRegistry.class, "removed", Long.class);

	/**
	 * A key indicating when a record was initially saved in the server index
	 */
	public static final TKey<EuropeanaModelRegistry, Long> INITIALSAVE = TKey
			.register(EuropeanaModelRegistry.class, "initial", Long.class);

	/**
	 * A key indicating when a record was updated in the server index
	 */
	public static final TKey<EuropeanaModelRegistry, Long> UPDATEDSAVE = TKey
			.register(EuropeanaModelRegistry.class, "updatedsave", Long.class);

	/**
	 * A key to represent the enriched EDM
	 */
	public static final TKey<EuropeanaModelRegistry, String> EDMENRICHEDRECORD = TKey
			.register(EuropeanaModelRegistry.class, "edmenrichedrecord",
					String.class);
}
