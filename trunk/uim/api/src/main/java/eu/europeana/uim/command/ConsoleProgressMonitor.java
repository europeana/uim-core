package eu.europeana.uim.command;

import eu.europeana.uim.common.ProgressMonitor;

import java.io.PrintStream;

public class ConsoleProgressMonitor implements ProgressMonitor {
	
	private final PrintStream out;
	
	private boolean cancelled = false;
	private int worked = 0;

	public ConsoleProgressMonitor(PrintStream out) {
		super();
		this.out = out;
	}
	
	@Override
	public void beginTask(String task, int work) {
		out.print("Starting:" + task + ", " + work + " units of work. [");
	}

	@Override
	public void worked(int work) {
		out.print(".");
		worked += work;
		if (worked % 10 == 0) {
			out.print("|");
		}
	}

	@Override
	public void done() {
		out.println("]");
	}

	@Override
	public void subTask(String subtask) {
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

}
