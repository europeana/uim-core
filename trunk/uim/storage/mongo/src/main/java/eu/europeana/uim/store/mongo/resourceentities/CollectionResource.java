/**
 * 
 */
package eu.europeana.uim.store.mongo.resourceentities;

import java.util.LinkedHashMap;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Reference;

import eu.europeana.uim.store.mongo.decorators.MongoCollectionDecorator;

/**
 * @author geomark
 *
 */
@Entity
public class CollectionResource {

	@Id
    private ObjectId mongoid;

    @Reference
    private MongoCollectionDecorator<Long> collection;
	
	private LinkedHashMap<String, List<String>> resources;

	public ObjectId getMongoid() {
		return mongoid;
	}

	public MongoCollectionDecorator<Long> getCollection() {
		return collection;
	}

	public void setCollection(MongoCollectionDecorator<Long> collection) {
		this.collection = collection;
	}

	public LinkedHashMap<String, List<String>> getResources() {
		return resources;
	}

	public void setResources(LinkedHashMap<String, List<String>> resources) {
		this.resources = resources;
	}
}
