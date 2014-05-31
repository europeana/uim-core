package eu.europeana.uim;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Test;

import eu.europeana.uim.common.MDRFieldRegistry;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import org.junit.Before;

/**
 * Base, abstract class to test {@link MetaDataRecord} implementations.
 *
 * @param <I> generic ID
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public abstract class AbstractMetaDataRecordTest<I> {

    /**
     * engine instance configured in setup up and used throughout tests
     */
    protected StorageEngine<I> engine = null;

    private enum TestEnum {

        EN;
    }

    /**
     * Setups storage engine.
     */
    @Before
    public void setUp() {
        engine = getStorageEngine();
        performSetUp();
    }

    /**
     * Override this for additional setup
     */
    protected void performSetUp() {
        // nothing todo
    }

    /**
     * @return configured storage engine
     */
    protected abstract StorageEngine<I> getStorageEngine();

    /**
     * Tests that the implementation of {@link MetaDataRecord} returns null for
     * getFirstField if there is no field and empty lists for the other getters.
     *
     * @throws StorageEngineException
     */
    @Test
    public void testNullNotNullFields() throws StorageEngineException {
        Request<I> request = createRequest();

        MetaDataRecord<I> record = engine.createMetaDataRecord(request.getCollection(), null);

        // no value for this field exists, therefore the first field is null
        assertNull(record.getFirstValue(MDRFieldRegistry.rawformat));

        // never return null - get empty list when nothing is there
        assertNotNull(record.getQualifiedValues(MDRFieldRegistry.rawformat));
        // never return null - get empty list when nothing is there
        assertNotNull(record.getValues(MDRFieldRegistry.rawformat, TestEnum.EN));
    }

    /**
     * @return created and initialized request (creates provider and collection
     * as well)
     * @throws StorageEngineException
     */
    protected Request<I> createRequest() throws StorageEngineException {
        Provider<I> provider0 = engine.createProvider();
        provider0.setMnemonic("TEL");
        provider0.setName("The European Library");
        engine.updateProvider(provider0);

        Collection<I> collection0 = engine.createCollection(provider0);
        collection0.setMnemonic("a0001");
        collection0.setName("TEL's collection 001");
        engine.updateCollection(collection0);

        Request<I> request0 = engine.createRequest(collection0, new Date(0));
        engine.updateRequest(request0);

        return request0;
    }
}
