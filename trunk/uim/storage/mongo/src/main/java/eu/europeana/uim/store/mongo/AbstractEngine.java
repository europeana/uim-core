/**
 * 
 */
package eu.europeana.uim.store.mongo;

import org.bson.types.ObjectId;

import eu.europeana.uim.api.ResourceEngine;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.UimEntity;
import eu.europeana.uim.store.bean.CollectionBean;
import eu.europeana.uim.store.bean.ProviderBean;
import eu.europeana.uim.store.mongo.decorators.MongoCollectionDecorator;
import eu.europeana.uim.store.mongo.decorators.MongoProviderDecorator;

/**
 * @author geomark
 *
 */
public abstract class AbstractEngine {

	public AbstractEngine(){
		
	}
	
	/**
	 * Method that gurantees that the UIM Entity which is used for
	 * search purposes will always be converted to MongoDB entity type which
	 * contains a valid ObjectID reference.
	 * 
	 * @param uimType
	 * @return an appropriate MongoDB entity type
	 */
	 UimEntity<?> ensureConsistency(UimEntity<?> uimType) {
		if (uimType instanceof CollectionBean) {
			
			CollectionBean<String> coll = (CollectionBean<String>) uimType;
			
			@SuppressWarnings("rawtypes")
			MongoProviderDecorator<String> provref = new MongoProviderDecorator<String>();
			ProviderBean embprov = (ProviderBean) coll.getProvider();
			provref.setEmbeddedProvider(embprov);
			
			ObjectId provid = ObjectId.massageToObjectId(coll.getProvider().getId());
			provref.setMongoId(provid);			
			MongoCollectionDecorator<String> tmp = new MongoCollectionDecorator<String>(provref);
			tmp.setEmbeddedCollection(coll);
			ObjectId id = ObjectId.massageToObjectId(uimType.getId());
			tmp.setMongoId(id);
			
			return tmp;
		}
		if (uimType instanceof ProviderBean) {
			ProviderBean<String> prov = (ProviderBean<String>) uimType;
			MongoProviderDecorator<String> tmp = new MongoProviderDecorator<String>();
			tmp.setEmbeddedProvider(prov);
			ObjectId id = ObjectId.massageToObjectId(uimType.getId());
			tmp.setMongoId(id);
			return tmp;
		}
		return uimType;
	}
}
