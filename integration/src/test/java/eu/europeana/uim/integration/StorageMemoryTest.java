package eu.europeana.uim.integration;

import static org.ops4j.pax.exam.CoreOptions.felix;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.waitForFrameworkStartup;
import static org.ops4j.pax.exam.OptionUtils.combine;

import org.apache.karaf.testing.AbstractIntegrationTest;
import org.apache.karaf.testing.Helper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;

import eu.europeana.uim.Registry;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.store.Provider;

/**
 * Integration test for UIM commands<br/>
 * Warning: /!\ if you do not want to be driven insane, do check -- twice -- if you do NOT have a
 * running Karaf instance somewhere on your system<br/>
 * 
 * @author Manuel Bernhardt
 * @author Markus Muhr (markus.muhr@theeuropeanlibrary.org)
 * @since Apr 7, 2014
 */
@RunWith(JUnit4TestRunner.class)
public class StorageMemoryTest extends AbstractIntegrationTest {
    /**
     * @return setup configuration
     * @throws Exception
     */
    @Configuration
    public static Option[] configuration() throws Exception {
        return combine(
                Helper.getDefaultOptions(
                        systemProperty("karaf.name").value("junit"),
                        systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value(
                                "INFO")),

                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-common").versionAsInProject(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-api").versionAsInProject(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-storage-memory").versionAsInProject(),

                felix(),

                waitForFrameworkStartup());
    }

    /**
     * Tests storage.
     * 
     * @throws Exception
     */
    @Test
    public void testStorage() throws Exception {
        Registry registry = getOsgiService(Registry.class);

        StorageEngine<?> storage = null;
        while (storage == null) {
            storage = registry.getStorageEngine();
            Thread.sleep(500);
        }

        Provider<?> provider = storage.createProvider();
        Assert.assertNull(provider);
    }
}
