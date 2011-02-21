package eu.europeana.uim.common;

import java.util.logging.Level;
import java.util.logging.Logger;

/** Simple memory based implementation of a ProgressMonitor. This class just
 * holds all information in fields and exposes the field values through getter methods.
 * 
 * @author andreas.juffinger@kb.nl
 */
public class LoggingProgressMonitor extends MemoryProgressMonitor {

	private final static Logger log = Logger.getLogger(LoggingProgressMonitor.class.getName());
	
	private Level level;
	private int logfrq = 100;

	private long start;
	

	/**
	 * Creates a new instance of this class logging progress with 
	 * the defined logging level.
	 * 
	 * @param level
	 */
	public LoggingProgressMonitor(Level level) {
		this.level = level;
	}
	
	
	/**
	 * Creates a new instance of this class with the given 
	 * log level and log frequency
	 * @param level
	 * @param logfrq
	 */
	public LoggingProgressMonitor(Level level, int logfrq) {
		this.level = level;
		this.logfrq = logfrq;
	}
	
	
	@Override
	public void beginTask(String task, int work) {
		super.beginTask(task, work);
		this.start = System.currentTimeMillis();
		
		log.log(level, "Begin task: <" + task + "> " + work + " units of work.");
	}

	@Override
	public void worked(int work) {
		super.worked(work);
		
		if (getWorked() % logfrq == 0) {
			long period = System.currentTimeMillis() - start;
			double persec = getWorked() * 1000.0 / period;
			log.log(level, String.format("%d units of worked. So far %d done in %.3f sec. Average %.3f/sec", logfrq, getWorked(), period / 1000.0, persec));
		}
	}

	@Override
	public void done() {
		super.done();
		log.log(level, String.format("%d units done.", getWorked()));
	}

	@Override
	public void subTask(String subtask) {
		super.subTask(subtask);
		log.log(level, String.format("%d units of worked. Start subtask: <" + subtask +">", getWorked()));
	}

	@Override
	public void setCancelled(boolean canceled) {
		super.setCancelled(canceled);

		log.log(level, String.format("%d units of worked. CANCEL: <" + canceled +">", getWorked()));
	}


}
