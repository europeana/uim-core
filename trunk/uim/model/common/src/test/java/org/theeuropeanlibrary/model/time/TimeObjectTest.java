/* TimeObjectTest.java - created on Sep 30, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.time;

import java.io.IOException;
import java.text.SimpleDateFormat;

import junit.framework.Assert;

import org.junit.Test;
import org.theeuropeanlibrary.model.spatial.NamedPlace;

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
    }

    /**
     * Tests the conversion of TemporalTextualExpression
     * 
     * @throws IOException
     */
    @Test
    public void testTemporalTextualExpression() throws IOException {
        TemporalTextualExpression enc = new TemporalTextualExpression("20th century");
        Assert.assertEquals("20th century", enc.getText());
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
    }

}
