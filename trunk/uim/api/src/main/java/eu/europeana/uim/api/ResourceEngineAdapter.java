/* ResourceEngineAdapter.java - created on May 3, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.api;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.input.NullInputStream;

/**
 * Dummy implementation of the ResourceEngine
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @param <I>
 * @date May 3, 2011
 */
public class ResourceEngineAdapter<I> implements ResourceEngine<I> {

    @Override
    public String getIdentifier() {
        return ResourceEngineAdapter.class.getSimpleName();
    }

    @Override
    public void addGlobalResource(String key, String value) {

    }

    @Override
    public void updateGlobalResource(String key, String value) {

    }

    @Override
    public void cleanGlobalResource(String key) {

    }

    @Override
    public AbstractConfiguration getGlobalResources() {
        return new BaseConfiguration();
    }

    @Override
    public void addProviderResource(I id, String key, String value) {

    }

    @Override
    public void updateProviderResource(I id, String key, String value) {

    }

    @Override
    public void truncateProviderResource(I id, String key) {

    }

    @Override
    public AbstractConfiguration getProviderSpecificResources(I providerId) {
        return new BaseConfiguration();
    }

    @Override
    public void addCollectionResource(I id, String key, String value) {

    }

    @Override
    public void updateCollectionResource(I id, String key, String value) {

    }

    @Override
    public void truncateCollectionResource(I id, String key) {

    }

    @Override
    public AbstractConfiguration getCollectionSpecificResources(I collectionId) {
        return new BaseConfiguration();
    }

    @Override
    public void addExecutionResource(I id, String key, String value) {

    }

    @Override
    public void updateExecutionResource(I id, String key, String value) {

    }

    @Override
    public void truncateExecutionResource(I id, String key) {

    }

    @Override
    public AbstractConfiguration getExecutionSpecificResources(I executionId) {
        return new BaseConfiguration();
    }


    @Override
    public File asFile(String value) {
      return new File("");
    }

    @Override
    public InputStream asInputStream(String value) {
       return new NullInputStream(0);
    }

    @Override
    public AbstractConfiguration getExecutionEffectiveResources(I executionId, I collectionId,
            I providerId) {
       return new BaseConfiguration();
    }

}
