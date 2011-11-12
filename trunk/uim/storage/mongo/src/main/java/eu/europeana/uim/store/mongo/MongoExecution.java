package eu.europeana.uim.store.mongo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

import eu.europeana.uim.store.ControlledVocabularyKeyValue;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.UimDataSet;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
@Entity
public class MongoExecution extends AbstractMongoEntity<Long> implements Execution<Long> {
    private boolean       isActive;
    private Date          startTime;
    private Date          endTime;
    private boolean       canceled;
    private String        workflowIdentifier;
    private int           success;
    private int           failure;
    private int           processed;

    private Map<String, String> values = new HashMap<String, String>();

    @Reference
    private UimDataSet<Long> dataSet;

    /**
     * Creates a new instance of this class.
     */
    public MongoExecution() {
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param id
     */
    public MongoExecution(Long id) {
        super(id);
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public Date getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public UimDataSet<Long> getDataSet() {
        return dataSet;
    }

    @Override
    public void setDataSet(UimDataSet<Long> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public String getWorkflow() {
        return workflowIdentifier;
    }

    @Override
    public void setWorkflow(String workflow) {
        this.workflowIdentifier = workflow;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    @Override
    public int getSuccessCount() {
        return success;
    }

    @Override
    public void setSuccessCount(int number) {
        this.success = number;
    }

    @Override
    public int getFailureCount() {
        return failure;
    }

    @Override
    public void setFailureCount(int number) {
        this.failure = number;
    }

    @Override
    public int getProcessedCount() {
        return processed;
    }

    @Override
    public void setProcessedCount(int number) {
        this.processed = number;
    }
    

    @Override
    public void putValue(String key, String value) {
        values.put(key, value);
    }

    @Override
    public String getValue(String key) {
        return values.get(key);
    }

    
    
    @Override
    public Map<String, String> values() {
         return values;
    }

	@Override
	public void putValue(ControlledVocabularyKeyValue key, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getValue(ControlledVocabularyKeyValue key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLogFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogFile(String logfile) {
		// TODO Auto-generated method stub
		
	}
}
