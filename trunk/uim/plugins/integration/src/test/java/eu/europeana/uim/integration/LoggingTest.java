package eu.europeana.uim.integration;

import static org.ops4j.pax.exam.CoreOptions.felix;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.waitForFrameworkStartup;
import static org.ops4j.pax.exam.OptionUtils.combine;

import org.apache.karaf.testing.AbstractIntegrationTest;
import org.apache.karaf.testing.Helper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;

import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.api.LoggingEngine.Level;
import eu.europeana.uim.api.Registry;

/**
 * Integration test for UIM commands<br/>
 * Warning: /!\ if you do not want to be driven insane, do check -- twice -- if you do NOT have a
 * running Karaf instance somewhere on your system<br/>
 * 
 * @author Manuel Bernhardt
 */
@RunWith(JUnit4TestRunner.class)
public class LoggingTest extends AbstractIntegrationTest {

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
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-logging-memory").versionAsInProject(),

                felix(),

                waitForFrameworkStartup());
    }

    /**
     * Tests logging.
     * 
     * @throws Exception
     */
    @Test
    public void testLogging() throws Exception {
        Registry registry = getOsgiService(Registry.class);

        LoggingEngine<?, ?> logging = null;
        while (logging == null) {
            logging = registry.getLoggingEngine();
            Thread.sleep(500);
        }

        logging.log("module", null, "test", Level.INFO, "tst tst");
    }
}
