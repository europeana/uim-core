package eu.europeana.uim.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.DataSet;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;

/** noop storage engine adapter 
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public abstract class StorageEngineAdapter implements StorageEngine {

    @Override
	public String getIdentifier() {
		return StorageEngineAdapter.class.getSimpleName();
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
	public void setConfiguration(Map<String, String> config) {
	}


	@Override
	public Map<String, String> getConfiguration() {
		return null;
	}


	@Override
	public EngineStatus getStatus() {
		return null;
	}

	@Override
	public long size() {
		return 0;
	}

	@Override
	public Provider createProvider() {
		return null;
	}

	@Override
	public void updateProvider(Provider provider) throws StorageEngineException {
	}

	@Override
	public Provider getProvider(long id) {
		return null;
	}

	@Override
	public Provider findProvider(String mnemonic) {
		
		return null;
	}

	@Override
	public List<Provider> getAllProviders() {
		return new ArrayList<Provider>();
	}

	@Override
	public Collection createCollection(Provider provider) {
		
		return null;
	}

	@Override
	public void updateCollection(Collection collection)
			throws StorageEngineException {
		

	}

	@Override
	public Collection getCollection(long id) {
		return null;
	}

    @Override
    public List<Collection> getAllCollections() {
		return new ArrayList<Collection>();
    }

	@Override
	public Collection findCollection(String mnemonic) {
		
		return null;
	}

	@Override
	public List<Collection> getCollections(Provider provider) {
		return new ArrayList<Collection>();
	}

	@Override
	public Request createRequest(Collection collection, Date date) {
		return null;
	}

	@Override
	public void updateRequest(Request request) throws StorageEngineException {
		

	}
	
	@Override
	public Request getRequest(long id) throws StorageEngineException {
		return null;
	}


	@Override
	public List<Request> getRequests(Collection collection) {
		return new ArrayList<Request>();
	}

	@Override
	public MetaDataRecord createMetaDataRecord(Request request) {
		
		return null;
	}

    @Override
    public MetaDataRecord createMetaDataRecord(Request request, String identifier) throws StorageEngineException {
        return null;
    }

	@Override
	public void updateMetaDataRecord(MetaDataRecord record)
			throws StorageEngineException {
		

	}

	@Override
	public Execution createExecution(DataSet entity, String workflow) {
		return null;
	}

	@Override
	public void updateExecution(Execution execution)
			throws StorageEngineException {
		

	}

	
	
	@Override
	public Execution getExecution(long id) throws StorageEngineException {
		return null;
	}


	@Override
	public List<Execution> getAllExecutions() {
		return new ArrayList<Execution>();
	}

	@Override
	public MetaDataRecord[] getMetaDataRecords(long... ids) {
		return null;
	}

	
	@Override
	public long[] getByRequest(Request request) {
		return new long[0];
	}

	@Override
	public long[] getByCollection(Collection collection) {
		return  new long[0];
	}

	@Override
	public long[] getByProvider(Provider provider, boolean recursive) {
		
		return  new long[0];
	}

	@Override
	public long[] getAllIds() {
		return  new long[0];
	}

	
	@Override
	public int getTotalByRequest(Request request) {
		return 0;
	}

	@Override
	public int getTotalByCollection(Collection collection) {
		return 0;
	}

	@Override
	public int getTotalByProvider(Provider provider, boolean recursive) {
		return 0;
	}

	@Override
	public int getTotalForAllIds() {
		return 0;
	}
}
