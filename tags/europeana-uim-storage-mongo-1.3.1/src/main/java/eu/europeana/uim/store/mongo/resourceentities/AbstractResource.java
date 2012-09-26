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

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 * Abstract Morphia Resource Entity
 * 
 * @author Georgios Markakis (gwarkx@hotmail.com)
 * @date Jan 6, 2012
 */
@Entity
public abstract class AbstractResource {

	@Id
    private ObjectId mongoid;

	/**
	 * @param mongoid the mongoid to set
	 */
	public void setMongoid(ObjectId mongoid) {
		this.mongoid = mongoid;
	}

	/**
	 * @return the mongoid
	 */
	public ObjectId getMongoid() {
		return mongoid;
	}
}
