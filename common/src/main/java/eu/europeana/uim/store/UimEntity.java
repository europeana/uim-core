package eu.europeana.uim.store;

import java.util.Set;

/**
 * Functionality to retrieve the unique ID for UIM entities.
 *
 * @param <I> generic ID
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface UimEntity<I> {

    /**
     * @return unique ID
     */
    I getId();

//    /**
//     * @param id unique ID
//     */
//    void setId(I id);
   
//    /**
//     * @return list of all external identifiers connected to this entity
//     */
//    Set<Object> getExternalIdentifiers();
//
//    /**
//     * @param externalId external identifier connected to this entity
//     */
//    void addExternalIdentifier(Object externalId);
//    
//    /**
//     * @param externalId external identifier connected to this entity
//     */
//    void removeExternalIdentifier(Object externalId);
}
