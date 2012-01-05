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
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.store.bean.ExecutionBean;
import eu.europeana.uim.store.mongo.converters.MongoDBExecutionBeanBytesConverter;
import eu.europeana.uim.store.mongo.converters.MongoDBRequestBeanBytesConverter;

/**
 * 
 * @author Georgios Markakis
 */
@Entity
public class MongoExecutionDecorator<I> extends MongoAbstractEntity<ObjectId> implements Execution<ObjectId> {

	/**
	 * 
	 */
	@NotSaved
	private ExecutionBean<ObjectId> embeddedExecution;
	
	@Serialized
	byte[] embeddedbinary;
		
	@Reference
	private UimDataSet<ObjectId> datasetRefrerence;
	
	
    public MongoExecutionDecorator(){
    	this.embeddedExecution = new ExecutionBean<ObjectId>();
    }
    
    
    public MongoExecutionDecorator(ObjectId id){
    	this.embeddedExecution = new ExecutionBean<ObjectId>(id);
    }
    
    
	@PrePersist 
	void prePersist() 
	{
		embeddedbinary = MongoDBExecutionBeanBytesConverter.getInstance().encode(embeddedExecution);
		
	}
	
	@PostPersist
	void postPersist(){
		if(embeddedExecution.getId() == null){
			embeddedExecution.setId(getMongoId());
		}
	}
	
	
	@PostLoad
	void postLoad(){
		embeddedExecution = MongoDBExecutionBeanBytesConverter.getInstance().decode(embeddedbinary);
		if(datasetRefrerence instanceof MongoCollectionDecorator){
			MongoCollectionDecorator<ObjectId> tmp = (MongoCollectionDecorator<ObjectId>)datasetRefrerence;
			embeddedExecution.setDataSet(tmp.getEmbeddedCollection());
		}
		else if(datasetRefrerence instanceof MongoMetadataRecordDecorator){
			MongoMetadataRecordDecorator<ObjectId> tmp = (MongoMetadataRecordDecorator<ObjectId>)datasetRefrerence;
			embeddedExecution.setDataSet(tmp.getEmebeddedMdr());
		}
		else if(datasetRefrerence instanceof MongoRequestDecorator){
			MongoRequestDecorator<ObjectId> tmp = (MongoRequestDecorator<ObjectId>)datasetRefrerence;
			embeddedExecution.setDataSet(tmp.getEmbeddedRequest());
		}
	}
    
    
    
    
	//Getters & Setters
    
    public ExecutionBean<ObjectId> getEmbeddedExecution() {
		return embeddedExecution;
	}
    
	@Override
	public ObjectId getId() {
		return embeddedExecution.getId();
	}

	@Override
	public String getName() {
		return embeddedExecution.getName();
	}

	@Override
	public void setName(String name) {
		embeddedExecution.setName(name);
	}

	@Override
	public String getWorkflow() {
		return embeddedExecution.getWorkflow();
	}

	@Override
	public void setWorkflow(String identifier) {
		embeddedExecution.setWorkflow(identifier);
	}

	@Override
	public UimDataSet<ObjectId> getDataSet() {
		return embeddedExecution.getDataSet();
	}

	@Override
	public void setDataSet(UimDataSet<ObjectId> dataSet) {
		
		if(dataSet instanceof MongoCollectionDecorator){
			MongoCollectionDecorator<ObjectId> tmp = (MongoCollectionDecorator<ObjectId>)dataSet;
			embeddedExecution.setDataSet(tmp.getEmbeddedCollection());
		}
		else if(dataSet instanceof MongoMetadataRecordDecorator){
			MongoMetadataRecordDecorator<ObjectId> tmp = (MongoMetadataRecordDecorator<ObjectId>)dataSet;
			embeddedExecution.setDataSet(tmp.getEmebeddedMdr());
		}
		else if(dataSet instanceof MongoRequestDecorator){
			MongoRequestDecorator<ObjectId> tmp = (MongoRequestDecorator<ObjectId>)dataSet;
			embeddedExecution.setDataSet(tmp.getEmbeddedRequest());
		}
		
		this.datasetRefrerence = dataSet;
	}

	@Override
	public boolean isActive() {
		return embeddedExecution.isActive();
	}

	@Override
	public void setActive(boolean active) {
		embeddedExecution.setActive(active);
		
	}

	@Override
	public Date getStartTime() {
		return embeddedExecution.getStartTime();
	}

	@Override
	public void setStartTime(Date start) {
		embeddedExecution.setStartTime(start);
	}

	@Override
	public Date getEndTime() {
		return embeddedExecution.getEndTime();
	}

	@Override
	public void setEndTime(Date end) {
		embeddedExecution.setEndTime(end);
	}

	@Override
	public boolean isCanceled() {
		return embeddedExecution.isCanceled();
	}

	@Override
	public void setCanceled(boolean canceled) {
		embeddedExecution.setCanceled(canceled);
		
	}

	@Override
	public int getSuccessCount() {
		return embeddedExecution.getSuccessCount();
	}

	@Override
	public void setSuccessCount(int number) {
		embeddedExecution.setSuccessCount(number);
	}

	@Override
	public int getFailureCount() {
		return embeddedExecution.getFailureCount();
	}

	@Override
	public void setFailureCount(int number) {
		embeddedExecution.setFailureCount(number);
		
	}

	@Override
	public int getProcessedCount() {
		return embeddedExecution.getProcessedCount();
	}

	@Override
	public void setProcessedCount(int number) {
		embeddedExecution.setProcessedCount(number);
		
	}

	@Override
	public void putValue(String key, String value) {
		embeddedExecution.putValue(key, value);
		
	}

	@Override
	public String getValue(String key) {
		return embeddedExecution.getValue(key);
	}

	@Override
	public void putValue(ControlledVocabularyKeyValue key, String value) {
		embeddedExecution.putValue(key, value);
		
	}

	@Override
	public String getValue(ControlledVocabularyKeyValue key) {
		return embeddedExecution.getValue(key);
	}

	@Override
	public Map<String, String> values() {
		return embeddedExecution.values();
	}

	@Override
	public String getLogFile() {
		return embeddedExecution.getLogFile();
	}

	@Override
	public void setLogFile(String logfile) {
		embeddedExecution.setLogFile(logfile);
	}

}
