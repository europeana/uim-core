/* AbstractBaseResourceEngine.java - created on May 4, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.management.RuntimeErrorException;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.CompositeConfiguration;

import eu.europeana.uim.api.ResourceEngine;

/**
 * Abstract base class for storage engines. Implements the configuration order logic (execution,
 * collection, provider, global) and basic operations for file resources.
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @param <I>
 *            the identifier class
 * @date May 4, 2011
 */
public abstract class AbstractBaseResourceEngine<I> implements ResourceEngine<I> {

    File filePoolDir;

    /**
     * Creates a new instance of this class.
     * 
     * @param filePoolDir
     *            the directory to get and store file resources
     */
    public AbstractBaseResourceEngine(File filePoolDir) {
        if (!filePoolDir.exists() && !filePoolDir.mkdirs()) { throw new RuntimeException(
                "Could not create " + filePoolDir.getAbsolutePath()); }
        if (!filePoolDir.isDirectory()) { throw new RuntimeException(filePoolDir.getAbsolutePath() +
                                                                     " is not a directory"); }
        this.filePoolDir = filePoolDir;
    }

    @Override
    public AbstractConfiguration getExecutionEffectiveResources(I executionId, I collectionId,
            I providerId) {
        CompositeConfiguration config = new CompositeConfiguration();

        // Apache Commons configuration checks for existing properties in the order of insertion
        if (executionId != null)
            config.addConfiguration(getExecutionSpecificResources(executionId));
        if (collectionId != null)
            config.addConfiguration(getCollectionSpecificResources(collectionId));
        if (providerId != null) config.addConfiguration(getProviderSpecificResources(providerId));
        config.addConfiguration(getGlobalResources());
        return config;
    }

    @Override
    public File asFile(String value) {
        if (value != null) {

            if (value.startsWith("fileref://")) { return new File(filePoolDir,
                    value.substring("fileref://".length())); }
            if (value.startsWith("file://")) { return new File(value.substring("file://".length())); }

            return new File(value);
        }
        return null;
    }

    @Override
    public InputStream asInputStream(String value) {
        if (value != null) {
            
            //check, if this an http source
            if (value.startsWith("http://") || value.startsWith("https://")) {
                try {
                    URL url = new URL(value);
                    URLConnection connection = url.openConnection();
                    return connection.getInputStream();
                } catch (MalformedURLException e) {
                    throw new RuntimeException("Not a well-formed URL " + value, e);
                } catch (IOException e) {
                    throw new RuntimeException("Could not open connection to URL " + value, e);
                }
            }
            //as a fallback, try to read from a file
            File file = asFile(value);
           
                try {
                    InputStream is = new FileInputStream(file);
                    return is;
                } catch (FileNotFoundException e) {
                    throw new RuntimeException("Could not find file" + value, e);
                }
            }
        
        return null;

    }

}
