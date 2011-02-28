package eu.europeana.uim.workflow;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import eu.europeana.uim.MDRFieldRegistry;
import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.TKey;
import eu.europeana.uim.UIMTask;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;

/** Abstract implementation of a workflow start with convenient methods to manage
 * a queue of batches.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 14, 2011
 */
public abstract class AbstractWorkflowStart implements WorkflowStart {
    private final static Logger log = Logger.getLogger(AbstractWorkflowStart.class.getName());

    @Override
    public int getPreferredThreadCount() {
        return 1;
    }

    @Override
    public int getMaximumThreadCount() {
        return 1;
    }

    
    @Override
    public void initialize(ExecutionContext context) {
    }

    
    @Override
    public void initialize(ExecutionContext context, StorageEngine storage)  throws StorageEngineException {
    }

	
    @Override
    public List<String> getParameters() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public TKey<MDRFieldRegistry, ?>[] getInputFields() {
        return new TKey[]{};
    }


    @Override
    public TKey<MDRFieldRegistry, ?>[] getOutputFields() {
        return new TKey[]{};
    }

	
    @Override
    public void processRecord(MetaDataRecord mdr, ExecutionContext context) {
    }

    

}
