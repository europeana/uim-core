package eu.europeana.uim.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.apache.karaf.shell.commands.Action;
import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.commands.Option;
import org.apache.felix.service.command.CommandSession;

import eu.europeana.uim.Registry;
import eu.europeana.uim.plugin.ingestion.IngestionPlugin;
import eu.europeana.uim.resource.ResourceEngine;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.util.SampleProperties;
import eu.europeana.uim.workflow.Workflow;
import java.util.concurrent.BlockingQueue;

/**
 * Store for the UIM process.
 *
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 22, 2011
 */
@SuppressWarnings({"all"})
@Command(name = "uim", scope = "store")
public class UIMStore implements Action {

    private static final Logger log = Logger.getLogger(UIMStore.class.getName());

    protected enum Operation {

        createProvider("<mnemonic> <name> [true|false] the mnemonic, name and aggregator flag"),
        updateProvider(
                "<mnemonic> <field> <value> set the appropriate field value (field=oaiBaseUrl|oaiMetadataPrefix"),
        listProvider("lists the providers"),
        createCollection(
                "p provider <mnemonic> <name> the provider as well as the mnemonic and name values"),
        updateCollection(
                "<mnemonic> <field> <value> set the appropriate field value (field=oaiBaseUrl|oaiMetadataPrefix|language"),
        addBlacklistWorkflow(
                "Puts the given workflow onto the blacklist (stored in resource engine)"),
        removeBlacklistWorkflow(
                "Remove the given workflow from the blacklist (stored in resource engine)"),
        listCollection("lists the collections"),
        listGlobalResources("lists the global resources"),
        listProviderResources("lists the provider resources"),
        listCollectionResources("lists the collection resources"),
        checkpoint("creates a checkpoint (a data synchronization)"),
        loadConfigData("loads a set of provider/collections"),
        loadSampleData("loads a set of sample provider/collections");

        private final String desc;

        private Operation(String desc) {
            this.desc = desc;
        }

        public String getDescription() {
            return desc;
        }
    }

    private final Registry registry;

    @Option(name = "-o", aliases = {"--operation"}, required = false)
    protected Operation operation;

    @Option(name = "-p", aliases = {"--parent"})
    protected String parent;

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
    public UIMStore(Registry registry) {
        this.registry = registry;
    }

    @Override
    public Object execute(CommandSession session) throws Exception {
        PrintStream out = session.getConsole();

        if (operation == null) {
            out.println("Please specify an operation with the '-o' option. Possible values are:");
            for (Operation o : Operation.values()) {
                out.println(o.toString() + "\t" + o.getDescription());
            }
            return null;
        }

        try {
            StorageEngine storage = registry.getStorageEngine();
            ResourceEngine resource = registry.getResourceEngine();

            switch (operation) {
                case createProvider:
                    createProvider(storage, out);
                    break;
                case updateProvider:
                    updateProvider(storage, out);
                    break;
                case listProvider:
                    listProvider(storage, out);
                    break;
                case createCollection:
                    createCollection(storage, out);
                    break;
                case updateCollection:
                    updateCollection(storage, out);
                    break;
                case addBlacklistWorkflow:
                    addBlacklistWorkflow(resource, argument0);
                    break;
                case removeBlacklistWorkflow:
                    removeBlacklistWorkflow(resource, argument0);
                    break;
                case listCollection:
                    listCollection(storage, out);
                    break;
                case listGlobalResources:
                    listGlobalResources(storage, resource, out);
                    break;
                case listProviderResources:
                    listProviderResources(storage, resource, out);
                    break;
                case listCollectionResources:
                    listCollectionResources(storage, resource, out);
                    break;
                case checkpoint:
                    checkpoint(storage, out);
                    break;
                case loadConfigData:
                    new SampleProperties().loadConfigData(storage, new FileInputStream(new File(
                            argument0)));
                    break;
                case loadSampleData:
                    new SampleProperties().loadSampleData(storage);
                    break;
            }
        } catch (Throwable t) {
            log.log(Level.SEVERE, "Failed to start storage command:", t);
        }

        return null;
    }

    /**
     * key for blacklisted workflows in resource engine
     */
    public static List<String> blackListKey = new ArrayList<String>() {
        {
            add("Workflow Blacklist");
        }
    };

    private void removeBlacklistWorkflow(ResourceEngine resource, String blacklistWorkflow) {
        LinkedHashMap<String, List<String>> resources = resource.getGlobalResources(blackListKey);
        List<String> blackList = resources.get(blackListKey.get(0));
        if (blackList == null) {
            return;
        }
        boolean remove = true;
        while (remove) {
            remove = blackList.remove(blacklistWorkflow);
        }
        resource.setGlobalResources(resources);
    }

    private void addBlacklistWorkflow(ResourceEngine resource, String blacklistWorkflow) {
        LinkedHashMap<String, List<String>> resources = resource.getGlobalResources(blackListKey);
        List<String> blackList = resources.get(blackListKey.get(0));
        if (blackList == null) {
            blackList = new ArrayList<>();
            resources.put(blackListKey.get(0), blackList);
        }
        blackList.add(blacklistWorkflow);
        resource.setGlobalResources(resources);
    }

    /**
     * @param resource
     * @param out
     */
    private <I> void listGlobalResources(StorageEngine<I> storage, ResourceEngine resource,
            PrintStream out) {
        List<Workflow<?, ?>> workflows = new ArrayList<>();
        Workflow workflow = registry.getWorkflow(argument0);
        if (workflow == null) {
            workflows = registry.getWorkflows();
        } else {
            workflows.add(workflow);
        }

        for (Workflow current : workflows) {
            List<String> keys = getParameters(current);

            LinkedHashMap<String, List<String>> resources = resource.getGlobalResources(keys);
            out.println("Global Resources for <" + current.getIdentifier() + ">:"
                    + resources.toString() + "\n");
        }

        LinkedHashMap<String, List<String>> blackWorkflow = resource.getGlobalResources(blackListKey);
        out.println("Blacklisted Workflows are:" + blackWorkflow.toString() + "\n");
    }

    /**
     * @param resource
     * @param out
     * @throws StorageEngineException
     */
    private <I> void listProviderResources(StorageEngine<I> storage, ResourceEngine resource,
            PrintStream out) throws StorageEngineException {
        List<Workflow<?, ?>> workflows = new ArrayList<>();
        Workflow workflow = registry.getWorkflow(argument0);
        if (workflow == null) {
            workflows = registry.getWorkflows();
        } else {
            workflows.add(workflow);
        }

        for (Workflow current : workflows) {
            List<String> keys = getParameters(current);

            BlockingQueue<Provider<I>> providers = storage.getAllProviders();
            for (Provider provider : providers) {
                LinkedHashMap<String, List<String>> resources = resource.getProviderResources(
                        provider, keys);
                out.println("Provider " + provider.getMnemonic() + " Resources for <"
                        + current.getIdentifier() + ">:" + resources.toString());

            }
        }
    }

    private List<String> getParameters(Workflow current) {
        List<String> keys = new ArrayList<>();
        keys.addAll(current.getStart().getParameters());
        List<IngestionPlugin<?, ?>> steps = current.getSteps();
        for (IngestionPlugin plugin : steps) {
            keys.addAll(plugin.getParameters());
        }
        return keys;
    }

    /**
     * @param resource
     * @param out
     * @throws StorageEngineException
     */
    private <I> void listCollectionResources(StorageEngine<I> storage, ResourceEngine resource,
            PrintStream out) throws StorageEngineException {
        List<Workflow<?, ?>> workflows = new ArrayList<>();
        Workflow workflow = registry.getWorkflow(argument0);
        if (workflow == null) {
            workflows = registry.getWorkflows();
        } else {
            workflows.add(workflow);
        }

        for (Workflow current : workflows) {
            List<String> keys = getParameters(current);

            BlockingQueue<Provider<I>> providers = storage.getAllProviders();
            for (Provider provider : providers) {
                BlockingQueue<Collection<I>> collections = storage.getCollections(provider);
                if (collections != null && !collections.isEmpty()) {
                    out.println(provider.getMnemonic());
                    for (Collection collection : collections) {
                        LinkedHashMap<String, List<String>> resources = resource.getCollectionResources(
                                collection, keys);
                        out.println("Collection " + collection.getMnemonic() + " Resources for <"
                                + current.getIdentifier() + ">:" + resources.toString() + "\n");
                    }
                }
            }
        }
    }

    private Provider createProvider(StorageEngine storage, PrintStream out)
            throws StorageEngineException {
        if (argument0 == null || argument1 == null) {
            out.println("Failed to create provider. No arguments specified, should be <mnemonic> <name> [<true|false>]");
            return null;
        }

        Provider provider = storage.createProvider();
        provider.setMnemonic(argument0);
        provider.setName(argument1);
        if (argument2 != null) {
            provider.setAggregator(Boolean.parseBoolean(argument2));
        }

        if (parent != null) {
            Object id = storage.getUimId(parent);
            Provider pParent = storage.getProvider(id);

            if (pParent != null) {
                provider.getRelatedIn().add(pParent);
                pParent.getRelatedOut().add(provider);

                storage.updateProvider(provider);
                storage.updateProvider(pParent);
            } else {
                out.println("Failed to create provider. Parent <" + parent + "> not found.");

            }
        } else {
            storage.updateProvider(provider);
        }

        storage.checkpoint();
        return provider;
    }

    private void updateProvider(StorageEngine storage, PrintStream out)
            throws StorageEngineException {
        if (argument0 == null || argument1 == null) {
            out.println("Failed to update provider. No arguments specified, should be <mnemonic> <field> <value>");
            return;
        }

        Object id = storage.getUimId(argument0);
        Provider provider = storage.getProvider(id);

        String method = "set" + StringUtils.capitalize(argument1);
        try {
            Method setter = provider.getClass().getMethod(method, String.class);
            setter.invoke(provider, argument2);

            storage.updateProvider(provider);
            storage.checkpoint();

            out.println("Successfully executed " + method + "(" + argument2 + ")");
        } catch (Throwable e) {
            out.println("Failed to update provider. Failed to update using method <" + method
                    + "(" + argument2 + ") reason:" + e.getLocalizedMessage());
            e.printStackTrace(out);
        }
    }

    private void listProvider(StorageEngine storage, PrintStream out) throws StorageEngineException {
        Set<Provider> mainprovs = new HashSet<>();
        BlockingQueue<Provider> providers = storage.getAllProviders();
        for (Provider provider : providers) {
            if (provider.getRelatedIn() == null || provider.getRelatedIn().isEmpty()) {
                mainprovs.add(provider);
            }
        }

        Set<Provider> processed = new HashSet<>();
        printTree(mainprovs, processed, "+-", out);
    }

    private void printTree(Set<Provider> providers, Set<Provider> processed, String indent,
            PrintStream out) {
        for (Provider provider : providers) {

            String p = "(" + provider.getId() + ") " + provider.toString();
            out.println(indent + p);

            if (!processed.contains(provider)) {
                processed.add(provider);
                if (provider.getRelatedOut() != null && !provider.getRelatedOut().isEmpty()) {
                    String in = indent.substring(0, indent.length() - 2);
                    printTree(provider.getRelatedOut(), processed, in + "|  +-", out);
                }
            }
        }
    }

    private Collection createCollection(StorageEngine storage, PrintStream out)
            throws StorageEngineException {
        if (argument0 == null || argument1 == null || parent == null) {
            out.println("Failed to create collection. No arguments specified, should be -p provider <mnemonic> <name>");
            return null;
        }

        Object id = storage.getUimId(parent);
        Provider provider = storage.getProvider(id);
        if (provider == null) {
            out.println("Failed to create collection. Provider \"" + parent + "\" not found.");
            return null;
        }

        Collection collection = storage.createCollection(provider);
        collection.setMnemonic(argument0);
        collection.setName(argument1);
        storage.updateCollection(collection);
        storage.checkpoint();

        return collection;
    }

    private void updateCollection(StorageEngine storage, PrintStream out)
            throws StorageEngineException {
        if (argument0 == null || argument1 == null) {
            out.println("Failed to update collection. No arguments specified, should be <mnemonic> <field> <value>");
            return;
        }
        
        Object id = storage.getUimId(argument0);
        Collection collection = storage.getCollection(id);

        String method = "set" + StringUtils.capitalize(argument1);
        try {
            Method setter = collection.getClass().getMethod(method, String.class);
            if ("null".equalsIgnoreCase(argument2)) {
                argument2 = null;
            }
            setter.invoke(collection, argument2);

            storage.updateCollection(collection);
            storage.checkpoint();

            out.println("Successfully executed " + method + "(" + argument2 + ")");
        } catch (Throwable e) {
            out.println("Failed to update collection. Failed to update using method <" + method
                    + "(\"" + argument2 + "\") reason:" + e.getMessage());
            e.printStackTrace(out);
        }
    }

    private void listCollection(StorageEngine storage, PrintStream out)
            throws StorageEngineException {
        if (parent == null) {
            BlockingQueue<Provider> providers = storage.getAllProviders();
            for (Provider provider : providers) {
                BlockingQueue<Collection> collections = storage.getCollections(provider);
                if (collections != null && !collections.isEmpty()) {
                    out.println(provider.getMnemonic());
                    for (Collection collection : collections) {
                        String p = "|  -+ (" + collection.getId() + ") " + collection.toString();
                        out.println(p);
                    }
                }
            }
        } else {
            Object id = storage.getUimId(parent);
            Provider provider = storage.getProvider(id);
            out.println(provider.getMnemonic());

            BlockingQueue<Collection> collections = storage.getCollections(provider);
            for (Collection collection : collections) {
                String p = "|  -+ (" + collection.getId() + ") " + collection.toString();
                out.println(p);
            }
        }
    }

    private void checkpoint(StorageEngine storage, PrintStream out) throws StorageEngineException {
        storage.checkpoint();
    }

    // @SuppressWarnings("unused")
    private void setFieldValues(String[] split) {
        String[] arguments = split[1].split("\\|");
        this.argument0 = null;
        this.argument1 = null;
        this.argument2 = null;
        this.parent = null;

        this.argument0 = arguments[0];
        if (arguments.length > 1) {
            this.argument1 = arguments[1];
        }
        if (arguments.length > 2) {
            this.argument2 = arguments[2];
        }
        if (arguments.length > 3) {
            this.parent = arguments[3];
        }
    }

    /**
     * @return registry
     */
    public Registry getRegistry() {
        return registry;
    }
}
