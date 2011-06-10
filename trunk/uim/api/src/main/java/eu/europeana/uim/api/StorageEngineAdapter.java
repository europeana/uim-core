package eu.europeana.uim.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.UimDataSet;

/**
 * noop storage engine adapter for testing purposes
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 16, 2011
 */
public abstract class StorageEngineAdapter implements StorageEngine<Long> {
    @Override
    public String getIdentifier() {
        return StorageEngineAdapter.class.getSimpleName();
    }

    @Override
    public void setConfiguration(Map<String, String> config) {
    }

    @Override
    public Map<String, String> getConfiguration() {
        return null;
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
    public void completed(ExecutionContext context){
    }

    @Override
    public EngineStatus getStatus() {
        return null;
    }

    @Override
    public Provider<Long> createProvider() {
        return null;
    }

    @Override
    public void updateProvider(Provider<Long> provider) throws StorageEngineException {
    }

    @Override
    public Provider<Long> getProvider(Long id) {
        return null;
    }

    @Override
    public Provider<Long> findProvider(String mnemonic) {

        return null;
    }

    @Override
    public List<Provider<Long>> getAllProviders() {
        return new ArrayList<Provider<Long>>();
    }

    @Override
    public Collection<Long> createCollection(Provider<Long> provider) {

        return null;
    }

    @Override
    public void updateCollection(Collection<Long> collection) throws StorageEngineException {

    }

    @Override
    public Collection<Long> getCollection(Long id) {
        return null;
    }

    @Override
    public List<Collection<Long>> getAllCollections() {
        return new ArrayList<Collection<Long>>();
    }

    @Override
    public Collection<Long> findCollection(String mnemonic) {

        return null;
    }

    @Override
    public List<Collection<Long>> getCollections(Provider<Long> provider) {
        return new ArrayList<Collection<Long>>();
    }

    @Override
    public Request<Long> createRequest(Collection<Long> collection, Date date) {
        return null;
    }

    @Override
    public void updateRequest(Request<Long> request) throws StorageEngineException {

    }

    @Override
    public Request<Long> getRequest(Long id) throws StorageEngineException {
        return null;
    }
    
    @Override
    public List<Request<Long>> getRequests(MetaDataRecord<Long> mdr) throws StorageEngineException {
         return null;
    }

    @Override
    public List<Request<Long>> getRequests(Collection<Long> collection) {
        return new ArrayList<Request<Long>>();
    }

    
    @Override
    public void addRequestRecord(Request<Long> request, MetaDataRecord<Long> record)
            throws StorageEngineException {
    }

    @Override
    public MetaDataRecord<Long> createMetaDataRecord(Collection<Long> collection, String identifier)
            throws StorageEngineException {
        return null;
    }

    @Override
    public void updateMetaDataRecord(MetaDataRecord<Long> record) throws StorageEngineException {

    }

    @Override
    public Execution<Long> createExecution(UimDataSet<Long> entity, String workflow) {
        return null;
    }

    @Override
    public void updateExecution(Execution<Long> execution) throws StorageEngineException {

    }

    @Override
    public Execution<Long> getExecution(Long id) throws StorageEngineException {
        return null;
    }

    @Override
    public List<Execution<Long>> getAllExecutions() {
        return new ArrayList<Execution<Long>>();
    }

    @Override
    public MetaDataRecord<Long> getMetaDataRecord(Long id) {
        return null;
    }
    
    @Override
    public List<MetaDataRecord<Long>> getMetaDataRecords(List<Long> id) {
        return null;
    }

    @Override
    public Long[] getByRequest(Request<Long> request) {
        return new Long[0];
    }

    @Override
    public Long[] getByCollection(Collection<Long> collection) {
        return new Long[0];
    }
    

    @Override
    public Long[] getByProvider(Provider<Long> provider, boolean recursive) {

        return new Long[0];
    }

    @Override
    public Long[] getAllIds() {
        return new Long[0];
    }

    @Override
    public int getTotalByRequest(Request<Long> request) {
        return 0;
    }

    @Override
    public int getTotalByCollection(Collection<Long> collection) {
        return 0;
    }

    @Override
    public int getTotalByProvider(Provider<Long> provider, boolean recursive) {
        return 0;
    }

    @Override
    public int getTotalForAllIds() {
        return 0;
    }
}
