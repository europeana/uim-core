package eu.europeana.uim.command;

import java.io.PrintStream;
import java.util.List;
import java.util.logging.Logger;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.osgi.service.command.CommandSession;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.workflow.Workflow;
import eu.europeana.uim.workflow.WorkflowStart;
import eu.europeana.uim.workflow.WorkflowStep;


@Command(name = "uim", scope = "workflow")
public class UIMWorkflow implements Action {

	private static final Logger log = Logger.getLogger(UIMWorkflow.class.getName());
	
	private enum Operation {
		listWorkflows
	}

	private Registry registry;

	@Option(name="-o", aliases={"--operation"}, required=false)
	private Operation operation;


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

        
        StorageEngine storage = registry.getStorage();
        switch(operation) {
        case listWorkflows: listWorkflows(storage, out); break;
		}
		return null;
	}




	/**
	 * @param storage
	 * @param out
	 */
	private void listWorkflows(StorageEngine storage, PrintStream out) {
		List<Workflow> workflows = getRegistry().getWorkflows();
		
		StringBuilder builder = new StringBuilder();
        for (Workflow workflow : workflows) {
            if (builder.length() > 0) {
                builder.append("\n\tWorkflow:");
            }
            builder.append(workflow.getName());
            WorkflowStart start = workflow.getStart();
            builder.append("\n\t\t  " + start.getIdentifier());
            List<WorkflowStep> steps = workflow.getSteps();
            for (WorkflowStep step : steps) {
				builder.append("n\t\t  " + step.getIdentifier());
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
