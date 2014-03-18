package eu.europeana.uim.store.memory;

import java.util.HashSet;
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
        ENGLISH,
        FRENCH
    }
    
    /**
     * Tests adding and retrieving values from a {@link MetaDataRecord}.
     * 
     * @throws StorageEngineException
     */
    @Test
    public void testMetadataRecord() throws StorageEngineException {
        MetaDataRecord<Long> record = getStorageEngine().createMetaDataRecord(createRequest().getCollection(),
                "ID 1");

        TKey<MemoryStorageMetaDataRecordTest, String> testKey = TKey.register(MemoryStorageMetaDataRecordTest.class,
                "test", String.class);
        
        record.addField(testKey, "unqualified");
        HashSet<Enum<?>> englishQualifier = new HashSet<Enum<?>>() { { add(Language.ENGLISH); }};
        record.addQField(testKey, "english", englishQualifier);
        HashSet<Enum<?>> frenchQualifier = new HashSet<Enum<?>>() { { add(Language.FRENCH); }};
        record.addQField(testKey, "french", frenchQualifier);
        
        Assert.assertEquals("unqualified", record.getFirstField(testKey));
        List<QualifiedValue<String>> values = record.getField(testKey);
        Assert.assertEquals(3, values.size());
        List<String> frenchValues = record.getQField(testKey, frenchQualifier);
        Assert.assertEquals(1, frenchValues.size());
    }
}
