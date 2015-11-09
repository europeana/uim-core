package eu.europeana.uim.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;

import java.io.File;
import java.util.Date;

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
import eu.europeana.uim.common.progress.MemoryProgressMonitor;
import eu.europeana.uim.orchestration.ActiveExecution;
import eu.europeana.uim.orchestration.Orchestrator;
import eu.europeana.uim.storage.StorageEngine;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.store.bean.CollectionBean;
import eu.europeana.uim.store.bean.ProviderBean;
import eu.europeana.uim.store.bean.RequestBean;
import eu.europeana.uim.workflow.Workflow;
import eu.europeana.uim.workflows.SysoutWorkflow;

/**
 * Integration test for the Orchestrator, using the MemoryStorageEngine
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 * @author Markus Muhr (markus.muhr@theeuropeanlibrary.org)
 * @since Apr 7, 2014
 */
@SuppressWarnings({ "unchecked", "rawtypes", "cast" })
@RunWith(PaxExam.class)
public class OrchestratorTest {

    @Inject
    private Registry     registry;

    @Inject
    private Orchestrator orchestrator;

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
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-storage-memory").versionAsInProject().start(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-logging-memory").versionAsInProject().start(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-orchestration-basic").versionAsInProject().start(), };
    }

    /**
     * @throws Exception
     */
    @Test
    public void processSampleData() throws Exception {
        StorageEngine<Long> storage = null;
        while (storage == null) {
            storage = (StorageEngine<Long>)registry.getStorageEngine();
            Thread.sleep(500);
        }
        Assert.assertNotNull(storage);

        Provider<Long> p = new ProviderBean<Long>(1L);
        Collection<Long> c = new CollectionBean<Long>(2L, p);
        Request<Long> r = new RequestBean<Long>(3L, c, new Date());

        // load the provider data
        Thread.sleep(1000);

// Provider<Long> p = storage.getProvider(0l);
// Collection<Long> c = storage.getCollections(p).get(0);
// Request<Long> r = storage.createRequest(c, new Date());
        for (int i = 0; i < 999; i++) {
            MetaDataRecord<Long> record = storage.createMetaDataRecord(c, "id=" + i);
            storage.updateMetaDataRecord(record);
            storage.addRequestRecord(r, record);
        }

        assertEquals("Wrong count of imported test MDRs", 999, storage.getByCollection(c).length);

        MemoryProgressMonitor monitor = new MemoryProgressMonitor();
        // run the workflow

        // Initialize workflow
        System.out.println("WORKFLOWS " + registry.getWorkflows());
        Workflow<MetaDataRecord<Long>, Long> workflow = (Workflow<MetaDataRecord<Long>, Long>)registry.getWorkflow(SysoutWorkflow.class.getSimpleName());
        int wait = 0;
        while (workflow == null && wait++ < 10) {
            workflow = (Workflow<MetaDataRecord<Long>, Long>)registry.getWorkflow(SysoutWorkflow.class.getSimpleName());
            Thread.sleep(1000);
        }
        Assert.assertNotNull(workflow);

        ActiveExecution<MetaDataRecord<Long>, Long> execution = (ActiveExecution<MetaDataRecord<Long>, Long>)orchestrator.executeWorkflow(
                workflow, c);
        execution.getMonitor().addListener(monitor);

        execution.waitUntilFinished();

        assertEquals("Wrong count of processed MDRs", 999, monitor.getWorked());

        Thread.sleep(1000);
        assertEquals("Zombie execution", 0, orchestrator.getActiveExecutions().size());

        Execution<Long> e = storage.getExecution(execution.getExecution().getId());
        assertFalse("Status of execution not correctly saved when it is finished", e.isActive());
    }
}