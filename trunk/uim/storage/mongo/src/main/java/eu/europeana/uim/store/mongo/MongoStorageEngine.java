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
public class MongoStorageEngine extends AbstractEngine implements StorageEngine<String> {

    private static final String DEFAULT_UIM_DB_NAME = "UIM";
    private static final String MNEMONICFIELD = "searchMnemonic";
    private static final String NAMEFIELD = "searchName"; 
    private static final String LOCALIDFIELD = "mongoId";
    private static final String RECORDUIDFIELD = "uniqueID";
    
    
    
    
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
    public void completed(ExecutionContext<String> context) {
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
    public Provider<String> createProvider() {
        Provider<String> p = new MongoProviderDecorator<String>();
        ds.save(p);
        return p;
    }

    @Override
    public void updateProvider(Provider<String> provider) throws StorageEngineException {
    	    	
    	ArrayList<MongoProviderDecorator> allresults = new ArrayList<MongoProviderDecorator>();
		ArrayList<MongoProviderDecorator> result1 = (ArrayList<MongoProviderDecorator>) ds.find(MongoProviderDecorator.class).filter(NAMEFIELD, provider.getName()).asList();
    	ArrayList<MongoProviderDecorator>  result2 = (ArrayList<MongoProviderDecorator>) ds.find(MongoProviderDecorator.class).filter(MNEMONICFIELD, provider.getMnemonic()).asList();	

    	allresults.addAll(result1);
    	allresults.addAll(result2);


        for (Provider<String> p : allresults) {
            if (p.getName() != null && (p.getName().equals(provider.getName()) || p.getMnemonic().equals(provider.getMnemonic())) && !p.getId().equals(provider.getId())) {
                throw new StorageEngineException("Provider with name '" + provider.getMnemonic() + "' already exists");
            }
            if (p.getMnemonic() != null && p.getMnemonic().equals(provider.getMnemonic()) && !p.getId().equals(provider.getId())) {
                throw new StorageEngineException("Provider with mnemonic '" + provider.getMnemonic() + "' already exists");
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

    @Override
    public Provider<String> getProvider(String id) {
        return ds.find(MongoProviderDecorator.class).filter(LOCALIDFIELD,new ObjectId(id)).get();
    }

    @Override
    public Provider<String> findProvider(String mnemonic) {
        return ds.find(MongoProviderDecorator.class).field(MNEMONICFIELD).equal(mnemonic).get();
    }

    @Override
    public List<Provider<String>> getAllProviders() {
        final List<Provider<String>> res = new ArrayList<Provider<String>>();
        for (Provider<String> p : ds.find(MongoProviderDecorator.class).asList()) {
            res.add(p);
        }
        return res;
    }

    @Override
    public Collection<String> createCollection(Provider<String> provider) {
        Collection<String> c = new MongoCollectionDecorator<String>(provider);
        ds.save(c);
        return c;
    }

    @Override
    public void updateCollection(Collection<String> collection) throws StorageEngineException {
    	
    	ArrayList<MongoCollectionDecorator> allresults = new ArrayList<MongoCollectionDecorator>();
    	
        
		ArrayList<MongoCollectionDecorator> result1 = (ArrayList<MongoCollectionDecorator>) ds.find(MongoCollectionDecorator.class).filter(NAMEFIELD, collection.getName()).asList();
    	ArrayList<MongoCollectionDecorator>  result2 = (ArrayList<MongoCollectionDecorator>) ds.find(MongoCollectionDecorator.class).filter(MNEMONICFIELD, collection.getMnemonic()).asList();	

    	allresults.addAll(result1);
    	allresults.addAll(result2);
    	
        for (Collection<String> c : allresults) {
            if (c.getName() != null && (c.getName().equals(collection.getName())) && !c.getId().equals(collection.getId())) {
                throw new StorageEngineException("Collection with name '" + collection.getMnemonic() + "' already exists");
            }
            if (c.getMnemonic() != null && c.getMnemonic().equals(collection.getMnemonic()) && !c.getId().equals(collection.getId())) {
                throw new StorageEngineException("Collection with mnemonic '" + collection.getMnemonic() + "' already exists");
            }

        }
        ds.merge(collection);
    }

    @Override
    public Collection<String> getCollection(String id) {
        return ds.find(MongoCollectionDecorator.class).filter(LOCALIDFIELD, new ObjectId(id)).get();
    }

    @Override
    public Collection<String> findCollection(String mnemonic) {
        return ds.find(MongoCollectionDecorator.class).filter(MNEMONICFIELD, mnemonic).get();
    }

    @Override
    public List<Collection<String>> getCollections(Provider<String> provider) {
        List<Collection<String>> res = new ArrayList<Collection<String>>();
        for (Collection<String> c : ds.find(MongoCollectionDecorator.class).filter("provider", provider).asList()) {
            res.add(c);
        }
        return res;
    }

    @Override
    public List<Collection<String>> getAllCollections() {
        List<Collection<String>> res = new ArrayList<Collection<String>>();
        for (Collection<String> c : ds.find(MongoCollectionDecorator.class).asList()) {
            res.add(c);
        }
        return res;
    }

    @Override
    public Request<String> createRequest(Collection<String> collection, Date date) throws StorageEngineException {
		collection = (Collection<String>) ensureConsistency(collection);    	
        Request<String> r = new MongoRequestDecorator<String>((MongoCollectionDecorator<String>) collection, date);
        ds.save(r);
        return r;
    }

    @Override
    public void updateRequest(Request<String> request) throws StorageEngineException {
    	
    	MongoRequestDecorator<String> request2 = (MongoRequestDecorator<String>)request;
    		
        for (MongoRequestDecorator<?> r : ds.find(MongoRequestDecorator.class).filter("collection", request2.getCollectionReference()).asList()) {
            if (r.getDate().equals(request.getDate()) && !r.getId().equals(request2.getId())) {
                String unique = "REQUEST/" + request2.getCollection().getMnemonic() + "/" + request2.getDate();
                throw new IllegalStateException("Duplicate unique key for request: <" + unique + ">");
            }
        }
    	
        ds.merge(request);
    }

    @Override
    public List<Request<String>> getRequests(Collection<String> collection) {
        List<Request<String>> res = new ArrayList<Request<String>>();
        for (Request<String> r : ds.find(MongoRequestDecorator.class).filter("collection", collection).asList()) {
            res.add(r);
        }
        return res;
    }

    @Override
    public Request<String> getRequest(String id) throws StorageEngineException {
        return ds.find(MongoRequestDecorator.class).filter(LOCALIDFIELD, new ObjectId(id)).get();
    }
    
    @Override
    public List<Request<String>> getRequests(MetaDataRecord<String> mdr) throws StorageEngineException {
    	ds.find(MongoRequestDecorator.class).filter(LOCALIDFIELD, mdr).get();
		return null;
    }

    
    @Override
    public MetaDataRecord<String>  createMetaDataRecord(Collection<String>  collection, String identifier) throws StorageEngineException {
    	MongoMetadataRecordDecorator<String>  mdr = new MongoMetadataRecordDecorator<String>((MongoCollectionDecorator<String>) collection,identifier);
    	ds.save(mdr);
		return mdr;
    }

    
    
    @Override
    public void addRequestRecord(Request<String> request, MetaDataRecord<String> record)
            throws StorageEngineException {
        
    	MongoRequestDecorator<String> req = (MongoRequestDecorator<String>)request;
  
    	req.getRequestrecords().add((MongoMetadataRecordDecorator<String>) record);
    	
    	ds.merge(req);
    }

    
    
    @Override
    public void updateMetaDataRecord(MetaDataRecord<String> record) throws StorageEngineException {
    	ds.merge(record);
    }

    @Override
    public Execution<String> createExecution(UimDataSet<String> entity, String workflow) throws StorageEngineException {
        MongoExecutionDecorator<String> me = new MongoExecutionDecorator<String>();
        me.setDataSet(entity);
        me.setWorkflow(workflow);
        ds.save(me);
        return me;
    }

    @Override
    public void updateExecution(Execution<String> execution) throws StorageEngineException {
        ds.merge(execution);
    }

    @Override
    public List<Execution<String>> getAllExecutions() {
        List<Execution<String>> res = new ArrayList<Execution<String>>();
        for (Execution<String> e : ds.find(MongoExecutionDecorator.class).asList()) {
            res.add(e);
        }
        return res;
    }

    @Override
    public List<MetaDataRecord<String>> getMetaDataRecords(List<String> ids) {
        ArrayList<MetaDataRecord<String>> res = new ArrayList<MetaDataRecord<String>>();
        BasicDBObject query = new BasicDBObject();
        query.put(RECORDUIDFIELD, new BasicDBObject("$in", ids));
        for (DBObject object : records.find(query)) {
            Request<String> request = ds.find(MongoRequestDecorator.class).filter(RECORDUIDFIELD, object.get("request")).get();
            //res.add(new MongoMetadataRecord(object, request.getCollection(), (String) object.get("identifier"), ((Long) object.get(LOCALIDFIELD)).longValue()));
        }

        return res;
    }

    @Override
    public MetaDataRecord<String> getMetaDataRecord(String id) throws StorageEngineException {

    	@SuppressWarnings("unchecked")
		MongoMetadataRecordDecorator<String> request = ds.find(MongoMetadataRecordDecorator.class).filter(RECORDUIDFIELD, id).get();
		return request;
    }


    @Override
    public String[] getByRequest(Request<String> request) {
    	
    	MongoRequestDecorator<String> cast = (MongoRequestDecorator<String>)request;
    	HashSet<MongoMetadataRecordDecorator<String>> reqrecords = cast.getRequestrecords();
    	String[] res = new String[reqrecords.size()];
    	
    	int i = 0;
    	
    	for(MongoMetadataRecordDecorator<String> rec : reqrecords){
    		
    		res[i] = rec.getId();
    		
    		i++;
    	}

        return res;
    }

    @Override
    public String[] getByCollection(Collection<String> collection) {
		collection = (Collection<String>) ensureConsistency(collection);
    	ArrayList<String> ids = new ArrayList<String>();
    	List <MongoMetadataRecordDecorator> reqrecords = ds.find(MongoMetadataRecordDecorator.class).filter("collection", collection).asList();
    	String[] res = new String[reqrecords.size()];
    	
    	int i = 0;
    	for(MongoMetadataRecordDecorator<String> rec : reqrecords){
    		
    		res[i] = rec.getId();
    		
    		i++;
    	}
        return res;
    }

    
    @Override
    public String[] getByProvider(Provider<String> provider, boolean recursive) {
    	provider = (Provider<String>) ensureConsistency(provider);
    	ArrayList<String> vals = new ArrayList<String>();
    	  	
    	List <MongoCollectionDecorator> mongoCollections = ds.find(MongoCollectionDecorator.class).filter("provider", provider).asList();
    	
    	for(MongoCollectionDecorator<String> p : mongoCollections){
    		String[] tmp = getByCollection(p);
    		
           for(int i=0; i<tmp.length ;i++){
        	   vals.add(tmp[i]);
           }
    	}
    	
    	if(recursive == true){
    		
    		Set<Provider<String>> relin = provider.getRelatedOut();
    		
    		for(Provider<String> relpr : relin){
    			String[] tmp  = getByProvider(relpr,false);
    	        for(int i=0; i<tmp.length ;i++){
    	        	   vals.add(tmp[i]);
    	           }			
    		}
    	}
    
        return vals.toArray(new String[vals.size()]);
    }



    
    @Override
    public String[] getAllIds() {
    	ArrayList<String> vals = new ArrayList<String>();
    	
        List<MongoMetadataRecordDecorator<String>> res = new ArrayList<MongoMetadataRecordDecorator<String>>();
        for (MongoMetadataRecordDecorator<String> c : ds.find(MongoMetadataRecordDecorator.class).asList()) {
        	vals.add(c.getId());
        }
        
        return vals.toArray(new String[vals.size()]);

    }

    
    @Override
    public int getTotalByRequest(Request<String> request) {
    	MongoRequestDecorator<String> req = (MongoRequestDecorator<String>) request;
    	
    	return req.getRequestrecords().size();
    }

    @Override
    public int getTotalByCollection(Collection<String> collection) {
    	List<MongoMetadataRecordDecorator> records = ds.find(MongoMetadataRecordDecorator.class).filter("collection", collection).asList();
         
        return records.size();
    }


    // TODO recursive
    @Override
    public int getTotalByProvider(Provider<String> provider, boolean recursive) {
    	
    	int recordcounter = 0;
    	
    	List <MongoCollectionDecorator> mongoCollections = ds.find(MongoCollectionDecorator.class).filter("provider", provider).asList();
    	
    	for(MongoCollectionDecorator<String> p : mongoCollections){
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
    public Execution<String> getExecution(String id) throws StorageEngineException {
        return ds.find(MongoExecutionDecorator.class, LOCALIDFIELD, new ObjectId(id)).get();
    }


}
