/* Translatable.java - created on Aug 15, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.translation;

import java.util.Locale;

/**
 * Interface that marks an object to being able to be translated for different
 * locales.
 *
 * @author Ruud Diterwich
 * @since Aug 15, 2011
 */
public interface Translatable {

    /**
     * @param locale
     * @return Translation of this object
     */
    String translate(Locale locale);
}
