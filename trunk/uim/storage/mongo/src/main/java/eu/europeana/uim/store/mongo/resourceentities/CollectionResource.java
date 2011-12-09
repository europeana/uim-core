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
import com.google.code.morphia.annotations.Serialized;

import eu.europeana.uim.store.Collection;
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
	
	// We have to serialize this because certain characters (ie .) are 
	// not allowed to be stored directry in MongoDB
	@Serialized
	private LinkedHashMap<String, List<String>> resources;

	public CollectionResource(){
		
	}
	
	@SuppressWarnings("unchecked")
	public CollectionResource(Collection<?> collection){
		this.collection = (MongoCollectionDecorator<Long>) collection;
		this.resources = new LinkedHashMap<String, List<String>>();
	}
	
	public ObjectId getMongoid() {
		return mongoid;
	}

	public MongoCollectionDecorator<Long> getCollection() {
		return collection;
	}

	public LinkedHashMap<String, List<String>> getResources() {
		return resources;
	}

	public void setResources(LinkedHashMap<String, List<String>> resources) {
		this.resources = resources;
	}
}
