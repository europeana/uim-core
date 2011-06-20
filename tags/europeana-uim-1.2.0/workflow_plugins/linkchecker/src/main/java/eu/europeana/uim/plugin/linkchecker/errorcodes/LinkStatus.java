package eu.europeana.uim.plugin.linkchecker.errorcodes;

public enum LinkStatus implements Status{
    UNKNOWN("Unknown Error"),

    REDIRECT_DEPTH_EXCEEDED("Redirect Depth Exceeded"),
    BAD_URL("Bad URL"),
    FAILED_TO_OPEN_CONNECTION("Failed to Open Connection"),
    FAILED_TO_CONNECT("Failed to Connect"),
    HTTP_ERROR("Http error"),
    NO_RESPONSE_CODE("No Response Code"),
    ;

	private final String description;
    
    
    LinkStatus(String description){
    	this.description = description;
    }


	public String getDescription() {
		return description;
	}
    
    
}
