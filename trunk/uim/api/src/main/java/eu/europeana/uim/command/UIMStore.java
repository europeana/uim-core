package eu.europeana.uim.command;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.osgi.service.command.CommandSession;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.util.SampleProperties;

/**
 * Store for the UIM process.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @date Mar 22, 2011
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Command(name = "uim", scope = "store")
public class UIMStore implements Action {
    private enum Operation {
        createProvider, updateProvider, listProvider, createCollection, updateCollection, listCollection, checkpoint, loadSampleData
    }

    private Registry  registry;

    @Option(name = "-o", aliases = { "--operation" }, required = false)
    private Operation operation;

    @Option(name = "-p", aliases = { "--parent" })
    private String    parent;

    @Argument(index = 0)
    private String    argument0;

    @Argument(index = 1)
    private String    argument1;

    @Argument(index = 2)
    private String    argument2;

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
            out.println("  createProvider\t\t\t\t\t\t<mnemonic> <name> [true|false] the mnemonic, name and aggregator flag");
            out.println("  updateProvider\t\t\t\t\t\t<mnemonic> <field> <value> set the appropriate field value (field=oaiBaseUrl|oaiMetadataPrefix");
            out.println("  listProvider\t\t\t\t\t\tlists the providers");
            out.println("  createCollection\t\t\t\t\t\t-p provider <mnemonic> <name> the provider as well as the mnemonic and name values");
            out.println("  updateCollection\t\t\t\t\t\t<mnemonic> <field> <value> set the appropriate field value (field=oaiBaseUrl|oaiMetadataPrefix|language");
            out.println("  listCollection\t\t\t\t\t\tlists the collections");
            out.println("  checkpoint\t\t\t\t\t\tsynchronizes the storage with its backend.");
            out.println("  loadSampleData\t\t\t\t\t\tloads a set of sample provider/collections");
            return null;
        }

        StorageEngine<?> storage = registry.getStorage();
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
        case listCollection:
            listCollection(storage, out);
            break;
        case checkpoint:
            checkpoint(storage, out);
            break;
        case loadSampleData:
            new SampleProperties().loadSampleData(storage);
            break;
        }
        return null;
    }

    private Provider createProvider(StorageEngine<?> storage, PrintStream out)
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
            Provider pParent = storage.findProvider(parent);

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
        if (argument0 == null || argument1 == null || argument2 == null) {
            out.println("Failed to update provider. No arguments specified, should be <mnemonic> <field> <value>");
            return;
        }

        Provider provider = storage.findProvider(argument0);

        String method = "set" + StringUtils.capitalize(argument1);
        try {
            Method setter = provider.getClass().getMethod(method, String.class);
            setter.invoke(provider, argument2);

            storage.updateProvider(provider);
            storage.checkpoint();
        } catch (Throwable e) {
            out.println("Failed to update provider. Failed to update using method <" + method +
                        "(" + argument2 + ")");
        }
    }

    private void listProvider(StorageEngine storage, PrintStream out) throws StorageEngineException {
        Set<Provider> mainprovs = new HashSet<Provider>();
        List<Provider> providers = storage.getAllProviders();
        for (Provider provider : providers) {
            if (provider.getRelatedIn() == null || provider.getRelatedIn().isEmpty()) {
                mainprovs.add(provider);
            }
        }

        Set<Provider> processed = new HashSet<Provider>();
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

        Provider provider = storage.findProvider(parent);
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
        if (argument0 == null || argument1 == null || argument2 == null) {
            out.println("Failed to update collection. No arguments specified, should be <mnemonic> <field> <value>");
            return;
        }

        Collection collection = storage.findCollection(argument0);

        String method = "set" + StringUtils.capitalize(argument1);
        try {
            Method setter = collection.getClass().getMethod(method, String.class);
            setter.invoke(collection, argument2);

            storage.updateCollection(collection);
            storage.checkpoint();
        } catch (Throwable e) {
            out.println("Failed to update collection. Failed to update using method <" + method +
                        "(" + argument2 + ")");
        }
    }

    private void listCollection(StorageEngine storage, PrintStream out)
            throws StorageEngineException {
        if (parent == null) {
            List<Provider> providers = storage.getAllProviders();
            for (Provider provider : providers) {
                List<Collection> collections = storage.getCollections(provider);
                if (collections != null && !collections.isEmpty()) {
                    out.println(provider.getMnemonic());
                    for (Collection collection : collections) {
                        String p = "|  -+ (" + collection.getId() + ") " + collection.toString();
                        out.println(p);
                    }
                }
            }
        } else {
            Provider provider = storage.findProvider(parent);
            out.println(provider.getMnemonic());

            List<Collection> collections = storage.getCollections(provider);
            for (Collection collection : collections) {
                String p = "|  -+ (" + collection.getId() + ") " + collection.toString();
                out.println(p);
            }
        }
    }

    private void checkpoint(StorageEngine storage, PrintStream out) throws StorageEngineException {
        storage.checkpoint();
    }

    @SuppressWarnings("unused")
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
     * @return the registry
     */
    public Registry getRegistry() {
        return registry;
    }

}
