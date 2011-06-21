package org.theeuropeanlibrary.uim.gui.gwt.shared;

import com.google.gwt.view.client.ProvidesKey;

/**
 * Collection data object used in GWT for visualization.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Apr 28, 2011
 */
public class SearchRecordDTO extends DataSourceDTO {
    /**
     * The key provider that provides the unique ID of a contact.
     */
    public static final ProvidesKey<SearchRecordDTO> KEY_PROVIDER = new ProvidesKey<SearchRecordDTO>() {
                                                                      @Override
                                                                      public Object getKey(
                                                                              SearchRecordDTO item) {
                                                                          return item == null
                                                                                  ? null
                                                                                  : item.getId();
                                                                      }
                                                                  };

    private String                                   title;
    private String                                   creator;
    private String                                   year;

    /**
     * Creates a new instance of this class.
     */
    public SearchRecordDTO() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param id
     * @param title
     * @param creator
     * @param year
     */
    public SearchRecordDTO(Long id, String title, String creator, String year) {
        super(id);
        this.title = title;
        this.creator = creator;
        this.year = year;
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
}
