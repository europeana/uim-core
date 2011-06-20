package eu.europeana.uim.plugin.linkchecker.exceptions;

import eu.europeana.uim.plugin.linkchecker.errorcodes.Status;

public class ImageTransformationException extends GenericLinkCheckerException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public ImageTransformationException(String message) {
		super(message);
	}
	
	/**
	 * @param status
	 */
	public ImageTransformationException(Status status) {
		super(status);
	}

}
