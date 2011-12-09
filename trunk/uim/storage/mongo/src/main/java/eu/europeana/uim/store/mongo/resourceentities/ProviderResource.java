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
	
	private LinkedHashMap<String, List<String>> resources;

	public MongoProviderDecorator<Long> getProvider() {
		return provider;
	}

	public void setProvider(MongoProviderDecorator<Long> provider) {
		this.provider = provider;
	}

	public LinkedHashMap<String, List<String>> getResources() {
		return resources;
	}

	public void setResources(LinkedHashMap<String, List<String>> resources) {
		this.resources = resources;
	}

	public ObjectId getMongoid() {
		return mongoid;
	}

	
}
