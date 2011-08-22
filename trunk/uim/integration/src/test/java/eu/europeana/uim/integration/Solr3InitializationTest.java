package eu.europeana.uim.integration;

import static org.ops4j.pax.exam.CoreOptions.felix;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.waitForFrameworkStartup;
import static org.ops4j.pax.exam.OptionUtils.combine;

import java.io.FileNotFoundException;

import org.apache.karaf.testing.AbstractIntegrationTest;
import org.apache.karaf.testing.Helper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.container.def.PaxRunnerOptions;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;

import eu.europeana.uim.solr3.Solr3Initializer;

/**
 * Integration test for UIM commands<br/>
 * Warning: /!\ if you do not want to be driven insane, do check -- twice -- if you do NOT have a
 * running Karaf instance somewhere on your system<br/>
 * 
 * @author Manuel Bernhardt
 */
@RunWith(JUnit4TestRunner.class)
public class Solr3InitializationTest extends AbstractIntegrationTest {

    /**
     * @return setup configuration
     * @throws Exception
     */
    @Configuration
    public static Option[] configuration() throws Exception {
        boolean debug = false;
        Option[] options = combine(
                Helper.getDefaultOptions(
                        systemProperty("karaf.name").value("junit"),
                        systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value(
                                "INFO")),

                PaxRunnerOptions.vmOption("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5006"),

                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-common").versionAsInProject(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-api").versionAsInProject(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-storage-memory").versionAsInProject(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-logging-memory").versionAsInProject(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-plugin-solr3").versionAsInProject(),

                felix(),

                waitForFrameworkStartup());

        // add std debug config if we want do debugging
        if (debug) {
            options = combine(
                    options,
                    PaxRunnerOptions.vmOption("-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5006"));
        }
        return options;

    }

    /**
     * Tests logging.
     * 
     * @throws Throwable
     */
    @Test
    public void testSolrSetup() throws Throwable  {

        Solr3Initializer init = new Solr3Initializer("file:///data", "ignore");
        try {
            init.initialize(Solr3Initializer.class.getClassLoader());
        } catch (Throwable t) {
            if (!(t.getCause().getCause() instanceof FileNotFoundException)) { throw t; }

        }
    }
}
