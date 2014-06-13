/* StorageEngineAdapterTest.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.storage;

import eu.europeana.uim.store.MetaDataRecord;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Tests dummy implementation of {@link StorageEngine}.
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 16, 2011
 */
public class StorageEngineAdapterTest {

    /**
     * Checks to default values.
     *
     * @throws StorageEngineException
     */
    @Test
    public void testStorageAdapterNoop() throws StorageEngineException {
        StorageEngine<Long> engine = new StorageEngineAdapter<Long>() {
        };

        assertNull(engine.createProvider());
        assertNull(engine.createCollection(null));
        assertNull(engine.createRequest(null));
        assertNull(engine.createMetaDataRecord(null));

        engine.updateProvider(null);
        engine.updateCollection(null);
        engine.updateRequest(null);
        engine.updateMetaDataRecord((MetaDataRecord<Long>) null);

        assertNotNull(engine.getAllProviders());
        assertNotNull(engine.getAllCollections());

        assertNotNull(engine.getMetaDataRecordsByRequest(null));
        assertNotNull(engine.getMetaDataRecordsByCollection(null));
        assertNotNull(engine.getMetaDataRecordsByProvider(null, false));
        assertNotNull(engine.getMetaDataRecordsByProvider(null, true));

        assertEquals(0, engine.getMetaDataRecordsByRequest(null).size());
        assertEquals(0, engine.getMetaDataRecordsByCollection(null).size());
        assertEquals(0, engine.getMetaDataRecordsByProvider(null, false).size());
        assertEquals(0, engine.getMetaDataRecordsByProvider(null, true).size());
    }
}
