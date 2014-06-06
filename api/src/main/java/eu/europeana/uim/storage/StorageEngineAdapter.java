package eu.europeana.uim.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.europeana.uim.EngineStatus;
import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.store.UimEntity;
import eu.europeana.uim.workflow.Workflow;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * noop storage engine adapter for testing purposes
 *
 * @param <I> generic identifier
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 16, 2011
 */
public abstract class StorageEngineAdapter<I> implements StorageEngine<I> {

    @Override
    public String getIdentifier() {
        return StorageEngineAdapter.class.getSimpleName();
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
    public void shutdown() {
    }

    @Override
    public void checkpoint() {
    }

    @Override
    public void command(String command) {
    }

    @Override
    public void completed(ExecutionContext<?, I> context) {
    }

    @Override
    public EngineStatus getStatus() {
        return null;
    }

    @Override
    public Provider<I> createProvider() {
        return null;
    }

    @Override
    public void updateProvider(Provider<I> provider) throws StorageEngineException {
    }

    @Override
    public Provider<I> getProvider(I id) {
        return null;
    }

    @Override
    public BlockingQueue<Provider<I>> getAllProviders() throws StorageEngineException {
        return new LinkedBlockingQueue<>();
    }

    @Override
    public BlockingQueue<I> getMetaDataRecordIdsByProvider(Provider<I> provider) throws StorageEngineException {
        return new LinkedBlockingQueue<>();
    }

    @Override
    public BlockingQueue<MetaDataRecord<I>> getMetaDataRecordsByProvider(Provider<I> provider) throws StorageEngineException {
        return new LinkedBlockingQueue<>();
    }

    @Override
    public Collection<I> createCollection(Provider<I> provider) {
        return null;
    }

    @Override
    public void updateCollection(Collection<I> collection) throws StorageEngineException {
    }

    @Override
    public Collection<I> getCollection(I id) {
        return null;
    }

    @Override
    public BlockingQueue<Collection<I>> getCollections(Provider<I> provider) throws StorageEngineException {
        return new LinkedBlockingQueue<>();
    }

    @Override
    public BlockingQueue<Collection<I>> getAllCollections() throws StorageEngineException {
        return new LinkedBlockingQueue<>();
    }

    @Override
    public BlockingQueue<I> getMetaDataRecordIdsByCollection(Collection<I> collection) throws StorageEngineException {
        return new LinkedBlockingQueue<>();
    }

    @Override
    public BlockingQueue<MetaDataRecord<I>> getMetaDataRecordsByCollection(Collection<I> collection) throws StorageEngineException {
        return new LinkedBlockingQueue<>();
    }

    @Override
    public Request<I> createRequest(Collection<I> collection) {
        return null;
    }

    @Override
    public void updateRequest(Request<I> request) throws StorageEngineException {

    }

    @Override
    public Request<I> getRequest(I id) throws StorageEngineException {
        return null;
    }

    @Override
    public BlockingQueue<Request<I>> getRequests(MetaDataRecord<I> mdr) throws StorageEngineException {
        return new LinkedBlockingQueue<>();
    }

    @Override
    public BlockingQueue<Request<I>> getRequests(Collection<I> collection) throws StorageEngineException {
        return new LinkedBlockingQueue<>();
    }

    @Override
    public BlockingQueue<I> getMetaDataRecordIdsByRequest(Request<I> request) throws StorageEngineException {
        return new LinkedBlockingQueue<>();
    }

    @Override
    public BlockingQueue<MetaDataRecord<I>> getMetaDataRecordsByRequest(Request<I> request) throws StorageEngineException {
        return new LinkedBlockingQueue<>();
    }

    @Override
    public Execution<I> createExecution(UimDataSet<I> dataSet, Workflow workflow) throws StorageEngineException {
        return null;
    }

    @Override
    public void updateExecution(Execution<I> execution) throws StorageEngineException {
    }

    @Override
    public Execution<I> getExecution(I id) throws StorageEngineException {
        return null;
    }

    @Override
    public BlockingQueue<Execution<I>> getAllExecutions() throws StorageEngineException {
        return new LinkedBlockingQueue<>();
    }

    @Override
    public MetaDataRecord<I> createMetaDataRecord(Collection<I> collection) throws StorageEngineException {
        return null;
    }

    @Override
    public void addRequestRecord(Request<I> request, MetaDataRecord<I> record) throws StorageEngineException {
    }

    @Override
    public void updateMetaDataRecord(MetaDataRecord<I>... record) throws StorageEngineException {
    }

    @Override
    public List<MetaDataRecord<I>> getMetaDataRecords(I... ids) throws StorageEngineException {
        return new ArrayList<>();
    }

    @Override
    public I getUimId(Class<? extends UimEntity> entityClass, String externalId) {
        return null;
    }

    @Override
    public List<I> getUimIds(Class<? extends UimEntity> entityClass, String... externalId) {
        return new ArrayList<>();
    }
}
