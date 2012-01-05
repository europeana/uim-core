/**
 * 
 */
package eu.europeana.uim.store.mongo.decorators;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Indexed;

/**
 * 
 * @author Georgios Markakis
 */

@Entity
public abstract class MongoAbstractNamedEntity<I> extends MongoAbstractEntity<I> {

	@Indexed
	private String searchMnemonic;

	public MongoAbstractNamedEntity(){
		super();
	}
	
	public MongoAbstractNamedEntity(I id){
		super(id);
	}
	
	public String getSearchMnemonic() {
		return searchMnemonic;
	}

	public void setSearchMnemonic(String searchMnemonic) {
		this.searchMnemonic = searchMnemonic;
	}
	
	
}
