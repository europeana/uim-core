/**
 * 
 */
package eu.europeana.uim.store.mongo.resourceentities;

import java.util.LinkedHashMap;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Serialized;


/**
 * 
 * @author geomark
 */
@Entity
public class GlobalResource {
	
	@Id
    private ObjectId mongoid;

	// We have to serialize this because certain characters (ie .) are 
	// not allowed to be stored directly in MongoDB
	@Serialized
	private LinkedHashMap<String, List<String>> resources;

	
	public GlobalResource(){
		resources = new LinkedHashMap<String, List<String>>();
	}
	
	
	
	/**
	 * @return the resources
	 */
	public LinkedHashMap<String, List<String>> getResources() {
		return resources;
	}

	public ObjectId getMongoid() {
		
		return mongoid;
	}


}
