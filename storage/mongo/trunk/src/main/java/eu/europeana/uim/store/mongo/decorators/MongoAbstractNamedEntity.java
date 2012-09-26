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
package eu.europeana.uim.store.mongo.decorators;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;

/**
 * An Abstract class inherited by all UIM Entity Classes that have a mnemonic
 * (ie Provider or Collection)
 * 
 * @author Georgios Markakis (gwarkx@hotmail.com)
 * @date Jan 12, 2012
 */
@Entity
public abstract class MongoAbstractNamedEntity<I> extends
		MongoAbstractEntity<I> {

	/**
	 * The (indexed & searchable mnemonic)
	 */
	@Indexed
	private String searchMnemonic;

	/**
	 * Default Constructor
	 */
	public MongoAbstractNamedEntity() {
		super();
	}

	/**
	 * Get the Search Mnemonic
	 * 
	 * @return the Search Mnemonic
	 */
	public String getSearchMnemonic() {
		return searchMnemonic;
	}

	/**
	 * Set the Search Mnemonic
	 * 
	 * @param searchMnemonic
	 */
	public void setSearchMnemonic(String searchMnemonic) {
		this.searchMnemonic = searchMnemonic;
	}

}
