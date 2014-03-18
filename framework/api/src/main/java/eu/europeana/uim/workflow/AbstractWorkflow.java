package eu.europeana.uim.workflow;

import java.util.LinkedList;
import java.util.List;

import eu.europeana.uim.plugin.ingestion.IngestionPlugin;
import eu.europeana.uim.plugin.source.WorkflowStart;
import eu.europeana.uim.store.UimDataSet;

/**
 * Abstract implementation of {@link Workflow}. It holds a separate {@link WorkflowStart} to get
 * initial data and then it manages an arbitrary amount of {@link IngestionPlugin}s. A name and a
 * description should be provided so that human readable details are available.
 * 
 * @param <U>
 *            uim data set type
 * @param <I>
 *            generic identifier
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public abstract class AbstractWorkflow<U extends UimDataSet<I>, I> implements Workflow<U, I> {
    /**
     * meaningful name of this workflow
     */
    private final String                      name;
    /**
     * meaningful description of this workflow
     */
    private final String                      description;
    /**
     * explicit starting point of the workflow, loading or importing mostly.
     */
    private WorkflowStart<U, I>               start;

    /**
     * arbitrary plugins to be executed
     */
    private final List<IngestionPlugin<U, I>> steps;

    /**
     * Creates a new instance of this class and initializes members.
     * 
     * @param name
     *            meaningful name of this workflow
     * @param description
     *            meaningful description of this workflow
     */
    public AbstractWorkflow(String name, String description) {
        this(name, description, null);
    }

    /**
     * Creates a new instance of this class and initializes members.
     * 
     * @param name
     *            meaningful name of this workflow
     * @param description
     *            meaningful description of this workflow
     * @param start
     *            explicit starting point of the workflow, loading or importing mostly.
     */
    public AbstractWorkflow(String name, String description, WorkflowStart<U, I> start) {
        this.name = name;
        this.description = description;
        this.start = start;
        this.steps = new LinkedList<IngestionPlugin<U, I>>();
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

    @Override
    public List<IngestionPlugin<U, I>> getSteps() {
        return this.steps;
    }

    @Override
    public IngestionPlugin<U, I> getStep(String identifier) {
        for (IngestionPlugin<U, I> plugin : steps) {
            if (plugin.getIdentifier().equals(identifier)) { return plugin; }
        }
        return null;
    }

    @Override
    public WorkflowStart<U, I> getStart() {
        return start;
    }

    /**
     * @param start
     *            defined start point of work flow
     */
    public void setStart(WorkflowStart<U, I> start) {
        this.start = start;
    }

    /**
     * Adds a new plugin as a next step to the list.
     * 
     * @param step
     *            plugin to be added into the workflow
     */
    public void addStep(IngestionPlugin<U, I> step) {
        steps.add(step);
    }
}
