/**
 * 
 */
package eu.europeana.uim.store.mongo;

import org.bson.types.ObjectId;

import eu.europeana.uim.api.ResourceEngine;
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
			MongoCollectionDecorator<String> tmp = new MongoCollectionDecorator<String>();
			ObjectId id = ObjectId.massageToObjectId(uimType.getId());
			tmp.setMongoId(id);
			return tmp;
		}
		if (uimType instanceof ProviderBean) {
			MongoProviderDecorator<String> tmp = new MongoProviderDecorator<String>();
			ObjectId id = ObjectId.massageToObjectId(uimType.getId());
			tmp.setMongoId(id);
			return tmp;
		}
		return uimType;
	}
}
