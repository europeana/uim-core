package eu.europeana.uim.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.storage.StorageEngineException;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;

/**
 * Convenient utility functions to populate provider etc. from a properties file given as stream or
 * getting it as resource.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since Mar 21, 2011
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SampleProperties {
    private static final Logger log = Logger.getLogger(SampleProperties.class.getName());

    /**
     * Loads properties as resource and stores them to the storage.
     * 
     * @param storage
     *            to update the objects in the backend
     * @throws StorageEngineException
     * @throws IOException
     */
    public void loadSampleData(StorageEngine<?> storage) throws StorageEngineException, IOException {
        InputStream stream = SampleProperties.class.getResourceAsStream("/sampledata.properties");
        loadConfigData(storage, stream);
    }

    /**
     * Stores the given properties to the storage.
     * 
     * @param storage
     *            to update the objects in the backend
     * @param stream
     *            holding properties to be used for updates
     * @throws StorageEngineException
     * @throws IOException
     */
    public void loadConfigData(StorageEngine<?> storage, InputStream stream)
            throws StorageEngineException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String line = reader.readLine();
        while (line != null) {
            if (line.trim().startsWith("#") || line.trim().length() == 0) {
            } else {
                String[] split = line.split("=");
                if (split[0].startsWith("provider")) {
                    String[] arguments = split[1].split("\\|");

                    if (arguments.length > 3) {
                        createProvider(storage, arguments[3], arguments[0], arguments[1],
                                arguments[2]);
                    } else if (split.length > 2) {
                        createProvider(storage, null, arguments[0], arguments[1], arguments[2]);
                    } else {
                        createProvider(storage, null, arguments[0], arguments[1], null);
                    }
                } else if (split[0].startsWith("oai.provurl")) {
                    String[] arguments = split[1].split("\\|");
                    Provider provider = storage.findProvider(arguments[0]);
                    if (provider != null) {
                        provider.setOaiBaseUrl(arguments[1]);
                        storage.updateProvider(provider);
                    } else {
                        log.warning("Failed to set provider oai url. Provider <" + arguments[0] +
                                    " not found.");
                    }
                } else if (split[0].startsWith("oai.provprefix")) {
                    String[] arguments = split[1].split("\\|");
                    Provider provider = storage.findProvider(arguments[0]);
                    if (provider != null) {
                        provider.setOaiMetadataPrefix(arguments[1]);
                        storage.updateProvider(provider);
                    } else {
                        log.warning("Failed to set provider oai prefix. Provider <" + arguments[0] +
                                    " not found.");
                    }
                } else if (split[0].startsWith("collection")) {
                    String[] arguments = split[1].split("\\|");
                    createCollection(storage, arguments[3], arguments[0], arguments[1],
                            arguments[2]);
                } else if (split[0].startsWith("oai.collurl")) {
                    String[] arguments = split[1].split("\\|");
                    Collection collection = storage.findCollection(arguments[0]);
                    if (collection != null) {
                        collection.setOaiBaseUrl(arguments[1]);
                        storage.updateCollection(collection);
                    } else {
                        log.warning("Failed to set collection oai url. Collection <" +
                                    arguments[0] + " not found.");
                    }
                } else if (split[0].startsWith("oai.collprefix")) {
                    String[] arguments = split[1].split("\\|");
                    Collection collection = storage.findCollection(arguments[0]);
                    if (collection != null) {
                        collection.setOaiMetadataPrefix(arguments[1]);
                        storage.updateCollection(collection);
                    } else {
                        log.warning("Failed to set collection oai prefix. Collection <" +
                                    arguments[0] + " not found.");
                    }
                } else if (split[0].startsWith("oai.collset")) {
                    String[] arguments = split[1].split("\\|");
                    Collection collection = storage.findCollection(arguments[0]);
                    if (collection != null) {
                        collection.setOaiSet(arguments[1]);
                        storage.updateCollection(collection);
                    } else {
                        log.warning("Failed to set collection oai set. Collection <" +
                                    arguments[0] + " not found.");
                    }
                }
            }
            line = reader.readLine();
        }
    }

    private Collection<?> createCollection(StorageEngine<?> storage, String parent,
            String mnemonic, String name, String language) throws StorageEngineException {
        Provider provider = storage.findProvider(parent);
        if (provider == null) {
            log.warning("Failed to create collection. Provider \"" + parent + "\" not found.");
            return null;
        }

        Collection collection = storage.createCollection(provider);
        collection.setMnemonic(mnemonic);
        collection.setName(name);
        collection.setLanguage(language);
        storage.updateCollection(collection);
        return collection;
    }

    private Provider createProvider(StorageEngine storage, String parent, String mnemonic,
            String name, String aggregator) throws StorageEngineException {

        Provider provider = storage.createProvider();
        provider.setMnemonic(mnemonic);
        provider.setName(name);
        if (aggregator != null) {
            provider.setAggregator(Boolean.parseBoolean(aggregator));
        }

        if (parent != null) {
            Provider pParent = storage.findProvider(parent);

            if (pParent != null) {
                provider.getRelatedIn().add(pParent);
                pParent.getRelatedOut().add(provider);

                storage.updateProvider(provider);
                storage.updateProvider(pParent);
            } else {
                log.warning("Failed to create provider. Parent <" + parent + "> not found.");

            }
        } else {
            storage.updateProvider(provider);
        }
        return provider;
    }
}
