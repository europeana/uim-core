package eu.europeana.uim.integration;

import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngine;
import org.apache.karaf.testing.Helper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.Constants;

import static org.junit.Assert.assertEquals;
import static org.ops4j.pax.exam.CoreOptions.felix;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.waitForFrameworkStartup;
import static org.ops4j.pax.exam.OptionUtils.combine;
import static org.ops4j.pax.exam.container.def.PaxRunnerOptions.scanFeatures;


/**
 * Integration test for UIM commands<br/>
 * Warning: /!\ if you do not want to be driven insane, do check -- twice -- if you
 * do NOT have a running Karaf instance somewhere on your system<br/>
 *
 * @author Manuel Bernhardt
 */
@RunWith(JUnit4TestRunner.class)
public class CommandTest extends AbstractUIMIntegrationTest {

    @Configuration
    public static Option[] configuration() throws Exception {
         return combine(
 				Helper.getDefaultOptions(
						systemProperty("karaf.name").value("junit"),
						systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("INFO")),
						
				scanFeatures(
                        maven().groupId("org.apache.karaf").artifactId("apache-karaf").type("xml").classifier("features").versionAsInProject(),
                        "spring"),

                // our modules. Karaf / Pax Exam don't fare well together in regards to feature descriptors
                // so until they do have these, we need to specify the OSGIfied maven bundles by hand here
                // this should be in sync with the feature descriptor at /etc/uim-features.xml

                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-common").versionAsInProject(),
                
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-api").versionAsInProject(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-storage-memory").versionAsInProject(),

//                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-plugin-basic").versionAsInProject(),
//                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-plugin-fileimp").versionAsInProject(),

                felix(),

                waitForFrameworkStartup()
        );
    }

    @Test
    public void testUIInfo() throws Exception {
    	Registry registry = getOsgiService(Registry.class);

    	StorageEngine storage = null;
    	while (storage == null) {
    		storage = registry.getStorage();
    		Thread.sleep(500);
    	}

        assertEquals("MemoryStorageEngine", registry.getStorage().getIdentifier());

        String property = bundleContext.getProperty( Constants.FRAMEWORK_VERSION );
        assertEquals("1.5", property);
        
        //assertEquals("UIM Registry: No plugins. MemoryStorageEngine.", getCommandResult("uim:info"));
    }

}
