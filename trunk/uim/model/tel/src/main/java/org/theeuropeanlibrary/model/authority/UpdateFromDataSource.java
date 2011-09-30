/* UpdateFromDataSource.java - created on 25 de Jul de 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.authority;

import java.util.Date;

import org.theeuropeanlibrary.model.common.FieldId;

/**
 * Holds the last update date at a data source
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 25 de Jul de 2011
 */
public class UpdateFromDataSource {
    /**
     * An identifier of the data source
     */
    @FieldId(1)
    private String dataSourceId;

    /**
     * The latest update of the record in the data source
     */
    @FieldId(2)
    private Date   updateDate;

    /**
     * Creates a new instance of this class.
     */
    public UpdateFromDataSource() {
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param dataSourceId
     *            An identifier of the data source
     * @param updateDate
     *            The latest update of the record in the data source
     */
    public UpdateFromDataSource(String dataSourceId, Date updateDate) {
        super();
        this.dataSourceId = dataSourceId;
        this.updateDate = updateDate;
    }

    /**
     * @return An identifier of the data source
     */
    public String getDataSourceId() {
        return dataSourceId;
    }

    /**
     * @param dataSourceId
     *            An identifier of the data source
     */
    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    /**
     * @return The latest update of the record in the data source
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     *            The latest update of the record in the data source
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
