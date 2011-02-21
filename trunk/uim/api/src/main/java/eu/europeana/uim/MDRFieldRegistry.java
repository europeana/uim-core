package eu.europeana.uim;

public class MDRFieldRegistry {

	public static final TKey<MDRFieldRegistry, String> title = TKey.register(MDRFieldRegistry.class, "title", String.class);
	
	public static final TKey<MDRFieldRegistry, String> rawformat = TKey.register(MDRFieldRegistry.class, "rawformat", String.class);
	public static final TKey<MDRFieldRegistry, String> rawrecord = TKey.register(MDRFieldRegistry.class, "rawrecord", String.class);

	/*  Uri object

    uri_source  // extracted webserver part of uri   - only onne request/second per webserver SVP!!
    url         // url to be checked
    item_type       // enum URIT_LINK   only do linkcheck
                    //      URIT_THUMB  also do thumb generation
                    // if used in multiple mdrs where one is a link and one is a thumb, object should be item_type thumb
    time_created
    priority  // queue booster always include in searches! def 5  lower = higher prio higher = lower prio
    status  // enum URIS_CREATED
            //      URIS_VERIFIED  Link check
            //      URIS_COMPLETED
            //      URIS_FAILED
            //  thumb related states
            //      URIS_ORG_SAVED
            //      URIS_FULL_GENERATED
            //      URIS_BRIEF_GENERATED
    pid     // since prrocessing of this record takes several seconds, it makes sense to have a locking mechanism
            // in order to support multiple machines sharing the task
    mdr[]       // refs to all mdr using this url


    time_lastcheck  // time stamp, last check of url


    err_code    // enum URIE_NO_ERROR
                //      URIE_OTHER_ERROR
                //      URIE_TIMEOUT
                //      URIE_HTTP_ERROR
                //      URIE_HTML_ERROR
                //      URIE_URL_ERROR
                //  thumb related states
                //      URIE_MIMETYPE_ERROR
                //      URIE_UNSUPORTED_MIMETYPE_ERROR
                //      URIE_DOWNLOAD_FAILED
                //      URIE_WAS_HTML_PAGE_ERROR
                //      URIE_WRONG_FILESIZE  did not match server indicated filesize
                //      URIE_FILE_STORAGE_FAILED
                //      URIE_OBJ_CONVERT_ERROR
                //      URIE_UNRECOGNIZED_FORMAT

    err_msg     //  Further explanation of what whent wrong


    // thumb meaningfull fields
    content_hash    // hash of downloaded org obj, used for org filename and & duplicate detection
                    // sometimes thousands of different urls point to the same dummy img, then we only store it once
    url_hash    // hash of url, used as base filename for _generated_ images
    mime_type   // string as reported by webserver
    file_type   // string as reported by image magic or similar

    filenames[] // all generated files saved in filesystem, can be deleted upon deletion of uri item
                // org files might occur in multiple Uri objects, so should only be removed after garbage collection,
                // after inspecting the content_hash field

 */

/*  MDR object
        source_data     // xml blob exactly as delivered
        content_hash    // hash of source_data lowercased, and single spaced

        status          // enum of current status final states are one of ENRICHEMENT_DONE and BAD
                        // BAD indicates no further processing of this record should happen
        priority  // queue booster always include in searches! def 5  lower = higher prio higher = lower prio
        time_created    // datetime created
        time_last_change
                // this field will always be set to current time at each write, so cant
                // be changed manually, any manual intervention will be overwritten

        pid     // used in prototype, unsure if process locking will be needed in the orchestrator
                // previously was actually a float to indicate server also serverid.pid

        requests []  // all requests this mdr is a member of
        collection   // back ref for performance...
        provider     // back ref for performance...
        aggregator   // back ref for performance...

        warning  // boolean indicating something bad has happened, but not catastrophic
        err_source // module where latest warning / error happened
        err_msg    // information of error, meaningfull string or stack dump

        ese     // top of ese namespace
        link_check_done // the uris for this item has been parsed (weather success / failure irrelevant here)
        bad_uri_isShownBy  // only set if True, for single lookup, details in Uri
        bad_uri_isShownAt  // only set if True, for single lookup, details in Uri
        bad_uri_object     // only set if True, for single lookup, details in Uri

        // Future tech
        edm     // top of edm namespace
     */
}
