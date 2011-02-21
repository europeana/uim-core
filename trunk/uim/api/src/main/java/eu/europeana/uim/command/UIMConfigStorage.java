package eu.europeana.uim.command;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.felix.gogo.commands.Action;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.command.CommandSession;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngine;

/**
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */

@Command(name = "config", scope = "storage")
public class UIMConfigStorage implements Action {
	private static final Logger log = Logger.getLogger(UIMConfigStorage.class.getName());
	
    @Argument
    String storage;

    private Registry registry;

    public UIMConfigStorage(Registry registry) {
        this.registry = registry;
    }

    @Override
    public Object execute(CommandSession session) throws Exception {

        if (storage == null) {
            session.getConsole().println("Available storage engines: ");
            session.getConsole().println();
            // list available storage engines
            for(StorageEngine s : registry.getStorages()) {
                if(s == registry.getStorage()) {
                    session.getConsole().println("* " + s.getIdentifier());
                } else {
                    session.getConsole().println("  " + s.getIdentifier());
                }
            }
        } else {
            StorageEngine selected = null;
            for(StorageEngine s : registry.getStorages()) {
                if(s.getIdentifier().equals(storage)) {
                    selected = s;
                    break;
                }
            }
            if(selected != null) {
                registry.setConfiguredStorageEngine(selected.getIdentifier());
                session.getConsole().println("Activated storage engine '" + selected.getIdentifier() + "'");
                
        		Bundle bundle = FrameworkUtil.getBundle(UIMConfigStorage.class);
        		
        		ServiceReference caRef = bundle.getBundleContext().getServiceReference(ConfigurationAdmin.class.getName());
        		ConfigurationAdmin configAdmin = (ConfigurationAdmin)  bundle.getBundleContext().getService(caRef);

        		try {
        			Configuration config = configAdmin.getConfiguration("eu.europeana.uim");

        			@SuppressWarnings("unchecked")
					Dictionary<String, String> dictionary = config.getProperties();
        			if (dictionary == null) {
        				dictionary = new Hashtable<String, String>();
        			}
        			dictionary.put("defaultStorageEngine", selected.getIdentifier());
        			config.update(dictionary);
        			
        		} catch (IOException e) {
        			log.log(Level.SEVERE, "Failed to store config change with config service.", e);
        		}
                
            } else {
                session.getConsole().println("Could not find storage engine with identifier '" + storage + "'");
            }
        }
        return null;
    }
}
