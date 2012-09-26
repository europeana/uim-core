package eu.europeana.uim.workflow;

import java.util.LinkedList;
import java.util.List;

import eu.europeana.uim.api.IngestionPlugin;

/**
 * Abstract implementation of {@link Workflow}. It holds a separate {@link WorkflowStart} to get
 * initial data and then it manages an arbitrary amount of {@link IngestionPlugin}s. A name and a
 * description should be provided so that human readable details are available.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 4, 2011
 */
public abstract class AbstractWorkflow implements Workflow {
    /**
     * meaningful name of this workflow
     */
    private final String                name;
    /**
     * meaningful description of this workflow
     */
    private final String                description;
    /**
     * explicit starting point of the workflow, loading or importing mostly.
     */
    private WorkflowStart               start;

    /**
     * arbitrary plugins to be executed
     */
    private final List<IngestionPlugin> steps;

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
    public AbstractWorkflow(String name, String description, WorkflowStart start) {
        this.name = name;
        this.description = description;
        this.start = start;
        this.steps = new LinkedList<IngestionPlugin>();
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
    public List<IngestionPlugin> getSteps() {
        return this.steps;
    }

    @Override
    public IngestionPlugin getStep(String identifier) {
        for (IngestionPlugin plugin : steps) {
            if (plugin.getIdentifier().equals(identifier)) { return plugin; }
        }
        return null;
    }

    @Override
    public WorkflowStart getStart() {
        return start;
    }

    /**
     * @param start
     *            defined start point of work flow
     */
    public void setStart(WorkflowStart start) {
        this.start = start;
    }

    /**
     * Adds a new plugin as a next step to the list.
     * 
     * @param step
     *            plugin to be added into the workflow
     */
    public void addStep(IngestionPlugin step) {
        steps.add(step);
    }
}
