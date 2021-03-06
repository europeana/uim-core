package eu.europeana.uim.command;

import java.io.PrintStream;
import java.util.List;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.felix.service.command.CommandSession;

import eu.europeana.uim.api.IngestionPlugin;
import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.workflow.Workflow;
import eu.europeana.uim.workflow.WorkflowStart;

/**
 * Workflow for the UIM process.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
@Command(name = "uim", scope = "workflow")
public class UIMWorkflow implements Action {

    /**
     */
    protected enum Operation {
        listWorkflows
    }

    private Registry  registry;

    @Option(name = "-o", aliases = { "--operation" }, required = false)
    protected Operation operation;

    /**
     * Creates a new instance of this class.
     * 
     * @param registry
     */
    public UIMWorkflow(Registry registry) {
        this.registry = registry;
    }

    @Override
    public Object execute(CommandSession session) throws Exception {
        PrintStream out = session.getConsole();

        if (operation == null) {
            out.println("Please specify an operation with the '-o' option. Possible values are:");
            out.println("  listWorkflows\t\t\t\t\t\tlists the workflows");
            return null;
        }

        StorageEngine<?> storage = registry.getStorageEngine();
        switch (operation) {
        case listWorkflows:
            listWorkflows(storage, out);
            break;
        }
        return null;
    }

    /**
     * @param storage
     * @param out
     */
    private void listWorkflows(StorageEngine<?> storage, PrintStream out) {
        List<Workflow> workflows = getRegistry().getWorkflows();

        StringBuilder builder = new StringBuilder();
        for (Workflow workflow : workflows) {
            if (builder.length() > 0) {
                builder.append("\n\tWorkflow:");
            }
            builder.append(workflow.getName());
            WorkflowStart start = workflow.getStart();
            builder.append("\n\t\t  " + start.getClass().getSimpleName());
            List<IngestionPlugin> steps = workflow.getSteps();
            for (IngestionPlugin step : steps) {
                builder.append("n\t\t  " + step.getClass().getSimpleName());
            }
        }
    }

    /**
     * @return the registry
     */
    public Registry getRegistry() {
        return registry;
    }
}
