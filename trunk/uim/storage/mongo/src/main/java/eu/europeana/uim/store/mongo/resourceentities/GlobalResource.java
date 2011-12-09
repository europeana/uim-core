/**
 * 
 */
package eu.europeana.uim.store.mongo.resourceentities;

import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;


/**
 * 
 * @author geomark
 */
@Entity
public class GlobalResource {
	
	@Id
    private ObjectId mongoid;

	private List<String> resources;

	public ObjectId getMongoid() {
		return mongoid;
	}

	public List<String> getResources() {
		return resources;
	}

	public void setResources(List<String> resources) {
		this.resources = resources;
	}
}
