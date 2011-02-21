package eu.europeana.uim.common.parse;

import java.util.ArrayList;
import java.util.List;

/** A memory based record handler which holds a list of
 * the record maps.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public class MemoryRecordHandler implements RecordHandler {

	private final String recordElement;
	private final List<RecordMap> memory = new ArrayList<RecordMap>();
	
	/**
	 * Creates a new instance of this class which "listens" to elements 
	 * packed in the given record element
	 * @param recordElement
	 */
	public MemoryRecordHandler(String recordElement){
		this.recordElement = recordElement;
	}
	
	@Override
	public String getRecordElement() {
		return this.recordElement;
	}

	
	@Override
	public void record(RecordMap record) {
		memory.add(record);
	}

	
	/**
	 * @return the memory
	 */
	public List<RecordMap> getMemory() {
		return memory;
	}

	
	
}
