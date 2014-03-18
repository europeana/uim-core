/**
 * 
 */
package eu.europeana.uim.store.mongo.aggregators;

import java.util.HashSet;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Serialized;

import eu.europeana.uim.store.mongo.decorators.MongoAbstractEntity;

/**
 * @author geomark
 *
 */
@Entity
public class MDRPerRequestAggregator extends MongoAbstractEntity<String>{

	/**
	 * Required by Morphia (do not remove)
	 */
	public MDRPerRequestAggregator(){
		
	}
	
	@Indexed (unique = true, dropDups = true)
	String requestId;
	

	@Serialized
	HashSet<String> mdrIDs;
	
	/**
	 * @return the requestId
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * @param requestId the requestId to set
	 */
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	/**
	 * @return the mdrIDs
	 */
	public HashSet<String> getMdrIDs() {
		return mdrIDs;
	}

	/**
	 * @param mdrIDs the mdrIDs to set
	 */
	public void setMdrIDs(HashSet<String> mdrIDs) {
		this.mdrIDs = mdrIDs;
	}
	
}
