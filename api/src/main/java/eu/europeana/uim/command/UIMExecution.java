package eu.europeana.uim.command;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;
import org.apache.felix.service.command.CommandSession;

import eu.europeana.uim.Registry;
import eu.europeana.uim.common.progress.LoggingProgressMonitor;
import eu.europeana.uim.orchestration.ActiveExecution;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.UimEntity;
import eu.europeana.uim.workflow.Workflow;
import java.util.concurrent.BlockingQueue;

/**
 * uim:orchestrator list start workflow (collection | provider) dataSet
 * pause requestId cancel
 * requestId status requestId
 *
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
@Command(name = "uim", scope = "exec")
public class UIMExecution implements Action {
    private static final Logger log = Logger.getLogger(UIMExecution.class.getName());

    protected enum Operation {

        list,
        start,
        pause,
        resume,
        cancel,
        status,
        help
    }

    private final Registry registry;

    private static final DateFormat df = new SimpleDateFormat("d MMM yyyy HH:mm:ss");

    @Option(name = "-o", aliases = {"--operation"}, required = false)
    protected Operation operation;

    @Argument(index = 0)
    protected String argument0;

    @Argument(index = 1)
    protected String argument1;

    @Argument(index = 2)
    protected String argument2;

    /**
     * Creates a new instance of this class.
     *
     * @param registry
     */
    public UIMExecution(Registry registry) {
        this.registry = registry;
    }

    @Override
    public Object execute(CommandSession session) throws Exception {
        PrintStream out = session.getConsole();
        if (operation == null) {
            out.println("Please specify an operation with the '-o' option. Possible values are:");
            out.println("  list\t\t\t\t\t\tlists the current executions");
            out.println("  start <workflow> <collection> [key=value,key=value]\tstarts a new execution");
            out.println("  pause <execution>\t\t\t\tpauses the given execution");
            out.println("  cancel <execution>\t\t\t\tcancels the given execution");
            out.println("  status <execution>\t\t\t\tgives status information about the given execution");
            return null;
        }

        try {
            switch (operation) {
                case list:
                    listExecutions(out);
                    break;
                case start:
                    start(out);
                    break;
                case pause:
                    pause(out);
                    break;
                case resume:
                    resume(out);
                    break;
                case cancel:
                    cancel(out);
                    break;
                case status:
                    status(out);
                    break;
                default:
                    out.println("Master, I am truly sorry but this doesn't work.");
                    break;
            }
        } catch (Throwable t) {
            log.log(Level.SEVERE, "Failed to start execution command:", t);
        }
        return null;
    }

    private void status(PrintStream out) {
        ActiveExecution<?, ?> execution = getOrListExecution(out, "status");
        if (execution != null) {
            execution.getMonitor().addListener(new LoggingProgressMonitor(Level.INFO, 2500));
        } else {
            out.println("Could not find execution to start status with ID " + argument0);
        }
    }

    private void pause(PrintStream out) {
        ActiveExecution<?, ?> execution = getOrListExecution(out, "pause");
        if (execution != null) {
            execution.setPaused(true);
            // orchestrator.pause();
        } else {
            out.println("Could not find execution to pause with ID " + argument0);
        }
    }

    private void resume(PrintStream out) {
        ActiveExecution<?, ?> execution = getOrListExecution(out, "resume");
        if (execution != null) {
            execution.setPaused(false);
            // orchestrator.pause();
        } else {
            out.println("Could not find execution to pause with ID " + argument0);
        }
    }

    private void cancel(PrintStream out) {
        ActiveExecution<?, ?> execution = getOrListExecution(out, "cancel");
        if (execution != null) {
            execution.getMonitor().setCancelled(true);

        } else {
            out.println("Could not find execution to cancel with ID " + argument0);
        }

    }

    private ActiveExecution<?, ?> getOrListExecution(PrintStream out, String command) {
        if (argument0 == null || argument0.length() == 0) {
            out.println("No can do. The correct syntax is: " + command + " <execution>");
            out.println("Possible executions are:");
            for (ActiveExecution<?, ?> e : registry.getOrchestrator().getActiveExecutions()) {
                out.println(String.format("Execution %s: Workflow %s, data set %s",
                        e.getExecution().getId(), e.getWorkflow().getName(), e.getDataSet()));
            }
            out.println();
            return null;
        } else {
            ActiveExecution<?, ?> execution = null;
            for (ActiveExecution<?, ?> e : registry.getOrchestrator().getActiveExecutions()) {
                if (e.getExecution().getId().equals(Long.parseLong(argument0))) {
                    execution = e;
                    break;
                }
            }
            return execution;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void start(PrintStream out) throws StorageEngineException {

        if (argument0 == null || argument1 == null) {
            out.println("No can do. The correct syntax is: start <workflow> <collection>");
            out.println();
        }

        StorageEngine storage = registry.getStorageEngine();

        Workflow workflow = registry.getWorkflow(argument0);
        Object id = storage.getUimId(argument1);
        Collection collection = storage.getCollection(id);

        if (workflow == null) {
            printWorfklows(out, registry.getWorkflows());
        } else if (collection == null) {
            printCollections(out, storage, storage.getAllCollections());

        } else {
            Properties properties = new Properties();
            if (argument2 != null) {
                String[] split = argument2.split("&");
                for (String keyval : split) {
                    String[] split2 = keyval.split("=");
                    if (split2.length > 1) {
                        properties.setProperty(split2[0], split2[1]);
                    }
                }
            }

            out.println();
            out.println("Starting to run worfklow '" + workflow.getName() + "' on collection '"
                    + collection.getMnemonic() + "' (" + collection.getName()
                    + ") with properties:" + properties.toString());

            ActiveExecution<?, ?> execution = registry.getOrchestrator().executeWorkflow(workflow,
                    collection, properties);
            execution.getMonitor().addListener(new LoggingProgressMonitor(Level.INFO));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }
    }

    @SuppressWarnings("unused")
    private void printProviders(PrintStream out, StorageEngine<?> storage,
            BlockingQueue<Provider<?>> providers) {
        out.println("No provider specified. Possible choices are:");
        int counter = 0;
        for (Provider<?> p : providers) {
            out.println(counter++ + ") " + p.getName());
        }
    }

    private void printCollections(PrintStream out, StorageEngine<?> storage,
            BlockingQueue<Collection<?>> collections) {
        out.println("No collection specified. Possible choices are:");
        int counter = 0;
        for (Collection<?> collection : collections) {
            out.println(counter++ + ") " + collection.getMnemonic() + ", " + collection.getName());
        }
    }

    private void printWorfklows(PrintStream out, List<Workflow<?, ?>> workflows) {
        out.println("No workflow specified. Possible choices are:");
        for (int i = 0; i < workflows.size(); i++) {
            Workflow<?, ?> w = workflows.get(i);
            out.println(i + ") " + w.getName() + " - " + w.getDescription());
        }
    }

    private void listExecutions(PrintStream out) {
        out.println("Active executions:");
        if (registry.getOrchestrator().getActiveExecutions().isEmpty()) {
            out.println("No active executions.");
        } else {
            for (ActiveExecution<?, ?> e : registry.getOrchestrator().getActiveExecutions()) {
                out.println(String.format(
                        "Execution %d: Workflow %s, data set %s, started="
                        + df.format(e.getExecution().getStartTime()) + ", active="
                        + e.getExecution().isActive() + ", paused=" + e.isPaused()
                        + ", cancelled=" + e.getMonitor().isCancelled(),
                        e.getExecution().getId(), e.getWorkflow().getName(), e.getDataSet()));
            }
        }
    }

    @SuppressWarnings("unused")
    private String getDataSetName(UimEntity<?> dataSet) {
        if (dataSet instanceof Collection) {
            return ((Collection<?>) dataSet).getName();
        }
        if (dataSet instanceof Provider) {
            return ((Provider<?>) dataSet).getName();
        }
        if (dataSet instanceof MetaDataRecord) {
            return "MetaDataRecord " + dataSet.getId();
        }
        if (dataSet instanceof Request) {
            Request<?> request = ((Request<?>) dataSet);
            return "Request on collection '" + request.getCollection().getName() + "' at "
                    + df.format(request.getDate());
        }
        return "There is no spoon.";
    }
}
