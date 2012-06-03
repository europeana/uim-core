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
import com.google.code.morphia.annotations.NotSaved;
import com.google.code.morphia.annotations.PostLoad;
import com.google.code.morphia.annotations.PostPersist;
import com.google.code.morphia.annotations.PrePersist;
import com.google.code.morphia.annotations.Reference;
import com.google.code.morphia.annotations.Serialized;
import eu.europeana.uim.store.ControlledVocabularyKeyValue;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.store.bean.ExecutionBean;
import eu.europeana.uim.store.mongo.converters.MongoDBExecutionBeanBytesConverter;

/**
 * Morphia based MongoDB representation for a Execution object. This object as a
 * decorator to the ExecutionBean class guaranteeing thus to a certain extent
 * the compatibility between MongoDB Storage Engine and the Memory Storage
 * Engine
 * 
 * @author Georgios Markakis (gwarkx@hotmail.com)
 * @date Jan 12, 2012
 * @see ExecutionBean
 */
@Entity
public class MongoExecutionDecorator<I> extends MongoAbstractEntity<String>
		implements Execution<String> {

	/**
	 * The embeddedExecution object wrapped in this Decorator. It is not saved
	 * in the database but handled by lifecycle methods given the contents of
	 * the stored embeddedbinary object
	 */
	@NotSaved
	private ExecutionBean<String> embeddedExecution;

	/**
	 * The contents of the embeddedExecution object stored in a binary form in
	 * MongoDB
	 */
	@Serialized
	byte[] embeddedbinary;

	/**
	 * A searchable reference to the UimDataSet related to this Execution
	 */
	@Reference
	private UimDataSet<String> datasetRefrerence;

	/**
	 * The default constructor (required by Morphia but not used in this
	 * implementation)
	 */
	public MongoExecutionDecorator() {
		this.embeddedExecution = new ExecutionBean<String>();
	}

	/**
	 * The constructor actually used by this implementation
	 * 
	 * @param id
	 *            the Execution id
	 */
	public MongoExecutionDecorator(String id) {
		this.embeddedExecution = new ExecutionBean<String>(id);
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
		embeddedbinary = MongoDBExecutionBeanBytesConverter.getInstance()
				.encode(embeddedExecution);

	}

	/**
	 * Called after the Decorator is stored into the Database. It assigns the
	 * automatically generated ObjectId string value to the wrapped UIMEntity.
	 */
	@PostPersist
	void postPersist() {
		if (embeddedExecution.getId() == null) {
			embeddedExecution.setId(getMongoId().toString());
		}
	}

	/**
	 * Called after retrieving the Decorator via a query. It re-instantiates the
	 * wrapped UIMEntity object from the stored embeddedbinary byte array.
	 */
	@PostLoad
	void postLoad() {
		embeddedExecution = MongoDBExecutionBeanBytesConverter.getInstance()
				.decode(embeddedbinary);
		if (datasetRefrerence instanceof MongoCollectionDecorator) {
			MongoCollectionDecorator<String> tmp = (MongoCollectionDecorator<String>) datasetRefrerence;
			embeddedExecution.setDataSet(tmp.getEmbeddedCollection());
		} else if (datasetRefrerence instanceof MongoMetadataRecordDecorator) {
			MongoMetadataRecordDecorator<String> tmp = (MongoMetadataRecordDecorator<String>) datasetRefrerence;
			embeddedExecution.setDataSet(tmp.getEmebeddedMdr());
		} else if (datasetRefrerence instanceof MongoRequestDecorator) {
			MongoRequestDecorator<String> tmp = (MongoRequestDecorator<String>) datasetRefrerence;
			embeddedExecution.setDataSet(tmp.getEmbeddedRequest());
		}
	}

	/**
	 * Auxiliary method that returns the embedded ExecutionBean type of this
	 * Decorator
	 * 
	 * @return the ExecutionBean object
	 */
	public ExecutionBean<String> getEmbeddedExecution() {
		return embeddedExecution;
	}

	/**
	 * Auxiliary method that sets the embedded ExecutionBean type of this
	 * Decorator
	 * @param exbean the ExecutionBean object
	 */
	public void setEmbeddedExecution(ExecutionBean<String> exbean){
		this.embeddedExecution = exbean;
	}
	
	
	/**
	 * @return the datasetRefrerence
	 */
	public UimDataSet<String> getDatasetRefrerence() {
		return datasetRefrerence;
	}

	/**
	 * @param datasetRefrerence the datasetRefrerence to set
	 */
	public void setDatasetRefrerence(UimDataSet<String> datasetRefrerence) {
		this.datasetRefrerence = datasetRefrerence;
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
		return embeddedExecution.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#getName()
	 */
	@Override
	public String getName() {
		return embeddedExecution.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		embeddedExecution.setName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#getWorkflow()
	 */
	@Override
	public String getWorkflow() {
		return embeddedExecution.getWorkflow();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#setWorkflow(java.lang.String)
	 */
	@Override
	public void setWorkflow(String identifier) {
		embeddedExecution.setWorkflow(identifier);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#getDataSet()
	 */
	@Override
	public UimDataSet<String> getDataSet() {
		return embeddedExecution.getDataSet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.store.Execution#setDataSet(eu.europeana.uim.store.UimDataSet
	 * )
	 */
	@Override
	public void setDataSet(UimDataSet<String> dataSet) {

		if (dataSet instanceof MongoCollectionDecorator) {
			MongoCollectionDecorator<String> tmp = (MongoCollectionDecorator<String>) dataSet;
			embeddedExecution.setDataSet(tmp.getEmbeddedCollection());
		} else if (dataSet instanceof MongoMetadataRecordDecorator) {
			MongoMetadataRecordDecorator<String> tmp = (MongoMetadataRecordDecorator<String>) dataSet;
			embeddedExecution.setDataSet(tmp.getEmebeddedMdr());
		} else if (dataSet instanceof MongoRequestDecorator) {
			MongoRequestDecorator<String> tmp = (MongoRequestDecorator<String>) dataSet;
			embeddedExecution.setDataSet(tmp.getEmbeddedRequest());
		}

		this.datasetRefrerence = dataSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#isActive()
	 */
	@Override
	public boolean isActive() {
		return embeddedExecution.isActive();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#setActive(boolean)
	 */
	@Override
	public void setActive(boolean active) {
		embeddedExecution.setActive(active);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#getStartTime()
	 */
	@Override
	public Date getStartTime() {
		return embeddedExecution.getStartTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#setStartTime(java.util.Date)
	 */
	@Override
	public void setStartTime(Date start) {
		embeddedExecution.setStartTime(start);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#getEndTime()
	 */
	@Override
	public Date getEndTime() {
		return embeddedExecution.getEndTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#setEndTime(java.util.Date)
	 */
	@Override
	public void setEndTime(Date end) {
		embeddedExecution.setEndTime(end);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#isCanceled()
	 */
	@Override
	public boolean isCanceled() {
		return embeddedExecution.isCanceled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#setCanceled(boolean)
	 */
	@Override
	public void setCanceled(boolean canceled) {
		embeddedExecution.setCanceled(canceled);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#getSuccessCount()
	 */
	@Override
	public int getSuccessCount() {
		return embeddedExecution.getSuccessCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#setSuccessCount(int)
	 */
	@Override
	public void setSuccessCount(int number) {
		embeddedExecution.setSuccessCount(number);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#getFailureCount()
	 */
	@Override
	public int getFailureCount() {
		return embeddedExecution.getFailureCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#setFailureCount(int)
	 */
	@Override
	public void setFailureCount(int number) {
		embeddedExecution.setFailureCount(number);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#getProcessedCount()
	 */
	@Override
	public int getProcessedCount() {
		return embeddedExecution.getProcessedCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#setProcessedCount(int)
	 */
	@Override
	public void setProcessedCount(int number) {
		embeddedExecution.setProcessedCount(number);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#putValue(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void putValue(String key, String value) {
		embeddedExecution.putValue(key, value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#getValue(java.lang.String)
	 */
	@Override
	public String getValue(String key) {
		return embeddedExecution.getValue(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#putValue(eu.europeana.uim.store.
	 * ControlledVocabularyKeyValue, java.lang.String)
	 */
	@Override
	public void putValue(ControlledVocabularyKeyValue key, String value) {
		embeddedExecution.putValue(key, value);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#getValue(eu.europeana.uim.store.
	 * ControlledVocabularyKeyValue)
	 */
	@Override
	public String getValue(ControlledVocabularyKeyValue key) {
		return embeddedExecution.getValue(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#values()
	 */
	@Override
	public Map<String, String> values() {
		return embeddedExecution.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#getLogFile()
	 */
	@Override
	public String getLogFile() {
		return embeddedExecution.getLogFile();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.store.Execution#setLogFile(java.lang.String)
	 */
	@Override
	public void setLogFile(String logfile) {
		embeddedExecution.setLogFile(logfile);
	}

}
