package eu.europeana.uim.workflows;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import eu.europeana.uim.MDRFieldRegistry;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.StorageEngineException;

public class SyserrPlugin implements IngestionPlugin {

    private static TKey<SyserrPlugin, Data> DATA_KEY = TKey.register(SyserrPlugin.class, "data", Data.class);

    public SyserrPlugin() {
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
        Data data = context.getValue(DATA_KEY);

        if (data.randsleep){
            try {
                int sleep = data.random.nextInt(100);
                Thread.sleep(sleep);
                System.err.println(getClass().getSimpleName() + ": " + identifier + "(s=" + sleep + ")");
            } catch (InterruptedException e) {
            }
        } else {
            System.err.println(getClass().getSimpleName() + ": " + identifier);
        }

        if (data.processed++ % data.errorrate == 0) {
            throw new RuntimeException("Failed plugin at record: " + data.processed);
        }
    }


    @Override
    public void initialize(ExecutionContext context) {
        Data data = new Data();
        context.putValue(DATA_KEY, data);
        
        String property = context.getProperties().getProperty("syserr.random.sleep","false");
        data.randsleep = Boolean.parseBoolean(property);

        property = context.getProperties().getProperty("syserr.error.rate","3");
        data.errorrate = Integer.parseInt(property);
    }

    @Override
    public void completed(ExecutionContext context) {
    }

    @Override
    public List<String> getParameters() {
        return Arrays.asList("syserr.random.sleep", "syserr.error.rate");
    }
    
    
    
    private final static class Data implements Serializable {
        public Random random = new Random();
        public boolean randsleep = false;
        public int errorrate = 3;
        public int processed = 0;
    }


}
