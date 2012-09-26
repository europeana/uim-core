/* TitleType.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.qualifier;

/**
 * This represents the information about the status of a link at the time of last check
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @date May 26, 2011
 */
public enum LinkStatus {
    /** LinkStatus NOT_CHECKED */
    NOT_CHECKED,
    /** LinkStatus VALID */
    VALID,
    /** LinkStatus CACHED */
    CACHED,
    /** LinkStatus FAILED_CONNECTION */
    FAILED_CONNECTION,
    /** LinkStatus FAILED_DOWNLOAD */
    FAILED_DOWNLOAD,
    /** LinkStatus FAILED_FOURHUNDRED */
    FAILED_FOURHUNDRED,
    /** LinkStatus FAILED_FIVEHUNDRED */
    FAILED_FIVEHUNDRED
    
}
