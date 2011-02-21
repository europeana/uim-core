package eu.europeana.uim.store.memory;

import eu.europeana.uim.store.UimEntity;

public class AbstractMemoryEntity implements UimEntity {

	private long id;

	public AbstractMemoryEntity(){
	}
	
	public AbstractMemoryEntity(long id) {
		this.id = id;
	}

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
