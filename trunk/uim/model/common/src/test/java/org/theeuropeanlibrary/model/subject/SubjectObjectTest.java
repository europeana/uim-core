/* SubjectObjectTest.java - created on Sep 30, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.subject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.theeuropeanlibrary.model.Identifier;
import org.theeuropeanlibrary.qualifier.KnowledgeOrganizationSystem;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Sep 30, 2011
 */
public class SubjectObjectTest {


    /**
     * 
     */
    @Test
    public void testTopicSimple() {
        Topic topic = new Topic();
        assertNull(topic.getTopicName());
        
        topic = new Topic("test");
        assertNotNull(topic.getTopicName());
        assertEquals("test", topic.getTopicName());
                
        topic = new Topic("main");
        assertNotNull(topic.getTopicName());
        assertEquals("main", topic.getTopicName());
        
        assertEquals(new Topic("main"), topic);
        assertEquals(new Topic("main").hashCode(), topic.hashCode());
    }

    /**
     * Tests the conversion of Topic
     * 
     * @throws IOException
     */
    @Test
    public void testTopic() throws IOException {
        Topic enc = new Topic("topic");
        enc.setTopicDescription("description");
        for (int i = 0; i < KnowledgeOrganizationSystem.values().length; i++) {
            enc.getIdentifiers().add(
                    new Identifier("" + i, KnowledgeOrganizationSystem.values()[i].toString()));
        }

        Assert.assertEquals("topic", enc.getTopicName());
        Assert.assertEquals("description", enc.getTopicDescription());
        Assert.assertEquals(KnowledgeOrganizationSystem.values().length, enc.getIdentifiers().size());
        for (int i = 0; i < enc.getIdentifiers().size(); i++) {
            Assert.assertEquals("" + i, enc.getIdentifiers().get(i).getIdentifier());
            Assert.assertEquals(KnowledgeOrganizationSystem.values()[i],
                    KnowledgeOrganizationSystem.valueOf(enc.getIdentifiers().get(i).getScope()));
        }
        
        
        assertEquals(new Topic("topic", "description", enc.getIdentifiers()), enc);
        assertEquals(new Topic("topic", "description", enc.getIdentifiers()).hashCode(), enc.hashCode());
    }
    

    /**
     * Tests the conversion of Topic
     * 
     * @throws IOException
     */
    @Test
    public void testTitleSubject() throws IOException {
        TitleSubject enc = new TitleSubject();
        enc.setMiscellaneousInformation("description");
        for (int i = 0; i < KnowledgeOrganizationSystem.values().length; i++) {
            enc.getIdentifiers().add(
                    new Identifier("" + i, KnowledgeOrganizationSystem.values()[i].toString()));
        }

        Assert.assertEquals("description", enc.getMiscellaneousInformation());
        Assert.assertEquals(KnowledgeOrganizationSystem.values().length, enc.getIdentifiers().size());
        for (int i = 0; i < enc.getIdentifiers().size(); i++) {
            Assert.assertEquals("" + i, enc.getIdentifiers().get(i).getIdentifier());
            Assert.assertEquals(KnowledgeOrganizationSystem.values()[i],
                    KnowledgeOrganizationSystem.valueOf(enc.getIdentifiers().get(i).getScope()));
        }
        
        assertEquals(new TitleSubject("description", enc.getIdentifiers()), enc);
        assertEquals(new TitleSubject("description", enc.getIdentifiers()).hashCode(), enc.hashCode());

    }
}
