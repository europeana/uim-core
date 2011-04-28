package org.theeuropeanlibrary.uim.gui.gwt.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public class ExecutionDTO implements IsSerializable, Comparable<ExecutionDTO> {
    /**
     * The key provider that provides the unique ID of a contact.
     */
    public static final ProvidesKey<ExecutionDTO> KEY_PROVIDER = new ProvidesKey<ExecutionDTO>() {
      @Override
    public Object getKey(ExecutionDTO item) {
        return item == null ? null : item.getId();
      }
    };
    
    private Long        id;
    private String      name;
    private String      workflow;

    private ProgressDTO progress;

    private int         scheduled;
    private int         completed;
    private int         failure;

    private boolean     isActive;
    private Date        startTime;
    private Date        endTime;
    private String      dataset;
    private boolean     done;

    public ExecutionDTO() {
    }

    public ExecutionDTO(long id) {
        this.id = id;
    }

    public String getWorkflow() {
        return workflow;
    }

    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }

    public String getDataSet() {
        return dataset;
    }

    public void setDataSet(String dataset) {
        this.dataset = dataset;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProgressDTO getProgress() {
        return progress;
    }

    public void setProgress(ProgressDTO progress) {
        this.progress = progress;
    }

    public int getScheduled() {
        return scheduled;
    }

    public void setScheduled(int scheduled) {
        this.scheduled = scheduled;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public int compareTo(ExecutionDTO execution) {
        return execution.getStartTime().compareTo(this.getStartTime());
    }
}
