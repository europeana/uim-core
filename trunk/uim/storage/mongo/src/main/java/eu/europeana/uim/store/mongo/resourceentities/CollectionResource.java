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

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Serialized;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.mongo.decorators.MongoCollectionDecorator;

/**
* 
* @author Georgios Markakis (gwarkx@hotmail.com)
* @date Jan 6, 2012
*/
@Entity
public class CollectionResource extends AbstractResource {

    @Reference
    private MongoCollectionDecorator<String> collection;
	
	// We have to serialize this because certain characters (ie .) are 
	// not allowed to be stored directly in MongoDB
	@Serialized
	private LinkedHashMap<String, List<String>> resources;

	public CollectionResource(){
		
	}
	
	@SuppressWarnings("unchecked")
	public CollectionResource(Collection<?> collection){
		this.collection = (MongoCollectionDecorator<String>) collection;
		this.resources = new LinkedHashMap<String, List<String>>();
	}
	
	public MongoCollectionDecorator<String> getCollection() {
		return collection;
	}

	public LinkedHashMap<String, List<String>> getResources() {
		return resources;
	}

	public void setResources(LinkedHashMap<String, List<String>> resources) {
		this.resources = resources;
	}
}
