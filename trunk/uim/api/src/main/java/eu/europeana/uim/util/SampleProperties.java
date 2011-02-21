package eu.europeana.uim.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.logging.Logger;

import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.command.UIMStore;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;

public class SampleProperties {
	private static final Logger log = Logger.getLogger(SampleProperties.class.getName());
	
	public void loadSampleData(StorageEngine storage) throws StorageEngineException, IOException {
		InputStream stream = SampleProperties.class.getResourceAsStream("/sampledata.properties");
		loadPropertiesData(storage, stream);
	}
	
	
	
	public void loadPropertiesData(StorageEngine storage, InputStream stream) throws StorageEngineException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

		String line = reader.readLine();
		while (line != null) {
			if (line.trim().startsWith("#") ||
					line.trim().length() == 0) {
			} else {
				String[] split = line.split("=");
				if (split[0].startsWith("provider")) {
					String[] arguments = split[1].split("\\|");
					
					if (arguments.length > 3) {
						createProvider(storage, arguments[3], arguments[0], arguments[1], arguments[2]);
					} else 	if (split.length > 2) {
						createProvider(storage, null, arguments[0], arguments[1], arguments[2]);
					} else 	{
						createProvider(storage, null, arguments[0], arguments[1], null);
					}
				} else if (split[0].startsWith("oai.provurl")) {
					String[] arguments = split[1].split("\\|");
					Provider provider = storage.findProvider(arguments[0]);
					if (provider != null) {
						provider.setOaiBaseUrl(arguments[1]);
						storage.updateProvider(provider);
					} else {
						log.warning("Failed to set provider oai url. Provider <" + arguments[0] + " not found.");
					}
				} else if (split[0].startsWith("oai.provprefix")) {
					String[] arguments = split[1].split("\\|");
					Provider provider = storage.findProvider(arguments[0]);
					if (provider != null) {
						provider.setOaiMetadataPrefix(arguments[1]);
						storage.updateProvider(provider);
					} else {
						log.warning("Failed to set provider oai prefix. Provider <" + arguments[0] + " not found.");
					}
				} else if (split[0].startsWith("collection")) {
					String[] arguments = split[1].split("\\|");
					createCollection(storage, arguments[2], arguments[0], arguments[1]);
				} else if (split[0].startsWith("oai.collurl")) {
					String[] arguments = split[1].split("\\|");
					Collection collection = storage.findCollection(arguments[0]);
					if (collection != null) {
						collection.setOaiBaseUrl(arguments[1]);
						storage.updateCollection(collection);
					} else {
						log.warning("Failed to set collection oai url. Collection <" + arguments[0] + " not found.");
					}
				} else if (split[0].startsWith("oai.collprefix")) {
					String[] arguments = split[1].split("\\|");
					Collection collection = storage.findCollection(arguments[0]);
					if (collection != null) {
						collection.setOaiMetadataPrefix(arguments[1]);
						storage.updateCollection(collection);
					} else {
						log.warning("Failed to set collection oai prefix. Collection <" + arguments[0] + " not found.");
					}
				} else if (split[0].startsWith("oai.collset")) {
					String[] arguments = split[1].split("\\|");
					Collection collection = storage.findCollection(arguments[0]);
					if (collection != null) {
						collection.setOaiSet(arguments[1]);
						storage.updateCollection(collection);
					} else {
						log.warning("Failed to set collection oai set. Collection <" + arguments[0] + " not found.");
					}
				}
			}
			line = reader.readLine();
		}
	}
	
	
	private Collection createCollection(StorageEngine storage, String parent, String mnemonic, String name) throws StorageEngineException {
		Provider provider = storage.findProvider(parent);
		if (provider == null) {
			log.warning("Failed to create collection. Provider \"" + parent + "\" not found.");
			return null;
		}

		Collection collection = storage.createCollection(provider);
		collection.setMnemonic(mnemonic);
		collection.setName(name);
		storage.updateCollection(collection);
		return collection;
	}
	
	
	
	private Provider createProvider(StorageEngine storage, String parent, String mnemonic, String name, String aggregator) throws StorageEngineException {

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
