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

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Serialized;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.ControlledVocabularyKeyValue;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.bean.CollectionBean;

/**
 * 
 * @author Georgios Markakis
 */

@Entity
public class MongoCollectionDecorator<I> implements Collection<I>{

	@Serialized
	private CollectionBean<I> embeddedCollection;
	
	@Reference
	private  MongoProviderDecorator<I> provider;
	
	@Indexed
	private String searchMnemonic;
	
    @Id
    private ObjectId mongoId;
	
	@Indexed
	private Long lid;
	
	public MongoCollectionDecorator(){
		this.embeddedCollection = new CollectionBean<I>();	
	}
	
	public MongoCollectionDecorator(I id, Provider<I> provider){
		MongoProviderDecorator<I> prov = (MongoProviderDecorator<I>)provider;
		this.lid = (Long)id;
		this.embeddedCollection = new CollectionBean<I>(id,prov.getEmbeddedProvider());
		this.provider = (MongoProviderDecorator<I>) provider;
	}
	
	
    public CollectionBean<I> getEmbeddedCollection() {
		return embeddedCollection;
	}
	
	@Override
	public I getId() {
		return embeddedCollection.getId();
	}

	@Override
	public Provider<I> getProvider() {
		return embeddedCollection.getProvider();
	}

	@Override
	public String getMnemonic() {
		return embeddedCollection.getMnemonic();
	}

	@Override
	public void setMnemonic(String mnemonic) {
		embeddedCollection.setMnemonic(mnemonic);
		this.searchMnemonic = mnemonic;
	}

	@Override
	public String getName() {
		return embeddedCollection.getName();
	}

	@Override
	public void setName(String name) {
		embeddedCollection.setName(name);
	}

	@Override
	public String getLanguage() {
		return embeddedCollection.getLanguage();
	}

	@Override
	public void setLanguage(String language) {
		embeddedCollection.setLanguage(language);
		
	}

	@Override
	public String getOaiBaseUrl(boolean fallback) {
		return embeddedCollection.getOaiBaseUrl(fallback);
	}

	@Override
	public void setOaiBaseUrl(String baseUrl) {
		embeddedCollection.setOaiBaseUrl(baseUrl);
	}

	@Override
	public String getOaiSet() {
		return embeddedCollection.getOaiSet();
	}

	@Override
	public void setOaiSet(String set) {
		embeddedCollection.setOaiSet(set);
	}

	@Override
	public String getOaiMetadataPrefix(boolean fallback) {
		return embeddedCollection.getOaiMetadataPrefix(fallback);
	}

	@Override
	public void setOaiMetadataPrefix(String prefix) {
		embeddedCollection.setOaiMetadataPrefix(prefix);
	}

	@Override
	public Date getLastModified() {
		return embeddedCollection.getLastModified();
	}

	@Override
	public void setLastModified(Date date) {
		embeddedCollection.setLastModified(date);
	}

	@Override
	public Date getLastSynchronized() {
		return embeddedCollection.getLastSynchronized();
	}

	@Override
	public void setLastSynchronized(Date date) {
		embeddedCollection.setLastSynchronized(date);
	}

	@Override
	public void putValue(String key, String value) {
		embeddedCollection.putValue(key, value);
	}

	@Override
	public String getValue(String key) {
		return embeddedCollection.getValue(key);
	}

	@Override
	public void putValue(ControlledVocabularyKeyValue key, String value) {
		embeddedCollection.putValue(key, value);
	}

	@Override
	public String getValue(ControlledVocabularyKeyValue key) {
		return embeddedCollection.getValue(key);
	}

	@Override
	public Map<String, String> values() {
		return embeddedCollection.values();
	}

}
