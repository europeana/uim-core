package eu.europeana.uim;

public class MDRFieldRegistry {

	public static final TKey<MDRFieldRegistry, String> title = TKey.register(MDRFieldRegistry.class, "title", String.class);
	
	public static final TKey<MDRFieldRegistry, String> rawformat = TKey.register(MDRFieldRegistry.class, "rawformat", String.class);
	public static final TKey<MDRFieldRegistry, String> rawrecord = TKey.register(MDRFieldRegistry.class, "rawrecord", String.class);


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
