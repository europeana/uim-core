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

import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.mongo.decorators.MongoProviderDecorator;

/**
 * @author geomark
 *
 */
@Entity
public class ProviderResource {

	@Id
    private ObjectId mongoid;
	
	@Reference
	private  MongoProviderDecorator<Long> provider;
	
	// We have to serialize this because certain characters (ie .) are 
	// not allowed to be stored directly in MongoDB
	@Serialized
	private LinkedHashMap<String, List<String>> resources;


	public ProviderResource(){
	}
	
	@SuppressWarnings("unchecked")
	public ProviderResource(Provider<?> provider){
		this.provider = (MongoProviderDecorator<Long>) provider;
		this.resources = new LinkedHashMap<String, List<String>>();
		
	}
	
	public MongoProviderDecorator<Long> getProvider() {
		return provider;
	}


	public LinkedHashMap<String, List<String>> getResources() {
		return resources;
	}

	public ObjectId getMongoid() {
		return mongoid;
	}

	
}
