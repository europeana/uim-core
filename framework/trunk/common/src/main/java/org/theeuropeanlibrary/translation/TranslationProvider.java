/* Translations.java - created on Aug 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.translation;

import java.util.Locale;

/**
 * 
 * 
 * @author Ruud Diterwich
 * @since Aug 16, 2011
 */
public interface TranslationProvider {

    /**
     * @param key
     * @param locale
     * @return translation, or null
     */
    String getTranslation(String key, Locale locale);
}
