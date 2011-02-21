package eu.europeana.uim.api;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.DataSet;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface StorageEngine {

	public enum EngineStatus {
		REGISTERED,
		BOOTING,
		RUNNING,
		STOPPED,
		FAILURE
	}
	
	
	public String getIdentifier();
	
	public void setConfiguration(Map<String, String> config);
	public Map<String, String> getConfiguration();
	
	public void initialize();
	public void shutdown();
	public void checkpoint();
	
	public EngineStatus getStatus();
	public long size();
	

	Provider createProvider() throws StorageEngineException;
	void updateProvider(Provider provider) throws StorageEngineException;
	Provider getProvider(long id) throws StorageEngineException;
	Provider findProvider(String mnemonic) throws StorageEngineException;
	List<Provider> getAllProviders() throws StorageEngineException;

	Collection createCollection(Provider provider) throws StorageEngineException;
	void updateCollection(Collection collection) throws StorageEngineException;
	Collection getCollection(long id) throws StorageEngineException;
	Collection findCollection(String mnemonic) throws StorageEngineException;
	List<Collection> getCollections(Provider provider) throws StorageEngineException;
    List<Collection> getAllCollections() throws StorageEngineException;

	Request createRequest(Collection collection, Date date) throws StorageEngineException;
	void updateRequest(Request request) throws StorageEngineException;
	Request getRequest(long id) throws StorageEngineException;
	List<Request> getRequests(Collection collection) throws StorageEngineException;

	MetaDataRecord createMetaDataRecord(Request request) throws StorageEngineException;
    MetaDataRecord createMetaDataRecord(Request request, String identifier) throws StorageEngineException;
	void updateMetaDataRecord(MetaDataRecord record) throws StorageEngineException;
	
	Execution createExecution(DataSet dataSet, String workflow) throws StorageEngineException;
	void updateExecution(Execution execution) throws StorageEngineException;
	Execution getExecution(long id) throws StorageEngineException;
	List<Execution> getAllExecutions() throws StorageEngineException;

	MetaDataRecord[] getMetaDataRecords(long...ids) throws StorageEngineException;
	

	long[] getByRequest(Request request) throws StorageEngineException;
	long[] getByCollection(Collection collection) throws StorageEngineException;
	long[] getByProvider(Provider provider, boolean recursive) throws StorageEngineException;
	long[] getAllIds() throws StorageEngineException;

    int getTotalByRequest(Request request) throws StorageEngineException;
    int getTotalByCollection(Collection collection) throws StorageEngineException;
    int getTotalByProvider(Provider provider, boolean recursive) throws StorageEngineException;
    int getTotalForAllIds() throws StorageEngineException;

}
