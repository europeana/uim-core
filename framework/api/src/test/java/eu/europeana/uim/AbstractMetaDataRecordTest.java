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

/**
 * Base, abstract class to test {@link MetaDataRecord} implementations.
 * 
 * @param <I>
 *            generic ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public abstract class AbstractMetaDataRecordTest<I> {
    StorageEngine<I> engine = getStorageEngine();

    /**
     * @return provides a specific implemtation of a storage typed with the kind of key used for all
     *         data sets
     */
    protected abstract StorageEngine<I> getStorageEngine();

    private enum TestEnum {
        EN;
    }

    /**
     * Tests that the implementation of {@link MetaDataRecord} returns null for getFirstField if
     * there is no field and empty lists for the other getters.
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
     * @return created and initialized request (creates provider and collection as well)
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
