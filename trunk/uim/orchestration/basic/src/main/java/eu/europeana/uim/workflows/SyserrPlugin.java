package eu.europeana.uim.workflows;

import java.util.Random;

import eu.europeana.uim.MDRFieldRegistry;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;

public class SyserrPlugin implements IngestionPlugin {

	private String name;
	private boolean randsleep = false;
	private Random random = new Random();
	
	private int processed = 0;
	private int errorrate = 1;
	
	public SyserrPlugin() {
		this.name = "";
	}

	public SyserrPlugin(String name) {
		this.name = name;
	}

	public SyserrPlugin(String name,  int errorrate) {
		this.name = name;
		this.errorrate = errorrate;
	}

	public SyserrPlugin(String name, int errorrate, boolean randsleep) {
		this.name = name;
		this.randsleep = randsleep;
		this.errorrate = errorrate;
	}

	@Override
	public int getPreferredThreadCount() {
		return 5;
	}

	@Override
	public int getMaximumThreadCount() {
		return 10;
	}


	@Override
	@SuppressWarnings("unchecked")
	public TKey<MDRFieldRegistry, ?>[] getInputParameters() {
		return new TKey[0];
	}

	@Override
	@SuppressWarnings("unchecked")
	public TKey<MDRFieldRegistry, ?>[] getOutputParameters() {
		return new TKey[0];
	}

	@Override
	@SuppressWarnings("unchecked")
	public TKey<MDRFieldRegistry, ?>[] getTransientParameters() {
		return new TKey[0];
	}


	@Override
	public String getIdentifier() {
		return SyserrPlugin.class.getSimpleName() + (name != null ? ":" + name : "");
	}

	
	@Override
	public String getDescription() {
		return "Writes the identifiers of MDRs to sysout.";
	}

	@Override
	public void processRecord(MetaDataRecord mdr, ExecutionContext context) {
		String identifier = mdr.getIdentifier();
		if (randsleep){
			try {
				int sleep = random.nextInt(100);
				Thread.sleep(sleep);
				System.err.println(getIdentifier() + ": " + identifier + "(s=" + sleep + ")");
			} catch (InterruptedException e) {
			}
		} else {
			System.err.println(getIdentifier() + ": " + identifier);
		}
		
		if (processed++ % errorrate == 0) {
			throw new RuntimeException("Failed plugin at record: " + processed);
		}
	}

}
