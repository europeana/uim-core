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
	
	private LinkedHashMap<String, List<String>> resources;

	
	public ObjectId getMongoid() {
		return mongoid;
	}

	public String getWorkflowid() {
		return workflowid;
	}

	public void setWorkflowid(String workflowid) {
		this.workflowid = workflowid;
	}

	public LinkedHashMap<String, List<String>> getResources() {
		return resources;
	}

	public void setResources(LinkedHashMap<String, List<String>> resources) {
		this.resources = resources;
	}
}
