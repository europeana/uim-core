/**
 * 
 */
package eu.europeana.uim.store.mongo;

import java.util.Iterator;
import java.util.Set;

import org.bson.types.ObjectId;

import eu.europeana.uim.api.ResourceEngine;
import eu.europeana.uim.common.TKey;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.store.UimEntity;
import eu.europeana.uim.store.bean.CollectionBean;
import eu.europeana.uim.store.bean.ExecutionBean;
import eu.europeana.uim.store.bean.MetaDataRecordBean;
import eu.europeana.uim.store.bean.ProviderBean;
import eu.europeana.uim.store.bean.RequestBean;
import eu.europeana.uim.store.mongo.decorators.MongoCollectionDecorator;
import eu.europeana.uim.store.mongo.decorators.MongoExecutionDecorator;
import eu.europeana.uim.store.mongo.decorators.MongoMetadataRecordDecorator;
import eu.europeana.uim.store.mongo.decorators.MongoProviderDecorator;
import eu.europeana.uim.store.mongo.decorators.MongoRequestDecorator;

/**
 * @author geomark
 *
 */
public abstract class AbstractEngine {

    /** The key holding the MONGOID identifier in the Metadata Record.
     */
    public static final TKey<AbstractEngine, String>  MONGOID  = 
    		TKey.register(AbstractEngine.class,"mongoid",String.class);
	
	public AbstractEngine(){
		
	}
	
	/**
	 * Method that guarantees that the UIM Entity which is used for
	 * search purposes will always be converted to MongoDB entity type which
	 * contains a valid ObjectID reference.
	 * 
	 * @param uimType
	 * @return an appropriate MongoDB entity type
	 */
	 UimEntity<?> ensureConsistency(UimEntity<?> uimType) {
		if (uimType instanceof CollectionBean) {
			
			CollectionBean<String> coll = (CollectionBean<String>) uimType;
			return convertCollectionBean2Decorator(coll);
		}
		if (uimType instanceof ProviderBean) {
			ProviderBean<String> prov = (ProviderBean<String>) uimType;
			return convertProviderBean2Decorator(prov,true);
		}
		if (uimType instanceof RequestBean) {
			
			RequestBean<String> req = (RequestBean<String>)uimType; 
			
			MongoCollectionDecorator<String> colldec = convertCollectionBean2Decorator((CollectionBean<String>) req.getCollection());
			MongoRequestDecorator<String> requestDec = new MongoRequestDecorator<String>(colldec,req.getDate());
			ObjectId reqid = ObjectId.massageToObjectId(req.getId());
			requestDec.setMongoId(reqid);
			requestDec.setEmbeddedRequest(req);
			
			return requestDec;
		}
		if (uimType instanceof MetaDataRecordBean) {
			
			MetaDataRecordBean<String> mdr = (MetaDataRecordBean<String>) uimType;
			MongoCollectionDecorator<String> colldec = convertCollectionBean2Decorator((CollectionBean<String>) mdr.getCollection());
			
			String mongoID = mdr.getFirstValue(MONGOID);
			
			MongoMetadataRecordDecorator<String> mdrdec = new MongoMetadataRecordDecorator<String>(colldec,mdr.getId()); 
			ObjectId reqid = ObjectId.massageToObjectId(mongoID);
			mdrdec.setMongoId(reqid);
			mdrdec.setEmebeddedMdr(mdr);
			
			return mdrdec;
		}
		if (uimType instanceof ExecutionBean) {
			ExecutionBean<String> exbean = (ExecutionBean<String>) uimType;
			UimDataSet<String> dataset = (UimDataSet<String>) ensureConsistency(exbean.getDataSet());
			
			MongoExecutionDecorator<String> exdec = new MongoExecutionDecorator<String>(exbean.getId());
			ObjectId reqid = ObjectId.massageToObjectId(exbean.getId());
			exdec.setMongoId(reqid);
			exdec.setEmbeddedExecution(exbean);
			exdec.setDatasetRefrerence(dataset);
			
			return exdec;
		}
		return uimType;
	}
	 
	 
	 /**
	 * @param provbean
	 * @return
	 */
	private MongoProviderDecorator<String> convertProviderBean2Decorator(ProviderBean<String> provbean,boolean recursive){
		
			MongoProviderDecorator<String> provdec = new MongoProviderDecorator<String>();
			provdec.setEmbeddedProvider(provbean);
			ObjectId id = ObjectId.massageToObjectId(provbean.getId());
			provdec.setMongoId(id);
			provdec.setName(provbean.getName());
			provdec.setSearchMnemonic(provbean.getMnemonic());
			
			if(recursive == true){		
			
			Set<Provider<String>> relin = provbean.getRelatedIn();
			Iterator<Provider<String>> initerator = relin.iterator();			
			while(initerator.hasNext()){
				MongoProviderDecorator<String> tmpprovdec = convertProviderBean2Decorator((ProviderBean<String>) initerator.next(),false);
				provdec.getRelatedIn().add(tmpprovdec);
			}
			
			Set<Provider<String>> relout = provbean.getRelatedOut();
			Iterator<Provider<String>> outiterator = relout.iterator();
			while(outiterator.hasNext()){
				MongoProviderDecorator<String> tmpprovdec = convertProviderBean2Decorator((ProviderBean<String>) outiterator.next(),false);
				provdec.getRelatedOut().add(tmpprovdec);
			}
			}
			
			
		 return provdec;
		 
	 }
	 
	 /**
	 * @param collbean
	 * @return
	 */
	private MongoCollectionDecorator<String> convertCollectionBean2Decorator(CollectionBean<String> collbean){
		 
		 MongoProviderDecorator<String> provdec = convertProviderBean2Decorator((ProviderBean<String>) collbean.getProvider(),true);
		 
		MongoCollectionDecorator<String> colldec = new MongoCollectionDecorator<String>(provdec);
		colldec.setEmbeddedCollection(collbean);
		ObjectId id = ObjectId.massageToObjectId(collbean.getId());
		colldec.setMongoId(id);
		colldec.setName(collbean.getName());
		colldec.setMnemonic(collbean.getMnemonic());
		return colldec;
		 
	 }
	 
}
