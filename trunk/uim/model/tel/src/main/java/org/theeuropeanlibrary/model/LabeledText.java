/* Places.java - created on Mar 21, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model;

import org.theeuropeanlibrary.model.common.FieldId;
import org.theeuropeanlibrary.model.common.Text;


/**
 * Any textual content, or textual description of a resource, with a label provided by TEL. The
 * label is in English, and is meant to inform the user about the meaning of the content, when the
 * meaning is lost at data transformation from the source format.
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 21, 2011
 */
public class LabeledText extends Text {
    /**
     * Text in English, meant to inform the user about the meaning of the content, when the meaning
     * was lost at data transformation from the source format
     */
    @FieldId(2)
    private String label;

    /**
     * Creates a new instance of this class.
     */
    public LabeledText() {
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param textualContent
     *            textual content
     * @param label
     *            Text in English, meant to inform the user about the meaning of the content, when
     *            the meaning was lost at data transformation from the source format
     */
    public LabeledText(String textualContent, String label) {
        super(textualContent);
        this.label = label;
    }

    /**
     * @return Text in English, meant to inform the user about the meaning of the content, when the
     *         meaning was lost at data transformation from the source format
     */
    public String getLabel() {
        return label;
    }

    /**
     * @param label
     *            Text in English, meant to inform the user about the meaning of the content, when
     *            the meaning was lost at data transformation from the source format
     */
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        LabeledText other = (LabeledText)obj;
        if (label == null) {
            if (other.label != null) return false;
        } else if (!label.equals(other.label)) return false;
        return true;
    }
}
