/* GlobalModelRegistryTest.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.HashSet;
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
        record.addField(GlobalModelRegistry.CONCEPT, proxy);
        
        List<QualifiedValue<Concept>> list = record.getField(GlobalModelRegistry.CONCEPT);
        assertEquals(1, list.size());
        
        Concept mProxy = record.getFirstField(GlobalModelRegistry.CONCEPT);
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
        record.addField(GlobalModelRegistry.CONCEPT, proxy);
        
        Agent aCreator = new ConceptBean("urn:agent/musem/a");
        Agent bCreator = new ConceptBean("urn:agent/aggregator/b");
        Agent p1Creator = new ConceptBean("urn:agent/painter/p1");
        Agent p2Creator = new ConceptBean("urn:agent/painter/p2");
        Agent p3Creator = new ConceptBean("urn:agent/painter/p3");
        
        Agent xCreator = new ConceptBean("urn:agent/aggregator/x");

        record.addQField(GlobalModelRegistry.AGENT, aCreator, new HashSet<Enum<?>>(){{
            add(ConceptLevel.AGGREGATION);
            add(AgentRelation.CREATOR);
        }});
        
        record.addQField(GlobalModelRegistry.AGENT, xCreator, new HashSet<Enum<?>>(){{
            add(ConceptLevel.AGGREGATION);
            add(AgentRelation.MANUFACTURER);
        }});
        
        record.addQField(GlobalModelRegistry.AGENT, bCreator, new HashSet<Enum<?>>(){{
            add(ConceptLevel.PROXY);
            add(AgentRelation.CREATOR);
        }});
        
        record.addQField(GlobalModelRegistry.AGENT, p1Creator, new HashSet<Enum<?>>(){{
            add(ConceptLevel.OBJECT);
            add(AgentRelation.CREATOR);
        }});
        
        
        record.addQField(GlobalModelRegistry.AGENT, p2Creator, new HashSet<Enum<?>>(){{
            add(ConceptLevel.OBJECT);
            add(AgentRelation.CREATOR);
        }});
        
        
        record.addQField(GlobalModelRegistry.AGENT, p3Creator, new HashSet<Enum<?>>(){{
            add(ConceptLevel.OBJECT);
            add(AgentRelation.CONTRIBUTOR);
        }});
        
        
        // retrieve all agents at once
        List<QualifiedValue<Agent>> alist = record.getField(GlobalModelRegistry.AGENT);
        assertEquals(6, alist.size());

        // retrieve all creators at once - one agent is a manufacturer
        List<Agent> clist = record.getQField(GlobalModelRegistry.AGENT, new HashSet<Enum<?>>(){{
            add(AgentRelation.CREATOR);
        }});
        assertEquals(4, clist.size());
        
        // retrieve all object related agents
        List<Agent> plist = record.getQField(GlobalModelRegistry.AGENT, new HashSet<Enum<?>>(){{
            add(ConceptLevel.OBJECT);
        }});
        assertEquals(3, plist.size());
        
        // retrieve all object related creators
        List<Agent> pclist = record.getQField(GlobalModelRegistry.AGENT, new HashSet<Enum<?>>(){{
            add(AgentRelation.CREATOR);
            add(ConceptLevel.OBJECT);
        }});
        assertEquals(2, pclist.size());
        
    }
    
    
}
