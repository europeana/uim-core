package eu.europeana.uim.plugin.source;

import eu.europeana.uim.store.UimDataSet;

/**
 * Abstract implementation of {@link WorkflowStart}. A name and a description
 * should be provided so that human readable details are available. As
 * identifier the simple class name is used.
 *
 * @param <U> uim data set type
 * @param <I> generic identifier
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public abstract class AbstractWorkflowStart<U extends UimDataSet<I>, I> implements
        WorkflowStart<U, I> {

    /**
     * meaningful name of this plugin
     */
    private final String name;
    /**
     * meaningful description of this plugin
     */
    private final String description;

    /**
     * Creates a new instance of this class and initializes members.
     *
     * @param name meaningful name of this plugin
     * @param description meaningful description of this plugin
     */
    public AbstractWorkflowStart(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getIdentifier() {
        return getClass().getSimpleName();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
