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
import eu.europeana.uim.store.ControlledVocabularyKeyValue;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.bean.CollectionBean;
import eu.europeana.uim.store.mongo.converters.MongoDBCollectionBeanBytesConverter;

/**
 * Morphia based MongoDB representation for a Collection object. This object as
 * a decorator to the CollectionBean class guaranteeing thus to a certain extent
 * the compatibility between MongoDB Storage Engine and the Memory Storage
 * Engine
 * 
 * @author Georgios Markakis (gwarkx@hotmail.com)
 * @date Jan 12, 2012
 * @see CollectionBean
 */
@Entity
public class MongoCollectionDecorator<I> extends
		MongoAbstractNamedEntity<String> implements Collection<String> {

	/**
	 * The embeddedCollection object wrapped in this Decorator. It is not saved
	 * in the database but handled by lifecycle methods given the contents of
	 * the stored embeddedbinary object
	 */
	@NotSaved
	private CollectionBean<String> embeddedCollection;

	/**
	 * The contents of the embeddedCollection object stored in a binary form in
	 * MongoDB
	 */
	@Serialized
	byte[] embeddedbinary;

	/**
	 * A searchable reference to the collection's Provider (not the object
	 * itself)
	 */
	@Reference(lazy = true)
	private MongoProviderDecorator<String> provider;

	/**
	 * The (searchable & indexed) provider name stored in Mongodb
	 */
	@Indexed
	private String searchName;

	/**
	 * The default constructor (required by Morphia but not used in this 
	 * implementation)
	 */
	public MongoCollectionDecorator() {
	}

	/**
	 * The constructor actually used by this implementation
	 * @param provider the Collection's provider
	 */
	public MongoCollectionDecorator(Provider<String> provider) {
		MongoProviderDecorator<String> provider2 = (MongoProviderDecorator<String>) provider;
		embeddedCollection = new CollectionBean<String>();
		embeddedCollection.setProvider(provider2.getEmbeddedProvider());
		this.provider = (MongoProviderDecorator<String>) provider;
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
		embeddedbinary = MongoDBCollectionBeanBytesConverter.getInstance()
				.encode(embeddedCollection);
	}

	/**
	 * Called after the Decorator is stored into the Database. It assigns the
	 * automatically generated ObjectId string value to the wrapped UIMEntity.
	 */
	@PostPersist
	void postPersist() {
		if (embeddedCollection.getId() == null) {
			embeddedCollection.setId(getMongoId().toString());
		}
	}

	/**
	 * Called after retrieving the Decorator via a query. It re-instantiates the
	 * wrapped UIMEntity object from the stored embeddedbinary byte array.
	 */
	@PostLoad
	void postLoad() {
		embeddedCollection = MongoDBCollectionBeanBytesConverter.getInstance()
				.decode(embeddedbinary);
		embeddedCollection.setProvider(provider.getEmbeddedProvider());

	}

	/**
	 * Auxiliary method that returns the embedded CollectionBean type of this
	 * Decorator
	 * 
	 * @return the CollectionBean object
	 */
	public CollectionBean<String> getEmbeddedCollection() {
		return embeddedCollection;
	}

	
	
	/**
	 * @param collection
	 */
	public void setEmbeddedCollection(CollectionBean<String> collection) {
		this.embeddedCollection = collection;
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
		return embeddedCollection.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#getProvider()
	 */
	@Override
	public Provider<String> getProvider() {
		return embeddedCollection.getProvider();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#getMnemonic()
	 */
	@Override
	public String getMnemonic() {
		return embeddedCollection.getMnemonic();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#setMnemonic(java.lang.String)
	 */
	@Override
	public void setMnemonic(String mnemonic) {
		embeddedCollection.setMnemonic(mnemonic);
		setSearchMnemonic(mnemonic);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#getName()
	 */
	@Override
	public String getName() {
		return embeddedCollection.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		embeddedCollection.setName(name);
		this.searchName = name;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#getLanguage()
	 */
	@Override
	public String getLanguage() {
		return embeddedCollection.getLanguage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#setLanguage(java.lang.String)
	 */
	@Override
	public void setLanguage(String language) {
		embeddedCollection.setLanguage(language);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#getOaiBaseUrl(boolean)
	 */
	@Override
	public String getOaiBaseUrl(boolean fallback) {
		return embeddedCollection.getOaiBaseUrl(fallback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#setOaiBaseUrl(java.lang.String)
	 */
	@Override
	public void setOaiBaseUrl(String baseUrl) {
		embeddedCollection.setOaiBaseUrl(baseUrl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#getOaiSet()
	 */
	@Override
	public String getOaiSet() {
		return embeddedCollection.getOaiSet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#setOaiSet(java.lang.String)
	 */
	@Override
	public void setOaiSet(String set) {
		embeddedCollection.setOaiSet(set);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#getOaiMetadataPrefix(boolean)
	 */
	@Override
	public String getOaiMetadataPrefix(boolean fallback) {
		return embeddedCollection.getOaiMetadataPrefix(fallback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.store.Collection#setOaiMetadataPrefix(java.lang.String)
	 */
	@Override
	public void setOaiMetadataPrefix(String prefix) {
		embeddedCollection.setOaiMetadataPrefix(prefix);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#getLastModified()
	 */
	@Override
	public Date getLastModified() {
		return embeddedCollection.getLastModified();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#setLastModified(java.util.Date)
	 */
	@Override
	public void setLastModified(Date date) {
		embeddedCollection.setLastModified(date);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#getLastSynchronized()
	 */
	@Override
	public Date getLastSynchronized() {
		return embeddedCollection.getLastSynchronized();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.store.Collection#setLastSynchronized(java.util.Date)
	 */
	@Override
	public void setLastSynchronized(Date date) {
		embeddedCollection.setLastSynchronized(date);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#putValue(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void putValue(String key, String value) {
		embeddedCollection.putValue(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#getValue(java.lang.String)
	 */
	@Override
	public String getValue(String key) {
		return embeddedCollection.getValue(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#putValue(eu.europeana.uim.store.
	 * ControlledVocabularyKeyValue, java.lang.String)
	 */
	@Override
	public void putValue(ControlledVocabularyKeyValue key, String value) {
		embeddedCollection.putValue(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#getValue(eu.europeana.uim.store.
	 * ControlledVocabularyKeyValue)
	 */
	@Override
	public String getValue(ControlledVocabularyKeyValue key) {
		return embeddedCollection.getValue(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Collection#values()
	 */
	@Override
	public Map<String, String> values() {
		return embeddedCollection.values();
	}

}
