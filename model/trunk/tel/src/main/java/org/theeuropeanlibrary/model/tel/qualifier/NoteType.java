/* NoteType.java - created on 19 de Nov de 2012, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel.qualifier;


/**
 * Types of notes in bibliographic records (Usually used together with TextRelation.NOTE)
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @since 19 de Nov de 2012
 */
public enum NoteType {
    /** 
     * 300 General Notes 
     */
    GENERAL,
    /** 
     * 301 Notes Pertaining to Identification Numbers
     */
    IDENTIFIERS,
    /** 
     * 304 Notes Pertaining to Title and Statement of Responsibility
     */
    TITLE_AND_STATEMENT_OF_RESPONSABILITY,
    /** 
     * 305 Notes Pertaining to Edition and Bibliographic History
     */
    EDITION,
    /** 
     * 306 Notes Pertaining to Publication, Distribution, etc.
     */
    PUBLICATION,
    /** 
     * 308 Notes Pertaining to Series
     */
    SERIES,
    /** 
     * 310 Notes Pertaining to Binding and Availability
     */
    BINDING_OR_AVAILABILITY,
    /** 
     * 312 Notes Pertaining to Related Titles
     */
    RELATED_TITLES,
    /** 
     * 313 Notes Pertaining to Subject Access
     */
    SUBJECT,
    /** 
     * 314 Notes Pertaining to Intellectual Responsibility
     */
    INTELECTUAL_RESPONSIBILITY,
    /** 
     * 318 Action Note
     */
    PRESERVATION,
    /** 
     * 322 Credits Note (Projected and Video Material and Sound Recordings)
     */
    CREDITS,
    /** 
     * 323 Cast Note (Projected and Video Material and Sound Recordings)
     */
    PERFORMERS,
    /** 
     * 345 Acquisition Information Note
     */
    ACQUISITION,     
    /** 
     * 321 EXTERNAL INDEXES/ABSTRACTS/REFERENCES NOTE
     */
    EXTERNAL_INDEXES, 
    /** 
     * 514 DATA QUALITY NOTE
     * */
    QUALITY, 
    /** 
     * 526 Study Program information note
     *  */
    STUDY_PROGRAM,
    /** 
     * 536 Funding Information Note 
     *  */
    FUNDING, 
    /** 
     * 556 Information About Documentation Note 
     *  */
    DOCUMENTATION,
    
       
    
    
    
    
    
    
    
}
