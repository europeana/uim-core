package eu.europeana.uim.store.memory;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import eu.europeana.uim.store.Request;

public class MemoryRequest extends AbstractMemoryEntity implements Request {

	private MemoryCollection collection;
	private Date date;
	
	public MemoryRequest(MemoryCollection collection) {
		super();
		this.collection = collection;
	}


	public MemoryRequest(long id, MemoryCollection collection) {
		super(id);
		this.collection = collection;
	}

	

	public MemoryRequest(long id, MemoryCollection collection,
			Date date) {
		super(id);
		this.collection = collection;
		this.date= date;
	}
	
	
	public String getIdentifier() {
		return "Request:" + getCollection().getMnemonic() + " " + getDate();
	}
	


	public MemoryCollection getCollection() {
		return collection;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = DateUtils.truncate(date, Calendar.SECOND);
	}

}
