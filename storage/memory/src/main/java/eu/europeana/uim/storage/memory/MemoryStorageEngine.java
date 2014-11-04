package eu.europeana.uim.storage.memory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import eu.europeana.uim.EngineStatus;
import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.storage.modules.IdentifierStorageEngine;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.store.bean.CollectionBean;
import eu.europeana.uim.store.bean.ExecutionBean;
import eu.europeana.uim.store.bean.MetaDataRecordBean;
import eu.europeana.uim.store.bean.ProviderBean;
import eu.europeana.uim.store.bean.RequestBean;
import eu.europeana.uim.workflow.Workflow;
import gnu.trove.iterator.TLongLongIterator;
import gnu.trove.iterator.TLongObjectIterator;
import gnu.trove.map.hash.TLongLongHashMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * An in-memory implementation of the {@link StorageEngine} using Longs as IDs.
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class MemoryStorageEngine implements StorageEngine<Long> {

    private static final String IDENTIFIER = MemoryStorageEngine.class.getSimpleName();

    private final Map<Object, Long> idsLookup = new HashMap<>();
    private final AtomicLong uimIdGenerator = new AtomicLong();

    private final TLongObjectHashMap<Provider<Long>> providers = new TLongObjectHashMap<>();
    private final TLongObjectHashMap<Collection<Long>> collections = new TLongObjectHashMap<>();
    private final TLongObjectHashMap<Request<Long>> requests = new TLongObjectHashMap<>();
    private final TLongObjectHashMap<Execution<Long>> executions = new TLongObjectHashMap<>();
    private final TLongObjectHashMap<MetaDataRecord<Long>> metadatas = new TLongObjectHashMap<>();

    private final TLongObjectHashMap<List<Long>> metarequest = new TLongObjectHashMap<>();
    private final TLongLongHashMap metacollection = new TLongLongHashMap();
    private final TLongLongHashMap metaprovider = new TLongLongHashMap();

    private final EngineStatus status = EngineStatus.RUNNING;

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
    public void setConfiguration(Map<String, String> config) {
    }

    @Override
    public Map<String, String> getConfiguration() {
        return new HashMap<>();
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
    public void shutdown() {
    }

    @Override
    public EngineStatus getStatus() {
        return status;
    }

    @Override
    public Long getUimId(Object externalId) {
        return idsLookup.get(externalId);
    }

    @Override
    public List<Long> getUimIds(Object... externalIds) {
        List<Long> uimIds = new ArrayList<>();
        for (Object externalId : externalIds) {
            uimIds.add(getUimId(externalId));
        }
        return uimIds;
    }

    @Override
    public Provider<Long> createProvider() {
        return new ProviderBean<>();
    }

//    private void setupUimId(UimEntity<Long> entity) throws StorageEngineException {
//        Long uimId = getUimId(entity.getExternalIdentifiers());
//        
//        if (entity.getId() == null) {
//            if (uimId != null) {
//                throw new StorageEngineException(
//                        "Provider is duplicated, please retrieve provider via id and update it!");
//            }
//            uimId = uimIdGenerator.getAndIncrement();
//            entity.setId(uimId);
//        }
//        
//        addUimIdMappings(uimId, entity.getExternalIdentifiers());
//    }
//    
//        private Long getUimId(Set<Object> externalIdentifiers) throws StorageEngineException {
//        Long uimId = null;
//        for (Object provId : externalIdentifiers) {
//            Long existId = idsLookup.get(provId);
//            if (existId == null) {
//                continue;
//            }
//
//            if (uimId == null) {
//                uimId = existId;
//            } else if (!existId.equals(uimId)) {
//                throw new StorageEngineException(
//                        "Entity has contraticting ids attached to it!");
//            }
//        }
//        return uimId;
//    }
//
//    private void addUimIdMappings(Long uimId, Set<Object> externalIdentifiers) {
//        for (Object externalIdentifier : externalIdentifiers) {
//            idsLookup.put(externalIdentifier, uimId);
//        }
//    }
    @Override
    public void updateProvider(Provider<Long> provider) throws StorageEngineException {
        if (!(provider instanceof ProviderBean)) {
            throw new StorageEngineException(
                    "Cannot store this provider as this implementation does only support ProviderBean!");
        }
        
        if (provider.getMnemonic() == null) {
            throw new StorageEngineException(
                    "Cannot store provider without mnemonic/code.");
        }

        final String mnemonic = IdentifierStorageEngine.DatasetPrefix.PROVIDER + IdentifierStorageEngine.SEPARATOR + provider.getMnemonic();
        if (idsLookup.containsKey(mnemonic)) {
            Long pid = idsLookup.get(mnemonic);
            if (!Objects.equals(pid, provider.getId())) {
                throw new StorageEngineException(
                        "Cannot store provider duplicate mnemonic/code.");
            }
        }

        if (provider.getId() == null) {
            Long uimId = uimIdGenerator.getAndIncrement();
            ((ProviderBean<Long>) provider).setId(uimId);
            idsLookup.put(mnemonic, uimId);
        }

        providers.put(provider.getId(), provider);

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
    public Provider<Long> getProvider(Long id) {
        Provider<Long> provider = providers.get(id);
        return provider;
    }

    @Override
    public BlockingQueue<Provider<Long>> getAllProviders() {
        BlockingQueue<Provider<Long>> result = new LinkedBlockingQueue<>();
        TLongObjectIterator<Provider<Long>> iterator = providers.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            result.add(iterator.value());
        }
        return result;
    }

    @Override
    public BlockingQueue<Long> getMetaDataRecordIdsByProvider(Provider<Long> provider, boolean recursive) {
        List<Long> mdrIds = new ArrayList<>();

        Set<Long> set = new HashSet<>();
        if (recursive) {
            getRecursive(provider, set);
        } else {
            set.add(provider.getId());
        }

        TLongLongIterator iterator = metaprovider.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            if (set.contains(iterator.value())) {
                mdrIds.add(iterator.key());
            }
        }
        Collections.sort(mdrIds);

        return new LinkedBlockingQueue<>(mdrIds);
    }

    @Override
    public BlockingQueue<MetaDataRecord<Long>> getMetaDataRecordsByProvider(Provider<Long> provider, boolean recursive) {
        List<Long> mdrIds = new ArrayList<>();

        Set<Long> set = new HashSet<>();
        if (recursive) {
            getRecursive(provider, set);
        } else {
            set.add(provider.getId());
        }

        TLongLongIterator iterator = metaprovider.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            if (set.contains(iterator.value())) {
                mdrIds.add(iterator.key());
            }
        }
        Collections.sort(mdrIds);

        BlockingQueue<MetaDataRecord<Long>> result = new LinkedBlockingQueue<>();
        for (Long mdrId : mdrIds) {
            result.add(metadatas.get(mdrId));
        }
        return result;
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
    public Collection<Long> createCollection(Provider<Long> provider) {
        CollectionBean<Long> coll = new CollectionBean<>();
        coll.setProvider(provider);
        return coll;
    }

    @Override
    public void updateCollection(Collection<Long> collection) throws StorageEngineException {
        if (!(collection instanceof CollectionBean)) {
            throw new StorageEngineException(
                    "Cannot store this collection as this implementation does only support CollectionBean!");
        }

        if ( collection.getMnemonic() == null) {
            throw new StorageEngineException(
                    "Cannot store collection without mnemonic/code.");
        }

        final String mnemonic = IdentifierStorageEngine.DatasetPrefix.COLLECTION + IdentifierStorageEngine.SEPARATOR + collection.getMnemonic();
        if (idsLookup.containsKey(mnemonic)) {
            Long pid = idsLookup.get(mnemonic);
            if (!Objects.equals(pid, collection.getId())) {
                throw new StorageEngineException(
                        "Cannot store collection duplicate mnemonic/code.");
            }
        }

        if (collection.getId() == null) {
            Long uimId = uimIdGenerator.getAndIncrement();
            ((CollectionBean<Long>) collection).setId(uimId);
            idsLookup.put(mnemonic, uimId);
        }

        collections.put(collection.getId(), collection);
    }

    @Override
    public Collection<Long> getCollection(Long id) {
        return collections.get(id);
    }

    @Override
    public BlockingQueue<Collection<Long>> getCollections(Provider<Long> provider) {
        BlockingQueue<Collection<Long>> result = new LinkedBlockingQueue<>();
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
    public BlockingQueue<Collection<Long>> getAllCollections() {
        BlockingQueue<Collection<Long>> result = new LinkedBlockingQueue<>();
        TLongObjectIterator<Collection<Long>> iterator = collections.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            Collection<Long> collection = iterator.value();
            result.add(collection);
        }
        return result;
    }

    @Override
    public BlockingQueue<Long> getMetaDataRecordIdsByCollection(Collection<Long> collection) {
        List<Long> result = new ArrayList<>();
        TLongLongIterator iterator = metacollection.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            if (iterator.value() == collection.getId()) {
                result.add(iterator.key());
            }
        }
        Collections.sort(result);
        return new LinkedBlockingQueue<>(result);
    }

    @Override
    public BlockingQueue<MetaDataRecord<Long>> getMetaDataRecordsByCollection(Collection<Long> collection) {
        List<Long> mdrIds = new ArrayList<>();
        TLongLongIterator iterator = metacollection.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            if (iterator.value() == collection.getId()) {
                mdrIds.add(iterator.key());
            }
        }
        Collections.sort(mdrIds);

        BlockingQueue<MetaDataRecord<Long>> result = new LinkedBlockingQueue<>();
        for (Long mdrId : mdrIds) {
            result.add(metadatas.get(mdrId));
        }
        return result;
    }

    @Override
    public Request<Long> createRequest(Collection<Long> collection) throws StorageEngineException {
        RequestBean<Long> request = new RequestBean<>();
        request.setCollection(collection);
        synchronized (this) {
            request.setDate(new Date());
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                //ignore
            }
        }
        return request;
    }

    @Override
    public void updateRequest(Request<Long> request) throws StorageEngineException {
        if (!(request instanceof RequestBean)) {
            throw new StorageEngineException(
                    "Cannot store this request as this implementation does only support RequestBean!");
        }

        String unique = IdentifierStorageEngine.DatasetPrefix.REQUEST + IdentifierStorageEngine.SEPARATOR + request.getCollection().getMnemonic() + "/" + request.getDate().getTime();

        if (idsLookup.containsKey(unique)) {
            Long pid = idsLookup.get(unique);
            if (!Objects.equals(pid, request.getId())) {
                throw new StorageEngineException(
                        "Cannot store request duplicate mnemonic/code.");
            }
        }

        if (request.getId() == null) {
            Long uimId = uimIdGenerator.getAndIncrement();
            ((RequestBean<Long>) request).setId(uimId);
            idsLookup.put(unique, uimId);
        }

        requests.put(request.getId(), request);
    }

    @Override
    public Request<Long> getRequest(Long id) throws StorageEngineException {
        return requests.get(id);
    }

    @Override
    public BlockingQueue<Request<Long>> getRequests(Collection<Long> collection) {
        BlockingQueue<Request<Long>> result = new LinkedBlockingDeque<>();
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
    public BlockingQueue<Request<Long>> getRequests(MetaDataRecord<Long> record)
            throws StorageEngineException {
        BlockingQueue<Request<Long>> result = new LinkedBlockingDeque<>();
        if (metarequest.contains(record.getId())) {
            List<Long> list = metarequest.get(record.getId());
            for (Long id : list) {
                result.add(getRequest(id));
            }
        }
        return result;
    }

    @Override
    public BlockingQueue<Long> getMetaDataRecordIdsByRequest(Request<Long> request) {
        List<Long> mdrIds = new ArrayList<>();
        TLongObjectIterator<List<Long>> iterator = metarequest.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            if (iterator.value().contains(request.getId())) {
                mdrIds.add(iterator.key());
            }
        }
        Collections.sort(mdrIds);

        return new LinkedBlockingQueue<>(mdrIds);
    }

    @Override
    public BlockingQueue<MetaDataRecord<Long>> getMetaDataRecordsByRequest(Request<Long> request) {
        List<Long> mdrIds = new ArrayList<>();
        TLongObjectIterator<List<Long>> iterator = metarequest.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            if (iterator.value().contains(request.getId())) {
                mdrIds.add(iterator.key());
            }
        }
        Collections.sort(mdrIds);

        BlockingQueue<MetaDataRecord<Long>> result = new LinkedBlockingQueue<>();
        for (Long mdrId : mdrIds) {
            result.add(metadatas.get(mdrId));
        }
        return result;
    }

    @Override
    public Execution<Long> createExecution(UimDataSet<Long> entity, Workflow workflow) {
        ExecutionBean<Long> execution = new ExecutionBean<>();
        execution.setDataSet(entity);
        execution.setWorkflow(workflow.getIdentifier());
        return execution;
    }

    @Override
    public void updateExecution(Execution<Long> execution) throws StorageEngineException {
        if (!(execution instanceof ExecutionBean)) {
            throw new StorageEngineException(
                    "Cannot store this execution as this implementation does only support ExecutionBean!");
        }

        String unique = IdentifierStorageEngine.DatasetPrefix.EXECUTION + IdentifierStorageEngine.SEPARATOR + execution.getWorkflow() + "/" + execution.getDataSet().getId() + "/" + execution.getStartTime();

        if (idsLookup.containsKey(unique)) {
            Long pid = idsLookup.get(unique);
            if (!Objects.equals(pid, execution.getId())) {
                throw new StorageEngineException(
                        "Cannot store request duplicate identifier.");
            }
        }

        if (execution.getId() == null) {
            Long uimId = uimIdGenerator.getAndIncrement();
            ((ExecutionBean<Long>) execution).setId(uimId);
            idsLookup.put(unique, uimId);
        }

        executions.put(execution.getId(), execution);
    }

    @Override
    public BlockingQueue<Execution<Long>> getAllExecutions() {
        BlockingQueue<Execution<Long>> result = new LinkedBlockingQueue<>();
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
    public void completed(ExecutionContext<?, Long> context) {
    }

    @Override
    public MetaDataRecord<Long> createMetaDataRecord(Collection<Long> collection)
            throws StorageEngineException {
        MetaDataRecordBean<Long> mdr = new MetaDataRecordBean<>();
        mdr.setCollection(collection);
        return mdr;
    }

    @Override
    public void updateMetaDataRecord(MetaDataRecord<Long> record) throws StorageEngineException {
        if (!(record instanceof MetaDataRecordBean)) {
            throw new StorageEngineException(
                    "Cannot store this record as this implementation does only support MetaDataRecordBean!");
        }

        if ( record.getUniqueId() == null) {
            throw new StorageEngineException(
                    "Cannot store record without identifier.");
        }

        final String uniqueId = IdentifierStorageEngine.DatasetPrefix.MDR + IdentifierStorageEngine.SEPARATOR + record.getUniqueId();
        if (idsLookup.containsKey(uniqueId)) {
            Long pid = idsLookup.get(uniqueId);
            if (!Objects.equals(pid, record.getId())) {
                throw new StorageEngineException(
                        "Cannot store record duplicate identifier.");
            }
        }

        if (record.getId() == null) {
            Long uimId = uimIdGenerator.getAndIncrement();
            ((MetaDataRecordBean<Long>) record).setId(uimId);
            idsLookup.put(uniqueId, uimId);
        }
        
        synchronized (metadatas) {
            metadatas.put(record.getId(), record);
            metacollection.put(record.getId(),
                    ((MetaDataRecordBean<Long>) record).getCollection().getId());
            metaprovider.put(record.getId(),
                    ((MetaDataRecordBean<Long>) record).getCollection().getProvider().getId());
        }
    }

    @Override
    public void addRequestRecord(Request<Long> request, MetaDataRecord<Long> record)
            throws StorageEngineException {
        if (!metarequest.contains(record.getId())) {
            metarequest.put(record.getId(), new ArrayList<Long>());
        }
        metarequest.get(record.getId()).add(request.getId());
    }

    @Override
    public MetaDataRecord<Long> getMetaDataRecord(Long id) {
        return metadatas.get(id);
    }

    @Override
    public List<MetaDataRecord<Long>> getMetaDataRecords(List<Long> ids) {
        List<MetaDataRecord<Long>> records = new ArrayList<>();
        for (Long id : ids) {
            records.add(metadatas.get(id));
        }
        return records;
    }
}
