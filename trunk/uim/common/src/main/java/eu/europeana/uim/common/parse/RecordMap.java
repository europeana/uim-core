package eu.europeana.uim.common.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/** Derived hash map with additional convenient methods for dealing with
 * the first best values.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public class RecordMap extends HashMap<RecordField, List<String>> {

	private HashMap<RecordField, List<String>> localToPrefixed = new HashMap<RecordField, List<String>>();
	
	/**
	 */
	private static final long serialVersionUID = 1L;


	/** Namespace aware getter for the first field value
	 * 
	 * @param arg0
	 * @return the first value of the list of values for this field 
	 */
	public String getFirst(RecordField arg0){
		List<String> list = super.get(arg0);
		if (list != null && !list.isEmpty()){
			return list.iterator().next();
		}
		return null;
	}


	/** retrieves all the field elements and merges them separated
	 * by the separator
	 * 
	 * @param arg0
	 * @param separator
	 * @return string representaation of all field values.
	 */
	public String getMerged(RecordField arg0, String separator){
		StringBuilder builder = new StringBuilder();
		List<String> list = super.get(arg0);
		if (list != null && !list.isEmpty()){
			for (String string : list) {
				if (builder.length() > 0) {
					builder.append(separator);
				}
				builder.append(string);
			}
			return builder.toString();
		}
		return null;
	}


	/**  Namespace unaware getter for the first field value
	 * 
	 * @param local
	 * @return the first value 
	 */
	public String getFirstByLocal(String local){
		List<String> list = getValueByLocal(local);
		if (list != null && !list.isEmpty()){
			return list.iterator().next();
		}
		return null;
	}

	
	/** Getter for all the values with the specified local
	 * field name.
	 * 
	 * @param local
	 * @return combined list of values
	 */
	public List<String> getValueByLocal(String local){
		Set<RecordField> keys = new HashSet<RecordField>();
		Set<Entry<RecordField,List<String>>> set = localToPrefixed.entrySet();
		for (Entry<RecordField, List<String>> entry : set) {
			if (entry.getValue().contains(local)) {
				keys.add(entry.getKey());
			}
		}
		
		List<String> result = new ArrayList<String>();
		for (RecordField key : keys) {
			if (containsKey(key)) {
				result.addAll(get(key));
			}
		}
		return result;
	}
	
	

	@Override
	public List<String> put(RecordField arg0, List<String> arg1) {
		if (!localToPrefixed.containsKey(arg0)){
			localToPrefixed.put(arg0, new ArrayList<String>());
		}
		
		localToPrefixed.get(arg0).add(arg0.getLocal());
		return super.put(arg0, arg1);
	}

	
	

	/** add a new field value
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public void add(RecordField arg0, String arg1) {
		if (!localToPrefixed.containsKey(arg0)){
			localToPrefixed.put(arg0, new ArrayList<String>());
		}
		
		localToPrefixed.get(arg0).add(arg0.getLocal());
		
		if (!super.containsKey(arg0)) {
			super.put(arg0, new ArrayList<String>());
		}
		super.get(arg0).add(arg1);
	}
	
	
}
