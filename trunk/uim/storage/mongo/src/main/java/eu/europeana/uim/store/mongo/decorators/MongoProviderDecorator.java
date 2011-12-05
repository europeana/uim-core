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

import java.util.Map;
import java.util.Set;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;

import eu.europeana.uim.store.ControlledVocabularyKeyValue;
import eu.europeana.uim.store.Provider;

/**
 * 
 * @author Georgios Markakis
 */

@Entity
public class MongoProviderDecorator<I>  implements Provider<I> {

	@Embedded
	private Provider<I> embeddedProvider;
	
	
	/**
	 * @return the embeddedProvider
	 */
	public Provider<I> getEmbeddedProvider() {
		return embeddedProvider;
	}


	/**
	 * @param embeddedProvider the embeddedProvider to set
	 */
	public void setEmbeddedProvider(Provider<I> embeddedProvider) {
		this.embeddedProvider = embeddedProvider;
	}


	MongoProviderDecorator(Provider<I> provider){
		this.embeddedProvider = provider;
	}
	
	
	@Override
	public I getId() {

		return embeddedProvider.getId();
	}

	@Override
	public Set<Provider<I>> getRelatedOut() {
		return embeddedProvider.getRelatedOut();
	}

	@Override
	public Set<Provider<I>> getRelatedIn() {
		return embeddedProvider.getRelatedIn();
	}

	@Override
	public String getMnemonic() {
		return embeddedProvider.getMnemonic();
	}

	@Override
	public void setMnemonic(String mnemonic) {
		embeddedProvider.setMnemonic(mnemonic);
		
	}

	@Override
	public String getName() {
		return embeddedProvider.getName();
	}

	@Override
	public void setName(String name) {
		embeddedProvider.setName(name);
		
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
