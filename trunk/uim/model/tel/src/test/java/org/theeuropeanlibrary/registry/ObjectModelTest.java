/* ConverterTest.java - created on Mar 22, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.registry;

import java.io.IOException;
import java.text.SimpleDateFormat;

import junit.framework.Assert;

import org.junit.Test;
import org.theeuropeanlibrary.model.Edition;
import org.theeuropeanlibrary.model.Facet;
import org.theeuropeanlibrary.model.Identifier;
import org.theeuropeanlibrary.model.LabeledText;
import org.theeuropeanlibrary.model.Link;
import org.theeuropeanlibrary.model.Numbering;
import org.theeuropeanlibrary.model.Text;
import org.theeuropeanlibrary.model.Title;
import org.theeuropeanlibrary.model.party.Family;
import org.theeuropeanlibrary.model.party.Meeting;
import org.theeuropeanlibrary.model.party.Organization;
import org.theeuropeanlibrary.model.party.Party;
import org.theeuropeanlibrary.model.party.Person;
import org.theeuropeanlibrary.model.spatial.BoundingBoxReferencedPlace;
import org.theeuropeanlibrary.model.spatial.GeoReferencedPlace;
import org.theeuropeanlibrary.model.spatial.NamedPlace;
import org.theeuropeanlibrary.model.spatial.SpatialEntity;
import org.theeuropeanlibrary.model.subject.Topic;
import org.theeuropeanlibrary.model.time.HistoricalPeriod;
import org.theeuropeanlibrary.model.time.Instant;
import org.theeuropeanlibrary.model.time.InstantGranularity;
import org.theeuropeanlibrary.model.time.Period;
import org.theeuropeanlibrary.model.time.TemporalTextualExpression;
import org.theeuropeanlibrary.qualifier.Language;
import org.theeuropeanlibrary.qualifier.PartyIdentifierType;
import org.theeuropeanlibrary.qualifier.SpatialIdentifierType;
import org.theeuropeanlibrary.qualifier.KnowledgeOrganizationSystem;

/**
 * Tests the conversion of a compreensive metadata record
 * 
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Apr 18, 2011
 */
@SuppressWarnings("unused")
public class ObjectModelTest {

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
