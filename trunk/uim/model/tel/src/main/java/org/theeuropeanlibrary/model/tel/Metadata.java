/* Places.java - created on Mar 21, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel;

import org.theeuropeanlibrary.model.common.FieldId;


/**
 * A metadata record represented as a XML string. Used to store the original record from the data
 * provider, and possibly other metadata records during ingestion
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 * @since 9 de Mai de 2011
 */
public class Metadata {
    /**
     * A metadata record represented as a XML string
     */
    @FieldId(1)
    private String recordInXml;

    /**
     * The format of the record
     */
    @FieldId(2)
    private String format;

    /**
     * Creates a new instance of this class.
     */
    public Metadata() {
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param recordInXml
     * @param format
     */
    public Metadata(String recordInXml, String format) {
        if (recordInXml == null) { throw new IllegalArgumentException(
                "Argument 'recordInXml' should not be null!"); }
        if (format == null) { throw new IllegalArgumentException(
                "Argument 'format' should not be null!"); }
        this.recordInXml = recordInXml;
        this.format = format;
    }

    /**
     * @return Text in English, meant to inform the user about the meaning of the content, when the
     *         meaning was lost at data transformation from the source format
     */
    public String getFormat() {
        return format;
    }

    /**
     * @param label
     *            Text in English, meant to inform the user about the meaning of the content, when
     *            the meaning was lost at data transformation from the source format
     */
    public void setFormat(String label) {
        this.format = label;
    }

    /**
     * @return A metadata record represented as a XML string
     */
    public String getRecordInXml() {
        return recordInXml;
    }

    /**
     * @param recordInXml
     *            A metadata record represented as a XML string
     */
    public void setRecordInXml(String recordInXml) {
        this.recordInXml = recordInXml;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((format == null) ? 0 : format.hashCode());
        result = prime * result + ((recordInXml == null) ? 0 : recordInXml.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Metadata other = (Metadata)obj;
        if (format == null) {
            if (other.format != null) return false;
        } else if (!format.equals(other.format)) return false;
        if (recordInXml == null) {
            if (other.recordInXml != null) return false;
        } else if (!recordInXml.equals(other.recordInXml)) return false;
        return true;
    }
    
    
}
