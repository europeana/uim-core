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

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.store.UimEntity;

/**
 * Abstract top base class for MongoDB decorators. It defines the ObjectId id
 * field which is set automatically upon saving an object within MongoDB.
 * 
 * @author Georgios Markakis (gwarkx@hotmail.com)
 * @date Jan 12, 2012
 */
@Entity
public abstract class MongoAbstractEntity<I> {

	/**
	 * The unique ID for all objects stored in MongoDB
	 */
	@Id
	private ObjectId mongoId;

	/**
	 * Default constructor
	 */
	public MongoAbstractEntity() {
	}

	/**
	 * Get the mongoID
	 * 
	 * @return the mongoID
	 */
	public ObjectId getMongoId() {
		return mongoId;
	}

	/**
	 * Sets the mongoID
	 * 
	 * @param mongoId
	 */
	public void setMongoId(ObjectId mongoId) {
		this.mongoId = mongoId;
	}

}
