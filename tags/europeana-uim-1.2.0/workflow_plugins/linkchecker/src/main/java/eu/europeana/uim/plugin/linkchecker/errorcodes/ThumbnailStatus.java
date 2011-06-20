package eu.europeana.uim.plugin.linkchecker.errorcodes;

public enum ThumbnailStatus implements Status {
    FAILED_TO_BIND_TO_INPUT_STREAM("Failed to Bibd to Input Stream"),
    FAILED_TO_READ_ORIG("Failed to Read Original"),
    SAVE_ORIG_FAILED("Save Original Failed"),
    GENERATE_THUMB_FULL_FAILED("Generation of Full thumbnail failed"),
    GENERATE_THUMB_BRIEF_FAILED("Generation of Medium thumbnail failed"),
    GENERATE_THUMB_TINY_FAILED("Generation of Small thumbnail failed"),

    GENERAL_EXCEPTION("General Exception"),
    OTHER_ERROR("Other Error"),
    MIMETYPE_ERROR("Mimetype Error"),
    WRONG_FILESIZE("Other Error"),
    WAS_HTML_PAGE_ERROR("Was HTML page ERROR"),
    FILE_STORAGE_FAILED("File Storage Failed"),
    OBJ_CONVERT_ERROR("Object conversion ERROR"),
    DOWNLOAD_FAILED("Download Failed"),
    UNRECOGNIZED_FORMAT("Unrecoginized Format"),
    UNSUPORTED_MIMETYPE_ERROR("Unsupported Mimetype Error")
    ;

	private final String description;
	  
    
    ThumbnailStatus(String description){
    	this.description = description;
    }
    
	public String getDescription() {
		return this.description ;
	}

}
