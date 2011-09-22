/* UimControlledVocabulary.java - created on Sep 3, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.store;


/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Sep 3, 2011
 */
public enum StandardControlledVocabulary implements ControlledVocabularyKeyValue {
    /**
     * country
     */
    COUNTRY,
    /**
     * prototype
     */
    PROVTYPE,
    /**
     * type of collection
     */
    COLLTYPE;

    private StandardControlledVocabulary() {
    }

    @Override
    public String getFieldId() {
        return this.toString();
    }

}
