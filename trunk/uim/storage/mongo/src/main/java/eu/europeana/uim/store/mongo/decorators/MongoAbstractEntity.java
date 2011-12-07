/**
 * 
 */
package eu.europeana.uim.store.mongo.decorators;

import org.bson.types.ObjectId;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;

import eu.europeana.uim.store.UimDataSet;
import eu.europeana.uim.store.UimEntity;

/**
 * 
 * 
 * @author Georgios Markakis
 */

@Entity
public class MongoAbstractEntity<I>{

    @Id
    private ObjectId mongoId;
	
	@Indexed
	private I lid;

	
	public MongoAbstractEntity(){
	}
	
	public MongoAbstractEntity(I lid){
		this.lid = lid;
	}
	
	
	public ObjectId getMongoId() {
		return mongoId;
	}

	public void setMongoId(ObjectId mongoId) {
		this.mongoId = mongoId;
	}

	public I getLid() {
		return lid;
	}

	public void setLid(I lid) {
		this.lid = lid;
	}

}
