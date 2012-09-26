package org.theeuropeanlibrary.uim.gui.gwt.shared;

import com.google.gwt.view.client.ProvidesKey;

/**
 * Collection data object used in GWT for visualization.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 28, 2011
 */
public class MetaDataRecordDTO extends DataSourceDTO {
    /**
     * The key provider that provides the unique ID of a contact.
     */
    public static final ProvidesKey<MetaDataRecordDTO> KEY_PROVIDER = new ProvidesKey<MetaDataRecordDTO>() {
                                                                        @Override
                                                                        public Object getKey(
                                                                                MetaDataRecordDTO item) {
                                                                            return item == null
                                                                                    ? null
                                                                                    : item.getId();
                                                                        }
                                                                    };

    private String                                     title;

    private String                                     creator;
    private String                                     contributor;
    private String                                     year;

    private String                                     language;
    private String                                     country;

    /**
     * Creates a new instance of this class.
     */
    public MetaDataRecordDTO() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param id
     * @param title
     * @param creator
     * @param contributor
     * @param year
     * @param language
     * @param country
     */
    public MetaDataRecordDTO(Long id, String title, String creator, String contributor,
                             String year, String language, String country) {
        super(id);
        this.title = title;
        this.creator = creator;
        this.contributor = contributor;
        this.year = year;
        this.language = language;
        this.country = country;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return creator
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * @return contributor
     */
    public String getContributor() {
        return contributor;
    }

    /**
     * @param contributor
     */
    public void setContributor(String contributor) {
        this.contributor = contributor;
    }

    /**
     * @return year
     */
    public String getYear() {
        return year;
    }

    /**
     * @param year
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * @return language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
