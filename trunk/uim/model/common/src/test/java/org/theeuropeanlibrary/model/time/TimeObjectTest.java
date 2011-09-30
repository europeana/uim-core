/* TimeObjectTest.java - created on Sep 30, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.text.SimpleDateFormat;

import junit.framework.Assert;

import org.junit.Test;
import org.theeuropeanlibrary.model.Identifier;
import org.theeuropeanlibrary.model.spatial.NamedPlace;
import org.theeuropeanlibrary.model.subject.Subject;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Sep 30, 2011
 */
public class TimeObjectTest {

    /**
     * Tests the conversion of Instant
     * 
     * @throws IOException
     */
    @Test
    public void testInstant() throws IOException {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");
        Instant enc = new Instant(2010);
        enc.setGranularity(InstantGranularity.YEAR);
        enc.setUncertain(false);
        Assert.assertEquals("2010", simpleDateformat.format(enc.getTime()));

        assertEquals(new Instant(2010), enc);
        assertEquals(new Instant(2010).hashCode(), enc.hashCode());
    }

    /**
     * Tests the conversion of Instant
     * 
     * @throws IOException
     */
    @Test
    public void testInstantParse() throws IOException {
        Instant enc = new Instant(2010);

        assertEquals(new Instant("2010"), enc);
        assertEquals(new Instant("2010").hashCode(), enc.hashCode());
        
        
        enc = new Instant(2000, InstantGranularity.MILLENNIUM);

        assertEquals(new Instant("2uuu"), enc);
        assertEquals(new Instant("2???").hashCode(), enc.hashCode());

        assertEquals(new Instant("?").getGranularity(), InstantGranularity.UNKNOWN);
        assertEquals(new Instant("19?").getGranularity(), InstantGranularity.DECADE);
        assertEquals(new Instant("19u?").getGranularity(), InstantGranularity.CENTURY);
        assertEquals(new Instant("1uuu").getGranularity(), InstantGranularity.MILLENNIUM);
        
        assertEquals(new Instant(2010,10,1).getGranularity(), InstantGranularity.DAY);
        assertEquals(new Instant(2010,10,1, 1, 1).getGranularity(), InstantGranularity.MINUTE);
        assertEquals(new Instant(2010,10,1, 1, 1, 1).getGranularity(), InstantGranularity.MILLISECOND);
        
        enc.setSubject(new Subject());
        assertFalse(new Instant("2uuu").equals(enc));
        Instant enc2 = new Instant("2uuu");
        enc2.setSubject(new Subject());
        assertEquals(enc2, enc);
    }

    
    
    /**
     * Tests the conversion of Period
     * 
     * @throws IOException
     */
    @Test
    public void testPeriod() throws IOException {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");
        Period enc = new Period(new Instant(2000), new Instant(2011));
        Assert.assertEquals("2000", simpleDateformat.format(enc.getStart().getTime()));
        Assert.assertEquals("2011", simpleDateformat.format(enc.getEnd().getTime()));

        assertEquals(new Period(new Instant(2000), new Instant(2011)), enc);
        assertEquals(new Period(new Instant(2000), new Instant(2011)).hashCode(), enc.hashCode());
    }

    /**
     * Tests the conversion of TemporalTextualExpression
     * 
     * @throws IOException
     */
    @Test
    public void testTemporalTextualExpression() throws IOException {
        TemporalTextualExpression enc = new TemporalTextualExpression("20th century", new Identifier("a"));
        Assert.assertEquals("20th century", enc.getText());

        assertFalse(new TemporalTextualExpression("20th century").equals(enc));
        assertEquals(new TemporalTextualExpression("20th century", new Identifier("a")), enc);
        assertEquals(new TemporalTextualExpression("20th century", new Identifier("a")).hashCode(), enc.hashCode());
    }

    /**
     * Tests the conversion of HistoricalPeriod
     * 
     * @throws IOException
     */
    @Test
    public void testHistoricalPeriod() throws IOException {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");
        HistoricalPeriod enc = new HistoricalPeriod();
        enc.setName("Revolution");
        enc.setGeographicScope(new NamedPlace("Portugal"));
        enc.setTemporalScope(new Instant(1974));
        Assert.assertEquals("Revolution", enc.getName());
        Assert.assertEquals("Portugal", enc.getGeographicScope().getPlaceName());
        Assert.assertEquals("1974",
                simpleDateformat.format(((Instant)enc.getTemporalScope()).getTime()));

        assertEquals(new HistoricalPeriod("Revolution", new Instant(1974), new NamedPlace(
                "Portugal"), enc.getIdentifiers()), enc);
        assertEquals(new HistoricalPeriod("Revolution", new Instant(1974), new NamedPlace(
                "Portugal"), enc.getIdentifiers()).hashCode(), enc.hashCode());
    }

}
