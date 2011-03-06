/* RevisableProgressMonitor.java - created on Mar 4, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common;

import java.util.ArrayList;
import java.util.List;

import eu.europeana.uim.common.MemoryProgressMonitor;
import eu.europeana.uim.common.ProgressMonitor;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Mar 4, 2011
 */
public class RevisableProgressMonitor implements ProgressMonitor {
    
    private final MemoryProgressMonitor delegate = new MemoryProgressMonitor();
    
    private final List<RevisingProgressMonitor> monitors = new ArrayList<RevisingProgressMonitor>();
    

    @Override
    public void beginTask(String task, int work) {
        delegate.setStart(System.currentTimeMillis());
        
        delegate.beginTask(task, work);
        for (ProgressMonitor monitor : monitors) {
            monitor.beginTask(task, work);
        }
    }

    @Override
    public void worked(int work) {
        delegate.worked(work);
        for (ProgressMonitor monitor : monitors) {
            monitor.worked(work);
        }
    }

    @Override
    public void done() {
        delegate.done();
        for (ProgressMonitor monitor : monitors) {
            monitor.done();
        }
    }

    @Override
    public void subTask(String subtask) {
        delegate.subTask(subtask);
        for (ProgressMonitor monitor : monitors) {
            monitor.subTask(subtask);
        }
    }

    @Override
    public void setCancelled(boolean canceled) {
        delegate.setCancelled(canceled);
        for (ProgressMonitor monitor : monitors) {
            monitor.setCancelled(canceled);
        }
    }

    @Override
    public boolean isCancelled() {
        boolean cancelled = delegate.isCancelled();
        for (ProgressMonitor monitor : monitors) {
            cancelled |= monitor.isCancelled();
        }
        return cancelled;
    }

    
    public String getTask() {
        return delegate.getTask();
    }

    public String getSubtask() {
        return delegate.getSubtask();
    }

    public int getWork() {
        return delegate.getWork();
    }

    public int getWorked() {
        return delegate.getWorked();
    }
    
    
    
    
    public long getStart() {
        return delegate.getStart();
    }

    public void addListener(RevisingProgressMonitor monitor) {
        monitor.setStart(getStart());
        monitor.setTask(getTask());
        monitor.setWork(getWork());
        
        monitor.setSubtask(getSubtask());
        monitor.setWorked(getWorked());
        
        monitors.add(monitor);
        monitor.attached();
    }
    
    
    public void removeListener(RevisingProgressMonitor monitor) {
        boolean remove = monitors.remove(monitor);
        if (remove) {
            monitor.detached();
        }
    }

}
