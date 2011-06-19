/* Concept.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.model;

import java.util.List;

/** A general concept of all sorts with minimum skos like
 * label information.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public interface Concept {
    
    /**
     * @return the identifier for this concept
     */
    public String getIdentifier();
    
    /**
     * @param label
     * @throws NotConsistentLabelException if the added label is inconsisten according ot skos specification
     */
    public void addPrefLabel(LexicalLabel label) throws NotConsistentLabelException;
    
    /**
     * @param label
     */
    public void setPrefLabel(LexicalLabel label);

    /**
     * @return the preferred label
     */
    public List<LexicalLabel> getPrefLabel();

    /**
     * @param language 
     * @return the preferred label or null with this language
     */
    public LexicalLabel getPrefLabel(String language);
    
    /**
     * @param label
     * @throws NotConsistentLabelException if the added label is inconsisten according ot skos specification
     */
    public void addHiddenLabel(LexicalLabel label) throws NotConsistentLabelException;
    
    /**
     * @param label
     */
    public void setHiddenLabel(LexicalLabel label);

    /**
     * @return the hidden labels
     */
    public List<LexicalLabel> getHiddenLabel();

    /**
     * @param language 
     * @return the preferred label or null with this language
     */
    public LexicalLabel getHiddenLabel(String language);

    
    /**
     * @param label
     * @throws NotConsistentLabelException if the added label is inconsisten according ot skos specification
     */
    public void addAltLabel(LexicalLabel label) throws NotConsistentLabelException;
    
    /**
     * @param label
     */
    public void setAltLabel(LexicalLabel label);

    /**
     * @return alternative labels
     */
    public List<LexicalLabel> getAltLabel();

    /**
     * @param language 
     * @return the preferred label or null with this language
     */
    public LexicalLabel getAltLabel(String language);

}
