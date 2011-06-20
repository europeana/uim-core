package eu.europeana.uim.integration;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.ops4j.pax.exam.CoreOptions.felix;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.waitForFrameworkStartup;
import static org.ops4j.pax.exam.OptionUtils.combine;
import static org.ops4j.pax.exam.container.def.PaxRunnerOptions.scanFeatures;

import java.util.Date;
import java.util.List;

import org.apache.karaf.testing.Helper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;

import eu.europeana.uim.api.ActiveExecution;
import eu.europeana.uim.api.Orchestrator;
import eu.europeana.uim.api.Registry;
import eu.europeana.uim.api.StorageEngine;
import eu.europeana.uim.common.MemoryProgressMonitor;
import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.store.Request;
import eu.europeana.uim.workflow.Workflow;
import eu.europeana.uim.workflow.dummy.DummyWorkflow;

/**
 * Integration test for the Orchestrator, using the MemoryStorageEngine
 * 
 * @author Manuel Bernhardt <bernhardt.manuel@gmail.com>
 */
@RunWith(JUnit4TestRunner.class)
public class OrchestratorTest extends AbstractUIMIntegrationTest {

    @Configuration
    public static Option[] configuration() throws Exception {
        return combine(
                Helper.getDefaultOptions(
                        systemProperty("karaf.name").value("junit"),
                        systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value(
                                "FINE")),

                // rhaa
                // systemProperty("integrationDir").value(System.getProperty("integrationDir")),

                // PaxRunnerOptions.vmOption(
                // "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5006" ),
                                
                                
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-common").versionAsInProject(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-api").versionAsInProject(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-storage-memory").versionAsInProject(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-plugin-basic").versionAsInProject(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-plugin-dummy").versionAsInProject(),
                mavenBundle().groupId("eu.europeana").artifactId("europeana-uim-workflow-dummy").versionAsInProject(),

                felix(),

                waitForFrameworkStartup());
    }

    @Test
    public void processSampleData() throws Exception {
        Registry registry = getOsgiService(Registry.class);

        StorageEngine<Long> storage = null;
        while (storage == null) {
            storage = (StorageEngine<Long>)registry.getStorageEngine();
            Thread.sleep(500);
        }

        // load the provider data
        getCommandResult("uim:store -o loadSampleData");
        Thread.sleep(1000);

        Provider<Long> p = storage.getProvider(0l);
        Collection<Long> c = storage.getCollections(p).get(0);
        Request<Long> r = storage.createRequest(c, new Date());

        for (int i = 0; i < 999; i++) {
            MetaDataRecord record = storage.createMetaDataRecord(c, "id=" + i);
            storage.updateMetaDataRecord(c, record);
            storage.addRequestRecord(r, record);
        }

        assertEquals("Wrong count of imported test MDRs", 999, storage.getTotalByCollection(c));

        Orchestrator o = getOsgiService(Orchestrator.class);
        MemoryProgressMonitor monitor = new MemoryProgressMonitor();
        // run the workflow
        Workflow w = null;
        List<Workflow> workflows = registry.getWorkflows();
        for (Workflow workflow : workflows) {
            if (workflow.getName().equals(DummyWorkflow.class.getName())) {
                w = workflow;
            }
        }

        ActiveExecution<Long> execution = (ActiveExecution<Long>)o.executeWorkflow(w, c);
        execution.getMonitor().addListener(monitor);

        execution.waitUntilFinished();

        assertEquals("Wrong count of processed MDRs", 999, monitor.getWorked());

        Thread.sleep(1000);
        assertEquals("Zombie execution", 0, o.getActiveExecutions().size());

        Execution<Long> e = storage.getExecution(execution.getId());
        assertFalse("Status of execution not correctly saved when it is finished", e.isActive());
    }
}
