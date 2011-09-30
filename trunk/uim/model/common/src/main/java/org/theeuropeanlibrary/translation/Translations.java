/* TranslationMap.java - created on Jul 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.translation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;

/**
 * @author Ruud Diterwich
 * @since Jul 18, 2011
 */
public class Translations {

    private static String                                                  NO_TRANSLATION     = "NO_TRANSLATION";

    private static ConcurrentMap<TranslationProvider, TranslationProvider> providers          = new ConcurrentHashMap<TranslationProvider, TranslationProvider>();

    /**
     * Example: fr_FR -> fr -> (empty) -> en
     */
    private static ConcurrentMap<Locale, List<Locale>>                     localeAlternatives = new MapMaker().makeComputingMap(new Function<Locale, List<Locale>>() {
          @Override
          public List<Locale> apply(Locale locale) {
              List<Locale> alternatives = new ArrayList<Locale>();
              alternatives.add(locale);
              if (!locale.getVariant().isEmpty()) {
                  alternatives.add(new Locale(locale.getLanguage(), locale.getCountry(),""));
              }
              if (!locale.getCountry().isEmpty()) {
                  alternatives.add(new Locale(locale.getLanguage(),"",""));
              }
              if (!locale.getLanguage().isEmpty()) {
                  alternatives.add(new Locale("","",""));
              }
              if (!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
                  alternatives.add(Locale.ENGLISH);
              }
              return alternatives;
          }
      });

    private static ConcurrentMap<CacheKey, String>            translationCache   = new MapMaker().makeComputingMap(new Function<CacheKey, String>() {
          @Override
          public String apply(CacheKey key) {
              for (Locale alt : localeAlternatives.get(key.getLocale())) {
                  for (TranslationProvider provider : providers.keySet()) {
                      String translation = provider.getTranslation(key.getKey(), alt);
                      if (translation != null && !translation.trim().equals("")) { return translation; }
                  }
              }
              return NO_TRANSLATION;
          }
      });

    private Translations() {
    }

    /**
     * Registers the provider
     * 
     * @param provider
     */
    public static void register(TranslationProvider provider) {
        providers.put(provider, provider);
        translationCache.clear();
    }

    /**
     * Unregisters the provider
     * 
     * @param provider
     */
    public static void unregister(TranslationProvider provider) {
        providers.remove(provider);
        translationCache.clear();
    }

    /**
     * @param key
     * @param locale
     * @return translated string, or null
     */
    public static String getTranslation(String key, Locale locale) {
        return getTranslation(key, locale, "");
    }

    /**
     * @param key
     * @param locale
     * @param defaultValue
     * @return translated string, or defaultValue
     */
    public static String getTranslation(String key, Locale locale, String defaultValue) {
        String string = translationCache.get(new CacheKey(key, locale));
        if (string != NO_TRANSLATION) {
            return string;
        } else {
            return defaultValue;
        }
    }

    static final class CacheKey  {
        private final String key;
        private final Locale locale;

        public CacheKey(String key, Locale locale) {
            super();
            this.key = key;
            this.locale = locale != null ? locale : Locale.ENGLISH;
        }
        
        /**
         * Returns the key.
         * @return the key
         */
        public String getKey() {
            return key;
        }

        /**
         * Returns the locale.
         * @return the locale
         */
        public Locale getLocale() {
            return locale;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((key == null) ? 0 : key.hashCode());
            result = prime * result + ((locale == null) ? 0 : locale.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            CacheKey other = (CacheKey)obj;
            if (key == null) {
                if (other.key != null) return false;
            } else if (!key.equals(other.key)) return false;
            if (locale == null) {
                if (other.locale != null) return false;
            } else if (!locale.equals(other.locale)) return false;
            return true;
        }

    }
}