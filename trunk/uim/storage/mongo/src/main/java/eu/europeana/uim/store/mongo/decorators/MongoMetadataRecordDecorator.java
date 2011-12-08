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

import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Serialized;

import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.bean.MetaDataRecordBean;

/**
 * 
 * @author Georgios Markakis
 */

@Entity
public class MongoMetadataRecordDecorator <I> implements MetaDataRecord<I> {

	@Serialized
	private MetaDataRecordBean<I> emebeddedMdr;
	
	@Id
    private ObjectId mongoId;
	
	@Indexed
	private Long lid;
	
    @Reference
    private MongoCollectionDecorator<I> collection;
	
    
	public MongoMetadataRecordDecorator (){
		this.emebeddedMdr = new MetaDataRecordBean<I>();
	}
	
	public MongoMetadataRecordDecorator(I id, MongoCollectionDecorator<I> collection){
		this.lid = (Long)id;
		this.emebeddedMdr = new MetaDataRecordBean<I>(id,collection.getEmbeddedCollection());
		this.collection = collection;
	}
	
	
    /**
	 * @return the emebeddedMdr
	 */
	public MetaDataRecordBean<I> getEmebeddedMdr() {
		return emebeddedMdr;
	}
	
	@Override
	public I getId() {
		return emebeddedMdr.getId();
	}

	@Override
	public Collection<I> getCollection() {
		return emebeddedMdr.getCollection();
	}

	@Override
	public <N, T> T getFirstValue(TKey<N, T> key, Enum<?>... qualifiers) {
		return emebeddedMdr.getFirstValue(key, qualifiers);
	}

	@Override
	public <N, T> eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T> getFirstQualifiedValue(
			TKey<N, T> key, Enum<?>... qualifiers) {
		return emebeddedMdr.getFirstQualifiedValue(key, qualifiers);
	}

	@Override
	public <N, T> List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T>> getQualifiedValues(
			TKey<N, T> key, Enum<?>... qualifiers) {
		return emebeddedMdr.getQualifiedValues(key, qualifiers);
	}

	@Override
	public <N, T> List<T> getValues(TKey<N, T> key, Enum<?>... qualifiers) {
		return emebeddedMdr.getValues(key, qualifiers);
	}

	@Override
	public <N, T> void addValue(TKey<N, T> key, T value, Enum<?>... qualifiers) {
		emebeddedMdr.addValue(key, value, qualifiers);
		
	}

	@Override
	public <N, T> List<eu.europeana.uim.store.MetaDataRecord.QualifiedValue<T>> deleteValues(
			TKey<N, T> key, Enum<?>... qualifiers) {
		return emebeddedMdr.deleteValues(key, qualifiers);
	}

}
