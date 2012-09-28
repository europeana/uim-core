/* AbstractResourceEngineTest.java - created on May 9, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eu.europeana.uim.store.Collection;
import eu.europeana.uim.store.Provider;
import eu.europeana.uim.workflow.Workflow;

/**
 * Abstract base class to test the contract of the resource engine
 * 
 * @author Rene Wiermer (rene.wiermer@kb.nl)
 * @param <I>
 * @date May 9, 2011
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public abstract class AbstractResourceEngineTest<I> {
    ResourceEngine engine          = null;
    final String   EXAMPLE_KEY_1   = "example1.property.file";
    final String   EXAMPLE_KEY_2   = "example2.property";
    final String   EXAMPLE_KEY_3   = "3";
    final String   EXAMPLE_KEY_4   = "4";
    final String   EXAMPLE_VALUE_1 = "exampleValue1";
    final String   EXAMPLE_VALUE_2 = "exampleValue2";

    /**
     * Setups storage engine.
     */
    @Before
    public void setUp() {
        engine = getResourceEngine();
        performSetUp();
    }

    /**
     * Override this for additional setup
     */
    protected void performSetUp() {
        // nothing todo
    }

    /**
     * @return configured storage engine
     */
    protected abstract ResourceEngine getResourceEngine();

    /**
     * @return provide an example identifier
     */
    protected abstract I nextID();

    /**
     * @return generate test workflow
     */
    protected abstract Workflow testGenerateWorkflow();

    /**
     * @return generate test provider
     */
    protected abstract Provider<I> testGenerateProvider();

    /**
     * @param provider
     * @return test collection
     */
    protected abstract Collection<I> testGenerateCollection(Provider<I> provider);

    abstract class AbstractCreateAndGetResourceTestCase<J> {
        public abstract LinkedHashMap<String, List<String>> getEntityResources(J entity,
                List<String> keys);

        public abstract void setEntityResources(J entity,
                LinkedHashMap<String, List<String>> resources);

        public abstract J getExampleEntity();

        public void run() {
            // simple put and get
            List<String> testList1 = new LinkedList<String>();
            testList1.add(EXAMPLE_VALUE_1);
            testList1.add(EXAMPLE_VALUE_2);

            LinkedHashMap<String, List<String>> testSet1 = new LinkedHashMap<String, List<String>>();
            testSet1.put(EXAMPLE_KEY_1, testList1);
            J testEntity = getExampleEntity();
            setEntityResources(testEntity, testSet1);

            LinkedHashMap<String, List<String>> result = getEntityResources(testEntity,
                    new LinkedList(testSet1.keySet()));
            assertNotNull(result);
            assertNotNull(result.get(EXAMPLE_KEY_1));
            assertEquals(2, result.get(EXAMPLE_KEY_1).size());
            assertEquals(EXAMPLE_VALUE_1, result.get(EXAMPLE_KEY_1).get(0));
            assertEquals(EXAMPLE_VALUE_2, result.get(EXAMPLE_KEY_1).get(1));

            // overwrite with fewer list entrys
            List<String> testList2 = new LinkedList<String>();
            testList2.add(EXAMPLE_VALUE_1);

            LinkedHashMap<String, List<String>> testSet2 = new LinkedHashMap<String, List<String>>();
            testSet2.put(EXAMPLE_KEY_1, testList2);

            setEntityResources(testEntity, testSet2);

            // ask for all keys
            LinkedHashMap<String, List<String>> result2 = getEntityResources(testEntity,
                    new LinkedList(testSet1.keySet()));
            assertNotNull(result2);
            // keys should still be the same as the first one
            assertEquals(testSet1.keySet().size(), result2.keySet().size());
            // but results for EXAMPLE_KEY_1 should differ
            assertEquals(1, result2.get(EXAMPLE_KEY_1).size());
            assertEquals(EXAMPLE_VALUE_1, result2.get(EXAMPLE_KEY_1).get(0));

            // check if you ask for more keys than there are actually stored
            // return null for those not stored (yet)
            List<String> testList = new LinkedList<String>();
            testList.add(EXAMPLE_VALUE_1);
            testList.add(EXAMPLE_VALUE_2);

            LinkedHashMap<String, List<String>> testSet3 = new LinkedHashMap<String, List<String>>();
            testSet3.put(EXAMPLE_KEY_3, testList);
            setEntityResources(testEntity, testSet3);

            LinkedList<String> keys = new LinkedList<String>();
            keys.add(EXAMPLE_KEY_3);
            keys.add(EXAMPLE_KEY_4);

            LinkedHashMap<String, List<String>> result3 = getEntityResources(testEntity, keys);
            assertNotNull(result3);
            assertEquals(2, result3.size());
            assertNotNull(result3.get(EXAMPLE_KEY_3));
            assertEquals(2, result3.get(EXAMPLE_KEY_3).size());
            assertNull(result3.get(EXAMPLE_KEY_4));

            // delete resource
            LinkedHashMap<String, List<String>> testSet4 = new LinkedHashMap<String, List<String>>();
            testSet4.put(EXAMPLE_KEY_3, null);
            setEntityResources(testEntity, testSet4);

            LinkedHashMap<String, List<String>> result4 = getEntityResources(testEntity, keys);
            assertNotNull(result4);
            assertEquals(2, result4.size());
            assertNull(result4.get(EXAMPLE_KEY_3));
        }
    }

    /**
     * Tests creation and retrieval of collection specific resources.
     */
    @Test
    public void testCreateAndGetGlobalResource() {
        LinkedHashMap<String, List<String>> resources = engine.getGlobalResources(Arrays.asList(
                EXAMPLE_KEY_1, EXAMPLE_KEY_3));

        assertEquals(2, resources.keySet().size());

        assertNull(resources.get(EXAMPLE_KEY_1));
        assertNull(resources.get(EXAMPLE_KEY_2));
        assertNull(resources.get(EXAMPLE_KEY_3));

        engine.checkpoint();

        resources.put(EXAMPLE_KEY_1, Arrays.asList("one"));
        resources.put(EXAMPLE_KEY_2, Arrays.asList(""));
        resources.put(EXAMPLE_KEY_3, Arrays.asList("two", "three"));
        resources.put(EXAMPLE_KEY_4, Collections.EMPTY_LIST);
        engine.setGlobalResources(resources);
        engine.checkpoint();

        resources = engine.getGlobalResources(Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_3));

        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_1));
        assertEquals(1, resources.get(EXAMPLE_KEY_1).size());
        // its not in the query
        assertNull(resources.get(EXAMPLE_KEY_2));
        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_3));
        assertEquals(2, resources.get(EXAMPLE_KEY_3).size());
        // its not in the query
        assertNull(resources.get(EXAMPLE_KEY_4));

        resources = engine.getGlobalResources(Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_2,
                EXAMPLE_KEY_3, EXAMPLE_KEY_4));

        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_1));
        assertEquals(1, resources.get(EXAMPLE_KEY_1).size());
        // its not in the query
        assertNotNull(resources.get(EXAMPLE_KEY_2));
        assertEquals(1, resources.get(EXAMPLE_KEY_2).size());
        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_3));
        assertEquals(2, resources.get(EXAMPLE_KEY_3).size());
        // its not in the query
        assertNotNull(resources.get(EXAMPLE_KEY_4));
        assertEquals(0, resources.get(EXAMPLE_KEY_4).size());

        resources.put(EXAMPLE_KEY_1, null);
        resources.put(EXAMPLE_KEY_3, Arrays.asList("two"));
        resources.put(EXAMPLE_KEY_4, null);
        engine.setGlobalResources(resources);
        engine.checkpoint();

        resources = engine.getGlobalResources(Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_2,
                EXAMPLE_KEY_3, EXAMPLE_KEY_4));

        // value given
        assertNull(resources.get(EXAMPLE_KEY_1));
        assertNull(resources.get(EXAMPLE_KEY_4));
        assertNotNull(resources.get(EXAMPLE_KEY_3));
        assertEquals(1, resources.get(EXAMPLE_KEY_3).size());

        resources.put(EXAMPLE_KEY_1, Arrays.asList("a"));
        engine.setGlobalResources(resources);
        engine.checkpoint();

        resources = engine.getGlobalResources(Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_2,
                EXAMPLE_KEY_3, EXAMPLE_KEY_4));

        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_1));
    }

    /**
     * Tests creation and retrieval of collection specific resources.
     */
    @Test
    public void testCreateAndGetWorkflowResource() {
        Workflow workflow = testGenerateWorkflow();

        LinkedHashMap<String, List<String>> resources = engine.getWorkflowResources(workflow,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_3));

        assertEquals(2, resources.keySet().size());

        assertNull(resources.get(EXAMPLE_KEY_1));
        assertNull(resources.get(EXAMPLE_KEY_2));
        assertNull(resources.get(EXAMPLE_KEY_3));

        engine.checkpoint();

        resources.put(EXAMPLE_KEY_1, Arrays.asList("one"));
        resources.put(EXAMPLE_KEY_2, Arrays.asList(""));
        resources.put(EXAMPLE_KEY_3, Arrays.asList("two", "three"));
        resources.put(EXAMPLE_KEY_4, Collections.EMPTY_LIST);
        engine.setWorkflowResources(workflow, resources);
        engine.checkpoint();

        resources = engine.getWorkflowResources(workflow,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_3));

        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_1));
        assertEquals(1, resources.get(EXAMPLE_KEY_1).size());
        // its not in the query
        assertNull(resources.get(EXAMPLE_KEY_2));
        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_3));
        assertEquals(2, resources.get(EXAMPLE_KEY_3).size());
        // its not in the query
        assertNull(resources.get(EXAMPLE_KEY_4));

        resources = engine.getWorkflowResources(workflow,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_2, EXAMPLE_KEY_3, EXAMPLE_KEY_4));

        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_1));
        assertEquals(1, resources.get(EXAMPLE_KEY_1).size());
        // its not in the query
        assertNotNull(resources.get(EXAMPLE_KEY_2));
        assertEquals(1, resources.get(EXAMPLE_KEY_2).size());
        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_3));
        assertEquals(2, resources.get(EXAMPLE_KEY_3).size());
        // its not in the query
        assertNotNull(resources.get(EXAMPLE_KEY_4));
        assertEquals(0, resources.get(EXAMPLE_KEY_4).size());

        resources.put(EXAMPLE_KEY_1, null);
        resources.put(EXAMPLE_KEY_3, Arrays.asList("two"));
        resources.put(EXAMPLE_KEY_4, null);
        engine.setWorkflowResources(workflow, resources);
        engine.checkpoint();

        resources = engine.getWorkflowResources(workflow,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_2, EXAMPLE_KEY_3, EXAMPLE_KEY_4));

        // value given
        assertNull(resources.get(EXAMPLE_KEY_1));
        assertNull(resources.get(EXAMPLE_KEY_4));
        assertNotNull(resources.get(EXAMPLE_KEY_3));
        assertEquals(1, resources.get(EXAMPLE_KEY_3).size());

        resources.put(EXAMPLE_KEY_1, Arrays.asList("a"));
        engine.setWorkflowResources(workflow, resources);
        engine.checkpoint();

        resources = engine.getWorkflowResources(workflow,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_2, EXAMPLE_KEY_3, EXAMPLE_KEY_4));

        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_1));
    }

    /**
     * Tests creation and retrieval of collection specific resources.
     */
    @Test
    public void testCreateAndGetProviderResource() {
        Provider<I> provider = testGenerateProvider();

        LinkedHashMap<String, List<String>> resources = engine.getProviderResources(provider,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_3));

        assertEquals(2, resources.keySet().size());

        assertNull(resources.get(EXAMPLE_KEY_1));
        assertNull(resources.get(EXAMPLE_KEY_2));
        assertNull(resources.get(EXAMPLE_KEY_3));

        engine.checkpoint();

        resources.put(EXAMPLE_KEY_1, Arrays.asList("one"));
        resources.put(EXAMPLE_KEY_2, Arrays.asList(""));
        resources.put(EXAMPLE_KEY_3, Arrays.asList("two", "three"));
        resources.put(EXAMPLE_KEY_4, Collections.EMPTY_LIST);
        engine.setProviderResources(provider, resources);
        engine.checkpoint();

        resources = engine.getProviderResources(provider,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_3));

        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_1));
        assertEquals(1, resources.get(EXAMPLE_KEY_1).size());
        // its not in the query
        assertNull(resources.get(EXAMPLE_KEY_2));
        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_3));
        assertEquals(2, resources.get(EXAMPLE_KEY_3).size());
        // its not in the query
        assertNull(resources.get(EXAMPLE_KEY_4));

        resources = engine.getProviderResources(provider,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_2, EXAMPLE_KEY_3, EXAMPLE_KEY_4));

        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_1));
        assertEquals(1, resources.get(EXAMPLE_KEY_1).size());
        // its not in the query
        assertNotNull(resources.get(EXAMPLE_KEY_2));
        assertEquals(1, resources.get(EXAMPLE_KEY_2).size());
        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_3));
        assertEquals(2, resources.get(EXAMPLE_KEY_3).size());
        // its not in the query
        assertNotNull(resources.get(EXAMPLE_KEY_4));
        assertEquals(0, resources.get(EXAMPLE_KEY_4).size());

        resources.put(EXAMPLE_KEY_1, null);
        resources.put(EXAMPLE_KEY_3, Arrays.asList("two"));
        resources.put(EXAMPLE_KEY_4, null);
        engine.setProviderResources(provider, resources);
        engine.checkpoint();

        resources = engine.getProviderResources(provider,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_2, EXAMPLE_KEY_3, EXAMPLE_KEY_4));

        // value given
        assertNull(resources.get(EXAMPLE_KEY_1));
        assertNull(resources.get(EXAMPLE_KEY_4));
        assertNotNull(resources.get(EXAMPLE_KEY_3));
        assertEquals(1, resources.get(EXAMPLE_KEY_3).size());

        resources.put(EXAMPLE_KEY_1, Arrays.asList("a"));
        engine.setProviderResources(provider, resources);
        engine.checkpoint();

        resources = engine.getProviderResources(provider,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_2, EXAMPLE_KEY_3, EXAMPLE_KEY_4));

        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_1));
    }

    /**
     * Tests creation and retrieval of collection specific resources.
     */
    @Test
    public void testCreateAndGetCollectionResources() {
        Provider<I> provider = testGenerateProvider();

        Collection<I> collection = testGenerateCollection(provider);

        LinkedHashMap<String, List<String>> resources = engine.getCollectionResources(collection,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_3));

        assertEquals(2, resources.keySet().size());

        assertNull(resources.get(EXAMPLE_KEY_1));
        assertNull(resources.get(EXAMPLE_KEY_2));
        assertNull(resources.get(EXAMPLE_KEY_3));

        engine.checkpoint();

        resources.put(EXAMPLE_KEY_1, Arrays.asList("one"));
        resources.put(EXAMPLE_KEY_2, Arrays.asList(""));
        resources.put(EXAMPLE_KEY_3, Arrays.asList("two", "three"));
        resources.put(EXAMPLE_KEY_4, Collections.EMPTY_LIST);
        engine.setCollectionResources(collection, resources);
        engine.checkpoint();

        resources = engine.getCollectionResources(collection,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_3));

        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_1));
        assertEquals(1, resources.get(EXAMPLE_KEY_1).size());
        // its not in the query
        assertNull(resources.get(EXAMPLE_KEY_2));
        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_3));
        assertEquals(2, resources.get(EXAMPLE_KEY_3).size());
        // its not in the query
        assertNull(resources.get(EXAMPLE_KEY_4));

        resources = engine.getCollectionResources(collection,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_2, EXAMPLE_KEY_3, EXAMPLE_KEY_4));

        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_1));
        assertEquals(1, resources.get(EXAMPLE_KEY_1).size());
        // its not in the query
        assertNotNull(resources.get(EXAMPLE_KEY_2));
        assertEquals(1, resources.get(EXAMPLE_KEY_2).size());
        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_3));
        assertEquals(2, resources.get(EXAMPLE_KEY_3).size());
        // its not in the query
        assertNotNull(resources.get(EXAMPLE_KEY_4));
        assertEquals(0, resources.get(EXAMPLE_KEY_4).size());

        resources.put(EXAMPLE_KEY_1, null);
        resources.put(EXAMPLE_KEY_3, Arrays.asList("two"));
        resources.put(EXAMPLE_KEY_4, null);
        engine.setCollectionResources(collection, resources);
        engine.checkpoint();

        resources = engine.getCollectionResources(collection,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_2, EXAMPLE_KEY_3, EXAMPLE_KEY_4));

        // value given
        assertNull(resources.get(EXAMPLE_KEY_1));
        assertNull(resources.get(EXAMPLE_KEY_4));
        assertNotNull(resources.get(EXAMPLE_KEY_3));
        assertEquals(1, resources.get(EXAMPLE_KEY_3).size());

        resources.put(EXAMPLE_KEY_1, Arrays.asList("a"));
        engine.setCollectionResources(collection, resources);
        engine.checkpoint();

        resources = engine.getCollectionResources(collection,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_2, EXAMPLE_KEY_3, EXAMPLE_KEY_4));

        // value given
        assertNotNull(resources.get(EXAMPLE_KEY_1));

    }

    /**
     * Tests creation and retrieval of collection specific resources.
     */
    @Test
    public void testCreateAndGetMixedResource() {
        Workflow workflow = testGenerateWorkflow();

        Provider<I> provider = testGenerateProvider();

        Collection<I> collection = testGenerateCollection(provider);

        LinkedHashMap<String, List<String>> colResources = engine.getCollectionResources(
                collection, Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_3));
        LinkedHashMap<String, List<String>> proResources = engine.getProviderResources(provider,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_3));
        LinkedHashMap<String, List<String>> worResources = engine.getWorkflowResources(workflow,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_3));
        LinkedHashMap<String, List<String>> gloResources = engine.getGlobalResources(Arrays.asList(
                EXAMPLE_KEY_1, EXAMPLE_KEY_3));

        assertNull(colResources.get(EXAMPLE_KEY_1));
        assertNull(colResources.get(EXAMPLE_KEY_2));
        assertNull(colResources.get(EXAMPLE_KEY_3));

        assertNull(proResources.get(EXAMPLE_KEY_1));
        assertNull(proResources.get(EXAMPLE_KEY_2));
        assertNull(proResources.get(EXAMPLE_KEY_3));

        assertNull(worResources.get(EXAMPLE_KEY_1));
        assertNull(worResources.get(EXAMPLE_KEY_2));
        assertNull(worResources.get(EXAMPLE_KEY_3));

        assertNull(gloResources.get(EXAMPLE_KEY_1));
        assertNull(gloResources.get(EXAMPLE_KEY_2));
        assertNull(gloResources.get(EXAMPLE_KEY_3));

        engine.checkpoint();

        colResources.put(EXAMPLE_KEY_1, Arrays.asList("cone", "ctwo"));
        proResources.put(EXAMPLE_KEY_1, Arrays.asList("pone", "ptwo"));
        worResources.put(EXAMPLE_KEY_1, Arrays.asList("wone", "wtwo"));
        gloResources.put(EXAMPLE_KEY_1, Arrays.asList("gone", "gtwo"));

        engine.setCollectionResources(collection, colResources);
        engine.setProviderResources(provider, proResources);
        engine.setWorkflowResources(workflow, worResources);
        engine.setGlobalResources(gloResources);
        engine.checkpoint();

        colResources = engine.getCollectionResources(collection,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_3));
        proResources = engine.getProviderResources(provider,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_3));
        worResources = engine.getWorkflowResources(workflow,
                Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_3));
        gloResources = engine.getGlobalResources(Arrays.asList(EXAMPLE_KEY_1, EXAMPLE_KEY_3));

        assertNotNull(colResources.get(EXAMPLE_KEY_1));
        assertNotNull(proResources.get(EXAMPLE_KEY_1));
        assertNotNull(worResources.get(EXAMPLE_KEY_1));
        assertNotNull(gloResources.get(EXAMPLE_KEY_1));

        assertEquals(colResources.get(EXAMPLE_KEY_1).get(0), "cone");
        assertEquals(proResources.get(EXAMPLE_KEY_1).get(1), "ptwo");
        assertEquals(worResources.get(EXAMPLE_KEY_1).get(1), "wtwo");
        assertEquals(gloResources.get(EXAMPLE_KEY_1).get(0), "gone");
    }
}
