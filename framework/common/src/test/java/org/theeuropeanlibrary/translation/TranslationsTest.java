/* TranslationsTest.java - created on Aug 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.translation;

import java.util.Locale;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests some basic translation stuff.
 * 
 * @author Ruud Diterwich
 * @since Aug 18, 2011
 */
public class TranslationsTest {
    private static final TranslationProvider provider = new TranslationProvider() {
        @Override
        public String getTranslation(String key, Locale locale) {
            if (key.equals("name") && locale.equals(Locale.ENGLISH)) return "name";
            if (key.equals("name") && locale.equals(new Locale(""))) return "(name)";
            if (key.equals("name") && locale.equals(Locale.FRENCH)) return "nomme";
            if (key.equals("bottle") && locale.equals(Locale.ENGLISH)) return "bottle";
            if (key.equals("bottle") && locale.equals(Locale.FRENCH)) return "bouteille";
            if (key.equals("bottle") && locale.equals(new Locale("nl"))) return "fles";
            if (key.equals("bottle") && locale.equals(new Locale("nl", "BE"))) return "fleske";
            return null;
        }
    };

    /**
     * Registers a provider.
     */
    @BeforeClass
    public static void registerProvider() {
        Translations.register(provider);
    }

    /**
     * Unregisters a provider
     */
    @AfterClass
    public static void unregisterProvider() {
        Translations.unregister(provider);
    }

    /**
     * Tests translations
     */
    @Test
    public void testTranslations() {
        Assert.assertEquals("name", Translations.getTranslation("name", Locale.ENGLISH));
        Assert.assertEquals("nomme", Translations.getTranslation("name", Locale.FRENCH));
    }

    /**
     * Tests fallbacks for languages
     */
    @Test
    public void testLanguageFallbacks() {
        Assert.assertEquals("(name)", Translations.getTranslation("name", Locale.CHINESE));
        Assert.assertEquals("bottle", Translations.getTranslation("bottle", Locale.CHINESE));
        Assert.assertEquals("fles", Translations.getTranslation("bottle", new Locale("nl", "NL")));
        Assert.assertEquals("fleske", Translations.getTranslation("bottle", new Locale("nl", "BE")));
    }
}
