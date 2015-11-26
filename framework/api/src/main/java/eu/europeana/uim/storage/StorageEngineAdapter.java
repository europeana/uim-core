package eu.europeana.uim.storage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.europeana.uim.EngineStatus;
import eu.europeana.uim.orchestration.ExecutionContext;
import eu.europeana.uim.storage.modules.criteria.KeyCriterium;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.UimDataSet;

/**
 * noop storage engine adapter for testing purposes
 * 
 * @param <I>
 *            generic identifier
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
		return new HashMap<String, String>();
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
	public Provider<I> findProvider(String mnemonic) {

		return null;
	}

	@Override
	public List<Provider<I>> getAllProviders() {
		return new ArrayList<Provider<I>>();
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
	public List<Collection<I>> getAllCollections() {
		return new ArrayList<Collection<I>>();
	}

	@Override
	public Collection<I> findCollection(String mnemonic) {

		return null;
	}

	@Override
	public List<Collection<I>> getCollections(Provider<I> provider) {
		return new ArrayList<Collection<I>>();
	}

	@Override
	public Request<I> createRequest(Collection<I> collection, Date date) {
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
	public List<Request<I>> getRequests(MetaDataRecord<I> mdr) throws StorageEngineException {
		return null;
	}

	@Override
	public List<Request<I>> getRequests(Collection<I> collection) {
		return new ArrayList<Request<I>>();
	}

	@Override
	public void addRequestRecord(Request<I> request, MetaDataRecord<I> record) throws StorageEngineException {
	}

	@Override
	public MetaDataRecord<I> createMetaDataRecord(Collection<I> collection, String identifier)
			throws StorageEngineException {
		return null;
	}

	@Override
	public void updateMetaDataRecord(MetaDataRecord<I> record) throws StorageEngineException {

	}

	@Override
	public Execution<I> createExecution(UimDataSet<I> entity, String workflow) {
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
	public List<Execution<I>> getAllExecutions() {
		return new ArrayList<Execution<I>>();
	}

	@Override
	public MetaDataRecord<I> getMetaDataRecord(I id) {
		return null;
	}

	@Override
	public List<MetaDataRecord<I>> getMetaDataRecords(List<I> id) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public I[] getByRequest(Request<I> request) {
		return (I[]) Collections.emptyList().toArray();
	}

	@SuppressWarnings("unchecked")
	@Override
	public I[] getByCollection(Collection<I> collection) {
		return (I[]) Collections.emptyList().toArray();
	}

	@SuppressWarnings("unchecked")
	@Override
	public I[] getByCollectionAndCriteria(Collection<I> collection, KeyCriterium<?, ?>... keyCriteria)
			throws StorageEngineException {
		return (I[]) Collections.emptyList().toArray();
	}

	@Override
	public int countByCollectionAndCriteria(Collection<I> collection, KeyCriterium<?, ?>... keyCriteria)
			throws StorageEngineException {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public I[] getByProvider(Provider<I> provider, boolean recursive) {
		return (I[]) Collections.emptyList().toArray();
	}

	@SuppressWarnings("unchecked")
	@Override
	public I[] getAllIds() {
		return (I[]) Collections.emptyList().toArray();
	}

	@Override
	public int getTotalByRequest(Request<I> request) {
		return 0;
	}

	@Override
	public int getTotalByCollection(Collection<I> collection) {
		return 0;
	}

	@Override
	public int getTotalByProvider(Provider<I> provider, boolean recursive) {
		return 0;
	}

	@Override
	public int getTotalForAllIds() {
		return 0;
	}
}
