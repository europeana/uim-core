/**
 * 
 */
package eu.europeana.uim.store.mongo.aggregators;

import java.util.HashSet;
import org.bson.types.ObjectId;
import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import com.google.code.morphia.annotations.Serialized;
import eu.europeana.uim.store.mongo.decorators.MongoAbstractEntity;

/**
 * @author geomark
 *
 */
@Entity
public class MDRPerCollectionAggregator extends MongoAbstractEntity<String>{

	
	/**
	 * Required by Morphia (do not remove)
	 */
	public MDRPerCollectionAggregator(){
		
	}
	
	
	@Indexed (unique = true, dropDups = true)
	String collectionId;
	
	@Serialized
	HashSet<String> mdrIDs;

	/**
	 * @return the collectionId
	 */
	public String getCollectionId() {
		return collectionId;
	}

	/**
	 * @param collectionId the collectionId to set
	 */
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
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
