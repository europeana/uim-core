package eu.europeana.uim.workflows;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import eu.europeana.uim.MDRFieldRegistry;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.StorageEngineException;

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
	public TKey<MDRFieldRegistry, ?>[] getInputFields() {
		return new TKey[0];
	}

	@Override
	@SuppressWarnings("unchecked")
	public TKey<MDRFieldRegistry, ?>[] getOutputFields() {
		return new TKey[0];
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
				System.err.println(getClass().getSimpleName() + ": " + identifier + "(s=" + sleep + ")");
			} catch (InterruptedException e) {
			}
		} else {
			System.err.println(getClass().getSimpleName() + ": " + identifier);
		}
		
		if (processed++ % errorrate == 0) {
			throw new RuntimeException("Failed plugin at record: " + processed);
		}
	}


    @Override
    public <T> void initialize(ExecutionContext context) throws StorageEngineException {
    }

    @Override
    public <T> void finalize(ExecutionContext context) throws StorageEngineException {
    }

    @Override
    public List<String> getParameters() {
        return Collections.EMPTY_LIST;
    }
	
}
