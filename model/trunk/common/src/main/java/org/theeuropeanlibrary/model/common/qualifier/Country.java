/* Country.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.qualifier;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.theeuropeanlibrary.translation.Translatable;
import org.theeuropeanlibrary.translation.Translations;

/**
 * Country enum with english country name and MARC codes, ISO 3166 2 letter codes.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Nuno Freire (nfreire@gmail.com)
 * @since Mar 18, 2011
 */
@SuppressWarnings("all")
public enum Country implements Translatable {
	AD("AD", "AND", "AN", "Andorra"), AE("AE", "ARE", "TS", "United Arab Emirates"), AF("AF", "AFG", "AF", "Afghanistan"), AG("AG", "ATG", "AQ", "Antigua and Barbuda"), AI("AI", "AIA", "AM", "Anguilla"), AL(
			"AL", "ALB", "AA", "Albania"), AM("AM", "ARM", "AI", "Armenia"), AN("AN", "ANT", "NA", "Netherlands Antilles"), AO("AO", "AGO", "AO", "Angola"), AQ("AQ", "ATA", "AY", "Antarctica"), AR("AR",
			"ARG", "AG", "Argentina"), AS("AS", "ASM", "AS", "American Samoa"), AT("AT", "AUT", "AU", "Austria"), AU("AU", "AUS", "AT", "Australia"), AW("AW", "ABW", "AW", "Aruba"), AX("AX", "ALA", "",
			"\u00c5land Islands"), AZ("AZ", "AZE", "AJ", "Azerbaijan"), BA("BA", "BIH", "BN", "Bosnia and Herzegovina"), BB("BB", "BRB", "BB", "Barbados"), BD("BD", "BGD", "BG", "Bangladesh"), BE("BE",
			"BEL", "BE", "Belgium"), BF("BF", "BFA", "UV", "Burkina Faso"), BG("BG", "BGR", "BU", "Bulgaria"), BH("BH", "BHR", "BA", "Bahrain"), BI("BI", "BDI", "BD", "Burundi"), BJ("BJ", "BEN", "DM",
			"Benin"), BL("BL", "BLM", "", "Saint Barth\u00e9lemy"), BM("BM", "BMU", "BM", "Bermuda"), BN("BN", "BRN", "BX", "Brunei Darussalam"), BO("BO", "BOL", "BO", "Bolivia"), BR("BR", "BRA", "BL",
			"Brazil"), BS("BS", "BHS", "BF", "Bahamas"), BT("BT", "BTN", "BT", "Bhutan"), BV("BV", "BVT", "BV", "Bouvet Island"), BW("BW", "BWA", "BS", "Botswana"), BY("BY", "BLR", "BW", "Belarus"), BZ(
			"BZ", "BLZ", "BH", "Belize"), CA("CA", "CAN", "CN", "Canada"), CC("CC", "CCK", "XB", "Cocos"), CD("CD", "COD", "CG", "Congo"), CF("CF", "CAF", "CX", "Central African Republic"), CG("CG", "COG",
			"CF", "Congo"), CH("CH", "CHE", "SZ", "Switzerland"), CI("CI", "CIV", "IV", "Cote D'Ivoire"), CK("CK", "COK", "CW", "Cook Islands"), CL("CL", "CHL", "CL", "Chile"), CM("CM", "CMR", "CM",
			"Cameroon"), CN("CN", "CHN", "CC", "China"), CO("CO", "COL", "CK", "Colombia"), CR("CR", "CRI", "CR", "Costa Rica"), CS("CS", "SCG", "RB", "Serbia and Montenegro"), CU("CU", "CUB", "VU", "Cuba"), CV(
			"CV", "CPV", "CV", "Cape Verde"), CX("CX", "CXR", "XA", "Christmas Island"), CY("CY", "CYP", "CY", "Cyprus"), CZ("CZ", "CZE", "XR", "Czech Republic"), DE("DE", "DEU", "GW", "Germany"), DJ("DJ",
			"DJI", "FT", "Djibouti"), DK("DK", "DNK", "DK", "Denmark"), DM("DM", "DMA", "DQ", "Dominica"), DO("DO", "DOM", "DR", "Dominican Republic"), DZ("DZ", "DZA", "AE", "Algeria"), EC("EC", "ECU",
			"EC", "Ecuador"), EE("EE", "EST", "ER", "Estonia"), EG("EG", "EGY", "UA", "Egypt"), EH("EH", "ESH", "SS", "Western Sahara"), ER("ER", "ERI", "EA", "Eritrea"), ES("ES", "ESP", "SP", "Spain"), ET(
			"ET", "ETH", "ET", "Ethiopia"), FI("FI", "FIN", "FI", "Finland"), FJ("FJ", "FJI", "FJ", "Fiji"), FK("FK", "FLK", "FK", "Falkland Islands"), FM("FM", "FSM", "FM", "Micronesia"), FO("FO", "FRO",
			"FA", "Faeroe Islands"), FR("FR", "FRA", "FR", "France"), GA("GA", "GAB", "GO", "Gabon"), GB("GB", "GBR", "UK", "United Kingdom"), GD("GD", "GRD", "GD", "Grenada"), GE("GE", "GEO", "GS",
			"Georgia"), GF("GF", "GUF", "FG", "French Guiana"), GG("GG", "GGY", "", "Guernsey"), GH("GH", "GHA", "GH", "Ghana"), GI("GI", "GIB", "GI", "Gibraltar"), GL("GL", "GRL", "GL", "Greenland"), GM(
			"GM", "GMB", "GM", "Gambia"), GN("GN", "GIN", "GV", "Guinea"), GP("GP", "GLP", "GP", "Guadaloupe"), GQ("GQ", "GNQ", "GV", "Equatorial Guinea"), GR("GR", "GRC", "GR", "Greece"), GS("GS", "SGS",
			"XS", "South Georgia"), GT("GT", "GTM", "GT", "Guatemala"), GU("GU", "GUM", "GU", "Guam"), GW("GW", "GNB", "PG", "Guinea-Bissau"), GY("GY", "GUY", "GY", "Guyana"), HK("HK", "HKG", "-HK",
			"Hong Kong"), HM("HM", "HMD", "HM", "Heard and McDonald Islands"), HN("HN", "HND", "HO", "Honduras"), HR("HR", "HRV", "", "Croatia"), HT("HT", "HTI", "HT", "Haiti"), HU("HU", "HUN", "HU",
			"Hungary"), ID("ID", "IDN", "IO", "Indonesia"), IE("IE", "IRL", "IE", "Ireland"), IL("IL", "ISR", "IS", "Israel"), IM("IM", "IMN", "", "Isle of Man"), IN("IN", "IND", "II", "India"), IO("IO",
			"IOT", "BI", "British Indian Ocean Territory"), IQ("IQ", "IRQ", "IQ", "Iraq"), IR("IR", "IRN", "IR", "Iran"), IS("IS", "ISL", "IC", "Iceland"), IT("IT", "ITA", "IT", "Italy"), JE("JE", "JEY",
			"", "Jersey"), JM("JM", "JAM", "JM", "Jamaica"), JO("JO", "JOR", "JO", "Jordan"), JP("JP", "JPN", "JA", "Japan"), KE("KE", "KEN", "KE", "Kenya"), KG("KG", "KGZ", "KG", "Kyrgyz Republic"), KH(
			"KH", "KHM", "CB", "Cambodia"), KI("KI", "KIR", "GB", "Kiribati"), KM("KM", "COM", "CQ", "Comoros"), KN("KN", "KNA", "XD", "St. Kitts and Nevis"), KP("KP", "PRK", "KO", "Korea South"), KR("KR",
			"KOR", "KN", "Korea North"), KW("KW", "KWT", "KU", "Kuwait"), KY("KY", "CYM", "CJ", "Cayman Islands"), KZ("KZ", "KAZ", "KZ", "Kazakhstan"), LA("LA", "LAO", "LS", "Laos"), LB("LB", "LBN", "LE",
			"Lebanon"), LC("LC", "LCA", "XK", "St. Lucia"), LI("LI", "LIE", "LH", "Liechtenstein"), LK("LK", "LKA", "", "Sri Lanka"), LR("LR", "LBR", "LB", "Liberia"), LS("LS", "LSO", "LO", "Lesotho"), LT(
			"LT", "LTU", "LI", "Lithuania"), LU("LU", "LUX", "LU", "Luxembourg"), LV("LV", "LVA", "LV", "Latvia"), LY("LY", "LBY", "LY", "Libya"), MA("MA", "MAR", "MR", "Morocco"), MC("MC", "MCO", "MC",
			"Monaco"), MD("MD", "MDA", "MV", "Moldova"), ME("ME", "MNE", "MO", "Montenegro"), MF("MF", "MAF", "", "Saint Martin"), MG("MG", "MDG", "MG", "Madagascar"), MH("MH", "MHL", "XE",
			"Marshall Islands"), MK("MK", "MKD", "XN", "Macedonia"), ML("ML", "MLI", "ML", "Mali"), MM("MM", "MMR", "", "Myanmar"), MN("MN", "MNG", "MP", "Mongolia"), MO("MO", "MAC", "-MH", "Macao"), MP(
			"MP", "MNP", "-NM", "Northern Mariana Islands"), MQ("MQ", "MTQ", "MQ", "Martinique"), MR("MR", "MRT", "MU", "Mauritania"), MS("MS", "MSR", "MJ", "Montserrat"), MT("MT", "MLT", "MM", "Malta"), MU(
			"MU", "MUS", "MF", "Mauritius"), MV("MV", "MDV", "XC", "Maldives"), MW("MW", "MWI", "MW", "Malawi"), MX("MX", "MEX", "MX", "Mexico"), MY("MY", "MYS", "MY", "Malaysia"), MZ("MZ", "MOZ", "MZ",
			"Mozambique"), NA("NA", "NAM", "SX", "Namibia"), NC("NC", "NCL", "NL", "New Caledonia"), NE("NE", "NER", "NG", "Niger"), NF("NF", "NFK", "NX", "Norfolk Island"), NG("NG", "NGA", "NR", "Nigeria"), NI(
			"NI", "NIC", "NQ", "Nicaragua"), NL("NL", "NLD", "NE", "Netherlands"), NO("NO", "NOR", "NO", "Norway"), NP("NP", "NPL", "NP", "Nepal"), NR("NR", "NRU", "NU", "Nauru"), NU("NU", "NIU", "XH",
			"Niue"), NZ("NZ", "NZL", "NZ", "New Zealand"), OM("OM", "OMN", "MK", "Oman"), PA("PA", "PAN", "PN", "Panama"), PE("PE", "PER", "PE", "Peru"), PF("PF", "PYF", "", "French Polynesia"), PG("PG",
			"PNG", "PP", "Papua New Guinea"), PH("PH", "PHL", "PH", "Philippines"), PK("PK", "PAK", "PK", "Pakistan"), PL("PL", "POL", "PL", "Poland"), PM("PM", "SPM", "XL", "St. Pierre and Miquelon"), PN(
			"PN", "PCN", "PC", "Pitcairn Island"), PR("PR", "PRI", "PR", "Puerto Rico"), PS("PS", "PSE", "WJ", "Palestina"), PT("PT", "PRT", "PO", "Portugal"), PW("PW", "PLW", "PW", "Palau"), PY("PY",
			"PRY", "PY", "Paraguay"), QA("QA", "QAT", "QA", "Qatar"), RE("RE", "REU", "RE", "Reunion"), RO("RO", "ROU", "RM", "Romania"), RS("RS", "SRB", "RB", "Serbia"), RU("RU", "RUS", "RU",
			"Russian Federation"), RW("RW", "RWA", "RW", "Rwanda"), SA("SA", "SAU", "SU", "Saudi Arabia"), SB("SB", "SLB", "BP", "Solomon Islands"), SC("SC", "SYC", "SE", "Seychelles"), SD("SD", "SDN",
			"SJ", "Sudan"), SE("SE", "SWE", "SW", "Sweden"), SG("SG", "SGP", "SI", "Singapore"), SH("SH", "SHN", "XJ", "St. Helena"), SI("SI", "SVN", "XV", "Slovenia"), SJ("SJ", "SJM", "-SB",
			"Svalbard & Jan Mayen Islands"), SK("SK", "SVK", "XO", "Slovakia"), SL("SL", "SLE", "SL", "Sierra Leone"), SM("SM", "SMR", "SM", "San Marino"), SN("SN", "SEN", "SG", "Senegal"), SO("SO", "SOM",
			"SO", "Somalia"), SR("SR", "SUR", "SR", "Suriname"), SS("SS", "SSD", "SD", "South Sudan"), ST("ST", "STP", "SF", "Sao Tome and Principe"), SV("SV", "SLV", "ES", "El Salvador"), SY("SY", "SYR", "SY", "Syrian Arab Republic"), SZ(
			"SZ", "SWZ", "SQ", "Swaziland"), TC("TC", "TCA", "TC", "Turks and Caicos Islands"), TD("TD", "TCD", "CD", "Chad"), TF("TF", "ATF", "", "French Southern Territories"), TG("TG", "TGO", "TG",
			"Togo"), TH("TH", "THA", "TH", "Thailand"), TJ("TJ", "TJK", "TA", "Tajikistan"), TK("TK", "TKL", "TL", "Tokelau"), TL("TL", "TLS", "EM", "Timor-Leste"), TM("TM", "TKM", "TK", "Turkmenistan"), TN(
			"TN", "TUN", "TI", "Tunisia"), TO("TO", "TON", "TO", "Tonga"), TR("TR", "TUR", "TU", "Turkey"), TT("TT", "TTO", "TF", "Trinidad and Tobago"), TV("TV", "TUV", "TV", "Tuvalu"), TW("TW", "TWN",
			"", "Taiwan"), TZ("TZ", "TZA", "TZ", "Tanzania"), UA("UA", "UKR", "UN", "Ukraine"), UG("UG", "UGA", "UG", "Uganda"), UM("UM", "UMI", "UP", "United States Minor Outlying Islands"), US("US",
			"USA", "US", "United States"), UY("UY", "URY", "UY", "Uruguay"), UZ("UZ", "UZB", "UZ", "Uzbekistan"), VA("VA", "VAT", "VC", "Vatican City State"), VC("VC", "VCT", "XM",
			"St. Vincent and the Grenadines"), VE("VE", "VEN", "VE", "Venezuela"), VG("VG", "VGB", "VB", "British Virgin Islands"), VI("VI", "VIR", "VI", "US Virgin Islands"), VN("VN", "VNM", "VM",
			"Vietnam"), VU("VU", "VUT", "NN", "Vanuatu"), WF("WF", "WLF", "WF", "Wallis and Futuna Islands"), WS("WS", "WSM", "WS", "Samoa"), YE("YE", "YEM", "YE", "Yemen"), YT("YT", "MYT", "OT", "Mayotte"), ZA(
			"ZA", "ZAF", "SA", "South Africa"), ZM("ZM", "ZMB", "ZA", "Zambia"), ZW("ZW", "ZWE", "RH", "Zimbabwe"), XX("XX","XXX","XX", "Unkown");

    private static final HashMap<String, List<String>> iso2Mapping = new HashMap<String, List<String>>() {{
        put("gb", Arrays.asList("uk"));
     }};
	
    private static final HashMap<String, Country> iso2Lookup = new HashMap<String, Country>() {{
      for (Country country : Country.values()) {
        if (country.getIso2() != null && country.getLocale() != null) {
            String code = country.getIso2().toLowerCase();
            put(code, country);
            if (iso2Mapping.containsKey(code)) {
                List<String> aliases = iso2Mapping.get(code);
                for (String alias : aliases) {
                    put(alias, country);
                }
            }
        }
      }
    }};
    
    private static final HashMap<String, List<String>> iso3Mapping = new HashMap<String, List<String>>() {{
        put("deu", Arrays.asList("ger"));
     }};


    private static final HashMap<String, Country> iso3Lookup = new HashMap<String, Country>() {{
      for (Country country : Country.values()) {
        if (country.getIso3() != null && country.getLocale() != null) {
            String code = country.getIso3().toLowerCase();
            put(code, country);
            
            if (iso3Mapping.containsKey(code)) {
                List<String> aliases = iso3Mapping.get(code);
                for (String alias : aliases) {
                    put(alias, country);
                }
            }
        }
      }
    }};

    private static final HashMap<String, List<String>> marcMapping = new HashMap<String, List<String>>() {{
        put("uk", Arrays.asList("-uk", "-ui", "-uik", "enk", "xxk"));
        put("us", Arrays.asList("-us", "xxu", "nyu", "mau", "nju", "wiu"));
        put("cn", Arrays.asList("-cn", "xxc"));
        put("wj", Arrays.asList("gz"));
     }};

    private static final HashMap<String, Country> marcLookup = new HashMap<String, Country>() {{
    	for (Country country : Country.values()) {
            String code = country.getMarc().toLowerCase();
            put(code, country);

            if (marcMapping.containsKey(code)) {
                List<String> aliases = marcMapping.get(code);
                for (String alias : aliases) {
                    put(alias, country);
                }
            }
    	}
    }};

    private static final HashMap<String, Country> textLookup = new HashMap<String, Country>() {{
      for (Country country : Country.values()) {
        put(country.getName().toLowerCase(), country);
      }
    }};

    private final String                               iso3;
    private final String                               marc;
    private final String                               name;
    private final Locale                               locale;
    private final String                               translationKey;

    Country(String iso2, String iso3, String marc, String name) {
        this.iso3 = iso3;
        this.marc = marc;
        this.name = name;
        this.locale = new Locale("", iso2);
        this.translationKey = "country." + iso3;
    }

    /**
     * @return iso3 code for country
     */
    public String getIso3() {
        return iso3;
    }

    /**
     * @return MARC representation for country
     */
    public String getMarc() {
        return marc;
    }

    /**
     * @return iso2 code for country
     */
    public String getIso2() {
        if (locale == null) return "";
        return locale.getCountry();
    }

    /**
     * @return natural name of country
     */
    public String getName() {
        return name;
    }

    @Override
    public String translate(Locale locale) {
        return getName(locale);
    }

    /**
     * @param name
     *            of the country returned in specified language
     * @return
     */
    public String getName(Locale locale) {
        return Translations.getTranslation(translationKey, locale);
    }

    /**
     * @return locale of country
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @param name
     *            natural name of country
     * @return country as enum looked up by name
     */
    public static Country getByName(String name) {
        if (name != null) { return textLookup.get(name.toLowerCase()); }
        return null;
    }

    /**
     * @param iso2
     *            iso2 code of country
     * @return country as enum looked up by iso2
     */
    public static Country getByIso2(String iso2) {
        if (iso2 != null) { return iso2Lookup.get(iso2.toLowerCase()); }
        return null;
    }

    /**
     * @param iso3
     *            iso3 code of country
     * @return country as enum looked up by iso3
     */
    public static Country getByIso3(String iso3) {
        if (iso3 != null) { return iso3Lookup.get(iso3.toLowerCase()); }
        return null;
    }

    /**
     * @param marc
     *            MARC code of country
     * @return country as enum looked up by MARC
     */
    public static Country getByMarc(String marc) {
        if (marc != null) { return marcLookup.get(marc.toLowerCase()); }
        return null;
    }

    /**
     * @param countryNameOrCode
     *            iso3, iso2, marc or name
     * @return country as enum looked up implicitly recognized type of given string representation,
     *         or null
     */
    public static Country lookupCountry(String countryNameOrCode, boolean marc) {
        if (countryNameOrCode != null) {
            if (marc) {
                Country country = Country.getByMarc(countryNameOrCode);
                return country;
            } else {
                if (countryNameOrCode.length() == 2) {
                    Country country = Country.getByIso2(countryNameOrCode);
                    return country;
                } else if (countryNameOrCode.length() == 3) {
                    Country country = Country.getByIso3(countryNameOrCode);
                    return country;
                } else {
                    Country country = Country.getByName(countryNameOrCode);
                    return country;
                }
            }
        }
        return null;
    }
}