package eu.europeana.uim;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.common.parse.RecordField;
import eu.europeana.uim.common.parse.RecordHandler;
import eu.europeana.uim.common.parse.RecordMap;
import eu.europeana.uim.store.Request;

/**
 * A record handler implementation for meta data records to be used with the commons record parser
 * api.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public class MetaDataRecordHandler implements RecordHandler {
    private static final Logger log    = Logger.getLogger(MetaDataRecordHandler.class.getName());

    @SuppressWarnings("rawtypes")
    private final StorageEngine storage;
    @SuppressWarnings("rawtypes")
    private final Request       request;

    private final String        recordElement;
    private int                 count;
    private Set<Object>         unique = new HashSet<Object>();

    /**
     * Creates a new instance of this class.
     * 
     * @param <I>
     * 
     * @param storage
     * @param request
     * @param recordElement
     */
    public <I> MetaDataRecordHandler(StorageEngine<I> storage, Request<I> request,
                                     String recordElement) {
        super();
        this.storage = storage;
        this.request = request;
        this.recordElement = recordElement;
    }

    @Override
    public String getRecordElement() {
        return recordElement;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void record(RecordMap record) {
        try {
            String identifier = new String();

            for (Entry<RecordField, List<String>> entry : record.entrySet()) {
                if ("identifier".equals(entry.getKey().getLocal())) {
                    String string = entry.getValue().get(0);
                    if (string != null && string.length() > 0) {
                        identifier = entry.getValue().get(0);
                    } else {
                        identifier = "" + count;
                    }
                }
            }

            MetaDataRecord<?> mdr = storage.createMetaDataRecord(request, identifier);

            for (Entry<RecordField, List<String>> entry : record.entrySet()) {
                if ("title".equals(entry.getKey().getLocal())) {
// if (entry.getKey().getLanguage() != null) {
// for (String value : entry.getValue()) {
// mdr.addField(MDRFieldRegistry.title, entry.getKey().getLanguage(),
// value);
// }
// } else {
                    for (String value : entry.getValue()) {
                        mdr.addField(MDRFieldRegistry.title, value);
                    }
// }
                }
                count++;
            }

            if (unique.contains(mdr.getId())) {
                log.warning("Duplicate identifier:" + mdr.getId());
            } else {
                storage.updateMetaDataRecord(mdr);
                unique.add(mdr.getId());
            }
        } catch (StorageEngineException e) {
            // CAUTIOUS, EXCEPTION IS WRITTEN TO STACK TRACE
            e.printStackTrace();
        }
    }
}
