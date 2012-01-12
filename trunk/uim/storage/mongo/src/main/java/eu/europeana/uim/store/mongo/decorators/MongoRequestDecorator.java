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

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.NotSaved;
import com.google.code.morphia.annotations.PostLoad;
import com.google.code.morphia.annotations.PostPersist;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Serialized;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.bean.RequestBean;
import eu.europeana.uim.store.mongo.converters.MongoDBRequestBeanBytesConverter;

/**
 * Morphia based MongoDB representation for a Request object. This object as a
 * decorator to the RequestBean class guaranteeing thus to a certain extent the
 * compatibility between MongoDB Storage Engine and the Memory Storage Engine
 * 
 * @author Georgios Markakis (gwarkx@hotmail.com)
 * @date Jan 12, 2012
 * @see RequestBean
 */
@Entity
public class MongoRequestDecorator<I> extends MongoAbstractEntity<String>
		implements Request<String> {

	/**
	 * The embeddedRequest object wrapped in this Decorator. It is not saved in
	 * the database but handled by lifecycle methods given the contents of the
	 * stored embeddedbinary object
	 */
	@NotSaved
	private RequestBean<String> embeddedRequest;

	/**
	 * The contents of the embeddedProvider object stored in a binary form in
	 * MongoDB
	 */
	@Serialized
	byte[] embeddedbinary;

	/**
	 * The indexed and searchable searchDate object
	 */
	@Indexed
	private Date searchDate;

	/**
	 * A searchable reference to the requests related Collection (not the object
	 * itself)
	 */
	@Reference
	private MongoCollectionDecorator<String> collection;

	/**
	 * A searchable reference to the request's related Records
	 */
	@Reference
	private HashSet<MongoMetadataRecordDecorator<String>> requestrecords;

	/**
	 * The default constructor (required by Morphia but not used in this
	 * implementation)
	 */
	public MongoRequestDecorator() {
	}

	/**
	 * The constructor actually used by this implementation
	 * 
	 * @param collection
	 *            the associated collection
	 */
	public MongoRequestDecorator(MongoCollectionDecorator<String> collection,
			Date date) {
		this.searchDate = date;
		this.embeddedRequest = new RequestBean<String>();
		this.embeddedRequest.setDate(date);
		this.embeddedRequest.setCollection(collection.getEmbeddedCollection());
		this.collection = collection;
		this.requestrecords = new HashSet<MongoMetadataRecordDecorator<String>>();
	}

	/*
	 * Lifecycle Methods
	 */

	/**
	 * Called before storing the Decorator into the database. It assigns the
	 * contents of the wrapped UIMEntity to the embeddedbinary byte array.
	 */
	@PrePersist
	void prePersist() {
		embeddedbinary = MongoDBRequestBeanBytesConverter.getInstance().encode(
				embeddedRequest);
	}

	/**
	 * Called after the Decorator is stored into the Database. It assigns the
	 * automatically generated ObjectId string value to the wrapped UIMEntity.
	 */
	@PostPersist
	void postPersist() {
		if (embeddedRequest.getId() == null) {
			embeddedRequest.setId(getMongoId().toString());
		}
	}

	/**
	 * Called after retrieving the Decorator via a query. It re-instantiates the
	 * wrapped UIMEntity object from the stored embeddedbinary byte array.
	 */
	@PostLoad
	void postload() {
		embeddedRequest = MongoDBRequestBeanBytesConverter.getInstance()
				.decode(embeddedbinary);
		embeddedRequest.setCollection(collection.getEmbeddedCollection());
		embeddedRequest.setId(getMongoId().toString());
	}

	/**
	 * Auxiliary method that returns the embedded MongoCollectionDecorator type
	 * of this Decorator
	 * 
	 * @return the embedded MongoCollectionDecorator
	 */
	public MongoCollectionDecorator<String> getCollectionReference() {
		return collection;
	}

	/**
	 * @param requestrecords
	 *            the requestrecords to set
	 */
	public void setRequestrecords(
			HashSet<MongoMetadataRecordDecorator<String>> requestrecords) {
		this.requestrecords = requestrecords;
	}

	/**
	 * @return the requestrecords
	 */
	public HashSet<MongoMetadataRecordDecorator<String>> getRequestrecords() {
		return requestrecords;
	}

	/**
	 * Auxiliary method that returns the embedded RequestBean type of this
	 * Decorator
	 * 
	 * @return the embedded ProviderBean
	 */
	public RequestBean<String> getEmbeddedRequest() {
		return embeddedRequest;
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
		return embeddedRequest.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Request#getCollection()
	 */
	@Override
	public Collection<String> getCollection() {
		return collection; // embeddedRequest.getCollection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Request#getDate()
	 */
	@Override
	public Date getDate() {
		return embeddedRequest.getDate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Request#setDataFrom(java.util.Date)
	 */
	@Override
	public void setDataFrom(Date from) {
		embeddedRequest.setDataFrom(from);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Request#getDataFrom()
	 */
	@Override
	public Date getDataFrom() {
		return embeddedRequest.getDataFrom();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Request#setDataTill(java.util.Date)
	 */
	@Override
	public void setDataTill(Date till) {
		embeddedRequest.setDataTill(till);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Request#getDataTill()
	 */
	@Override
	public Date getDataTill() {
		return embeddedRequest.getDataTill();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Request#setFailed(boolean)
	 */
	@Override
	public void setFailed(boolean failed) {
		embeddedRequest.setFailed(failed);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Request#isFailed()
	 */
	@Override
	public boolean isFailed() {
		return embeddedRequest.isFailed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Request#putValue(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void putValue(String key, String value) {
		embeddedRequest.putValue(key, value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Request#getValue(java.lang.String)
	 */
	@Override
	public String getValue(String key) {
		return embeddedRequest.getValue(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Request#values()
	 */
	@Override
	public Map<String, String> values() {
		return embeddedRequest.values();
	}

}
