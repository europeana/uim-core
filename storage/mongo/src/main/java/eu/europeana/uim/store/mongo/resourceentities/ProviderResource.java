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

package eu.europeana.uim.store.mongo.resourceentities;

import java.util.LinkedHashMap;
import java.util.List;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Serialized;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.mongo.decorators.MongoProviderDecorator;

/**
 * Morphia Entity Class representing a ProviderResource
 * 
 * @author Georgios Markakis (gwarkx@hotmail.com)
 * @date Jan 6, 2012
 */
@Entity
public class ProviderResource extends AbstractResource {

	/**
	 * A reference to the ProviderDecorator stored in MongoDB related to this
	 * resource
	 */
	@Reference
	private MongoProviderDecorator<String> provider;

	/**
	 * The HashMap containing the resources We have to serialize this because
	 * certain characters (ie .) are not allowed to be stored directly in
	 * MongoDB
	 */
	@Serialized
	private LinkedHashMap<String, List<String>> resources;

	/**
	 * The default constructor (required by Morphia but not used in this
	 * implementation)
	 */
	public ProviderResource() {
	}

	/**
	 * The constructor used in this implementation
	 * 
	 */
	@SuppressWarnings("unchecked")
	public ProviderResource(Provider<?> provider) {
		this.provider = (MongoProviderDecorator<String>) provider;
		this.resources = new LinkedHashMap<String, List<String>>();
	}

	/**
	 * Get Provider
	 * 
	 * @return the provider
	 */
	public MongoProviderDecorator<String> getProvider() {
		return provider;
	}

	/**
	 * Get the resources
	 * 
	 * @return
	 */
	public LinkedHashMap<String, List<String>> getResources() {
		return resources;
	}

}
