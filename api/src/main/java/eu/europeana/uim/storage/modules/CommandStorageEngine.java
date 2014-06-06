package eu.europeana.uim.storage.modules;

/**
 * Base class for storage engine typed with a ID class.
 *
 * @param <I> generic ID
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
public interface CommandStorageEngine<I> {

    /**
     * @param command arbitrary command interpreted by the engine implementation
     */
    void command(String command);
}
