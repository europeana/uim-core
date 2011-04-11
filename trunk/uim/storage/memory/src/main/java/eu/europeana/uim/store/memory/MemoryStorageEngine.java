package eu.europeana.uim.store.memory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.apache.commons.lang.ArrayUtils;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.DataSet;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.bean.CollectionBean;
import eu.europeana.uim.store.bean.ExecutionBean;
import eu.europeana.uim.store.bean.MetaDataRecordBean;
import eu.europeana.uim.store.bean.ProviderBean;
import eu.europeana.uim.store.bean.RequestBean;
import gnu.trove.TLongLongHashMap;
import gnu.trove.TLongLongIterator;
import gnu.trove.TLongObjectHashMap;
import gnu.trove.TLongObjectIterator;
import gnu.trove.TObjectLongHashMap;

/**
 * An in-memory implementation of the {@link StorageEngine} using Longs as IDs.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class MemoryStorageEngine implements StorageEngine<Long> {
    private static final String                      IDENTIFIER          = MemoryStorageEngine.class.getSimpleName();
    @SuppressWarnings("unused")
    private static final Logger                      log                 = Logger.getLogger(MemoryStorageEngine.class.getName());

    private TLongObjectHashMap<Provider<Long>>       providers           = new TLongObjectHashMap<Provider<Long>>();
    private TObjectLongHashMap<String>               providerMnemonics   = new TObjectLongHashMap<String>();

    private TLongObjectHashMap<Collection<Long>>     collections         = new TLongObjectHashMap<Collection<Long>>();
    private TObjectLongHashMap<String>               collectionMnemonics = new TObjectLongHashMap<String>();

    private TLongObjectHashMap<Request<Long>>        requests            = new TLongObjectHashMap<Request<Long>>();
    private TLongObjectHashMap<Execution<Long>>      executions          = new TLongObjectHashMap<Execution<Long>>();

    private Map<String, Long>                        mdrIdentifier       = new HashMap<String, Long>();

    private TLongLongHashMap                         metarequest         = new TLongLongHashMap();
    private TLongLongHashMap                         metacollection      = new TLongLongHashMap();
    private TLongLongHashMap                         metaprovider        = new TLongLongHashMap();
    private TLongObjectHashMap<MetaDataRecord<Long>> metadatas           = new TLongObjectHashMap<MetaDataRecord<Long>>();

    private AtomicLong                               providerId          = new AtomicLong();
    private AtomicLong                               collectionId        = new AtomicLong();
    private AtomicLong                               requestId           = new AtomicLong();
    private AtomicLong                               executionId         = new AtomicLong();

    private AtomicLong                               mdrId               = new AtomicLong();

    private EngineStatus                             status              = EngineStatus.RUNNING;

    /**
     * Creates a new instance of this class.
     */
    public MemoryStorageEngine() {
        super();
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void checkpoint() {
    }

    @Override
    public void command(String command) {
    }

    @Override
    public void completed(ExecutionContext context) {
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void setConfiguration(Map<String, String> config) {
    }

    @Override
    public Map<String, String> getConfiguration() {
        return new HashMap<String, String>();
    }

    @Override
    public EngineStatus getStatus() {
        return status;
    }

    @Override
    public long size() {
        return metadatas.size();
    }

    @Override
    public MetaDataRecord<Long> getMetaDataRecord(Long id) {
        return metadatas.get(id);
    }
    
    @Override
    public List<MetaDataRecord<Long>> getMetaDataRecords(List<Long> ids) {
        List<MetaDataRecord<Long>> records = new ArrayList<MetaDataRecord<Long>>();
        for (Long id : ids) {
            records.add(metadatas.get(id));
        }
        return records;
    }

    @Override
    public Provider<Long> createProvider() {
        return new ProviderBean<Long>(providerId.getAndIncrement());
    }

    @Override
    public void updateProvider(Provider<Long> provider) throws StorageEngineException {
        if (provider.getMnemonic() == null) { throw new StorageEngineException(
                "Cannot store provider without mnemonic/code."); }
        if (providerMnemonics.containsKey(provider.getMnemonic())) {
            Long pid = providerMnemonics.get(provider.getMnemonic());
            if (pid != provider.getId()) { throw new StorageEngineException(
                    "Cannot store provider duplicate mnemonic/code."); }
        }
        providers.put(provider.getId(), provider);
        providerMnemonics.put(provider.getMnemonic(), provider.getId());

        for (Provider<Long> related : provider.getRelatedOut()) {
            if (!related.getRelatedIn().contains(provider)) {
                related.getRelatedIn().add(provider);
            }
        }
        for (Provider<Long> related : provider.getRelatedIn()) {
            if (!related.getRelatedOut().contains(provider)) {
                related.getRelatedOut().add(provider);
            }
        }
    }

    @Override
    public List<Provider<Long>> getAllProviders() {
        ArrayList<Provider<Long>> result = new ArrayList<Provider<Long>>();
        TLongObjectIterator<Provider<Long>> iterator = providers.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            result.add(iterator.value());
        }
        return result;
    }

    @Override
    public Provider<Long> getProvider(Long id) {
        Provider<Long> provider = providers.get(id);
        return provider;
    }

    @Override
    public Provider<Long> findProvider(String mnemonic) {
        if (providerMnemonics.containsKey(mnemonic)) {
            Long id = providerMnemonics.get(mnemonic);
            return providers.get(id);
        }
        return null;
    }

    @Override
    public Collection<Long> createCollection(Provider<Long> provider) {
        return new CollectionBean<Long>(collectionId.getAndIncrement(), provider);
    }

    @Override
    public void updateCollection(Collection<Long> collection) throws StorageEngineException {
        if (collection.getMnemonic() == null) { throw new StorageEngineException(
                "Cannot store collection without mnemonic/code."); }
        if (collectionMnemonics.containsKey(collection.getMnemonic())) {
            Long pid = collectionMnemonics.get(collection.getMnemonic());
            if (pid != collection.getId()) { throw new StorageEngineException(
                    "Cannot store collection duplicate mnemonic/code."); }
        }
        collections.put(collection.getId(), collection);
        collectionMnemonics.put(collection.getMnemonic(), collection.getId());
    }

    @Override
    public List<Collection<Long>> getCollections(Provider<Long> provider) {
        ArrayList<Collection<Long>> result = new ArrayList<Collection<Long>>();
        TLongObjectIterator<Collection<Long>> iterator = collections.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            Collection<Long> collection = iterator.value();
            if (collection.getProvider().equals(provider)) {
                result.add(collection);
            }
        }
        return result;
    }

    @Override
    public List<Collection<Long>> getAllCollections() {
        ArrayList<Collection<Long>> result = new ArrayList<Collection<Long>>();
        TLongObjectIterator<Collection<Long>> iterator = collections.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            Collection<Long> collection = iterator.value();
            result.add(collection);
        }
        return result;
    }

    @Override
    public Collection<Long> getCollection(Long id) {
        return collections.get(id);
    }

    @Override
    public Collection<Long> findCollection(String mnemonic) {
        if (collectionMnemonics.containsKey(mnemonic)) {
            Long id = collectionMnemonics.get(mnemonic);
            return collections.get(id);
        }
        return null;
    }

    @Override
    public Request<Long> createRequest(Collection<Long> collection, Date date) {
        return new RequestBean<Long>(requestId.getAndIncrement(), collection, date);
    }

    @Override
    public void updateRequest(Request<Long> request) {
        TLongObjectIterator<Request<Long>> iterator = requests.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            Request<Long> candidate = iterator.value();
            if (request.getCollection().equals(candidate.getCollection())) {
                if (request.getDate().equals(candidate.getDate())) {
                    String unique = "REQUEST/" + request.getCollection().getMnemonic() + "/" +
                                    request.getDate();
                    throw new IllegalStateException("Duplicate unique key for request: <" + unique +
                                                    ">");
                }
            }
        }

        requests.put(request.getId(), request);
    }

    @Override
    public Request<Long> getRequest(Long id) throws StorageEngineException {
        return requests.get(id);
    }

    @Override
    public List<Request<Long>> getRequests(Collection<Long> collection) {
        ArrayList<Request<Long>> result = new ArrayList<Request<Long>>();
        TLongObjectIterator<Request<Long>> iterator = requests.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            Request<Long> request = iterator.value();
            if (request.getCollection().equals(collection)) {
                result.add(request);
            }
        }
        return result;
    }

    @Override
    public MetaDataRecord<Long> createMetaDataRecord(Request<Long> request, String identifier)
            throws StorageEngineException {
        MetaDataRecordBean<Long> mdr;
        Long id = mdrIdentifier.get(identifier);
        if (id != null) {
            mdr = new MetaDataRecordBean<Long>(id, request);
        } else {
            mdr = new MetaDataRecordBean<Long>(mdrId.getAndIncrement(), request);
            synchronized (mdrIdentifier) {
                mdrIdentifier.put(identifier, mdr.getId());
            }
        }
        return mdr;
    }

    @Override
    public void updateMetaDataRecord(MetaDataRecord<Long> record) {
        synchronized (metadatas) {
            metadatas.put(record.getId(), record);
            addMetaDataRecord(record);
        }
    }

    private void addMetaDataRecord(MetaDataRecord<Long> record) {
        metarequest.put(record.getId(), record.getRequest().getId());
        metacollection.put(record.getId(), record.getRequest().getCollection().getId());
        metaprovider.put(record.getId(), record.getRequest().getCollection().getProvider().getId());
    }

    @Override
    public Execution<Long> createExecution(DataSet<Long> entity, String workflow) {
        ExecutionBean<Long> execution = new ExecutionBean<Long>(executionId.getAndIncrement());
        execution.setDataSet(entity);
        execution.setWorkflowName(workflow);
        return execution;
    }

    @Override
    public void updateExecution(Execution<Long> execution) {
        executions.put(execution.getId(), execution);
    }

    @Override
    public List<Execution<Long>> getAllExecutions() {
        ArrayList<Execution<Long>> result = new ArrayList<Execution<Long>>();
        TLongObjectIterator<Execution<Long>> iterator = executions.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            result.add(iterator.value());
        }
        return result;
    }

    @Override
    public Execution<Long> getExecution(Long id) throws StorageEngineException {
        return executions.get(id);
    }

    @Override
    public Long[] getByRequest(Request<Long> request) {
        List<Long> result = new ArrayList<Long>();
        TLongLongIterator iterator = metarequest.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            if (iterator.value() == request.getId()) {
                result.add(iterator.key());
            }
        }

        Long[] ids = result.toArray(new Long[result.size()]);
        Arrays.sort(ids);
        return ids;
    }

    @Override
    public Long[] getByCollection(Collection<Long> collection) {
        List<Long> result = new ArrayList<Long>();
        TLongLongIterator iterator = metacollection.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            if (iterator.value() == collection.getId()) {
                result.add(iterator.key());
            }
        }

        Long[] ids = result.toArray(new Long[result.size()]);
        Arrays.sort(ids);
        return ids;
    }

    @Override
    public Long[] getByProvider(Provider<Long> provider, boolean recursive) {
        List<Long> result = new ArrayList<Long>();

        Set<Long> set = new HashSet<Long>();
        if (recursive) {
            getRecursive(provider, set);
        } else {
            set.add(provider.getId());
        }

        TLongLongIterator iterator = metaprovider.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            if (set.contains(iterator.value())) {
                result.add(iterator.key());
            }
        }

        Long[] ids = result.toArray(new Long[result.size()]);
        Arrays.sort(ids);
        return ids;
    }

    private void getRecursive(Provider<Long> provider, Set<Long> result) {
        if (!result.contains(provider.getId())) {
            result.add(provider.getId());
            for (Provider<Long> related : provider.getRelatedOut()) {
                getRecursive(related, result);
            }
        }
    }

    @Override
    public Long[] getAllIds() {
        return ArrayUtils.toObject(metadatas.keys());
    }
    
    
    @Override
    public BlockingQueue<Long[]> getBatchesByRequest(Request<Long> request)
            throws StorageEngineException {
        List<Long[]> batches = eu.europeana.uim.common.ArrayUtils.batches(getByRequest(request), 250);
        return new LinkedBlockingQueue<Long[]>(batches);
    }

    @Override
    public BlockingQueue<Long[]> getBatchesByCollection(Collection<Long> collection)
            throws StorageEngineException {
        List<Long[]> batches = eu.europeana.uim.common.ArrayUtils.batches(getByCollection(collection), 250);
        return new LinkedBlockingQueue<Long[]>(batches);
    }

    @Override
    public BlockingQueue<Long[]> getBatchesByProvider(Provider<Long> provider, boolean recursive)
            throws StorageEngineException {
        List<Long[]> batches = eu.europeana.uim.common.ArrayUtils.batches(getByProvider(provider, recursive), 250);
        return new LinkedBlockingQueue<Long[]>(batches);
    }

    

    @Override
    public int getTotalByRequest(Request<Long> request) {
        int result = 0;
        TLongLongIterator iterator = metarequest.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            if (iterator.value() == request.getId()) {
                result++;
            }
        }
        return result;
    }

    @Override
    public int getTotalByCollection(Collection<Long> collection) {
        int result = 0;
        TLongLongIterator iterator = metacollection.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            if (iterator.value() == collection.getId()) {
                result++;
            }
        }
        return result;
    }

    @Override
    public int getTotalByProvider(Provider<Long> provider, boolean recursive) {
        int result = 0;
        TLongLongIterator iterator = metaprovider.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            if (iterator.value() == provider.getId()) {
                result++;
            }
        }
        return result;
    }

    @Override
    public int getTotalForAllIds() {
        return metadatas.size();
    }
}
