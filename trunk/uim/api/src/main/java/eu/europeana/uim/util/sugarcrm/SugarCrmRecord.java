/* SugarCrmRecord.java - created on Aug 12, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.util.sugarcrm;

import eu.europeana.uim.util.sugarcrm.data.UpdatableField;


/**
 * Shared interface for the SugarCrmRecord
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date Aug 12, 2011
 */
public interface SugarCrmRecord {

    /**
     * Updates 
     * 
     * @param field
     */
    public abstract void setItemValue(UpdatableField field);

    /**
     * @param field
     * @return the content of the field
     */
    public abstract String getItemValue(SugarCrmField field);

}