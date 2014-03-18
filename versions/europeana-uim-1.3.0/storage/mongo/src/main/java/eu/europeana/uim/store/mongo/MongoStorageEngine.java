/*
 * Copyright 2007 EDL FOUNDATION
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * you may not use this work except in compliance with the
 * Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */
package eu.europeana.uim.store.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.types.ObjectId;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import eu.europeana.uim.api.EngineStatus;
import eu.europeana.uim.api.ExecutionContext;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.store.mongo.decorators.MongoCollectionDecorator;
import eu.europeana.uim.store.mongo.decorators.MongoExecutionDecorator;
import eu.europeana.uim.store.mongo.decorators.MongoMetadataRecordDecorator;
import eu.europeana.uim.store.mongo.decorators.MongoProviderDecorator;
import eu.europeana.uim.store.mongo.decorators.MongoRequestDecorator;

/**
 * Basic implementation of a StorageEngine based on MongoDB with Morphia.
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Georgios Markakis <gwarkx@hotmail.com>
 */
public class MongoStorageEngine extends AbstractEngine implements
		StorageEngine<String> {

	private static final String DEFAULT_UIM_DB_NAME = "UIM";
	private static final String MNEMONICFIELD = "searchMnemonic";
	private static final String NAMEFIELD = "searchName";
	private static final String LOCALIDFIELD = "mongoId";
	private static final String RECORDUIDFIELD = "uniqueID";
	private static final String REQUESTRECORDS = "requestrecords";
	
	
	Mongo mongo = null;
	private DB db = null;
	private DBCollection records = null;
	private Datastore ds = null;

	private EngineStatus status = EngineStatus.STOPPED;

	private String dbName;

	/**
	 * @param dbName
	 */
	public MongoStorageEngine(String dbName) {
		this.dbName = dbName;
	}

	/**
     * 
     */
	public MongoStorageEngine() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.StorageEngine#getIdentifier()
	 */
	public String getIdentifier() {
		return MongoStorageEngine.class.getSimpleName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.StorageEngine#setConfiguration(java.util.Map)
	 */
	public void setConfiguration(Map<String, String> config) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.StorageEngine#getConfiguration()
	 */
	public Map<String, String> getConfiguration() {
		return new HashMap<String, String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.StorageEngine#initialize()
	 */
	public void initialize() {
		try {
			if (dbName == null) {
				dbName = DEFAULT_UIM_DB_NAME;
			}
			status = EngineStatus.BOOTING;
			mongo = new Mongo();
			db = mongo.getDB(dbName);
			records = db.getCollection("records");
			Morphia morphia = new Morphia();

			morphia.map(MongoProviderDecorator.class)
					.map(MongoExecutionDecorator.class)
					.map(MongoCollectionDecorator.class)
					.map(MongoRequestDecorator.class);

			ds = morphia.createDatastore(mongo, dbName);
			status = EngineStatus.RUNNING;

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.StorageEngine#checkpoint()
	 */
	@Override
	public void checkpoint() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.StorageEngine#command(java.lang.String)
	 */
	@Override
	public void command(String command) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.StorageEngine#completed(eu.europeana.uim.api.
	 * ExecutionContext)
	 */
	@Override
	public void completed(ExecutionContext<String> context) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.StorageEngine#shutdown()
	 */
	public void shutdown() {
		status = EngineStatus.STOPPED;
	}

	/**
	 * @return
	 */
	public String getDbName() {
		return dbName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.StorageEngine#getStatus()
	 */
	public EngineStatus getStatus() {
		return status;
	}

	/**
	 * @return
	 */
	public long size() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.StorageEngine#createProvider()
	 */
	@Override
	public Provider<String> createProvider() {
		Provider<String> p = new MongoProviderDecorator<String>();
		ds.save(p);
		return p;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.StorageEngine#updateProvider(eu.europeana.uim.store
	 * .Provider)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void updateProvider(Provider<String> provider)
			throws StorageEngineException {

		provider = (Provider<String>) ensureConsistency(provider);

		ArrayList<MongoProviderDecorator> allresults = new ArrayList<MongoProviderDecorator>();
		ArrayList<MongoProviderDecorator> result1 = (ArrayList<MongoProviderDecorator>) ds
				.find(MongoProviderDecorator.class)
				.filter(NAMEFIELD, provider.getName()).asList();
		ArrayList<MongoProviderDecorator> result2 = (ArrayList<MongoProviderDecorator>) ds
				.find(MongoProviderDecorator.class)
				.filter(MNEMONICFIELD, provider.getMnemonic()).asList();

		allresults.addAll(result1);
		allresults.addAll(result2);

		for (Provider<String> p : allresults) {
			if (p.getName() != null
					&& (p.getName().equals(provider.getName()) || p
							.getMnemonic().equals(provider.getMnemonic()))
					&& !p.getId().equals(provider.getId())) {
				throw new StorageEngineException("Provider with name '"
						+ provider.getMnemonic() + "' already exists");
			}
			if (p.getMnemonic() != null
					&& p.getMnemonic().equals(provider.getMnemonic())
					&& !p.getId().equals(provider.getId())) {
				throw new StorageEngineException("Provider with mnemonic '"
						+ provider.getMnemonic() + "' already exists");
			}
		}

		for (Provider<String> related : provider.getRelatedOut()) {
			if (!related.getRelatedIn().contains(provider)) {
				related.getRelatedIn().add(provider);
				ds.merge(related);
			}
		}
		for (Provider<String> related : provider.getRelatedIn()) {
			if (!related.getRelatedOut().contains(provider)) {
				related.getRelatedOut().add(provider);
				ds.merge(related);
			}
		}

		ds.merge(provider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.StorageEngine#getProvider(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Provider<String> getProvider(String id) {
		return ds.find(MongoProviderDecorator.class)
				.filter(LOCALIDFIELD, new ObjectId(id)).get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.StorageEngine#findProvider(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Provider<String> findProvider(String mnemonic) {
		return ds.find(MongoProviderDecorator.class).field(MNEMONICFIELD)
				.equal(mnemonic).get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.StorageEngine#getAllProviders()
	 */
	@Override
	public List<Provider<String>> getAllProviders() {
		final List<Provider<String>> res = new ArrayList<Provider<String>>();
		for (Provider<String> p : ds.find(MongoProviderDecorator.class)
				.asList()) {
			res.add(p);
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.StorageEngine#createCollection(eu.europeana.uim.
	 * store.Provider)
	 */
	@Override
	public Collection<String> createCollection(Provider<String> provider) {
		Collection<String> c = new MongoCollectionDecorator<String>(provider);
		ds.save(c);
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.StorageEngine#updateCollection(eu.europeana.uim.
	 * store.Collection)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void updateCollection(Collection<String> collection)
			throws StorageEngineException {
		collection = (Collection<String>) ensureConsistency(collection);

		ArrayList<MongoCollectionDecorator> allresults = new ArrayList<MongoCollectionDecorator>();

		ArrayList<MongoCollectionDecorator> result1 = (ArrayList<MongoCollectionDecorator>) ds
				.find(MongoCollectionDecorator.class)
				.filter(NAMEFIELD, collection.getName()).asList();
		ArrayList<MongoCollectionDecorator> result2 = (ArrayList<MongoCollectionDecorator>) ds
				.find(MongoCollectionDecorator.class)
				.filter(MNEMONICFIELD, collection.getMnemonic()).asList();

		allresults.addAll(result1);
		allresults.addAll(result2);

		for (Collection<String> c : allresults) {
			if (c.getName() != null
					&& (c.getName().equals(collection.getName()))
					&& !c.getId().equals(collection.getId())) {
				throw new StorageEngineException("Collection with name '"
						+ collection.getMnemonic() + "' already exists");
			}
			if (c.getMnemonic() != null
					&& c.getMnemonic().equals(collection.getMnemonic())
					&& !c.getId().equals(collection.getId())) {
				throw new StorageEngineException("Collection with mnemonic '"
						+ collection.getMnemonic() + "' already exists");
			}

		}
		ds.merge(collection);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.StorageEngine#getCollection(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> getCollection(String id) {
		return ds.find(MongoCollectionDecorator.class)
				.filter(LOCALIDFIELD, new ObjectId(id)).get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.StorageEngine#findCollection(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<String> findCollection(String mnemonic) {
		return ds.find(MongoCollectionDecorator.class)
				.filter(MNEMONICFIELD, mnemonic).get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.StorageEngine#getCollections(eu.europeana.uim.store
	 * .Provider)
	 */
	@Override
	public List<Collection<String>> getCollections(Provider<String> provider) {
		List<Collection<String>> res = new ArrayList<Collection<String>>();
		for (Collection<String> c : ds.find(MongoCollectionDecorator.class)
				.filter("provider", provider).asList()) {
			res.add(c);
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.StorageEngine#getAllCollections()
	 */
	@Override
	public List<Collection<String>> getAllCollections() {
		List<Collection<String>> res = new ArrayList<Collection<String>>();
		for (Collection<String> c : ds.find(MongoCollectionDecorator.class)
				.asList()) {
			res.add(c);
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.StorageEngine#createRequest(eu.europeana.uim.store
	 * .Collection, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Request<String> createRequest(Collection<String> collection,
			Date date) throws StorageEngineException {
		collection = (Collection<String>) ensureConsistency(collection);
		Request<String> r = new MongoRequestDecorator<String>(
				(MongoCollectionDecorator<String>) collection, date);
		ds.save(r);
		return r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.StorageEngine#updateRequest(eu.europeana.uim.store
	 * .Request)
	 */
	@Override
	public void updateRequest(Request<String> request)
			throws StorageEngineException {

		MongoRequestDecorator<String> request2 = (MongoRequestDecorator<String>) request;

		for (MongoRequestDecorator<?> r : ds.find(MongoRequestDecorator.class)
				.filter("collection", request2.getCollectionReference())
				.asList()) {
			if (r.getDate().equals(request.getDate())
					&& !r.getId().equals(request2.getId())) {
				String unique = "REQUEST/"
						+ request2.getCollection().getMnemonic() + "/"
						+ request2.getDate();
				throw new IllegalStateException(
						"Duplicate unique key for request: <" + unique + ">");
			}
		}

		ds.merge(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.StorageEngine#getRequests(eu.europeana.uim.store
	 * .Collection)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Request<String>> getRequests(Collection<String> collection) {
		collection = (Collection<String>) ensureConsistency(collection);

		List<Request<String>> res = new ArrayList<Request<String>>();
		for (Request<String> r : ds.find(MongoRequestDecorator.class)
				.filter("collection", collection).asList()) {
			res.add(r);
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.StorageEngine#getRequest(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Request<String> getRequest(String id) throws StorageEngineException {
		return ds.find(MongoRequestDecorator.class)
				.filter(LOCALIDFIELD, new ObjectId(id)).get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.StorageEngine#getRequests(eu.europeana.uim.store
	 * .MetaDataRecord)
	 */
	@Override
	public List<Request<String>> getRequests(MetaDataRecord<String> mdr)
			throws StorageEngineException {
		List<Request<String>> requests = new ArrayList<Request<String>>();
		List<MongoRequestDecorator> results = ds.find(MongoRequestDecorator.class).filter(REQUESTRECORDS, mdr).asList();
		for(MongoRequestDecorator res : results){
		
			requests.add(res);
		}
		return requests;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.StorageEngine#createMetaDataRecord(eu.europeana.
	 * uim.store.Collection, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public MetaDataRecord<String> createMetaDataRecord(
			Collection<String> collection, String identifier)
			throws StorageEngineException {
		collection = (Collection<String>) ensureConsistency(collection);
		MongoMetadataRecordDecorator<String> mdr = new MongoMetadataRecordDecorator<String>(
				(MongoCollectionDecorator<String>) collection, identifier);
		ds.save(mdr);
		return mdr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.StorageEngine#addRequestRecord(eu.europeana.uim.
	 * store.Request, eu.europeana.uim.store.MetaDataRecord)
	 */
	@Override
	public void addRequestRecord(Request<String> request,
			MetaDataRecord<String> record) throws StorageEngineException {

		MongoRequestDecorator<String> req = (MongoRequestDecorator<String>) request;

		req.getRequestrecords().add(
				(MongoMetadataRecordDecorator<String>) record);

		ds.merge(req);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.StorageEngine#updateMetaDataRecord(eu.europeana.
	 * uim.store.MetaDataRecord)
	 */
	@Override
	public void updateMetaDataRecord(MetaDataRecord<String> record)
			throws StorageEngineException {
		ds.merge(record);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.StorageEngine#createExecution(eu.europeana.uim.store
	 * .UimDataSet, java.lang.String)
	 */
	@Override
	public Execution<String> createExecution(UimDataSet<String> entity,
			String workflow) throws StorageEngineException {
		MongoExecutionDecorator<String> me = new MongoExecutionDecorator<String>();
		me.setDataSet(entity);
		me.setWorkflow(workflow);
		ds.save(me);
		return me;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.StorageEngine#updateExecution(eu.europeana.uim.store
	 * .Execution)
	 */
	@Override
	public void updateExecution(Execution<String> execution)
			throws StorageEngineException {
		ds.merge(execution);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see eu.europeana.uim.api.StorageEngine#getAllExecutions()
	 */
	@Override
	public List<Execution<String>> getAllExecutions() {
		List<Execution<String>> res = new ArrayList<Execution<String>>();
		for (Execution<String> e : ds.find(MongoExecutionDecorator.class)
				.asList()) {
			res.add(e);
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.europeana.uim.api.StorageEngine#getMetaDataRecords(java.util.List)
	 */
	@Override
	public List<MetaDataRecord<String>> getMetaDataRecords(List<String> ids) {
		ArrayList<MetaDataRecord<String>> res = new ArrayList<MetaDataRecord<String>>();

		for (String id : ids) {

			try {
				MetaDataRecord<String> mdr = getMetaDataRecord(id);
				if (mdr != null) {
					res.add(mdr);
				}
			} catch (StorageEngineException e) {
				e.printStackTrace();
			}

		}

		return res;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.StorageEngine#getMetaDataRecord(java.lang.Object)
	 */
	@Override
	public MetaDataRecord<String> getMetaDataRecord(String id)
			throws StorageEngineException {

		@SuppressWarnings("unchecked")
		MongoMetadataRecordDecorator<String> request = ds
				.find(MongoMetadataRecordDecorator.class)
				.filter(RECORDUIDFIELD, id).get();
		return request;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.StorageEngine#getByRequest(eu.europeana.uim.store.Request)
	 */
	@Override
	public String[] getByRequest(Request<String> request) {

		MongoRequestDecorator<String> cast = (MongoRequestDecorator<String>) request;
		HashSet<MongoMetadataRecordDecorator<String>> reqrecords = cast
				.getRequestrecords();
		String[] res = new String[reqrecords.size()];

		int i = 0;

		for (MongoMetadataRecordDecorator<String> rec : reqrecords) {

			res[i] = rec.getId();

			i++;
		}

		return res;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.StorageEngine#getByCollection(eu.europeana.uim.store.Collection)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String[] getByCollection(Collection<String> collection) {
		collection = (Collection<String>) ensureConsistency(collection);
		List<MongoMetadataRecordDecorator> reqrecords = ds
				.find(MongoMetadataRecordDecorator.class)
				.filter("collection", collection).asList();
		String[] res = new String[reqrecords.size()];

		int i = 0;
		for (MongoMetadataRecordDecorator<String> rec : reqrecords) {

			res[i] = rec.getId();

			i++;
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.StorageEngine#getByProvider(eu.europeana.uim.store.Provider, boolean)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String[] getByProvider(Provider<String> provider, boolean recursive) {
		provider = (Provider<String>) ensureConsistency(provider);
		ArrayList<String> vals = new ArrayList<String>();

		List<MongoCollectionDecorator> mongoCollections = ds
				.find(MongoCollectionDecorator.class)
				.filter("provider", provider).asList();

		for (MongoCollectionDecorator<String> p : mongoCollections) {
			String[] tmp = getByCollection(p);

			for (int i = 0; i < tmp.length; i++) {
				vals.add(tmp[i]);
			}
		}

		if (recursive == true) {

			Set<Provider<String>> relin = provider.getRelatedOut();

			for (Provider<String> relpr : relin) {
				String[] tmp = getByProvider(relpr, false);
				for (int i = 0; i < tmp.length; i++) {
					vals.add(tmp[i]);
				}
			}
		}

		return vals.toArray(new String[vals.size()]);
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.StorageEngine#getAllIds()
	 */
	@Override
	public String[] getAllIds() {
		ArrayList<String> vals = new ArrayList<String>();

		for (MongoMetadataRecordDecorator<String> c : ds.find(
				MongoMetadataRecordDecorator.class).asList()) {
			vals.add(c.getId());
		}

		return vals.toArray(new String[vals.size()]);

	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.StorageEngine#getTotalByRequest(eu.europeana.uim.store.Request)
	 */
	@Override
	public int getTotalByRequest(Request<String> request) {
		MongoRequestDecorator<String> req = (MongoRequestDecorator<String>) request;

		return req.getRequestrecords().size();
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.StorageEngine#getTotalByCollection(eu.europeana.uim.store.Collection)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public int getTotalByCollection(Collection<String> collection) {
		List<MongoMetadataRecordDecorator> records = ds
				.find(MongoMetadataRecordDecorator.class)
				.filter("collection", collection).asList();

		return records.size();
	}


	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.StorageEngine#getTotalByProvider(eu.europeana.uim.store.Provider, boolean)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public int getTotalByProvider(Provider<String> provider, boolean recursive) {

		int recordcounter = 0;

		List<MongoCollectionDecorator> mongoCollections = ds
				.find(MongoCollectionDecorator.class)
				.filter("provider", provider).asList();

		for (MongoCollectionDecorator<String> p : mongoCollections) {
			int tmp = getTotalByCollection(p);
			recordcounter = recordcounter + tmp;
		}

		return recordcounter;
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.StorageEngine#getTotalForAllIds()
	 */
	@Override
	public int getTotalForAllIds() {
		return new Long(records.count()).intValue();
	}

	/* (non-Javadoc)
	 * @see eu.europeana.uim.api.StorageEngine#getExecution(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Execution<String> getExecution(String id)
			throws StorageEngineException {
		return ds.find(MongoExecutionDecorator.class, LOCALIDFIELD,
				new ObjectId(id)).get();
	}

}
