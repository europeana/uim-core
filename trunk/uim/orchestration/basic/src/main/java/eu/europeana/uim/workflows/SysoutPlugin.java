package eu.europeana.uim.workflows;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import eu.europeana.uim.MDRFieldRegistry;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;
import eu.europeana.uim.api.AbstractIngestionPlugin;
import eu.europeana.uim.api.ExecutionContext;

public class SysoutPlugin extends AbstractIngestionPlugin {

    private static TKey<SysoutPlugin, Data> DATA_KEY = TKey.register(SysoutPlugin.class, "data", Data.class);

	public SysoutPlugin() {
	}

    
    @Override
    public String getName() {
        return SysoutPlugin.class.getSimpleName();
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
	public String getDescription() {
		return "Writes the identifiers of MDRs to sysout.";
	}

	@Override
	public void processRecord(MetaDataRecord mdr, ExecutionContext context) {
		String identifier = mdr.getIdentifier();
        Data data = context.getValue(DATA_KEY);


		if (data.randsleep){
			try {
				int sleep = data.random.nextInt(100);
				Thread.sleep(sleep);
				System.out.println(getClass().getSimpleName() + ": " + identifier + "(s=" + sleep + ")");
			} catch (InterruptedException e) {
			}
		} else {
			System.out.println(getClass().getSimpleName() + ": " + identifier);
		}
	}

    @Override
    public void initialize(ExecutionContext context) {
        Data data = new Data();
        context.putValue(DATA_KEY, data);
        
        String property = context.getProperties().getProperty("sysout.random.sleep","false");
        data.randsleep = Boolean.parseBoolean(property);
    }

    @Override
    public void completed(ExecutionContext context) {
    }

    @Override
    public List<String> getParameters() {
        return Arrays.asList("sysout.random.sleep");
    }
    
    
    private final static class Data implements Serializable {
        public Random random = new Random();
        public boolean randsleep = false;
        public int processed = 0;
    }

}
