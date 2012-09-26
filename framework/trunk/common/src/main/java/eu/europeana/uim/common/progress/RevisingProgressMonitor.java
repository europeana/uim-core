/* RevisingProgressMonitor.java - created on Mar 4, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common.progress;

import eu.europeana.uim.common.progress.ProgressMonitor;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 4, 2011
 */
public interface RevisingProgressMonitor extends ProgressMonitor {

    /** access the start time
     * 
     * @return the start time of this progress monitor
     */
    public long getStart();
    
    /** set the start in long java time of the overall
     * progress
     * 
     * @param millis
     */
    public void setStart(long millis);
    
    /**
     * 
     * @return the number of total work steps
     */
    public int getWork();
    
    /** set the amount of work this monitor is monitoring
     * 
     * @param work
     */
    public void setWork(int work);

    /**
     * @return the number of work units done so far
     */
    public int getWorked();
    
    /** set the number of work units done so far
     * 
     * @param worked
     */
    public void setWorked(int worked);

    /** 
     * 
     * @return the name of the current task
     */
    public String getTask();
    
    /** set the name of the current task
     * 
     * @param task
     */
    public void setTask(String task);

    /** 
     * 
     * @return the name of the subtask currently working on
     */
    public String getSubtask();
    
    /** set the name of the subtask currently working on
     * 
     * @param subtask
     */
    public void setSubtask(String subtask);


    /** this revising monitor has been attached to a revisable 
     * monitor.
     */
    public void attached();

    /** this revising monitor has been detached to a revisable 
     * monitor.
     */
    public void detached();
    
    
}
