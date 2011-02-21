package eu.europeana.uim.common.parse;

/** A simple representation of an XML record
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public class RecordField {

	private final String prefix;
	private final String local;
	private final String language;
	
	
	/**
	 * Creates a new instance of this class.
	 * @param prefix
	 * @param local
	 * @param language
	 */
	public RecordField(String prefix, String local, String language) {
		super();
		this.prefix = prefix;
		this.local = local;
		this.language = language;
	}


	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}


	/**
	 * @return the local
	 */
	public String getLocal() {
		return local;
	}


	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	
	@Override
	public String toString() {
		String field = "";
		field += prefix == null ? "" : prefix + ":";
		field += local;
		field += language == null ? "" : "@" + language;
		return field;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((local == null) ? 0 : local.hashCode());
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecordField other = (RecordField) obj;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (local == null) {
			if (other.local != null)
				return false;
		} else if (!local.equals(other.local))
			return false;
		if (prefix == null) {
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
			return false;
		return true;
	}
	
	
	
}
