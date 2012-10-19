/* CerifConcept.java - created on 15 de Set de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.cerif;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;

import org.theeuropeanlibrary.translation.Translations;

/**
 * Models a Concept in the CERIF subject scheme TODO: in future work, this class concept should
 * support: related concepts and preferred concepts
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 15 de Set de 2011
 */
public class CerifConcept {
    /**
     * Unique code for the concept in CERIF
     */
    private String                              code;

    /**
     * English label for the concept in CERIF
     */
    private String                              label;

    /**
     * The direct broader concept (in the CERIF hierarchy) of this one
     */
    private CerifConcept                        broader;

    /**
     * The direct narrower concepts (in the CERIF hierarchy) of this one LinkedHashMap is used to
     * preserve insertion order
     */
    private LinkedHashMap<String, CerifConcept> narrower;

    private String translationKey;

    /**
     * Creates a new instance of this class.
     * 
     * @param code
     *            Unique code for the concept in CERIF
     * @param label
     *            English label for the concept in CERIF
     * @param broader
     *            The direct broader concept (in the CERIF hierarchy) of this one
     */
    public CerifConcept(String code, String label, CerifConcept broader) {
        super();
        this.code = code;
        this.label = label;
        this.broader = broader;
        this.translationKey = "cerif.ortelius." + code + ".label";
        narrower = new LinkedHashMap<String, CerifConcept>();
    }

    /**
     * Add a direct narrower concept (in the CERIF hierarchy) of this one
     * 
     * @param narrowerConcepth
     */
    public void addNarrower(CerifConcept narrowerConcepth) {
        narrower.put(narrowerConcepth.getCode(), narrowerConcepth);
    }

    /**
     * @return the narrower the direct narrower concepts (in the CERIF hierarchy) of this one
     */
    public Collection<CerifConcept> getNarrower() {
        return narrower.values();
    }

    /**
     * @return Unique code for the concept in CERIF
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            Unique code for the concept in CERIF
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return English label for the concept in CERIF
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param locale
     * @return Label for the concept in given locale
     */
    public String getLabel(Locale locale) {
        return Translations.getTranslation(translationKey, locale, label);
    }
    
    
    /**
     * @param locale
     * @return Description of the the concept in the given locale
     */
    public String getDescription(Locale locale) {
        return Translations.getTranslation("cerif.ortelius." + code + ".description",locale);
    }

    /**
     * @param label
     *            English label for the concept in CERIF
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * @return The direct broader concept (in the CERIF hierarchy) of this one
     */
    public CerifConcept getBroader() {
        return broader;
    }

    /**
     * @param broader
     *            The direct broader concept (in the CERIF hierarchy) of this one
     */
    public void setBroader(CerifConcept broader) {
        this.broader = broader;
    }

    /**
     * @return level in the CERIF hierarchy (top level is level 1)
     */
    public int getHierarchyLevel() {
        if (broader == null) return 1;
        return broader.getHierarchyLevel() + 1;
    }

    @Override
    public String toString() {
        return code + " - " + label;
    }
}
