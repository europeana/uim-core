/* TranslatableComparatorTest.java - created on Aug 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.translation;

import static java.util.Arrays.asList;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * 
 * @author Ruud Diterwich
 * @since Aug 18, 2011
 */
public class TranslatableComparatorTest {

    private static class Soup implements Translatable {
        @Override
        public String translate(Locale locale) {
            if (locale.equals(Locale.FRENCH)) return "soupe";
            if (locale.equals(Locale.ENGLISH)) return "soup";
            if (locale.equals(Locale.GERMAN)) return "Suppe";
            return "";
        }
    }
    
    private static class Water implements Translatable {
        @Override
        public String translate(Locale locale) {
            if (locale.equals(Locale.FRENCH)) return "eau";
            if (locale.equals(Locale.ENGLISH)) return "water";
            if (locale.equals(Locale.GERMAN)) return "Wasser";
            return "";
        }
    }
    
    
    /**
     * 
     */
    @Test
    public void testComparator() {
        Soup soup = new Soup();
        Water water = new Water();
        List<Translatable> list = asList(soup, water);
        Assert.assertEquals(asList(soup, water), list);
        Collections.sort(list, new TranslatableComparator(Locale.FRENCH));
        Assert.assertEquals(asList(water, soup), list);
        Collections.sort(list, new TranslatableComparator(Locale.ENGLISH));
        Assert.assertEquals(asList(soup, water), list);
        Collections.sort(list, new TranslatableComparator(Locale.GERMAN));
        Assert.assertEquals(asList(soup, water), list);
    }
}
