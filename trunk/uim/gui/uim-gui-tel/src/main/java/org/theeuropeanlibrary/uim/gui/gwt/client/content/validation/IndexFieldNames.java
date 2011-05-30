/* SearchFields.java - created on May 27, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.gui.gwt.client.content.validation;

import java.util.HashSet;
import java.util.Set;

/**
 * Specifies sets for index fields (should only be temporary).
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 27, 2011
 */
public class IndexFieldNames {
    /**
     * fields used plainly
     */
    public static final Set<String> plainIndexFields    = new HashSet<String>();
    static {
        plainIndexFields.add("c_mod");
        plainIndexFields.add("c_coll");
        plainIndexFields.add("c_lang_coll");
        plainIndexFields.add("c_lang_record");
        plainIndexFields.add("c_form");
        plainIndexFields.add("c_type");
        plainIndexFields.add("c_year");
        plainIndexFields.add("c_xyear");
        plainIndexFields.add("c_creator");
        plainIndexFields.add("c_person");
        plainIndexFields.add("c_corporate");
        plainIndexFields.add("c_meeting");
        plainIndexFields.add("c_spatial");
        plainIndexFields.add("c_period");
        plainIndexFields.add("c_publisher");
        plainIndexFields.add("c_source");
        plainIndexFields.add("c_macs_id");
        plainIndexFields.add("c_topic_number");
        plainIndexFields.add("f_title");
        plainIndexFields.add("f_year");
        plainIndexFields.add("f_creator");
        plainIndexFields.add("f_person");
        plainIndexFields.add("f_subject");
    }

    /**
     * language dependent fields used plainly
     */
    public static final Set<String> languageIndexFields = new HashSet<String>();
    static {
        languageIndexFields.add("title");
        languageIndexFields.add("subtitle");
        languageIndexFields.add("description");
        languageIndexFields.add("subject");
        languageIndexFields.add("others");
    }

    /**
     * languages
     */
    public static final Set<String> languages           = new HashSet<String>();
    static {
        languages.add("English (eng)");
        languages.add("German (ger)");
        languages.add("French (fre)");
    }
}
