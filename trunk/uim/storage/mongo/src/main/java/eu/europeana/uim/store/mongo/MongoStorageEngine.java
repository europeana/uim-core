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
public class MongoStorageEngine implements StorageEngine<ObjectId> {

    private static final String DEFAULT_UIM_DB_NAME = "UIM";
    private static final String MNEMONICFIELD = "searchMnemonic";
    private static final String NAMEFIELD = "searchName";
    
    private static final String LOCALIDFIELD = "mongoId";
    
    
    
    
    Mongo mongo = null;
    private DB db = null;
    private DBCollection records = null;
    private Datastore ds = null;

    private EngineStatus status = EngineStatus.STOPPED;

    private String dbName;
 
    public MongoStorageEngine(String dbName) {
        this.dbName = dbName;
    }

    public MongoStorageEngine() {

    }

    public String getIdentifier() {
        return MongoStorageEngine.class.getSimpleName();
    }

    public void setConfiguration(Map<String, String> config) {
    }

    public Map<String, String> getConfiguration() {
        return new HashMap<String, String>();
    }

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

          
            morphia.
            map(MongoProviderDecorator.class).
            map(MongoExecutionDecorator.class).
            map(MongoCollectionDecorator.class).
            map(MongoRequestDecorator.class);
            
            ds = morphia.createDatastore(mongo, dbName);
            status = EngineStatus.RUNNING;

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
    
	@Override
	public void checkpoint() {
	}
	
    @Override
    public void command(String command) {
    }

    @Override
    public void completed(ExecutionContext<ObjectId> context) {
    }

    public void shutdown() {
        status = EngineStatus.STOPPED;
    }

    public String getDbName() {
        return dbName;
    }

    public EngineStatus getStatus() {
        return status;
    }

    public long size() {
        return 0;
    }

    @Override
    public Provider<ObjectId> createProvider() {
        Provider<ObjectId> p = new MongoProviderDecorator<ObjectId>();
        ds.save(p);
        return p;
    }

    @Override
    public void updateProvider(Provider<ObjectId> provider) throws StorageEngineException {
    	
    	/*
    	@SuppressWarnings("unchecked")
		MongoProviderDecorator<Long> result = (MongoProviderDecorator<Long>) ds.find(MongoProviderDecorator.class).filter(NAMEFIELD, provider.getName()).filter(MNEMONICFIELD, provider.getMnemonic());
    	
    	if(result == null){
    		result = (MongoProviderDecorator<Long>) ds.find(MongoProviderDecorator.class).filter(LOCALIDFIELD,provider.getId());
    		if(result == null){
    			throw new StorageEngineException("Provider with name '" + provider.getMnemonic() + "' cannot be updated because it does not exist in MongoDB");
    		}
    		
    	}
    	else{
    		
    	}
    	*/
    	
        for (Provider<ObjectId> p : getAllProviders()) {
            if (p.getName() != null && (p.getName().equals(provider.getName()) || p.getMnemonic().equals(provider.getMnemonic())) && !p.getId().equals(provider.getId())) {
                throw new StorageEngineException("Provider with name '" + provider.getMnemonic() + "' already exists");
            }
            if (p.getMnemonic() != null && p.getMnemonic().equals(provider.getMnemonic()) && !p.getId().equals(provider.getId())) {
                throw new StorageEngineException("Provider with mnemonic '" + provider.getMnemonic() + "' already exists");
            }
        }

        for (Provider<ObjectId> related : provider.getRelatedOut()) {
            if (!related.getRelatedIn().contains(provider)) {
                related.getRelatedIn().add(provider);
                ds.merge(related);
            }
        }
        for (Provider<ObjectId> related : provider.getRelatedIn()) {
            if (!related.getRelatedOut().contains(provider)) {
                related.getRelatedOut().add(provider);
                ds.merge(related);
            }
        }

        ds.merge(provider);
    }

    @Override
    public Provider<ObjectId> getProvider(ObjectId id) {
        return ds.find(MongoProviderDecorator.class).filter(LOCALIDFIELD, id).get();
    }

    @Override
    public Provider<ObjectId> findProvider(String mnemonic) {
        return ds.find(MongoProviderDecorator.class).field(MNEMONICFIELD).equal(mnemonic).get();
    }

    @Override
    public List<Provider<ObjectId>> getAllProviders() {
        final List<Provider<ObjectId>> res = new ArrayList<Provider<ObjectId>>();
        for (Provider<ObjectId> p : ds.find(MongoProviderDecorator.class).asList()) {
            res.add(p);
        }
        return res;
    }

    @Override
    public Collection<ObjectId> createCollection(Provider<ObjectId> provider) {
        Collection<ObjectId> c = new MongoCollectionDecorator<ObjectId>(provider);
        ds.save(c);
        return c;
    }

    @Override
    public void updateCollection(Collection<ObjectId> collection) throws StorageEngineException {
        for (Collection<ObjectId> c : getAllCollections()) {
            if (c.getName() != null && (c.getName().equals(collection.getName())) && c.getId() != collection.getId()) {
                throw new StorageEngineException("Collection with name '" + collection.getMnemonic() + "' already exists");
            }
            if (c.getMnemonic() != null && c.getMnemonic().equals(collection.getMnemonic()) && c.getId() != collection.getId()) {
                throw new StorageEngineException("Collection with mnemonic '" + collection.getMnemonic() + "' already exists");
            }

        }
        ds.merge(collection);
    }

    @Override
    public Collection<ObjectId> getCollection(ObjectId id) {
        return ds.find(MongoCollectionDecorator.class).filter(LOCALIDFIELD, id).get();
    }

    @Override
    public Collection<ObjectId> findCollection(String mnemonic) {
        return ds.find(MongoCollectionDecorator.class).filter(MNEMONICFIELD, mnemonic).get();
    }

    @Override
    public List<Collection<ObjectId>> getCollections(Provider<ObjectId> provider) {
        List<Collection<ObjectId>> res = new ArrayList<Collection<ObjectId>>();
        for (Collection<ObjectId> c : ds.find(MongoCollectionDecorator.class).filter("provider", provider).asList()) {
            res.add(c);
        }
        return res;
    }

    @Override
    public List<Collection<ObjectId>> getAllCollections() {
        List<Collection<ObjectId>> res = new ArrayList<Collection<ObjectId>>();
        for (Collection<ObjectId> c : ds.find(MongoCollectionDecorator.class).asList()) {
            res.add(c);
        }
        return res;
    }

    @Override
    public Request<ObjectId> createRequest(Collection<ObjectId> collection, Date date) throws StorageEngineException {
    	    	
        Request<ObjectId> r = new MongoRequestDecorator<Long>((MongoCollectionDecorator<ObjectId>) collection, date);
        ds.save(r);
        return r;
    }

    @Override
    public void updateRequest(Request<ObjectId> request) throws StorageEngineException {
    	
    	MongoRequestDecorator<ObjectId> request2 = (MongoRequestDecorator<ObjectId>)request;
    		
        for (MongoRequestDecorator<?> r : ds.find(MongoRequestDecorator.class).filter("collection", request2.getCollectionReference()).asList()) {
            if (r.getDate().equals(request.getDate()) && !r.getId().equals(request2.getId())) {
                String unique = "REQUEST/" + request2.getCollection().getMnemonic() + "/" + request2.getDate();
                throw new IllegalStateException("Duplicate unique key for request: <" + unique + ">");
            }
        }
    	
        ds.merge(request);
    }

    @Override
    public List<Request<ObjectId>> getRequests(Collection<ObjectId> collection) {
        List<Request<ObjectId>> res = new ArrayList<Request<ObjectId>>();
        for (Request<ObjectId> r : ds.find(MongoRequestDecorator.class).filter("collection", collection).asList()) {
            res.add(r);
        }
        return res;
    }

    @Override
    public Request<ObjectId> getRequest(ObjectId id) throws StorageEngineException {
        return ds.find(MongoRequestDecorator.class).filter(LOCALIDFIELD, id).get();
    }
    
    @Override
    public List<Request<ObjectId>> getRequests(MetaDataRecord<ObjectId> mdr) throws StorageEngineException {
    	ds.find(MongoRequestDecorator.class).filter(LOCALIDFIELD, mdr).get();
		return null;
    }

    
    @Override
    public MetaDataRecord<ObjectId>  createMetaDataRecord(Collection<ObjectId>  request, String identifier) throws StorageEngineException {
    	MongoMetadataRecordDecorator<ObjectId>  mdr = new MongoMetadataRecordDecorator<ObjectId>((MongoCollectionDecorator<ObjectId>) request);
    	ds.save(mdr);
		return mdr;
    }

    
    
    @Override
    public void addRequestRecord(Request<ObjectId> request, MetaDataRecord<ObjectId> record)
            throws StorageEngineException {
        
    	MongoRequestDecorator<ObjectId> req = (MongoRequestDecorator<ObjectId>)request;
  
    	req.getRequestrecords().add((MongoMetadataRecordDecorator<ObjectId>) record);
    	
    	ds.merge(req);
    }

    
    
    @Override
    public void updateMetaDataRecord(MetaDataRecord<ObjectId> record) throws StorageEngineException {
    	ds.merge(record);
    }

    @Override
    public Execution<ObjectId> createExecution(UimDataSet<ObjectId> entity, String workflow) throws StorageEngineException {
        MongoExecutionDecorator<ObjectId> me = new MongoExecutionDecorator<ObjectId>();
        me.setDataSet(entity);
        me.setWorkflow(workflow);
        ds.save(me);
        return me;
    }

    @Override
    public void updateExecution(Execution<ObjectId> execution) throws StorageEngineException {
        ds.merge(execution);
    }

    @Override
    public List<Execution<ObjectId>> getAllExecutions() {
        List<Execution<ObjectId>> res = new ArrayList<Execution<ObjectId>>();
        for (Execution<ObjectId> e : ds.find(MongoExecutionDecorator.class).asList()) {
            res.add(e);
        }
        return res;
    }

    @Override
    public List<MetaDataRecord<ObjectId>> getMetaDataRecords(List<ObjectId> ids) {
        ArrayList<MetaDataRecord<ObjectId>> res = new ArrayList<MetaDataRecord<ObjectId>>();
        BasicDBObject query = new BasicDBObject();
        query.put(LOCALIDFIELD, new BasicDBObject("$in", ids));
        for (DBObject object : records.find(query)) {
            Request<ObjectId> request = ds.find(MongoRequestDecorator.class).filter(LOCALIDFIELD, object.get("request")).get();
            //res.add(new MongoMetadataRecord(object, request.getCollection(), (String) object.get("identifier"), ((Long) object.get(LOCALIDFIELD)).longValue()));
        }

        return res;
    }

    @Override
    public MetaDataRecord<ObjectId> getMetaDataRecord(ObjectId id) throws StorageEngineException {

    	@SuppressWarnings("unchecked")
		MongoMetadataRecordDecorator<ObjectId> request = ds.find(MongoMetadataRecordDecorator.class).filter(LOCALIDFIELD, id).get();
		return request;
    }


    @Override
    public ObjectId[] getByRequest(Request<ObjectId> request) {
    	
    	MongoRequestDecorator<ObjectId> cast = (MongoRequestDecorator<ObjectId>)request;
    	HashSet<MongoMetadataRecordDecorator<ObjectId>> reqrecords = cast.getRequestrecords();
    	ObjectId[] res = new ObjectId[reqrecords.size()];
    	
    	int i = 0;
    	
    	for(MongoMetadataRecordDecorator<ObjectId> rec : reqrecords){
    		
    		res[i] = rec.getId();
    		
    		i++;
    	}

        return res;
    }

    @Override
    public ObjectId[] getByCollection(Collection<ObjectId> collection) {

    	ArrayList<ObjectId> ids = new ArrayList<ObjectId>();
    	
    	List <MongoMetadataRecordDecorator> reqrecords = ds.find(MongoMetadataRecordDecorator.class).filter("collection", collection).asList();
    	
    	ObjectId[] res = new ObjectId[reqrecords.size()];
    	
    	int i = 0;
    	for(MongoMetadataRecordDecorator<ObjectId> rec : reqrecords){
    		
    		res[i] = rec.getId();
    		
    		i++;
    	}
        return res;
    }

    
    @Override
    public ObjectId[] getByProvider(Provider<ObjectId> provider, boolean recursive) {
    	
    	ArrayList<ObjectId> vals = new ArrayList<ObjectId>();
    	  	
    	List <MongoCollectionDecorator> mongoCollections = ds.find(MongoCollectionDecorator.class).filter("provider", provider).asList();
    	
    	for(MongoCollectionDecorator<ObjectId> p : mongoCollections){
    		ObjectId[] tmp = getByCollection(p);
    		
           for(int i=0; i<tmp.length ;i++){
        	   vals.add(tmp[i]);
           }
    	}
    	
    	if(recursive == true){
    		
    		Set<Provider<ObjectId>> relin = provider.getRelatedOut();
    		
    		for(Provider<ObjectId> relpr : relin){
    			ObjectId[] tmp  = getByProvider(relpr,false);
    	        for(int i=0; i<tmp.length ;i++){
    	        	   vals.add(tmp[i]);
    	           }			
    		}
    	}
    
        return vals.toArray(new ObjectId[vals.size()]);
    }



    
    @Override
    public ObjectId[] getAllIds() {
    	ArrayList<ObjectId> vals = new ArrayList<ObjectId>();
    	
        List<MongoMetadataRecordDecorator<ObjectId>> res = new ArrayList<MongoMetadataRecordDecorator<ObjectId>>();
        for (MongoMetadataRecordDecorator<ObjectId> c : ds.find(MongoMetadataRecordDecorator.class).asList()) {
        	vals.add(c.getId());
        }
        
        return vals.toArray(new ObjectId[vals.size()]);

    }

    
    @Override
    public int getTotalByRequest(Request<ObjectId> request) {
    	MongoRequestDecorator<ObjectId> req = (MongoRequestDecorator<ObjectId>) request;
    	
    	return req.getRequestrecords().size();
    }

    @Override
    public int getTotalByCollection(Collection<ObjectId> collection) {
    	List<MongoMetadataRecordDecorator> records = ds.find(MongoMetadataRecordDecorator.class).filter("collection", collection).asList();
         
        return records.size();
    }


    // TODO recursive
    @Override
    public int getTotalByProvider(Provider<ObjectId> provider, boolean recursive) {
    	
    	int recordcounter = 0;
    	
    	List <MongoCollectionDecorator> mongoCollections = ds.find(MongoCollectionDecorator.class).filter("provider", provider).asList();
    	
    	for(MongoCollectionDecorator<ObjectId> p : mongoCollections){
    		int tmp = getTotalByCollection(p);
    		recordcounter = recordcounter + tmp;
    	}
    	
        return recordcounter;
    }

    @Override
    public int getTotalForAllIds() {
        return new Long(records.count()).intValue();
    }
    
    
    @Override
    public Execution<ObjectId> getExecution(ObjectId id) throws StorageEngineException {
        return ds.find(MongoExecutionDecorator.class, LOCALIDFIELD, id).get();
    }


}
