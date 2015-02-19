/* TranslationMap.java - created on Jul 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.translation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.common.cache.CacheLoader;

/**
 * @author Ruud Diterwich
 * @since Jul 18, 2011
 */
public class Translations {


    private static ConcurrentMap<TranslationProvider, TranslationProvider> providers          = new ConcurrentHashMap<TranslationProvider, TranslationProvider>();

    /**
     * Example: fr_FR -> fr -> (empty) -> en
     */
    private static CacheLoader<Locale, List<Locale>> localeAlternatives = new CacheLoader<Locale, List<Locale>>() {
        @Override
        public List<Locale> load(Locale locale) {
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
      };
    
//    private static ConcurrentMap<Locale, List<Locale>>                     localeAlternatives = new MapMaker().makeComputingMap(new Function<Locale, List<Locale>>() {
//          @Override
//          public List<Locale> apply(Locale locale) {
//              List<Locale> alternatives = new ArrayList<Locale>();
//              alternatives.add(locale);
//              if (!locale.getVariant().isEmpty()) {
//                  alternatives.add(new Locale(locale.getLanguage(), locale.getCountry(),""));
//              }
//              if (!locale.getCountry().isEmpty()) {
//                  alternatives.add(new Locale(locale.getLanguage(),"",""));
//              }
//              if (!locale.getLanguage().isEmpty()) {
//                  alternatives.add(new Locale("","",""));
//              }
//              if (!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
//                  alternatives.add(Locale.ENGLISH);
//              }
//              return alternatives;
//          }
//      });

    private Translations() {
    }

    /**
     * Registers the provider
     * 
     * @param provider
     */
    public static void register(TranslationProvider provider) {
        providers.put(provider, provider);
    }

    /**
     * Unregisters the provider
     * 
     * @param provider
     */
    public static void unregister(TranslationProvider provider) {
        providers.remove(provider);
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
        Locale localeNullSafe = locale != null ? locale : Locale.ENGLISH;
        List<Locale> alts;
        try {
            alts = localeAlternatives.load(localeNullSafe);
        } catch (Exception e) {
            throw new RuntimeException("Could not load locale!", e);
        }
        for (Locale alt : alts) {
            for (TranslationProvider provider : providers.keySet()) {
                String translation = provider.getTranslation(key, alt);
                if (translation != null && !translation.trim().equals("")) { return translation; }
            }
        }
        return defaultValue;
    }

}