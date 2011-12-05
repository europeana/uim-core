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

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Entity;

import eu.europeana.uim.store.ControlledVocabularyKeyValue;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.UimDataSet;

/**
 * 
 * @author Georgios Markakis
 */
@Entity
public class MongoExecutionDecorator<I> implements Execution<I> {

	@Embedded
	private Execution<I> embeddedExecution;
	
	@Override
	public I getId() {
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
	public UimDataSet<I> getDataSet() {
		return embeddedExecution.getDataSet();
	}

	@Override
	public void setDataSet(UimDataSet<I> dataSet) {
		embeddedExecution.setDataSet(dataSet);
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
