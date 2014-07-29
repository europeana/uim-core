/* TranslatableComparable.java - created on Aug 15, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.translation;

import java.util.Comparator;
import java.util.Locale;

/**
 * Compares Translatable on equality.
 *
 * @author Ruud Diterwich
 * @since Aug 15, 2011
 */
public class TranslatableComparator implements Comparator<Translatable> {

    private final Locale locale;

    /**
     * Creates a new instance of this class.
     *
     * @param locale
     */
    public TranslatableComparator(Locale locale) {
        this.locale = locale;
    }

    @Override
    public int compare(Translatable o1, Translatable o2) {
        return o1.translate(locale).compareToIgnoreCase(o2.translate(locale));
    }
}
