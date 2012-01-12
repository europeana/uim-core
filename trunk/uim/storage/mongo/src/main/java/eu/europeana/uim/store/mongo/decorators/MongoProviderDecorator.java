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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.NotSaved;
import com.google.code.morphia.annotations.PostLoad;
import com.google.code.morphia.annotations.PostPersist;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Serialized;
import eu.europeana.uim.store.ControlledVocabularyKeyValue;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.bean.ProviderBean;
import eu.europeana.uim.store.mongo.converters.MongoDBProviderBeanBytesConverter;

/**
 * Morphia based MongoDB representation for a Provider object. This object as a
 * decorator to the ProviderBean class guaranteeing thus to a certain extent the
 * compatibility between MongoDB Storage Engine and the Memory Storage Engine
 * 
 * @author Georgios Markakis (gwarkx@hotmail.com)
 * @date Jan 6, 2012
 */
@Entity
public class MongoProviderDecorator<I> extends MongoAbstractNamedEntity<String>
		implements Provider<String> {

	/**
	 * The embeddedProvider object wrapped in this Decorator. It is not saved in
	 * the database but handled by lifecycle methods given the contents of the
	 * stored embeddedbinary object
	 */
	@NotSaved
	private ProviderBean<String> embeddedProvider;

	/**
	 * The contents of the embeddedProvider object stored in a binary form in
	 * MongoDB
	 */
	@Serialized
	byte[] embeddedbinary;

	/**
	 * References to "RelatedIn" MongoDB Provider objects
	 */
	@Reference
	private Set<Provider<String>> searchableRealtedIn;

	/**
	 * References to "RelatedOut" MongoDB Provider objects
	 */
	@Reference
	private Set<Provider<String>> searchableRealtedOut;

	/**
	 * The (indexed & searchable) provider name
	 */
	@Indexed
	private String searchName;

	/**
	 * Default Constructor
	 */
	public MongoProviderDecorator() {
		embeddedProvider = new ProviderBean<String>();
		searchableRealtedIn = new HashSet<Provider<String>>();
		searchableRealtedOut = new HashSet<Provider<String>>();
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
		updaterelated();
		embeddedbinary = MongoDBProviderBeanBytesConverter.getInstance()
				.encode(embeddedProvider);
	}

	/**
	 * Called after the Decorator is stored into the Database. It assigns the
	 * automatically generated ObjectId string value to the wrapped UIMEntity.
	 */
	@PostPersist
	void postPersist() {
		if (embeddedProvider.getId() == null) {
			embeddedProvider.setId(getMongoId().toString());
		}
	}

	/**
	 * Called after retrieving the Decorator via a query. It re-instantiates the
	 * wrapped UIMEntity object from the stored embeddedbinary byte array.
	 */
	@PostLoad
	void postload() {
		embeddedProvider = MongoDBProviderBeanBytesConverter.getInstance()
				.decode(embeddedbinary);
	}

	/**
	 * Auxiliary method for re-populating the relatedIn & relatedOut values of
	 * the embedded object from references
	 */
	private void updaterelated() {
		embeddedProvider.getRelatedIn().clear();
		for (Provider<String> p : searchableRealtedIn) {
			MongoProviderDecorator<String> cast = (MongoProviderDecorator<String>) p;
			embeddedProvider.getRelatedIn().add(cast.getEmbeddedProvider());
		}
		embeddedProvider.getRelatedOut().clear();
		for (Provider<String> p : searchableRealtedOut) {
			MongoProviderDecorator<String> cast = (MongoProviderDecorator<String>) p;
			embeddedProvider.getRelatedOut().add(cast.getEmbeddedProvider());
		}
	}

	/**
	 * Auxiliary method that returns the embedded ProviderBean type of this
	 * Decorator
	 * 
	 * @return the embedded ProviderBean
	 */
	public ProviderBean<String> getEmbeddedProvider() {
		return embeddedProvider;
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
		return embeddedProvider.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Provider#getRelatedOut()
	 */
	@Override
	public Set<Provider<String>> getRelatedOut() {
		return searchableRealtedOut;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Provider#getRelatedIn()
	 */
	@Override
	public Set<Provider<String>> getRelatedIn() {
		return searchableRealtedIn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Provider#getMnemonic()
	 */
	@Override
	public String getMnemonic() {
		return embeddedProvider.getMnemonic();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Provider#setMnemonic(java.lang.String)
	 */
	@Override
	public void setMnemonic(String mnemonic) {
		embeddedProvider.setMnemonic(mnemonic);
		setSearchMnemonic(mnemonic);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Provider#getName()
	 */
	@Override
	public String getName() {
		return embeddedProvider.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Provider#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		embeddedProvider.setName(name);
		this.searchName = name;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Provider#setAggregator(boolean)
	 */
	@Override
	public void setAggregator(boolean aggregator) {
		embeddedProvider.setAggregator(aggregator);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Provider#isAggregator()
	 */
	@Override
	public boolean isAggregator() {
		return embeddedProvider.isAggregator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Provider#getOaiBaseUrl()
	 */
	@Override
	public String getOaiBaseUrl() {
		return embeddedProvider.getOaiBaseUrl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Provider#setOaiBaseUrl(java.lang.String)
	 */
	@Override
	public void setOaiBaseUrl(String baseUrl) {
		embeddedProvider.setOaiBaseUrl(baseUrl);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Provider#getOaiMetadataPrefix()
	 */
	@Override
	public String getOaiMetadataPrefix() {
		return embeddedProvider.getOaiMetadataPrefix();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.store.Provider#setOaiMetadataPrefix(java.lang.String)
	 */
	@Override
	public void setOaiMetadataPrefix(String prefix) {
		embeddedProvider.setOaiMetadataPrefix(prefix);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Provider#putValue(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void putValue(String key, String value) {
		embeddedProvider.putValue(key, value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Provider#getValue(java.lang.String)
	 */
	@Override
	public String getValue(String key) {
		return embeddedProvider.getValue(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Provider#putValue(eu.europeana.uim.store.
	 * ControlledVocabularyKeyValue, java.lang.String)
	 */
	@Override
	public void putValue(ControlledVocabularyKeyValue key, String value) {
		embeddedProvider.putValue(key, value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Provider#getValue(eu.europeana.uim.store.
	 * ControlledVocabularyKeyValue)
	 */
	@Override
	public String getValue(ControlledVocabularyKeyValue key) {
		return embeddedProvider.getValue(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Provider#values()
	 */
	@Override
	public Map<String, String> values() {
		return embeddedProvider.values();
	}

}
