/* ContextLevel.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.qualifier;

/**
 * This class specifies the level of the context so either the metadata value is about the record
 * (language of the metadata record) or about the actual object (language of the book).
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 18, 2011
 */
public enum ContextLevel {
    /**
     * value is about the metadata
     */
    RECORD,
    /**
     * value is about the actual object (text, image, etc.)
     */
    CONTENT;
}
