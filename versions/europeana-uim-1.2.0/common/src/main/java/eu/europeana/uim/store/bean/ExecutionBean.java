package eu.europeana.uim.store.bean;

import java.util.Date;

import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.UimDataSet;

/**
 * Implementation of {@link Exception} using Longs as IDs.
 * 
 * @param <I>
 *            unique ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class ExecutionBean<I> extends AbstractEntityBean<I> implements Execution<I> {
    private boolean    active   = false;
    private Date       startTime;
    private Date       endTime;
    private UimDataSet<I> dataSet;
    private String     workflowIdentifier;
    private String     name;
    private boolean    canceled = false;
    private int        success;
    private int        failure;
    private int        processed;

    /**
     * Creates a new instance of this class.
     */
    public ExecutionBean() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param id
     *            unique ID
     */
    public ExecutionBean(I id) {
        super(id);
    }

    @Override
    public String getName() {
        if (name == null || name.length() == 0) {
            return autoName();
        } else {
            return name;
        }
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getWorkflowName() {
        return workflowIdentifier;
    }

    @Override
    public void setWorkflowName(String identifier) {
        this.workflowIdentifier = identifier;
    }

    /**
     * Creates a default name out of workflow name and data set.
     */
    private String autoName() {
        String autoname = null;
        if (workflowIdentifier != null && dataSet != null) {
            autoname = workflowIdentifier + "/" + dataSet.toString();
        }
        return autoname;
    }

    @Override
    public UimDataSet<I> getDataSet() {
        return dataSet;
    }

    @Override
    public void setDataSet(UimDataSet<I> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
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
    public String toString() {
        return "ExecutionBean [id=" + getId() + ", name=" + name + ", workflowIdentifier=" +
               workflowIdentifier + ", dataSet=" + dataSet + "]";
    }
}
