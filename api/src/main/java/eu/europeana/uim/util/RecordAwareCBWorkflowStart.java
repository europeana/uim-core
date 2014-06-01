/**
 *
 */
package eu.europeana.uim.util;

import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;

/**
 * A "record aware" version of CollectionBatchWorkflowStart.
 *
 * @param <I> generic identifier
 * @author Georgios Markakis (gwarkx@hotmail.com)
 * @since 13 Dec 2012
 */
public class RecordAwareCBWorkflowStart<I> extends CollectionBatchWorkflowStart<I> {

    @Override
    public int getTotalSize(ExecutionContext<Collection<I>, I> context) {
        int length = 0;
        try {
            length = context.getStorageEngine().getByCollection((Collection<I>) context.getDataSet()).length;
        } catch (StorageEngineException e) {
            e.printStackTrace();
        }
        return length;
    }
}
