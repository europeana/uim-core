/* CollectionValidation.java - created on 20 de Ago de 2012, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.edm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

/**
 * 
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 20 de Ago de 2012
 */
public class EdmValidationReport {
    
    int recordCount;
    int invalidRecords;
    
    HashMap<String, Integer> errorMessagesCounts=new HashMap<String, Integer>();
    
    public void addValidRecord() {
        recordCount++;
    }

    public void addInvalidRecord(List<String> errors) {
        recordCount++;
        invalidRecords++;
        for(String error: new HashSet<String>(errors)) {
            Integer msgCount = errorMessagesCounts.get(error);
            if(msgCount==null)
                errorMessagesCounts.put(error, 1);
            else
                errorMessagesCounts.put(error, msgCount+1);
        }
    }

    public int getRecordCount() {
        return recordCount;
    }

    public int getInvalidRecords() {
        return invalidRecords;
    }

    public HashMap<String, Integer> getErrorMessagesCounts() {
        return errorMessagesCounts;
    }

    public ArrayList<Entry<String, Integer>> getErrorMessagesCountsSorted() {
        ArrayList<Entry<String, Integer>> entries=new ArrayList<Entry<String,Integer>>(errorMessagesCounts.entrySet());
        Collections.sort(entries, new Comparator<Entry<String, Integer>>(){
            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });
        return entries;
    }

    /**
     * @return
     */
    public int getValidRecords() {
        return recordCount - invalidRecords;
    }

    public float getValidRecordsPercent() {
        return (float)getValidRecords() * 100 / recordCount ;
    }
    
    
    
}
