package eu.europeana.uim.workflows;

import java.util.Random;

import eu.europeana.uim.MDRFieldRegistry;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;
import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.StorageEngineException;

public class SysoutPlugin implements IngestionPlugin {

	private String name;
	private boolean randsleep = false;
	private Random random = new Random();
	
	public SysoutPlugin() {
		this.name = "";
	}

	public SysoutPlugin(String name) {
		this.name = name;
	}

	public SysoutPlugin(String name, boolean randsleep) {
		this.name = name;
		this.randsleep = randsleep;
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
		return SysoutPlugin.class.getSimpleName() + (name != null ? ":" + name : "");
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
				System.out.println(getIdentifier() + ": " + identifier + "(s=" + sleep + ")");
			} catch (InterruptedException e) {
			}
		} else {
			System.out.println(getIdentifier() + ": " + identifier);
		}
	}

    @Override
    public <T> void initialize(ActiveExecution<T> visitor) throws StorageEngineException {
    }

    @Override
    public <T> void finalize(ActiveExecution<T> visitor) throws StorageEngineException {
    }

}
