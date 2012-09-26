/* ConceptBeanTest.java - created on Jun 19, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.model.bean;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.europeana.uim.model.NotConsistentLabelException;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Jun 19, 2011
 */
public class ConceptBeanTest {

    /**
     * test the pref label together with the concept bean
     * 
     * @throws NotConsistentLabelException
     */
    @Test
    public void testPrefLabel() throws NotConsistentLabelException {
        ConceptBean bean = new ConceptBean("a");

        assertNotNull(bean.getPrefLabel());
        assertTrue(bean.getPrefLabel().isEmpty());

        LexicalLabelBean label = new LexicalLabelBean("love", "eng");
        bean.setPrefLabel(label);

        assertNotNull(bean.getPrefLabel());
        assertEquals(1, bean.getPrefLabel().size());
        assertEquals(label, bean.getPrefLabel().iterator().next());
        assertEquals("love", bean.getPrefLabel().iterator().next().getLabel());
        assertEquals("eng", bean.getPrefLabel().iterator().next().getLanguage());

        LexicalLabelBean label2 = new LexicalLabelBean("liebe", "ger");
        bean.addPrefLabel(label2);

        assertNotNull(bean.getPrefLabel());
        assertEquals(2, bean.getPrefLabel().size());

        LexicalLabelBean label3 = new LexicalLabelBean("liebe", "ger");
        bean.setPrefLabel(label3);

        assertNotNull(bean.getPrefLabel());
        assertEquals(2, bean.getPrefLabel().size());

        assertNotSame(label2, bean.getPrefLabel("ger"));
        assertEquals(label2, bean.getPrefLabel("ger"));
        assertSame(label3, bean.getPrefLabel("ger"));
    }

    /**
     * test the alt label together with the concept bean
     * 
     * @throws NotConsistentLabelException
     */
    @Test
    public void testAltLabel() throws NotConsistentLabelException {
        ConceptBean bean = new ConceptBean("b");

        assertNotNull(bean.getAltLabel());
        assertTrue(bean.getAltLabel().isEmpty());

        LexicalLabelBean label = new LexicalLabelBean("love", "eng");
        bean.setAltLabel(label);

        assertNotNull(bean.getAltLabel());
        assertEquals(1, bean.getAltLabel().size());
        assertEquals(label, bean.getAltLabel().iterator().next());
        assertEquals("love", bean.getAltLabel().iterator().next().getLabel());
        assertEquals("eng", bean.getAltLabel().iterator().next().getLanguage());

        LexicalLabelBean label2 = new LexicalLabelBean("liebe", "ger");
        bean.addAltLabel(label2);

        assertNotNull(bean.getAltLabel());
        assertEquals(2, bean.getAltLabel().size());

        LexicalLabelBean label3 = new LexicalLabelBean("liebe", "ger");
        bean.setAltLabel(label3);

        assertNotNull(bean.getAltLabel());
        assertEquals(2, bean.getAltLabel().size());

        assertNotSame(label2, bean.getAltLabel("ger"));
        assertEquals(label2, bean.getAltLabel("ger"));
        assertSame(label3, bean.getAltLabel("ger"));
    }

    /**
     * test the hidden label together with the concept bean
     * 
     * @throws NotConsistentLabelException
     */
    @Test
    public void testHiddenLabel() throws NotConsistentLabelException {
        ConceptBean bean = new ConceptBean("c");

        assertNotNull(bean.getHiddenLabel());
        assertTrue(bean.getHiddenLabel().isEmpty());

        LexicalLabelBean label = new LexicalLabelBean("love", "eng");
        bean.setHiddenLabel(label);

        assertNotNull(bean.getHiddenLabel());
        assertEquals(1, bean.getHiddenLabel().size());
        assertEquals(label, bean.getHiddenLabel().iterator().next());
        assertEquals("love", bean.getHiddenLabel().iterator().next().getLabel());
        assertEquals("eng", bean.getHiddenLabel().iterator().next().getLanguage());

        LexicalLabelBean label2 = new LexicalLabelBean("liebe", "ger");
        bean.addHiddenLabel(label2);

        assertNotNull(bean.getHiddenLabel());
        assertEquals(2, bean.getHiddenLabel().size());

        LexicalLabelBean label3 = new LexicalLabelBean("liebe", "ger");
        bean.setHiddenLabel(label3);

        assertNotNull(bean.getHiddenLabel());
        assertEquals(2, bean.getHiddenLabel().size());

        assertNotSame(label2, bean.getHiddenLabel("ger"));
        assertEquals(label2, bean.getHiddenLabel("ger"));
        assertSame(label3, bean.getHiddenLabel("ger"));
    }

    /**
     * test the hidden label together with the concept bean
     * 
     * @throws NotConsistentLabelException
     */
    @Test
    public void testHashCodeEquals() throws NotConsistentLabelException {
        ConceptBean beanA = new ConceptBean("a");
        ConceptBean beanB = new ConceptBean("b");
        ConceptBean beanC = new ConceptBean("c");

        ConceptBean beanD = new ConceptBean("a");

        assertEquals(beanA, beanD);
        assertFalse(beanA.equals(beanB));
        assertFalse(beanA.equals(beanC));

        beanA.setPrefLabel(new LexicalLabelBean("love", "eng"));
        beanB.setPrefLabel(new LexicalLabelBean("love", "eng"));
        beanC.setPrefLabel(new LexicalLabelBean("love", "eng"));
        beanD.setPrefLabel(new LexicalLabelBean("liebe", "ger"));

        assertEquals(beanA, beanD);
        assertFalse(beanA.equals(beanB));
        assertFalse(beanA.equals(beanC));

    }

    /**
     * test the hidden label together with the concept bean
     * 
     * @throws NotConsistentLabelException
     */
    @Test
    public void testNotConsistent() throws NotConsistentLabelException {
        ConceptBean beanA = new ConceptBean("a");

        beanA.setPrefLabel(new LexicalLabelBean("love", "eng"));
        beanA.addPrefLabel(new LexicalLabelBean("love", "eng"));
        try {
            beanA.addPrefLabel(new LexicalLabelBean("loving", "eng"));
            fail("Different label for same language is not consistent.");
        } catch (NotConsistentLabelException exc) {

        }
    }
}
