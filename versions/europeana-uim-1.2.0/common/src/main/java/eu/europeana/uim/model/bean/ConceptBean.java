/* ConceptBean.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.model.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.europeana.uim.model.Agent;
import eu.europeana.uim.model.Concept;
import eu.europeana.uim.model.LexicalLabel;
import eu.europeana.uim.model.NotConsistentLabelException;
import eu.europeana.uim.model.Spatial;
import eu.europeana.uim.model.Temporal;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public class ConceptBean implements Concept, Agent, Temporal, Spatial {

    private final String identifier;
    
    private Map<String, LexicalLabel> prefLabels = new HashMap<String, LexicalLabel>(); 
    private Map<String, LexicalLabel> altLabels = new HashMap<String, LexicalLabel>(); 
    private Map<String, LexicalLabel> hiddenLabels = new HashMap<String, LexicalLabel>(); 

    /**
     * Creates a new instance of this class.
     * @param identifier
     */
    public ConceptBean(String identifier){
        this.identifier = identifier;
    }
    
    

    /**
     * Returns the identifier.
     * @return the identifier
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }


    @Override
    public List<LexicalLabel> getPrefLabel() {
        ArrayList<LexicalLabel> result = new ArrayList<LexicalLabel>();
        result.addAll(prefLabels.values());
        return result;
    }

    @Override
    public void setPrefLabel(LexicalLabel label) {
        prefLabels.put(label.getLanguage(), label);
    }

    @Override
    public LexicalLabel getPrefLabel(String language) {
        return prefLabels.get(language);
    }

    @Override
    public void addPrefLabel(LexicalLabel label) throws NotConsistentLabelException {
        if (prefLabels.containsKey(label.getLanguage())) {
            LexicalLabel oldLabel = prefLabels.get(label.getLanguage());
            if (!oldLabel.getLabel().equals(label.getLabel())){
                throw new NotConsistentLabelException("Label <" + label + "> is not consistent with <" + oldLabel + ">");
            }
        }
        prefLabels.put(label.getLanguage(), label);
    }

    
    
    @Override
    public List<LexicalLabel> getHiddenLabel() {
        ArrayList<LexicalLabel> result = new ArrayList<LexicalLabel>();
        result.addAll(hiddenLabels.values());
        return result;
    }

    @Override
    public List<LexicalLabel> getAltLabel() {
        ArrayList<LexicalLabel> result = new ArrayList<LexicalLabel>();
        result.addAll(altLabels.values());
        return result;
    }


    @Override
    public void setAltLabel(LexicalLabel label) {
        altLabels.put(label.getLanguage(), label);
    }

    @Override
    public LexicalLabel getAltLabel(String language) {
        return altLabels.get(language);
    }

    @Override
    public void addAltLabel(LexicalLabel label) throws NotConsistentLabelException {
        if (altLabels.containsKey(label.getLanguage())) {
            LexicalLabel oldLabel = altLabels.get(label.getLanguage());
            if (!oldLabel.getLabel().equals(label.getLabel())){
                throw new NotConsistentLabelException("Label <" + label + "> is not consistent with <" + oldLabel + ">");
            }
        }
        altLabels.put(label.getLanguage(), label);
    }

    @Override
    public void addHiddenLabel(LexicalLabel label) throws NotConsistentLabelException {
        if (hiddenLabels.containsKey(label.getLanguage())) {
            LexicalLabel oldLabel = hiddenLabels.get(label.getLanguage());
            if (!oldLabel.getLabel().equals(label.getLabel())){
                throw new NotConsistentLabelException("Label <" + label + "> is not consistent with <" + oldLabel + ">");
            }
        }
        hiddenLabels.put(label.getLanguage(), label);
    }

    @Override
    public void setHiddenLabel(LexicalLabel label) {
        hiddenLabels.put(label.getLanguage(), label);
    }

    @Override
    public LexicalLabel getHiddenLabel(String language) {
        return hiddenLabels.get(language);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        ConceptBean other = (ConceptBean)obj;
        if (identifier == null) {
            if (other.identifier != null) return false;
        } else if (!identifier.equals(other.identifier)) return false;
        return true;
    }


}
