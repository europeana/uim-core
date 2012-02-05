/* SugarUtil.java - created on Feb 4, 2012, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.sugar.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.theeuropeanlibrary.model.common.qualifier.Country;
import org.theeuropeanlibrary.model.common.qualifier.Language;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 4, 2012
 */
public class SugarUtil {

    /**
     * generate a MD5 checksum of a string encoded in a hex string.
     * 
     * @param message
     *            the message( the password) to be encoded
     * @return hex string of the checksum
     */
    public static String generateMD5HexString(String message) {
        // 1. Prepare a MD5 hash password
        MessageDigest messageDiget;
        try {
            messageDiget = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not find MD5 digest algorithm", e);
        }

        messageDiget.update(message.getBytes());
        byte[] digestBytes = messageDiget.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < digestBytes.length; i++) {

            String hex = Integer.toHexString(0xFF & digestBytes[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    
    /**
     * Converts the SugarCrm language field content to simple Iso3 code.
     * 
     * @param code
     *            the full field entry from SugarCRM
     * @return the Iso3 code, "und" if not known
     */
    public static Language normalizeSugarLanguage(String code) {
        if (code == null || code.isEmpty()) return Language.UND;
        String[] split = code.split("-", 2);
        String isocode = split[0].trim();

        Language language = Language.lookupLanguage(isocode);
        if (language == null) {
            if (split.length > 1) {
                language = Language.lookupLanguage(split[1]);
            }
            if (language == null) {
                language = Language.UND;
            }
        }

        return language;
    }

    /**
     * Converts the SugarCrm language field content to simple Iso3 code.
     * 
     * @param code
     *            the full field entry from SugarCRM
     * @return the Iso3 code, "und" if not known
     */
    public static Country normalizeSugarCountry(String code) {
        if (code == null || code.isEmpty()) return Country.XX;
        String[] split = code.split("-", 2);
        String isocode = split[0].trim();

        Country country = Country.lookupCountry(isocode, false);
        if (country == null) {
            if (split.length > 1) {
                country = Country.lookupCountry(split[1], false);
            }
            if (country == null) {
                country = Country.XX;
            }
        }

        return country;
    }
}
