package eu.europeana.uim.store.memory;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.DataSet;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import gnu.trove.TLongArrayList;
import gnu.trove.TLongLongHashMap;
import gnu.trove.TLongLongIterator;
import gnu.trove.TLongObjectHashMap;
import gnu.trove.TLongObjectIterator;
import gnu.trove.TObjectLongHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class MemoryStorageEngine implements StorageEngine {

	public static final String IDENTIFIER = MemoryStorageEngine.class.getSimpleName();
	public static final Logger log = Logger.getLogger(MemoryStorageEngine.class.getName());

	private TLongObjectHashMap<Provider> providers = new TLongObjectHashMap<Provider>();
	private TObjectLongHashMap<String> providerMnemonics = new TObjectLongHashMap<String>();

	private TLongObjectHashMap<Collection> collections = new TLongObjectHashMap<Collection>();
	private TObjectLongHashMap<String> collectionMnemonics = new TObjectLongHashMap<String>();

	private TLongObjectHashMap<Request> requests = new TLongObjectHashMap<Request>();
	private TLongObjectHashMap<Execution> executions = new TLongObjectHashMap<Execution>();

	private Set<String> mdrIdentifier = new HashSet<String>();

	private TLongLongHashMap metarequest = new TLongLongHashMap();
	private TLongLongHashMap metacollection = new TLongLongHashMap();
	private TLongLongHashMap metaprovider = new TLongLongHashMap();
	private TLongObjectHashMap<MetaDataRecord> metadatas = new TLongObjectHashMap<MetaDataRecord>();

	private AtomicLong providerId= new AtomicLong();
	private AtomicLong collectionId= new AtomicLong();
	private AtomicLong requestId= new AtomicLong();
	private AtomicLong executionId= new AtomicLong();

	private AtomicLong mdrId= new AtomicLong();

	private EngineStatus status = EngineStatus.RUNNING;


	public MemoryStorageEngine() {
		super();
	}


	@Override
	public String getIdentifier(){
		return IDENTIFIER;
	}


	@Override
	public void initialize() {
	}

	@Override
	public void checkpoint() {
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
	public MetaDataRecord[] getMetaDataRecords(long... ids) {
		ArrayList<MetaDataRecord> result = new ArrayList<MetaDataRecord>(ids.length);
		for (long id : ids) {
			result.add((MetaDataRecord) metadatas.get(id));
		}
		return result.toArray(new MetaDataRecord[result.size()]);
	}






	@Override
	public Provider createProvider() {
		return new MemoryProvider(providerId.getAndIncrement());
	}
	@Override
	public void updateProvider(Provider provider) throws StorageEngineException {
		if (provider.getMnemonic() == null) {
			throw new StorageEngineException("Cannot store provider without mnemonic/code.");
		}
		if (providerMnemonics.containsKey(provider.getMnemonic())) {
			long pid = providerMnemonics.get(provider.getMnemonic());
			if (pid != provider.getId()) {
				throw new StorageEngineException("Cannot store provider duplicate mnemonic/code.");
			}
		}
		providers.put(provider.getId(), provider);
		providerMnemonics.put(provider.getMnemonic(), provider.getId());

		for(Provider related : provider.getRelatedOut()) {
			if (!related.getRelatedIn().contains(provider)){
				related.getRelatedIn().add(provider);
			}
		}
		for(Provider related : provider.getRelatedIn()) {
			if (!related.getRelatedOut().contains(provider)){
				related.getRelatedOut().add(provider);
			}
		}
	}
	@Override
	public List<Provider> getAllProviders() {
		ArrayList<Provider> result = new ArrayList<Provider>();
		TLongObjectIterator<Provider> iterator = providers.iterator();
		while (iterator.hasNext()) {
			iterator.advance();
			result.add(iterator.value());
		}
		return result;
	}
	@Override
	public Provider getProvider(long id) {
		Provider provider = providers.get(id);
		return provider;
	}
	@Override
	public Provider findProvider(String mnemonic) {
		if (providerMnemonics.containsKey(mnemonic)) {
			long id = providerMnemonics.get(mnemonic);
			return providers.get(id);
		}
		return null;
	}




	@Override
	public Collection createCollection(Provider provider) {
		return new MemoryCollection(collectionId.getAndIncrement(), (MemoryProvider) provider);
	}
	@Override
	public void updateCollection(Collection collection) throws StorageEngineException {
		if (collection.getMnemonic() == null) {
			throw new StorageEngineException("Cannot store collection without mnemonic/code.");
		}
		if (collectionMnemonics.containsKey(collection.getMnemonic())) {
			long pid = collectionMnemonics.get(collection.getMnemonic());
			if (pid != collection.getId()) {
				throw new StorageEngineException("Cannot store collection duplicate mnemonic/code.");
			}
		}
		collections.put(collection.getId(), collection);
		collectionMnemonics.put(collection.getMnemonic(), collection.getId());
	}
	@Override
	public List<Collection> getCollections(Provider provider) {
		ArrayList<Collection> result = new ArrayList<Collection>();
		TLongObjectIterator<Collection> iterator = collections.iterator();
		while (iterator.hasNext()) {
			iterator.advance();
			Collection collection = iterator.value();
			if (collection.getProvider().equals(provider)) {
				result.add(collection);
			}
		}
		return result;
	}
	@Override
	public List<Collection> getAllCollections() {
		ArrayList<Collection> result = new ArrayList<Collection>();
		TLongObjectIterator<Collection> iterator = collections.iterator();
		while (iterator.hasNext()) {
			iterator.advance();
			Collection collection = iterator.value();
			result.add(collection);
		}
		return result;
	}
	@Override
	public Collection getCollection(long id) {
		return collections.get(id);
	}
	@Override
	public Collection findCollection(String mnemonic) {
		if (collectionMnemonics.containsKey(mnemonic)) {
			long id = collectionMnemonics.get(mnemonic);
			return collections.get(id);
		}
		return null;
	}



	@Override
	public Request createRequest(Collection collection, Date date) {
		return new MemoryRequest(requestId.getAndIncrement(), (MemoryCollection) collection, date);
	}
	@Override
	public void updateRequest(Request request) {
		TLongObjectIterator<Request> iterator = requests.iterator();
		while (iterator.hasNext()) {
			iterator.advance();
			Request candidate = iterator.value();
			if (request.getCollection().equals(candidate.getCollection())) {
				if (request.getDate().equals(candidate.getDate())){
					String unique = "REQUEST/" +request.getCollection().getMnemonic() + "/" + request.getDate();
					throw new IllegalStateException("Duplicate unique key for request: <" + unique + ">");
				}
			}
		}

		requests.put(request.getId(), request);
	}

	@Override
	public Request getRequest(long id) throws StorageEngineException {
		return requests.get(id);
	}

	@Override
	public List<Request> getRequests(Collection collection) {
		ArrayList<Request> result = new ArrayList<Request>();
		TLongObjectIterator<Request> iterator = requests.iterator();
		while (iterator.hasNext()) {
			iterator.advance();
			Request request = iterator.value();
			if (request.getCollection().equals(collection)) {
				result.add(request);
			}
		}
		return result;
	}





	@Override
	public MetaDataRecord createMetaDataRecord(Request request) {
		MemoryMetaDataRecord mdr = new MemoryMetaDataRecord(mdrId.getAndIncrement());
		mdr.setRequest(request);
		return mdr;
	}

	@Override
	public MetaDataRecord createMetaDataRecord(Request request, String identifier) throws StorageEngineException {
		MetaDataRecord mdr = createMetaDataRecord(request);
		((MemoryMetaDataRecord)mdr).setIdentifier(identifier);
		return mdr;
	}

	@Override
	public void updateMetaDataRecord(MetaDataRecord record) {
		String unique = "MDR/" +  record.getRequest().getCollection().getProvider().getMnemonic() + "/"+ record.getIdentifier();

		synchronized(metadatas) {
			if (!metadatas.containsKey(record.getId())) {
				if (mdrIdentifier.contains(unique)) {
					throw new IllegalStateException("Duplicate unique key for record: <" + unique + ">");
				}
			}

			metadatas.put(record.getId(), record);
			mdrIdentifier.add(unique);
		}
		addMetaDataRecord(record);
	}

	private void addMetaDataRecord(MetaDataRecord record) {
		metarequest.put(record.getId(), record.getRequest().getId());
		metacollection.put(record.getId(), record.getRequest().getCollection().getId());
		metaprovider.put(record.getId(), record.getRequest().getCollection().getProvider().getId());
	}


	@Override
	public Execution createExecution(DataSet entity, String workflow) {
		MemoryExecution execution = new MemoryExecution(executionId.getAndIncrement());
		execution.setDataSet(entity);
		execution.setWorkflowName(workflow);
		return execution;
	}
	@Override
	public void updateExecution(Execution execution) {
		executions.put(execution.getId(), execution);
	}
	@Override
	public List<Execution> getAllExecutions() {
		ArrayList<Execution> result = new ArrayList<Execution>();
		TLongObjectIterator<Execution> iterator = executions.iterator();
		while (iterator.hasNext()) {
			iterator.advance();
			result.add(iterator.value());
		}
		return result;
	}


	@Override
	public Execution getExecution(long id) throws StorageEngineException {
		return executions.get(id);
	}


	@Override
	public long[] getByRequest(Request request) {
		TLongArrayList result = new TLongArrayList();
		TLongLongIterator iterator = metarequest.iterator();
		while(iterator.hasNext()) {
			iterator.advance();
			if (iterator.value() == request.getId()) {
				result.add(iterator.key());
			}
		}

		long[] ids = result.toNativeArray();
		Arrays.sort(ids);
		return ids;
	}

	@Override
	public long[] getByCollection(Collection collection) {
		TLongArrayList result = new TLongArrayList();
		TLongLongIterator iterator = metacollection.iterator();
		while(iterator.hasNext()) {
			iterator.advance();
			if (iterator.value() == collection.getId()) {
				result.add(iterator.key());
			}
		}

		long[] ids = result.toNativeArray();
		Arrays.sort(ids);
		return ids;
	}

	@Override
	public long[] getByProvider(Provider provider, boolean recursive) {
		TLongArrayList result = new TLongArrayList();

		Set<Long> set = new HashSet<Long>();
		if (recursive) {
			getRecursive(provider, set);
		} else {
			set.add(provider.getId());
		}


		TLongLongIterator iterator = metaprovider.iterator();
		while(iterator.hasNext()) {
			iterator.advance();
			if (set.contains(iterator.value())) {
				result.add(iterator.key());
			}
		}

		long[] ids = result.toNativeArray();
		Arrays.sort(ids);
		return ids;
	}

	public void getRecursive(Provider provider, Set<Long> result) {
		if (!result.contains(provider.getId())){
			result.add(provider.getId());
			for (Provider related : provider.getRelatedOut()) {
				getRecursive(related, result);
			}
		}
	}

	@Override
	public long[] getAllIds() {
		return metadatas.keys();
	}


	@Override
	public int getTotalByRequest(Request request) {
		int result = 0;
		TLongLongIterator iterator = metarequest.iterator();
		while(iterator.hasNext()) {
			iterator.advance();
			if (iterator.value() == request.getId()) {
				result++;
			}
		}
		return result;
	}


	@Override
	public int getTotalByCollection(Collection collection) {
		int result = 0;
		TLongLongIterator iterator = metacollection.iterator();
		while(iterator.hasNext()) {
			iterator.advance();
			if (iterator.value() == collection.getId()) {
				result++;
			}
		}
		return result;
	}


	@Override
	public int getTotalByProvider(Provider provider, boolean recursive) {
		int result = 0;
		TLongLongIterator iterator = metaprovider.iterator();
		while(iterator.hasNext()) {
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
