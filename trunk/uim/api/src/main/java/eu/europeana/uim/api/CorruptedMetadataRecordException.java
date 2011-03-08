/* CorruptedMetadataException.java - created on Mar 4ßÏÍ, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.api;

import eu.europeana.uim.MetaDataRecord;

/**
 * This exception should be thrown by {@link IngestionPlugin}s during processing of a
 * {@link MetaDataRecord}, if the record has been corrupted by either the plugin itself or if it was
 * already corrupted when it got into the plugin. In either case the record is corrupted and should
 * not be processed by any other plugin.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @date Mar 4, 2011
 */
public class CorruptedMetadataRecordException extends RuntimeException {
    /**
     * For inheritance reasons, pipes through to the super constructor.
     * 
     * @param message
     *            description of the error
     */
    public CorruptedMetadataRecordException(String message) {
        super(message);
    }

    /**
     * For inheritance reasons, pipes through to the super constructor.
     * 
     * @param message
     *            description of the error
     * @param cause
     *            root cause of the error
     */
    public CorruptedMetadataRecordException(String message, Throwable cause) {
        super(message, cause);
    }
}
