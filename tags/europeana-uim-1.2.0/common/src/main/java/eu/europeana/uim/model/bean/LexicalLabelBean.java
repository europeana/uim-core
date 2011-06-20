/* LexicalLabelBean.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.model.bean;

import eu.europeana.uim.model.LexicalLabel;

/**
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public class LexicalLabelBean implements LexicalLabel {

    private final String label;
    private final String language;
    
    /**
     * Creates a new instance of this class.
     * @param label
     * @param language
     */
    public LexicalLabelBean(String label, String language) {
        this.label = label;
        this.language = language;
    }
    
    
    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getLanguage() {
        return language;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((language == null) ? 0 : language.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        LexicalLabelBean other = (LexicalLabelBean)obj;
        if (label == null) {
            if (other.label != null) return false;
        } else if (!label.equals(other.label)) return false;
        if (language == null) {
            if (other.language != null) return false;
        } else if (!language.equals(other.language)) return false;
        return true;
    }
    
    

}
