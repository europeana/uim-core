/**
 * 
 */
package eu.europeana.uim.store.mongo.resourceentities;

import java.util.LinkedHashMap;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Serialized;

/**
 * @author geomark
 *
 */
@Entity
public class WorkflowResource {

	@Id
    private ObjectId mongoid;
	
	@Indexed
	private String workflowid;
	
	// We have to serialize this because certain characters (ie .) are 
	// not allowed to be stored directly in MongoDB
	@Serialized
	private LinkedHashMap<String, List<String>> resources;

	
	
	public WorkflowResource(String workflowid){
		this.workflowid = workflowid;
		this.resources = new LinkedHashMap<String, List<String>>();
	}
	
	public ObjectId getMongoid() {
		return mongoid;
	}

	public String getWorkflowid() {
		return workflowid;
	}

	public LinkedHashMap<String, List<String>> getResources() {
		return resources;
	}

}
