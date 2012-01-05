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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.NotSaved;
import com.google.code.morphia.annotations.PostLoad;
import com.google.code.morphia.annotations.PostPersist;
import com.google.code.morphia.annotations.PreLoad;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Serialized;

import eu.europeana.uim.store.ControlledVocabularyKeyValue;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.bean.ProviderBean;
import eu.europeana.uim.store.mongo.converters.MongoDBCollectionBeanBytesConverter;
import eu.europeana.uim.store.mongo.converters.MongoDBProviderBeanBytesConverter;

/**
 * 
 * @author Georgios Markakis
 */

@Entity
public class MongoProviderDecorator<I> extends MongoAbstractNamedEntity<ObjectId> implements Provider<ObjectId> {
	
	@Serialized
	byte[] embeddedbinary;
	
	@NotSaved
	private ProviderBean<ObjectId> embeddedProvider;
	
	
	@Reference
	private Set<Provider<ObjectId>> searchableRealtedIn;
	
	@Reference
	private Set<Provider<ObjectId>> searchableRealtedOut;
	
	@Indexed
	private String searchName;
	

	
	
	
	public MongoProviderDecorator(){
		embeddedProvider = new ProviderBean<ObjectId>();
		searchableRealtedIn = new HashSet<Provider<ObjectId>>();
		searchableRealtedOut = new HashSet<Provider<ObjectId>>();
	}
	

		
	/**
	 * 
	 */
	
	@PrePersist 
	void prePersist() 
	{
		updaterelated();	
		embeddedbinary = MongoDBProviderBeanBytesConverter.getInstance().encode(embeddedProvider);
	}
	
	@PostPersist
	void postPersist(){
		if(embeddedProvider.getId() == null){
			embeddedProvider.setId(getMongoId());
		}
	}
	

	@PostLoad
	void postload(){
			embeddedProvider = MongoDBProviderBeanBytesConverter.getInstance().decode(embeddedbinary);
	}


	
	
	private void updaterelated(){
		embeddedProvider.getRelatedIn().clear();
		
		for(Provider<ObjectId> p : searchableRealtedIn){
			MongoProviderDecorator<ObjectId> cast = (MongoProviderDecorator<ObjectId>)p;
			embeddedProvider.getRelatedIn().add(cast.getEmbeddedProvider());
		}

		embeddedProvider.getRelatedOut().clear();
		
		for(Provider<ObjectId> p : searchableRealtedOut){
			MongoProviderDecorator<ObjectId> cast = (MongoProviderDecorator<ObjectId>)p;
			embeddedProvider.getRelatedOut().add(cast.getEmbeddedProvider());
		}
	}
	
	
	//Getters & Setters
	
    public ProviderBean<ObjectId> getEmbeddedProvider() {
		return embeddedProvider;
	}
	
	@Override
	public ObjectId getId() {
		return embeddedProvider.getId();
	}

	@Override
	public Set<Provider<ObjectId>> getRelatedOut() {
		return searchableRealtedOut;
	}

	@Override
	public Set<Provider<ObjectId>> getRelatedIn() {
		return searchableRealtedIn; 
	}

	@Override
	public String getMnemonic() {
		return embeddedProvider.getMnemonic();
	}

	@Override
	public void setMnemonic(String mnemonic) {
		embeddedProvider.setMnemonic(mnemonic);
		setSearchMnemonic(mnemonic);
	}

	@Override
	public String getName() {
		return embeddedProvider.getName();
	}

	@Override
	public void setName(String name) {
		embeddedProvider.setName(name);
		this.searchName = name;
		
	}

	@Override
	public void setAggregator(boolean aggregator) {
		embeddedProvider.setAggregator(aggregator);
	}

	@Override
	public boolean isAggregator() {
		return embeddedProvider.isAggregator();
	}

	@Override
	public String getOaiBaseUrl() {
		return embeddedProvider.getOaiBaseUrl();
	}

	@Override
	public void setOaiBaseUrl(String baseUrl) {
		embeddedProvider.setOaiBaseUrl(baseUrl);
		
	}

	@Override
	public String getOaiMetadataPrefix() {
		return embeddedProvider.getOaiMetadataPrefix();
	}

	@Override
	public void setOaiMetadataPrefix(String prefix) {
		embeddedProvider.setOaiMetadataPrefix(prefix);
		
	}

	@Override
	public void putValue(String key, String value) {
		embeddedProvider.putValue(key, value);
		
	}

	@Override
	public String getValue(String key) {
		return embeddedProvider.getValue(key);
	}

	@Override
	public void putValue(ControlledVocabularyKeyValue key, String value) {
		embeddedProvider.putValue(key, value);
		
	}

	@Override
	public String getValue(ControlledVocabularyKeyValue key) {
		return embeddedProvider.getValue(key);
	}

	@Override
	public Map<String, String> values() {
		return embeddedProvider.values();
	}

}
