/**
 * 
 */
package eu.europeana.uim.sugarcrmclient.plugin.objects.queries;

/**
 * @author Georgios Markakis
 *
 */
public enum EqOp {

	IS("="),
	LIKE("LIKE"),
	GREATER_THAN(">"),
	LESS_THAN("<");
	
	private final String value;
	
	EqOp(String value){
	   this.value = value;
	}


	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
}
