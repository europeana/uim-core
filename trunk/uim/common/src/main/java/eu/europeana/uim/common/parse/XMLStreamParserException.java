package eu.europeana.uim.common.parse;

/** Typed exception for parsing errors.

 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public class XMLStreamParserException extends Exception {


	/**
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Creates a new instance of this class.
	 * @param arg0 the message of this exception
	 */
	public XMLStreamParserException(String arg0) {
		super(arg0);
	}


	/**
	 * Creates a new instance of this class.
	 * @param arg0 the massage of this exception
	 * @param arg1 the causing throwable
	 */
	public XMLStreamParserException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
