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

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.NotSaved;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Serialized;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.bean.RequestBean;

/**
 * 
 * @author Georgios Markakis
 */

@Entity
public class MongoRequestDecorator<I> implements Request<ObjectId> {

	/**
	 * 
	 */
	@NotSaved
	private RequestBean<ObjectId> embeddedRequest; 
	
	@Serialized
	byte[] embeddedbinary;
	
	@Id
    private ObjectId mongoid;
		
	@Indexed
	private Date searchDate;
	
    @Reference
    private MongoCollectionDecorator<ObjectId> collection;
    
    
    @Reference
    private HashSet<MongoMetadataRecordDecorator<ObjectId>> requestrecords;
    



	public MongoRequestDecorator(){
	}
	
	/**
	 * @param request
	 */
	public MongoRequestDecorator(MongoCollectionDecorator<ObjectId> collection, Date date){
		this.searchDate = date;
		this.requestrecords = new HashSet<MongoMetadataRecordDecorator<ObjectId>>();
		this.collection = collection;
	}
	

	
	
	
	
	//Getters & Setters
	
	public MongoCollectionDecorator<ObjectId> getCollectionReference(){
		return this.collection;
	}
	
	
	/**
	 * @param requestrecords the requestrecords to set
	 */
	public void setRequestrecords(HashSet<MongoMetadataRecordDecorator<ObjectId>> requestrecords) {
		this.requestrecords = requestrecords;
	}

	/**
	 * @return the requestrecords
	 */
	public HashSet<MongoMetadataRecordDecorator<ObjectId>> getRequestrecords() {
		return requestrecords;
	}
	
	
	
    public RequestBean<ObjectId> getEmbeddedRequest() {
		return embeddedRequest;
	}
	
	
	@Override
	public  ObjectId getId() {
		return embeddedRequest.getId();
	}

	@Override
	public Collection<ObjectId> getCollection() {
		return  collection; // embeddedRequest.getCollection();
	}

	@Override
	public Date getDate() {
		return embeddedRequest.getDate();
	}

	@Override
	public void setDataFrom(Date from) {
		embeddedRequest.setDataFrom(from);
	}

	@Override
	public Date getDataFrom() {
		return embeddedRequest.getDataFrom();
	}

	@Override
	public void setDataTill(Date till) {
		embeddedRequest.setDataTill(till);
		
	}

	@Override
	public Date getDataTill() {
		return embeddedRequest.getDataTill();
	}

	@Override
	public void setFailed(boolean failed) {
		embeddedRequest.setFailed(failed); 
		
	}

	@Override
	public boolean isFailed() {
		return embeddedRequest.isFailed();
	}

	@Override
	public void putValue(String key, String value) {
		embeddedRequest.putValue(key, value);
		
	}

	@Override
	public String getValue(String key) {
		return embeddedRequest.getValue(key);
	}

	@Override
	public Map<String, String> values() {
		return embeddedRequest.values();
	}

}
