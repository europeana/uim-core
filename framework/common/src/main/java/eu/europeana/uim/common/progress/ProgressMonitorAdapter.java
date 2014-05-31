package eu.europeana.uim.common.progress;

/**
 * Noop adapter implementation of a progress monitor.
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jul 18, 2012
 */
public class ProgressMonitorAdapter implements ProgressMonitor {

    @Override
    public void beginTask(String task, int work) {
    }

    @Override
    public void worked(int work) {
    }

    @Override
    public void done() {
    }

    @Override
    public void subTask(String subtask) {
    }

    @Override
    public void setCancelled(boolean canceled) {
    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
