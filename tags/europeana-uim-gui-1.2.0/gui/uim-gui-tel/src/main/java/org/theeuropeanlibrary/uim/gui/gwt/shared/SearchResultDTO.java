/* SearchResultDTO.java - created on May 27, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.gui.gwt.shared;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A search result object holding search records, search facets with count and the total number of
 * results.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 27, 2011
 */
public class SearchResultDTO implements IsSerializable {
    /**
     * list with search results
     */
    private List<SearchRecordDTO>   records;
    /**
     * facets uplooks
     */
    private Map<String, List<FacetValue>> facets;
    /**
     * number of records
     */
    private int                     numberRecords;

    /**
     * Creates a new instance of this class.
     */
    public SearchResultDTO() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param records
     *            list with search results
     * @param facets
     *            facets uplooks
     * @param numberRecords
     *            number of records
     */
    public SearchResultDTO(List<SearchRecordDTO> records, Map<String, List<FacetValue>> facets,
                           int numberRecords) {
        this.records = records;
        this.facets = facets;
        this.numberRecords = numberRecords;
    }

    /**
     * @return list with search results
     */
    public List<SearchRecordDTO> getRecords() {
        return records;
    }

    /**
     * @return facets uplooks
     */
    public Map<String, List<FacetValue>> getFacets() {
        return facets;
    }

    /**
     * @return number of records
     */
    public int getNumberRecords() {
        return numberRecords;
    }

    /**
     * @param records
     *            list with search results
     */
    public void setRecords(List<SearchRecordDTO> records) {
        this.records = records;
    }

    /**
     * @param facets
     *            facets uplooks
     */
    public void setFacets(Map<String, List<FacetValue>> facets) {
        this.facets = facets;
    }

    /**
     * @param numberRecords
     *            number of records
     */
    public void setNumberRecords(int numberRecords) {
        this.numberRecords = numberRecords;
    }

    /**
     * Value for a facet together with occurrence count.
     * 
     * @author Markus Muhr (markus.muhr@kb.nl)
     * @since May 27, 2011
     */
    public static class FacetValue {
        private final String value;
        private final int    count;

        /**
         * Creates a new instance of this class.
         * 
         * @param value
         * @param count
         */
        public FacetValue(String value, int count) {
            this.value = value;
            this.count = count;
        }

        /**
         * @return facet value
         */
        public String getValue() {
            return value;
        }

        /**
         * @return count of occurrence of the facet value
         */
        public int getCount() {
            return count;
        }
    }
}
