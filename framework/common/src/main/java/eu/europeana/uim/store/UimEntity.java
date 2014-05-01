package eu.europeana.uim.store;

/**
 * Functionality to retrieve the unique ID for UIM entities.
 * 
 * @param <I>
 *            generic ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface UimEntity<I> {
    /**
     * @return unique ID
     */
    I getId();
    
//    List<Object> getExternalIdentifiers();
}
