package eu.europeana.uim.common.progress;

/**
 * Simple memory based implementation of a ProgressMonitor. This class just
 * overwrites
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 2, 2011
 */
public abstract class CallbackProgressMonitor extends MemoryProgressMonitor {

    private int eventfrq = 1000;

    /**
     * Creates a new instance of this class callback progress with the defined
     * logging level.
     */
    public CallbackProgressMonitor() {
    }

    /**
     * Creates a new instance of this class callback progress with the defined
     * logging level.
     *
     * @param eventfrq
     */
    public CallbackProgressMonitor(int eventfrq) {
        this.eventfrq = eventfrq;
    }

    @Override
    public void worked(int work) {
        super.worked(work);

        if (getWorked() % eventfrq == 0) {
            event(getWorked());
        }
    }

    /**
     * Extension point to output results. This is called whenever a next set of
     * work (defined by the event frequency) has been finished.
     *
     * @param worked the total number of units of work done so far
     */
    public abstract void event(int worked);

}
