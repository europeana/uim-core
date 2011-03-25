package eu.europeana.uim.store.bean;

import java.util.Date;

import eu.europeana.uim.store.DataSet;
import eu.europeana.uim.store.Execution;

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
    private boolean    active = false;
    private Date       startTime;
    private Date       endTime;
    private Date       cancelTime;
    private DataSet<I> dataSet;
    private String     workflowIdentifier;

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
    public Date getCancelTime() {
        return cancelTime;
    }

    @Override
    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }

    @Override
    public DataSet<I> getDataSet() {
        return dataSet;
    }

    @Override
    public void setDataSet(DataSet<I> dataSet) {
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
    public String getWorkflowName() {
        return workflowIdentifier;
    }

    @Override
    public void setWorkflowName(String identifier) {
        this.workflowIdentifier = identifier;
    }

    @Override
    public String toString() {
        return "MemoryExecution [id=" + getId() + ", workflowIdentifier=" + workflowIdentifier +
               ", dataSet=" + dataSet + "]";
    }
}
