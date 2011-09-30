/* ConverterTest.java - created on Mar 22, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model;

import java.io.IOException;
import java.text.SimpleDateFormat;

import junit.framework.Assert;

import org.junit.Test;
import org.theeuropeanlibrary.model.common.Identifier;
import org.theeuropeanlibrary.model.common.Link;
import org.theeuropeanlibrary.model.common.Numbering;
import org.theeuropeanlibrary.model.common.Text;
import org.theeuropeanlibrary.model.common.Title;
import org.theeuropeanlibrary.model.common.party.Family;
import org.theeuropeanlibrary.model.common.party.Meeting;
import org.theeuropeanlibrary.model.common.party.Organization;
import org.theeuropeanlibrary.model.common.party.Party;
import org.theeuropeanlibrary.model.common.party.Person;
import org.theeuropeanlibrary.model.common.qualifier.KnowledgeOrganizationSystem;
import org.theeuropeanlibrary.model.common.qualifier.Language;
import org.theeuropeanlibrary.model.common.qualifier.PartyIdentifierType;
import org.theeuropeanlibrary.model.common.qualifier.SpatialIdentifierType;
import org.theeuropeanlibrary.model.common.spatial.BoundingBoxReferencedPlace;
import org.theeuropeanlibrary.model.common.spatial.GeoReferencedPlace;
import org.theeuropeanlibrary.model.common.spatial.NamedPlace;
import org.theeuropeanlibrary.model.common.spatial.SpatialEntity;
import org.theeuropeanlibrary.model.common.subject.Topic;
import org.theeuropeanlibrary.model.common.time.HistoricalPeriod;
import org.theeuropeanlibrary.model.common.time.Instant;
import org.theeuropeanlibrary.model.common.time.InstantGranularity;
import org.theeuropeanlibrary.model.common.time.Period;
import org.theeuropeanlibrary.model.common.time.TemporalTextualExpression;
import org.theeuropeanlibrary.model.tel.Edition;
import org.theeuropeanlibrary.model.tel.Facet;
import org.theeuropeanlibrary.model.tel.LabeledText;

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
