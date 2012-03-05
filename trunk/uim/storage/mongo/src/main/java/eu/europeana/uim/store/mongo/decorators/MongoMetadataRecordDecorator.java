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

import java.util.HashMap;
import java.util.List;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.NotSaved;
import com.google.code.morphia.annotations.PostLoad;
import com.google.code.morphia.annotations.PostPersist;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Serialized;
import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.bean.MetaDataRecordBean;
import eu.europeana.uim.store.mongo.converters.MongoDBEuropeanaMDRConverter;

/**
 * Morphia based MongoDB representation for a MetaDataRecord object. This object
 * as a decorator to the MetaDataRecordBean class guaranteeing thus to a certain
 * extent the compatibility between MongoDB Storage Engine and the Memory
 * Storage Engine
 * 
 * @author Georgios Markakis (gwarkx@hotmail.com)
 * @date Jan 12, 2012
 * @see MetaDataRecordBean
 */
@Entity
public class MongoMetadataRecordDecorator<I> extends MongoAbstractEntity<I>
		implements MetaDataRecord<String> {

	/**
	 * The emebeddedMdr object wrapped in this Decorator. It is not saved in the
	 * database but handled by lifecycle methods given the contents of the
	 * stored embeddedbinary object
	 */
	@NotSaved
	private MetaDataRecordBean<String> emebeddedMdr;

	/**
	 * The fields contained in the emebeddedMdr object stored in a binary form
	 * in MongoDB
	 */
	@Serialized
	private HashMap<String, List<byte[]>> fields;

	/**
	 * A searchable reference to the related Collectionobject (not the object
	 * itself)
	 */
	@Reference
	private MongoCollectionDecorator<String> collection;

	
	/**
	 * The (indexed & searchable) unique identifier
	 */
	@Indexed (unique=true, dropDups=true) 
	private String uniqueID;
	
	/**
	 * The default constructor (required by Morphia but not used in this
	 * implementation)
	 */
	public MongoMetadataRecordDecorator() {
	}


	
	/**
	 * Assigns a specific unique ID
	 * 
	 * @param collection
	 *            the associated collection
	 */
	public MongoMetadataRecordDecorator(
			MongoCollectionDecorator<String> collection,String uuid) {
		emebeddedMdr = new MetaDataRecordBean<String>();
		emebeddedMdr.setCollection(collection.getEmbeddedCollection());
		emebeddedMdr.setId(uuid);
		this.uniqueID = uuid;
		this.collection = collection;
	}
	
	
	/*
	 * Lifecycle methods
	 */

	/**
	 * Called before storing the Decorator into the database. It assigns the
	 * contents of the wrapped UIMEntity to the embeddedbinary byte array.
	 */
	@PrePersist
	void prePersist() {
		fields = MongoDBEuropeanaMDRConverter.getInstance()
				.encode(emebeddedMdr);
	}

	/**
	 * Called after the Decorator is stored into the Database. It assigns the
	 * automatically generated ObjectId string value to the wrapped UIMEntity.
	 */
	//@PostPersist
	//void postPersist() {
	//	if (emebeddedMdr.getId() == null) {
	//		emebeddedMdr.setId(getMongoId().toString());
	//	}
	//}

	/**
	 * Called after retrieving the Decorator via a query. It re-instantiates the
	 * wrapped UIMEntity object from the stored embeddedbinary byte array.
	 */
	@PostLoad
	void preload() {
		emebeddedMdr = MongoDBEuropeanaMDRConverter.getInstance()
				.decode(fields);
		
		if(emebeddedMdr.getId() == null){
			emebeddedMdr.setId(uniqueID);
		}
		
		emebeddedMdr.setCollection(collection.getEmbeddedCollection());
	}

	/**
	 * Auxiliary method that returns the embedded MetaDataRecordBean type of
	 * this Decorator
	 * 
	 * @return the MetaDataRecordBean object
	 */
	public MetaDataRecordBean<String> getEmebeddedMdr() {
		return emebeddedMdr;
	}

	/*
	 * Overridden (decorator-specific) methods
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.UimEntity#getId()
	 */
	@Override
	public String getId() {
		return emebeddedMdr.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.MetaDataRecord#getCollection()
	 */
	@Override
	public Collection<String> getCollection() {
		return emebeddedMdr.getCollection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.store.MetaDataRecord#getFirstValue(eu.europeana.uim.
	 * common.TKey, java.lang.Enum<?>[])
	 */
	@Override
	public <N, T> T getFirstValue(TKey<N, T> key, Enum<?>... qualifiers) {
		return emebeddedMdr.getFirstValue(key, qualifiers);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.store.MetaDataRecord#getFirstQualifiedValue(eu.europeana
	 * .uim.common.TKey, java.lang.Enum<?>[])
	 */
	@Override
	public <N, T> eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T> getFirstQualifiedValue(
			TKey<N, T> key, Enum<?>... qualifiers) {
		return emebeddedMdr.getFirstQualifiedValue(key, qualifiers);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.store.MetaDataRecord#getQualifiedValues(eu.europeana
	 * .uim.common.TKey, java.lang.Enum<?>[])
	 */
	@Override
	public <N, T> List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T>> getQualifiedValues(
			TKey<N, T> key, Enum<?>... qualifiers) {
		return emebeddedMdr.getQualifiedValues(key, qualifiers);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.store.MetaDataRecord#getValues(eu.europeana.uim.common
	 * .TKey, java.lang.Enum<?>[])
	 */
	@Override
	public <N, T> List<T> getValues(TKey<N, T> key, Enum<?>... qualifiers) {
		return emebeddedMdr.getValues(key, qualifiers);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.store.MetaDataRecord#addValue(eu.europeana.uim.common
	 * .TKey, java.lang.Object, java.lang.Enum<?>[])
	 */
	@Override
	public <N, T> void addValue(TKey<N, T> key, T value, Enum<?>... qualifiers) {
		emebeddedMdr.addValue(key, value, qualifiers);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.store.MetaDataRecord#deleteValues(eu.europeana.uim.common
	 * .TKey, java.lang.Enum<?>[])
	 */
	@Override
	public <N, T> List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T>> deleteValues(
			TKey<N, T> key, Enum<?>... qualifiers) {
		return emebeddedMdr.deleteValues(key, qualifiers);
	}

}
