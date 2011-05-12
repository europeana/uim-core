package eu.europeana.uim.store.mongo;

import java.util.Date;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Reference;

import eu.europeana.uim.store.DataSet;
import eu.europeana.uim.store.Execution;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
@Entity
public class MongoExecution extends AbstractMongoEntity<Long> implements Execution<Long> {
    private boolean       isActive;
    private Date          startTime;
    private Date          endTime;
    private Boolean       canceled;
    private String        workflowIdentifier;

    @Reference
    private DataSet<Long> dataSet;

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
    public DataSet<Long> getDataSet() {
        return dataSet;
    }

    @Override
    public void setDataSet(DataSet<Long> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public String getWorkflowName() {
        return workflowIdentifier;
    }

    @Override
    public void setWorkflowName(String workflow) {
        this.workflowIdentifier = workflow;
    }

    @Override
    public Boolean isCanceled() {
        return canceled;
    }

    @Override
    public void setCanceled(Boolean canceled) {
        this.canceled = canceled;
    }
}
