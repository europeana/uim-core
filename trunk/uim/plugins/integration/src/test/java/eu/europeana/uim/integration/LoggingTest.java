package eu.europeana.uim.integration;

import static org.ops4j.pax.exam.CoreOptions.felix;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.waitForFrameworkStartup;
import static org.ops4j.pax.exam.OptionUtils.combine;
import static org.ops4j.pax.exam.container.def.PaxRunnerOptions.scanFeatures;

import org.apache.karaf.testing.Helper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.container.def.PaxRunnerOptions;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;

import eu.europeana.uim.api.LoggingEngine;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.api.LoggingEngine.Level;
import eu.europeana.uim.api.Registry;


/**
 * Integration test for UIM commands<br/>
 * Warning: /!\ if you do not want to be driven insane, do check -- twice -- if you
 * do NOT have a running Karaf instance somewhere on your system<br/>
 *
 * @author Manuel Bernhardt
 */
@RunWith(JUnit4TestRunner.class)
public class LoggingTest extends AbstractUIMIntegrationTest {

    @Configuration
    public static Option[] configuration() throws Exception {
         return combine(
 				Helper.getDefaultOptions(
						systemProperty("karaf.name").value("junit"),
						systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("INFO")),
						
				scanFeatures(
                        maven().groupId("org.apache.karaf").artifactId("apache-karaf").type("xml").classifier("features").versionAsInProject(),
                        "spring"),

                //PaxRunnerOptions.vmOption( "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5006" ),
                        
                // our modules. Karaf / Pax Exam don't fare well together in regards to feature descriptors
                // so until they do have these, we need to specify the OSGIfied maven bundles by hand here
                // this should be in sync with the feature descriptor at /etc/uim-features.xml

                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-common").versionAsInProject(),
                
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-api").versionAsInProject(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-storage-memory").versionAsInProject(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-logging-memory").versionAsInProject(),

                mavenBundle().groupId("org.dom4j").artifactId("com.springsource.org.dom4j").version("1.6.1"),
                mavenBundle().groupId("org.apache.commons").artifactId("com.springsource.org.apache.commons.collections").version("3.2.1"),
                mavenBundle().groupId("org.jboss.javassist").artifactId("com.springsource.javassist").version("3.9.0.GA"),
                mavenBundle().groupId("javax.persistence").artifactId("com.springsource.javax.persistence").version("1.0.0"),
                mavenBundle().groupId("org.antlr").artifactId("com.springsource.antlr").version("2.7.7"),
                mavenBundle().groupId("net.sourceforge.cglib").artifactId("com.springsource.net.sf.cglib").version("2.2.0"),
                mavenBundle().groupId("javax.xml.stream").artifactId("com.springsource.javax.xml.stream").version("1.0.1"),
                mavenBundle().groupId("org.objectweb.asm").artifactId("com.springsource.org.objectweb.asm").version("1.5.3"),
                mavenBundle().groupId("org.objectweb.asm").artifactId("com.springsource.org.objectweb.asm.attrs").version("1.5.3"),
                mavenBundle().groupId("org.hibernate").artifactId("com.springsource.org.hibernate").version("3.3.2.GA"),
                mavenBundle().groupId("org.hibernate").artifactId("com.springsource.org.hibernate.annotations").version("3.3.1.ga"),
                mavenBundle().groupId("org.hibernate").artifactId("com.springsource.org.hibernate.annotations.common").version("3.3.0.ga"),
                mavenBundle().groupId("org.hibernate").artifactId("com.springsource.org.hibernate.ejb").version("3.3.2.GA"),
                
                felix(),

                waitForFrameworkStartup()
        );
    }

    @Test
    public void testLogging() throws Exception {
    	Registry registry = getOsgiService(Registry.class);

    	LoggingEngine<?> logging = null;
    	while (logging == null) {
    		logging = registry.getLoggingEngine();
    		Thread.sleep(500);
    	}

    	logging.log(Level.INFO, "tst tst", null, null, null);
    	
    }

}
