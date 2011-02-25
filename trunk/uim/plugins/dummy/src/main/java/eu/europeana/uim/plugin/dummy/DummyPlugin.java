package eu.europeana.uim.plugin.dummy;

import java.util.logging.Logger;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.AbstractIngestionPlugin;
import eu.europeana.uim.api.ExecutionContext;

public class DummyPlugin extends AbstractIngestionPlugin {
    private static Logger log = Logger.getLogger(DummyPlugin.class.getName());

    private static int counter = 0;

	public DummyPlugin() {
	}
	

    @Override
    public void processRecord(MetaDataRecord mdr, ExecutionContext context) {
        counter++;
        if(counter % 50 == 0) {
            log.info("Dummy plugin is processing MDR " + mdr.getId());
        }
    }


}
