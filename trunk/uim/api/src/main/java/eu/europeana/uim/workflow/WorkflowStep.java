package eu.europeana.uim.workflow;

import java.util.concurrent.ThreadPoolExecutor;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.StorageEngineException;



/**
 * Step in a UIM workflow. We use this in order to implement the command pattern for workflow execution.
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
public interface WorkflowStep {

    String getIdentifier();

    int getPreferredThreadCount();
    
    int getMaximumThreadCount();

    void setThreadPoolExecutor(ThreadPoolExecutor executor);
    ThreadPoolExecutor getThreadPoolExecutor();
    
    boolean isSavepoint();
    
    <T> void initialize(ActiveExecution<T> visitor) throws StorageEngineException;

    void processRecord(MetaDataRecord mdr, ExecutionContext context);

}
