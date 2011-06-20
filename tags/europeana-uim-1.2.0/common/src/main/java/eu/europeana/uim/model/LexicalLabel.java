/* LexicalLabel.java - created on Jun 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.model;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 18, 2011
 */
public interface LexicalLabel {

    /**
     * @return the lexial/textual label
     */
    String getLabel();
    
    
    /**
     * @return the language of this label
     */
    String getLanguage();
    
}
