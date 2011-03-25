package eu.europeana.uim.file;

import java.io.InputStream;

import eu.europeana.uim.MetaDataRecord;
import eu.europeana.uim.MetaDataRecordHandler;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.common.ProgressMonitor;
import eu.europeana.uim.common.parse.RecordParser;
import eu.europeana.uim.common.parse.XMLStreamParserException;
import eu.europeana.uim.store.Request;

/**
 * Loads from stream {@link MetaDataRecord}s.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
public class MDRStreamLoader {
    /**
     * Creates a new instance of this class.
     */
    public MDRStreamLoader() {
        super();
    }

    /**
     * Imports ese formatted records.
     * 
     * @param <I>
     *            generic ID
     * 
     * @param data
     * @param storage
     * @param request
     * @param monitor
     * @return IDs of imported records
     * @throws XMLStreamParserException
     * @throws StorageEngineException
     */
    public <I> I[] doEseImport(InputStream data, StorageEngine<I> storage, Request<I> request,
            ProgressMonitor monitor) throws XMLStreamParserException, StorageEngineException {
        I[] ids = null;

        RecordParser parser = new RecordParser();
        MetaDataRecordHandler handler = new MetaDataRecordHandler(storage, request,
                "europeana:record");

        // parse the file/stream
        parser.parse(data, handler, monitor);
        ids = storage.getByRequest(request);
        return ids;
    }
}
