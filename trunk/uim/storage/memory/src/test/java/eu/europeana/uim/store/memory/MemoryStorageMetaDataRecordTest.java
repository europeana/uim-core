package eu.europeana.uim.store.memory;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.AbstractMetaDataRecordTest;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.MetaDataRecord.QualifiedValue;

/**
 * Metadata tests using in-memory implementation of storage.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class MemoryStorageMetaDataRecordTest extends AbstractMetaDataRecordTest<Long> {
    @Override
    protected StorageEngine<Long> getStorageEngine() {
        return new MemoryStorageEngine();
    }

    private enum Language {
        ENGLISH, FRENCH
    }

    /**
     * Tests adding and retrieving values from a {@link MetaDataRecord}.
     * 
     * @throws StorageEngineException
     */
    @Test
    public void testMetadataRecord() throws StorageEngineException {
        MetaDataRecord<Long> record = getStorageEngine().createMetaDataRecord(
                createRequest().getCollection(), "ID 1");

        TKey<MemoryStorageMetaDataRecordTest, String> testKey = TKey.register(
                MemoryStorageMetaDataRecordTest.class, "test", String.class);

        record.addValue(testKey, "unqualified");
        record.addValue(testKey, "english", Language.ENGLISH);
        record.addValue(testKey, "french", Language.FRENCH);

        Assert.assertEquals("unqualified", record.getFirstValue(testKey));
        List<QualifiedValue<String>> values = record.getQualifiedValues(testKey);
        Assert.assertEquals(3, values.size());
        List<String> frenchValues = record.getValues(testKey, Language.FRENCH);
        Assert.assertEquals(1, frenchValues.size());
    }
}
