/* LinkcheckIngestionPluginTest.java - created on Mar 20, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.check.validate;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;
import org.theeuropeanlibrary.model.common.Title;
import org.theeuropeanlibrary.model.common.subject.Topic;
import org.theeuropeanlibrary.model.tel.ObjectModelRegistry;

import eu.europeana.uim.api.StorageEngineException;
import eu.europeana.uim.orchestration.AbstractBatchWorkflowTest;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * Tests the simple field validation workflow.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 20, 2011
 */
public class ValidateFieldWorkflowTest extends AbstractBatchWorkflowTest {
    /**
     * Tests a simple runthrough of the field validation plugin against artificial records.
     * 
     * @throws IOException
     * @throws StorageEngineException
     * @throws InterruptedException
     */
    @Test
    public void testSimpleValidation() throws IOException, InterruptedException,
            StorageEngineException {

        LinkedHashMap<String, List<String>> map = new LinkedHashMap<String, List<String>>();
        map.put(FieldCheckIngestionPlugin.KEY_FIELDSPEC,
                Arrays.asList("./src/test/resources/fieldspec.txt"));
        registry.getResourceEngine().setGlobalResources(map);

        executeWorkflow(new FieldCheckWorkflow(), 1);

    }

    @Override
    protected void fillRecord(MetaDataRecord<Long> record, int count) {
        record.addValue(ObjectModelRegistry.TOPIC, new Topic("Validation Topic"));
        record.addValue(ObjectModelRegistry.TITLE, new Title("Validation Title"));
    }
}
