package eu.europeana.uim.common;


/** Simple memor based implementation of a ProgressMonitor. This class just
 * overwrites 
 * 
 * @author andreas.juffinger@kb.nl
 */
public abstract class CallbackProgressMonitor extends MemoryProgressMonitor {

	private int eventfrq = 1000;


    /**
     * Creates a new instance of this class callback progress with 
     * the defined logging level.
     */
    public CallbackProgressMonitor() {
    }


    /**
     * Creates a new instance of this class callback progress with 
     * the defined logging level.
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

	public abstract void event(int worked);
	
}
