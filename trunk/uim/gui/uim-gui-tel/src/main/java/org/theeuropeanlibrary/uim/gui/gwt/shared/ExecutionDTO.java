package org.theeuropeanlibrary.uim.gui.gwt.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

/**
 * Execution data object used in GWT for visualization.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 28, 2011
 */
public class ExecutionDTO implements IsSerializable, Comparable<ExecutionDTO> {
    /**
     * The key provider that provides the unique ID of a contact.
     */
    public static final ProvidesKey<ExecutionDTO> KEY_PROVIDER = new ProvidesKey<ExecutionDTO>() {
                                                                   @Override
                                                                   public Object getKey(
                                                                           ExecutionDTO item) {
                                                                       return item == null ? null
                                                                               : item.getId();
                                                                   }
                                                               };

    private Long                                  id;
    private String                                name;
    private String                                workflow;
    private String                                dataset;

    private ProgressDTO                           progress;

    private int                                   scheduled;
    private int                                   completed;
    private int                                   failure;

    private boolean                               isActive;
    private Date                                  startTime;
    private Date                                  endTime;
    private boolean                               canceled;
    private boolean                               paused;

    /**
     * Creates a new instance of this class.
     */
    public ExecutionDTO() {
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param id
     */
    public ExecutionDTO(long id) {
        this.id = id;
    }

    /**
     * @return workflow
     */
    public String getWorkflow() {
        return workflow;
    }

    /**
     * @param workflow
     */
    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }

    /**
     * @return dataset
     */
    public String getDataSet() {
        return dataset;
    }

    /**
     * @param dataset
     */
    public void setDataSet(String dataset) {
        this.dataset = dataset;
    }

    /**
     * @return start time
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return end time
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * @param active
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return progress
     */
    public ProgressDTO getProgress() {
        return progress;
    }

    /**
     * @param progress
     */
    public void setProgress(ProgressDTO progress) {
        this.progress = progress;
    }

    /**
     * @return Is scheduled?
     */
    public int getScheduled() {
        return scheduled;
    }

    /**
     * @param scheduled
     */
    public void setScheduled(int scheduled) {
        this.scheduled = scheduled;
    }

    /**
     * @return Is completed?
     */
    public int getCompleted() {
        return completed;
    }

    /**
     * @param completed
     */
    public void setCompleted(int completed) {
        this.completed = completed;
    }

    /**
     * @return failure count
     */
    public int getFailure() {
        return failure;
    }

    /**
     * @param failure
     */
    public void setFailure(int failure) {
        this.failure = failure;
    }

    /**
     * @param canceled
     */
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    /**
     * @return canceled?
     */
    public boolean isCanceled() {
        return canceled;
    }

    /**
     * @return paused
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * @param paused
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    @Override
    public int compareTo(ExecutionDTO execution) {
        return execution.getStartTime().compareTo(this.getStartTime());
    }
}
