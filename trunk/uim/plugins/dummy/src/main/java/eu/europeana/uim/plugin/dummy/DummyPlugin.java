package eu.europeana.uim.plugin.dummy;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import eu.europeana.uim.MDRFieldRegistry;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;
import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.StorageEngineException;

public class DummyPlugin implements IngestionPlugin {

    private static int counter = 0;

    private static Logger log = Logger.getLogger(DummyPlugin.class.getName());

	private String description;

	public DummyPlugin() {
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public TKey<MDRFieldRegistry, ?>[] getInputFields() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public TKey<MDRFieldRegistry, ?>[] getOutputFields() {
		// TODO Auto-generated method stub
		return null;
	}


    @Override
    public void processRecord(MetaDataRecord mdr, ExecutionContext context) {
        counter++;
        if(counter % 50 == 0) {
            log.info("Dummy plugin is processing MDR " + mdr.getId());
        }
    }


	@Override
	public int getPreferredThreadCount() {
		return 5;
	}


	@Override
	public int getMaximumThreadCount() {
		return 5;
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
