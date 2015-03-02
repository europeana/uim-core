package eu.europeana.uim.integration;

import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.editConfigurationFileExtend;

import java.util.logging.Level;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.Configuration;

import eu.europeana.uim.Registry;
import eu.europeana.uim.logging.LoggingEngine;
import java.io.File;
import javax.inject.Inject;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.MavenUrlReference;

/**
 * Integration test for UIM commands<br/>
 * Warning: /!\ if you do not want to be driven insane, do check -- twice -- if you do NOT have a
 * running Karaf instance somewhere on your system<br/>
 * 
 * @author Manuel Bernhardt
 * @author Markus Muhr (markus.muhr@theeuropeanlibrary.org)
 * @since Apr 7, 2014
 */
@RunWith(PaxExam.class)
public class LoggingDatabaseTest {
    @Inject
    private Registry registry;

    @Configuration
    public Option[] config() {
        MavenArtifactUrlReference karafUrl = maven().groupId("org.apache.karaf").artifactId(
                "apache-karaf").version("3.0.0").type("tar.gz");

        MavenUrlReference karafStandardRepo = maven().groupId("org.apache.karaf.features").artifactId(
                "standard").classifier("features").type("xml").versionAsInProject();

        return new Option[] {
                // KarafDistributionOption.debugConfiguration("5005", true),
                karafDistributionConfiguration().frameworkUrl(karafUrl).unpackDirectory(
                        new File("target/exam")).useDeployFolder(false),
                logLevel(LogLevel.INFO),
                keepRuntimeFolder(),
                features(karafStandardRepo, "scr"),
                
                editConfigurationFileExtend("etc/eu.europeana.uim.logging.cfg", "db.driverClass", "org.hsqldb.jdbcDriver"),
                editConfigurationFileExtend("etc/eu.europeana.uim.logging.cfg", "db.dialect", "HSQL"),
                editConfigurationFileExtend("etc/eu.europeana.uim.logging.cfg", "db.jdbcUrl", "jdbc:hsqldb:mem:embedded"),
                editConfigurationFileExtend("etc/eu.europeana.uim.logging.cfg", "db.user", "sa"),
                editConfigurationFileExtend("etc/eu.europeana.uim.logging.cfg", "db.password", ""),
                editConfigurationFileExtend("etc/eu.europeana.uim.logging.cfg", "db.showsql", "false"),
                
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-common").versionAsInProject().start(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-api").versionAsInProject().start(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-logging-database").versionAsInProject().start(), };
    }

    /**
     * Tests logging.
     * 
     * @throws Exception
     */
    @Test
    public void testLogging() throws Exception {
        LoggingEngine<?> logging = null;
        while (logging == null) {
            logging = registry.getLoggingEngine();
            Thread.sleep(500);
        }
        logging.log(Level.INFO, "module", null, "test", "tst tst");
    }
}
