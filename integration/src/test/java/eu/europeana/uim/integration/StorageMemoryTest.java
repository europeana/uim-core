package eu.europeana.uim.integration;

import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;

import java.io.File;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.ops4j.pax.exam.options.MavenArtifactUrlReference;
import org.ops4j.pax.exam.options.MavenUrlReference;

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
@RunWith(PaxExam.class)
public class StorageMemoryTest {
    @Inject
    private Registry registry;

    /**
     * @return options
     */
    @org.ops4j.pax.exam.Configuration
    public Option[] config() {
        MavenArtifactUrlReference karafUrl = maven().groupId("org.apache.karaf").artifactId(
                "apache-karaf").version("3.0.3").type("tar.gz");

        MavenUrlReference karafStandardRepo = maven().groupId("org.apache.karaf.features").artifactId(
                "standard").classifier("features").type("xml").versionAsInProject();

        return new Option[] {
                // KarafDistributionOption.debugConfiguration("5005", true),
                karafDistributionConfiguration().frameworkUrl(karafUrl).unpackDirectory(
                        new File("target/exam")).useDeployFolder(false),
                keepRuntimeFolder(),
                KarafDistributionOption.features(karafStandardRepo, "scr"),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-common").versionAsInProject().start(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-api").versionAsInProject().start(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-storage-memory").versionAsInProject().start(), };
    }

    /**
     * Tests storage.
     * 
     * @throws Exception
     */
    @Test
    public void testStorage() throws Exception {
// StorageEngine<?> storage = null;
// while (storage == null) {
// storage = registry.getStorageEngine();
// Thread.sleep(500);
// }

        StorageEngine<?> storage = registry.getStorageEngine();
        Provider<?> provider = storage.createProvider();
        Assert.assertNotNull(provider);
    }
}