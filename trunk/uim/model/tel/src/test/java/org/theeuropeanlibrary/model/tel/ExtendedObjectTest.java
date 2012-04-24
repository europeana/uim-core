/* ConverterTest.java - created on Mar 22, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.tel;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;
import org.theeuropeanlibrary.model.common.qualifier.Language;

/**
 * Tests the conversion of a compreensive metadata record
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Apr 18, 2011
 */
@SuppressWarnings("unused")
public class ExtendedObjectTest {

    /**
     * Tests the conversion of Language
     * 
     * @throws IOException
     */
    @Test
    public void testLanguage() throws IOException {
        Language enc = Language.ENG;
    }

    /**
     * Tests the conversion of Facet
     * 
     * @throws IOException
     */
    @Test
    public void testFacet() throws IOException {
        Facet enc = new Facet("some type", "dcmi.type");
        Assert.assertEquals("some type", enc.getValue());
        Assert.assertEquals("dcmi.type", enc.getVocabulary());
    }



    /**
     * Tests the conversion of Edition
     * 
     * @throws IOException
     */
    @Test
    public void testEdition() throws IOException {
        Edition enc = new Edition(2, "revised");
        Assert.assertEquals(new Integer(2), enc.getNumber());
        Assert.assertEquals("revised", enc.getStatement());
    }

    /**
     * Tests the conversion of LabeledText
     * 
     * @throws IOException
     */
    @Test
    public void testLabeledText() throws IOException {
        LabeledText enc = new LabeledText("revised", "label");
        Assert.assertEquals("revised", enc.getContent());
        Assert.assertEquals("label", enc.getLabel());
    }

}
