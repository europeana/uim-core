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
            length = context.getStorageEngine().getMetaDataRecordIdsByCollection((Collection<I>) context.getDataSet()).size();
        } catch (StorageEngineException e) {
            throw new RuntimeException("Could not get number of ids in collection!", e);
        }
        return length;
    }
}
