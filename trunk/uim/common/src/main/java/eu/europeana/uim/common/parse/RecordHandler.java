package eu.europeana.uim.common.parse;


/** Interface for record handlers.
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public interface RecordHandler {

	/**
	 * @return the element within the records are parsed.
	 */
	public String getRecordElement();
	
	/** method to add a record map of all elements within the record element.
	 * 
	 * @param record
	 */
	public void record(RecordMap record);
	
}
