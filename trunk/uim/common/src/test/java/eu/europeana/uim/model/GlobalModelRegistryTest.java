/* GlobalModelRegistryTest.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Test;

import eu.europeana.uim.model.bean.ConceptBean;
import eu.europeana.uim.model.qualifier.AgentRelation;
import eu.europeana.uim.model.qualifier.ConceptLevel;
import eu.europeana.uim.store.MetaDataRecord;
import eu.europeana.uim.store.MetaDataRecord.QualifiedValue;
import eu.europeana.uim.store.bean.MetaDataRecordBean;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public class GlobalModelRegistryTest {

    /**
     */
    @Test
    public void testSimpleConcepts() {
        MetaDataRecord<Long> record = new MetaDataRecordBean<Long>(1L, null);

        Concept proxy = new ConceptBean("urn:proxy/123456789");
        record.addValue(GlobalModelRegistry.CONCEPT, proxy);

        List<QualifiedValue<Concept>> list = record.getQualifiedValues(GlobalModelRegistry.CONCEPT);
        assertEquals(1, list.size());

        Concept mProxy = record.getFirstValue(GlobalModelRegistry.CONCEPT);
        assertEquals(mProxy, proxy);
        assertSame(mProxy, proxy);
        assertEquals(mProxy.getIdentifier(), proxy.getIdentifier());
    }

    /**
     */
    @Test
    public void testAgentConcepts() {
        MetaDataRecord<Long> record = new MetaDataRecordBean<Long>(1L, null);

        Concept proxy = new ConceptBean("urn:proxy/123456789");
        record.addValue(GlobalModelRegistry.CONCEPT, proxy);

        Agent aCreator = new ConceptBean("urn:agent/musem/a");
        Agent bCreator = new ConceptBean("urn:agent/aggregator/b");
        Agent p1Creator = new ConceptBean("urn:agent/painter/p1");
        Agent p2Creator = new ConceptBean("urn:agent/painter/p2");
        Agent p3Creator = new ConceptBean("urn:agent/painter/p3");

        Agent xCreator = new ConceptBean("urn:agent/aggregator/x");

        record.addValue(GlobalModelRegistry.AGENT, aCreator, ConceptLevel.AGGREGATION,
                AgentRelation.CREATOR);
        record.addValue(GlobalModelRegistry.AGENT, xCreator, ConceptLevel.AGGREGATION,
                AgentRelation.MANUFACTURER);
        record.addValue(GlobalModelRegistry.AGENT, bCreator, ConceptLevel.PROXY,
                AgentRelation.CREATOR);
        record.addValue(GlobalModelRegistry.AGENT, p1Creator, ConceptLevel.OBJECT,
                AgentRelation.CREATOR);
        record.addValue(GlobalModelRegistry.AGENT, p2Creator, ConceptLevel.OBJECT,
                AgentRelation.CREATOR);
        record.addValue(GlobalModelRegistry.AGENT, p3Creator, ConceptLevel.OBJECT,
                AgentRelation.CONTRIBUTOR);

        // retrieve all agents at once
        List<QualifiedValue<Agent>> alist = record.getQualifiedValues(GlobalModelRegistry.AGENT);
        assertEquals(6, alist.size());

        // retrieve all creators at once - one agent is a manufacturer
        List<Agent> clist = record.getValues(GlobalModelRegistry.AGENT, AgentRelation.CREATOR);
        assertEquals(4, clist.size());

        // retrieve all object related agents
        List<Agent> plist = record.getValues(GlobalModelRegistry.AGENT, ConceptLevel.OBJECT);
        assertEquals(3, plist.size());

        // retrieve all object related creators
        List<Agent> pclist = record.getValues(GlobalModelRegistry.AGENT, AgentRelation.CREATOR,
                ConceptLevel.OBJECT);
        assertEquals(2, pclist.size());
    }
}
