package eu.europeana.uim.store.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.ArrayUtils;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.google.code.morphia.mapping.DefaultCreator;
import com.google.code.morphia.query.Query;
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
import eu.europeana.uim.store.mongo.decorators.MongoAbstractEntity;
import eu.europeana.uim.store.mongo.decorators.MongoAbstractNamedEntity;
import eu.europeana.uim.store.mongo.decorators.MongoCollectionDecorator;
import eu.europeana.uim.store.mongo.decorators.MongoExecutionDecorator;
import eu.europeana.uim.store.mongo.decorators.MongoMetadataRecordDecorator;
import eu.europeana.uim.store.mongo.decorators.MongoProviderDecorator;
import eu.europeana.uim.store.mongo.decorators.MongoRequestDecorator;

/**
 * Basic implementation of a StorageEngine based on MongoDB with Morphia.
 * Not optimized whatsoever.
 * <p/>
 * TODO optimize
 * TODO implement the recursive flag for providers
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Georgios Markakis <gwarkx@hotmail.com>
 */
public class MongoStorageEngine implements StorageEngine<Long> {

    private static final String DEFAULT_UIM_DB_NAME = "UIM";
    private static final String MNEMONICFIELD = "searchMnemonic";
    private static final String NAMEFIELD = "searchName";
    
    private static final String LOCALIDFIELD = "lid";
    
    
    
    
    Mongo mongo = null;
    private DB db = null;
    private DBCollection records = null;
    private Datastore ds = null;

    private EngineStatus status = EngineStatus.STOPPED;

    private AtomicLong providerIdCounter = null;
    private AtomicLong collectionIdCounter = null;
    private AtomicLong requestIdCounter = null;
    private AtomicLong executionIdCounter = null;
    private AtomicLong mdrIdCounter = null;

    private String dbName;

    // guys, if you change your minds again, here is a switch.
    private static final boolean ALLOW_DUPLICATE_MDR_IDENTIFIERS = true;
 
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
            //map(MongoAbstractEntity.class).
            //map(MongoAbstractNamedEntity.class).
            map(MongoProviderDecorator.class).
            map(MongoExecutionDecorator.class).
            map(MongoCollectionDecorator.class).
            map(MongoRequestDecorator.class);
            
            ds = morphia.createDatastore(mongo, dbName);
            status = EngineStatus.RUNNING;

            // initialize counters
            providerIdCounter = new AtomicLong(ds.find(MongoProviderDecorator.class).countAll());
            requestIdCounter = new AtomicLong(ds.find(MongoRequestDecorator.class).countAll());
            collectionIdCounter = new AtomicLong(ds.find(MongoCollectionDecorator.class).countAll());
            executionIdCounter = new AtomicLong(ds.find(MongoExecutionDecorator.class).countAll());
            mdrIdCounter = new AtomicLong(records.count());
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
    public void completed(ExecutionContext context) {
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
    public Provider createProvider() {
        Provider p = new MongoProviderDecorator(providerIdCounter.getAndIncrement());
        ds.save(p);
        return p;
    }

    @Override
    public void updateProvider(Provider<Long> provider) throws StorageEngineException {
    	
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
    	
        for (Provider p : getAllProviders()) {
            if (p.getName() != null && (p.getName().equals(provider.getName()) || p.getMnemonic().equals(provider.getMnemonic())) && !p.getId().equals(provider.getId())) {
                throw new StorageEngineException("Provider with name '" + provider.getMnemonic() + "' already exists");
            }
            if (p.getMnemonic() != null && p.getMnemonic().equals(provider.getMnemonic()) && !p.getId().equals(provider.getId())) {
                throw new StorageEngineException("Provider with mnemonic '" + provider.getMnemonic() + "' already exists");
            }
        }

        for (Provider<Long> related : provider.getRelatedOut()) {
            if (!related.getRelatedIn().contains(provider)) {
                related.getRelatedIn().add(provider);
                ds.merge(related);
            }
        }
        for (Provider<Long> related : provider.getRelatedIn()) {
            if (!related.getRelatedOut().contains(provider)) {
                related.getRelatedOut().add(provider);
                ds.merge(related);
            }
        }

        ds.merge(provider);
    }

    @Override
    public Provider getProvider(Long id) {
        return ds.find(MongoProviderDecorator.class).filter(LOCALIDFIELD, id).get();
    }

    @Override
    public Provider findProvider(String mnemonic) {
        return ds.find(MongoProviderDecorator.class).field(MNEMONICFIELD).equal(mnemonic).get();
    }

    @Override
    public List<Provider<Long>> getAllProviders() {
        final List<Provider<Long>> res = new ArrayList<Provider<Long>>();
        for (Provider p : ds.find(MongoProviderDecorator.class).asList()) {
            res.add(p);
        }
        return res;
    }

    @Override
    public Collection createCollection(Provider provider) {
        Collection c = new MongoCollectionDecorator(collectionIdCounter.getAndIncrement(), provider);
        ds.save(c);
        return c;
    }

    @Override
    public void updateCollection(Collection collection) throws StorageEngineException {
        for (Collection c : getAllCollections()) {
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
    public Collection<Long> getCollection(Long id) {
        return ds.find(MongoCollectionDecorator.class).filter(LOCALIDFIELD, id).get();
    }

    @Override
    public Collection<Long> findCollection(String mnemonic) {
        return ds.find(MongoCollectionDecorator.class).filter(MNEMONICFIELD, mnemonic).get();
    }

    @Override
    public List<Collection<Long>> getCollections(Provider<Long> provider) {
        List<Collection<Long>> res = new ArrayList<Collection<Long>>();
        for (Collection c : ds.find(MongoCollectionDecorator.class).filter("provider", provider).asList()) {
            res.add(c);
        }
        return res;
    }

    @Override
    public List<Collection<Long>> getAllCollections() {
        List<Collection<Long>> res = new ArrayList<Collection<Long>>();
        for (Collection c : ds.find(MongoCollectionDecorator.class).asList()) {
            res.add(c);
        }
        return res;
    }

    @Override
    public Request createRequest(Collection collection, Date date) throws StorageEngineException {
    	    	
        Request<Long> r = new MongoRequestDecorator<Long>(requestIdCounter.getAndIncrement(), (MongoCollectionDecorator<Long>) collection, date);
        ds.save(r);
        return r;
    }

    @Override
    public void updateRequest(Request request) throws StorageEngineException {
    	
    	MongoRequestDecorator<?> request2 = (MongoRequestDecorator<?>)request;
    		
        for (MongoRequestDecorator<?> r : ds.find(MongoRequestDecorator.class).filter("collection", request2.getCollectionReference()).asList()) {
            if (r.getDate().equals(request.getDate()) && !r.getId().equals(request2.getId())) {
                String unique = "REQUEST/" + request2.getCollection().getMnemonic() + "/" + request2.getDate();
                throw new IllegalStateException("Duplicate unique key for request: <" + unique + ">");
            }
        }
    	
        ds.merge(request);
    }

    @Override
    public List<Request<Long>> getRequests(Collection<Long> collection) {
        List<Request<Long>> res = new ArrayList<Request<Long>>();
        for (Request r : ds.find(MongoRequestDecorator.class).filter("collection", collection).asList()) {
            res.add(r);
        }
        return res;
    }

    @Override
    public Request getRequest(Long id) throws StorageEngineException {
        return ds.find(MongoRequestDecorator.class).filter(LOCALIDFIELD, id).get();
    }
    
    @Override
    public List<Request<Long>> getRequests(MetaDataRecord<Long> mdr) throws StorageEngineException {
    	ds.find(MongoRequestDecorator.class).filter(LOCALIDFIELD, mdr).get();
		return null;
    }

    
    @Override
    public MetaDataRecord<Long>  createMetaDataRecord(Collection<Long>  request, String identifier) throws StorageEngineException {
    	MongoMetadataRecordDecorator<Long>  mdr = new MongoMetadataRecordDecorator<Long>(mdrIdCounter.getAndIncrement(),(MongoCollectionDecorator<Long>) request);
    	ds.save(mdr);
		return mdr;
    }

    
    
    @Override
    public void addRequestRecord(Request<Long> request, MetaDataRecord<Long> record)
            throws StorageEngineException {
        
    	MongoRequestDecorator<Long> req = (MongoRequestDecorator<Long>)request;
  
    	req.getRequestrecords().add((MongoMetadataRecordDecorator<Long>) record);
    	
    	ds.merge(req);
    }

    
    
    @Override
    public void updateMetaDataRecord(MetaDataRecord<Long> record) throws StorageEngineException {
    	ds.merge(record);
    }

    @Override
    public Execution createExecution(UimDataSet entity, String workflow) throws StorageEngineException {
        MongoExecutionDecorator<Long> me = new MongoExecutionDecorator<Long>(executionIdCounter.getAndIncrement());
        me.setDataSet(entity);
        me.setWorkflow(workflow);
        ds.save(me);
        return me;
    }

    @Override
    public void updateExecution(Execution execution) throws StorageEngineException {
        ds.merge(execution);
    }

    @Override
    public List<Execution<Long>> getAllExecutions() {
        List<Execution<Long>> res = new ArrayList<Execution<Long>>();
        for (Execution<Long> e : ds.find(MongoExecutionDecorator.class).asList()) {
            res.add(e);
        }
        return res;
    }

    @Override
    public List<MetaDataRecord<Long>> getMetaDataRecords(List<Long> ids) {
        ArrayList<MetaDataRecord<Long>> res = new ArrayList<MetaDataRecord<Long>>();
        BasicDBObject query = new BasicDBObject();
        query.put(LOCALIDFIELD, new BasicDBObject("$in", ids));
        for (DBObject object : records.find(query)) {
            Request request = ds.find(MongoRequestDecorator.class).filter(LOCALIDFIELD, object.get("request")).get();
            //res.add(new MongoMetadataRecord(object, request.getCollection(), (String) object.get("identifier"), ((Long) object.get(LOCALIDFIELD)).longValue()));
        }

        return res;
    }

    @Override
    public MetaDataRecord<Long> getMetaDataRecord(Long id) throws StorageEngineException {

    	@SuppressWarnings("unchecked")
		MongoMetadataRecordDecorator<Long> request = ds.find(MongoMetadataRecordDecorator.class).filter(LOCALIDFIELD, id).get();
		return request;
    }


    @Override
    public Long[] getByRequest(Request request) {
    	
    	MongoRequestDecorator<Long> cast = (MongoRequestDecorator<Long>)request;
    	HashSet<MongoMetadataRecordDecorator<Long>> reqrecords = cast.getRequestrecords();
    	Long[] res = new Long[reqrecords.size()];
    	
    	int i = 0;
    	
    	for(MongoMetadataRecordDecorator<Long> rec : reqrecords){
    		
    		res[i] = rec.getId();
    		
    		i++;
    	}

        return res;
    }

    @Override
    public Long[] getByCollection(Collection collection) {

    	ArrayList<Long> ids = new ArrayList<Long>();
    	
    	List <MongoMetadataRecordDecorator> reqrecords = ds.find(MongoMetadataRecordDecorator.class).filter("collection", collection).asList();
    	
    	Long[] res = new Long[reqrecords.size()];
    	
    	int i = 0;
    	for(MongoMetadataRecordDecorator<Long> rec : reqrecords){
    		
    		res[i] = rec.getId();
    		
    		i++;
    	}
        return res;
    }

    
    @Override
    public Long[] getByProvider(Provider<Long> provider, boolean recursive) {
    	
    	ArrayList<Long> vals = new ArrayList<Long>();
    	  	
    	List <MongoCollectionDecorator> mongoCollections = ds.find(MongoCollectionDecorator.class).filter("provider", provider).asList();
    	
    	for(MongoCollectionDecorator p : mongoCollections){
    		Long[] tmp = getByCollection(p);
    		
           for(int i=0; i<tmp.length ;i++){
        	   vals.add(tmp[i]);
           }
    	}
    	
    	if(recursive == true){
    		
    		Set<Provider<Long>> relin = provider.getRelatedOut();
    		
    		for(Provider<Long> relpr : relin){
    			Long[] tmp  = getByProvider(relpr,false);
    	        for(int i=0; i<tmp.length ;i++){
    	        	   vals.add(tmp[i]);
    	           }			
    		}
    	}
    
        return vals.toArray(new Long[vals.size()]);
    }



    
    @Override
    public Long[] getAllIds() {
    	ArrayList<Long> vals = new ArrayList<Long>();
    	
        List<MongoMetadataRecordDecorator<Long>> res = new ArrayList<MongoMetadataRecordDecorator<Long>>();
        for (MongoMetadataRecordDecorator c : ds.find(MongoMetadataRecordDecorator.class).asList()) {
        	vals.add((Long) c.getId());
        }
        
        return vals.toArray(new Long[vals.size()]);

    }

    
    @Override
    public int getTotalByRequest(Request request) {
    	MongoRequestDecorator<Long> req = (MongoRequestDecorator<Long>) request;
    	
    	return req.getRequestrecords().size();
    }

    @Override
    public int getTotalByCollection(Collection<Long> collection) {
    	List<MongoMetadataRecordDecorator> records = ds.find(MongoMetadataRecordDecorator.class).filter("collection", collection).asList();
         
        return records.size();
    }


    // TODO recursive
    @Override
    public int getTotalByProvider(Provider provider, boolean recursive) {
    	
    	int recordcounter = 0;
    	
    	List <MongoCollectionDecorator> mongoCollections = ds.find(MongoCollectionDecorator.class).filter("provider", provider).asList();
    	
    	for(MongoCollectionDecorator p : mongoCollections){
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
    public Execution getExecution(Long id) throws StorageEngineException {
        return ds.find(MongoExecutionDecorator.class, LOCALIDFIELD, id).get();
    }


}
