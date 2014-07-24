/* Language.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common.qualifier;

import java.util.HashMap;
import java.util.Locale;

import org.theeuropeanlibrary.translation.Translatable;
import org.theeuropeanlibrary.translation.Translations;

/**
 * Language enum with english language name and ISO639-2 3 letter codes, ISO639 2 letter codes and
 * available java locale.
 * 
 * @author Anna Gos (anna.gos@kb.nl)
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 18, 2011
 */
@SuppressWarnings("all")
public enum Language implements Translatable {
    UND("und", "Undetermined", null), MUL("mul", "Multiple languages", null), ZXX("zxx", "No linguistic content", null), AFR("afr", "Afrikaans", new Locale("af")), ALB("alb", "Albanian", new Locale(
            "sq")), AMH("amh", "Amharic", new Locale("am")), ARA("ara", "Arabic", new Locale("ar")), ARM("arm", "Armenian", new Locale("hy")), AZE("aze", "Azerbaijani", new Locale("az")), BEL("bel",
            "Belarusian", new Locale("be")), BOS("bos", "Bosnian", new Locale("bs")), BRA("bra", "Brazilian", new Locale("br")), BUL("bul", "Bulgarian", new Locale("bg")), CAT("cat", "Catalan", new Locale(
            "es")), CHI("chi", "Chinese", new Locale("zh")), CZE("cze", "Czech", new Locale("cs")), DAN("dan", "Danish", new Locale("da")), DUT("dut", "Dutch", new Locale("nl")), ENG("eng", "English",
            new Locale("en")), EST("est", "Estonian", new Locale("et")), EPO("epo", "Esperanto", new Locale("pl")), // esperanto
    // is a language of
    // Poland
    FIN("fin", "Finnish", new Locale("fi")), FRE("fre", "French", new Locale("fr")), GER("ger", "German", new Locale("de")), GEO("geo", "Georgian", new Locale("ka")), GLE("gle", "Irish", new Locale(
            "ga")), GLG("glg", "Galician", new Locale("es")), // galician
    // is mostly spoken
    // in
    // Spain, but also
    // Portugal
    GRE("gre", "Greek, Modern", new Locale("el")),
    GRC("grc", "Greek, Ancient", null),
    GSW("gsw", "Swiss German", null), HEB("heb", "Hebrew", new Locale("he")), HIN("hin", "Hindi", new Locale("hi")), HRV("hrv", "Croatian", new Locale("hr")), HUN(
            "hun", "Hungarian", new Locale("hu")), ICE("ice", "Icelandic", new Locale("is")), IND("ind", "Indonesia", new Locale("in")), ITA("ita", "Italian", new Locale("it")), JPN("jpn", "Japanese",
            new Locale("jp")), LAT("lat", "Latin", new Locale("la")), LAV("lav", "Latvian", new Locale("lv")), LTG("ltg", "Latgalian", new Locale("lv")), // dialect
    // of latvia
    LIT("lit", "Lithuanian", new Locale("lt")), LTZ("ltz", "Luxembourgish", new Locale("lb")), MAC("mac", "Macedonian", new Locale("mk")), MAR("mar", "Marathi", new Locale("mr")), MLT("mlt", "Maltese",
            new Locale("mt")), MSA("msa", "Malay", new Locale("ms")), NOR("nor", "Norwegian", new Locale("no")), PER("per", "Persian", new Locale("fa")), POL("pol", "Polish", new Locale("pl")), POR("por",
            "Portuguese", new Locale("pt")), ROH("roh", "Romansh", null), RUM("rum", "Romanian", new Locale("ro")), RUS("rus", "Russian", new Locale("ru")), SLO("slo", "Slovak", new Locale("sk")), SLV(
            "slv", "Slovenian", new Locale("sl")), SPA("spa", "Spanish", new Locale("es")), SRP("srp", "Serbian", new Locale("sr")), SWE("swe", "Swedish", new Locale("sv")), TUR("tur", "Turkish",
            new Locale("tr")), UKR("ukr", "Ukrainian", new Locale("uk")), VIE("vie", "Vietnamese", new Locale("vi")), WEL("wel", "Welsh", new Locale("cy")), YID("yid", "Yiddish", new Locale("yi")),

    AAR("aar", "Afar", new Locale("aa")), ABK("abk", "Abkhazian", new Locale("ab")), ACE("ace", "Achinese", null), ACH("ach", "Acoli", null), ADA("ada", "Adangme", null), ADY("ady", "Adyghe", null), AFA(
            "afa", "Afro-Asiatic languages", null), AFH("afh", "Afrihili", null), AIN("ain", "Ainu", null), AKA("aka", "Akan", new Locale("ak")), AKK("akk", "Akkadian", null), ALE("ale", "Aleut", null), ALG(
            "alg", "Algonquian languages", null), ALT("alt", "Southern Altai", null), ANG("ang", "English, Old ", null), ANP("anp", "Angika", null), APA("apa", "Apache languages", null), ARC("arc",
            "Official Aramaic ", null), ARG("arg", "Aragonese", new Locale("an")), ARN("arn", "Mapudungun", null), ARP("arp", "Arapaho", null), ART("art", "Artificial languages", null), ARW("arw",
            "Arawak", null), ASM("asm", "Assamese", new Locale("as")), AST("ast", "Asturian", null), ATH("ath", "Athapascan languages", null), AUS("aus", "Australian languages", null), AVA("ava", "Avaric",
            new Locale("av")), AVE("ave", "Avestan", new Locale("ae")), AWA("awa", "Awadhi", null), AYM("aym", "Aymara", new Locale("ay")), BAD("bad", "Banda languages", null), BAI("bai",
            "Bamileke languages", null), BAK("bak", "Bashkir", new Locale("ba")), BAL("bal", "Baluchi", null), BAM("bam", "Bambara", new Locale("bm")), BAN("ban", "Balinese", null), BAS("bas", "Basa", null), BAT(
            "bat", "Baltic languages", null), BEJ("bej", "Beja", null), BEM("bem", "Bemba", null), BEN("ben", "Bengali", new Locale("bn")), BER("ber", "Berber languages", null), BHO("bho", "Bhojpuri", null), BIH(
            "bih", "Bihari languages", new Locale("bh")), BIK("bik", "Bikol", null), BIN("bin", "Bini", null), BIS("bis", "Bislama", new Locale("bi")), BLA("bla", "Siksika", null), BNT("bnt",
            "Bantu languages", null), BRE("bre", "Breton", new Locale("br")), BTK("btk", "Batak languages", null), BUA("bua", "Buriat", null), BUG("bug", "Buginese", null), BYN("byn", "Blin", null), CAD(
            "cad", "Caddo", null), CAI("cai", "Central American Indian languages", null), CAR("car", "Galibi Carib", null), CAU("cau", "Caucasian languages", null), CEB("ceb", "Cebuano", null), CEL("cel",
            "Celtic languages", null), CHA("cha", "Chamorro", new Locale("ch")), CHB("chb", "Chibcha", null), CHE("che", "Chechen", new Locale("ce")), CHG("chg", "Chagatai", null), CHK("chk", "Chuukese",
            null), CHM("chm", "Mari", null), CHN("chn", "Chinook jargon", null), CHO("cho", "Choctaw", null), CHP("chp", "Chipewyan", null), CHR("chr", "Cherokee", null), CHU("chu", "Church Slavic",
            new Locale("cu")), CHV("chv", "Chuvash", new Locale("cv")), CHY("chy", "Cheyenne", null), CMC("cmc", "Chamic languages", null), COP("cop", "Coptic", null), COR("cor", "Cornish",
            new Locale("kw")), COS("cos", "Corsican", new Locale("co")), CPE("cpe", "Creoles and pidgins, English based", null), CPF("cpf", "Creoles and pidgins, French-based", null), CPP("cpp",
            "Creoles and pidgins, Portuguese-based", null), CRE("cre", "Cree", new Locale("cr")), CRH("crh", "Crimean Tatar", null), CRP("crp", "Creoles and pidgins", null), CSB("csb", "Kashubian", null), CUS(
            "cus", "Cushitic languages", null), DAK("dak", "Dakota", null), DAR("dar", "Dargwa", null), DAY("day", "Land Dayak languages", null), DEL("del", "Delaware", null), DEN("den", "Slave ", null), DGR(
            "dgr", "Dogrib", null), DIN("din", "Dinka", null), DIV("div", "Divehi", new Locale("dv")), DOI("doi", "Dogri", null), DRA("dra", "Dravidian languages", null), DSB("dsb", "Lower Sorbian", null), DUA(
            "dua", "Duala", null), DUM("dum", "Dutch, Middle ", null), DYU("dyu", "Dyula", null), DZO("dzo", "Dzongkha", new Locale("dz")), EFI("efi", "Efik", null), 

            EGY("egy", "Egyptian, ancient", null),
            ARZ("arz", "Egyptian Arabic", null),
            SRI("sri", "Siriano", null),
            WLM("wlm", "Middle Welsh", null),
            PES("pes", "Iranian Persian", null),
            AXM("axm", "Armenian, Middle", null),
            PAN("pan", "Panjabi", null),
            SRM("srm", "Saramaccan", null),
            PSP("psp", "Philippine Sign", null),
            AEN("aen", "Armenian Sign", null),
            
            EKA("eka", "Ekajuk", null), ELX("elx", "Elamite", null), ENM("enm", "English, Middle ", null), BAQ("baq", "Basque", new Locale("eu")), EWE("ewe", "Ewe", new Locale("ee")), EWO("ewo", "Ewondo", null), FAN(
            "fan", "Fang", null), FAO("fao", "Faroese", new Locale("fo")), FAT("fat", "Fanti", null), FIJ("fij", "Fijian", new Locale("fj")), FIL("fil", "Filipino", null), FIU("fiu",
            "Finno-Ugrian languages", null), FON("fon", "Fon", null), FRM("frm", "French, Middle ", null), FRO("fro", "French, Old ", null), FRR("frr", "Northern Frisian", null), FRS("frs",
            "Eastern Frisian", null), FRY("fry", "Western Frisian", new Locale("fy")), FUL("ful", "Fulah", new Locale("ff")), FUR("fur", "Friulian", null), GAA("gaa", "Ga", null), GAY("gay", "Gayo", null), GBA(
            "gba", "Gbaya", null), GEM("gem", "Germanic languages", null), GEZ("gez", "Geez", null), GIL("gil", "Gilbertese", null), 
            GLA("gla", "Scottish Gaelic", new Locale("gd")), 
            GLV("glv", "Manx", new Locale(
            "gv")), GMH("gmh", "German, Middle High ", null), GOH("goh", "German, Old High ", null), GON("gon", "Gondi", null), GOR("gor", "Gorontalo", null), GOT("got", "Gothic", null), GRB("grb",
            "Grebo", null), GRN("grn", "Guarani", new Locale("gn")), GUJ("guj", "Gujarati", new Locale("gu")), GWI("gwi", "Gwich'in", null), HAI("hai", "Haida", null), HAT("hat", "Haitian",
            new Locale("ht")), HAU("hau", "Hausa", new Locale("ha")), HAW("haw", "Hawaiian", null), HER("her", "Herero", new Locale("hz")), HIL("hil", "Hiligaynon", null), HIM("him", "Himachali languages",
            null), HIT("hit", "Hittite", null), HMN("hmn", "Hmong", null), HMO("hmo", "Hiri Motu", new Locale("ho")), HSB("hsb", "Upper Sorbian", null), HUP("hup", "Hupa", null), IBA("iba", "Iban", null), IBO(
            "ibo", "Igbo", new Locale("ig")), IDO("ido", "Ido", new Locale("io")), III("iii", "Sichuan Yi", new Locale("ii")), IJO("ijo", "Ijo languages", null), IKU("iku", "Inuktitut", new Locale("iu")), ILE(
            "ile", "Interlingue", new Locale("ie")), ILO("ilo", "Iloko", null), INA("ina", "Interlingua ", new Locale("ia")), INC("inc", "Indic languages", null), INE("ine", "Indo-European languages", null), INH(
            "inh", "Ingush", null), IPK("ipk", "Inupiaq", new Locale("ik")), IRA("ira", "Iranian languages", null), IRO("iro", "Iroquoian languages", null), JAV("jav", "Javanese", new Locale("jv")), JBO(
            "jbo", "Lojban", null), JPR("jpr", "Judeo-Persian", null), JRB("jrb", "Judeo-Arabic", null), KAA("kaa", "Kara-Kalpak", null), KAB("kab", "Kabyle", null), KAC("kac", "Kachin", null), KAL("kal",
            "Kalaallisut", new Locale("kl")), KAM("kam", "Kamba", null), KAN("kan", "Kannada", new Locale("kn")), KAR("kar", "Karen languages", null), KAS("kas", "Kashmiri", new Locale("ks")), KAU("kau",
            "Kanuri", new Locale("kr")), KAW("kaw", "Kawi", null), KAZ("kaz", "Kazakh", new Locale("kk")), KBD("kbd", "Kabardian", null), KHA("kha", "Khasi", null), KHI("khi", "Khoisan languages", null), KHM(
            "khm", "Central Khmer", new Locale("km")), KHO("kho", "Khotanese", null), KIK("kik", "Kikuyu", new Locale("ki")), KIN("kin", "Kinyarwanda", new Locale("rw")), KIR("kir", "Kirghiz", new Locale(
            "ky")), KMB("kmb", "Kimbundu", null), KOK("kok", "Konkani", null), KOM("kom", "Komi", new Locale("kv")), KON("kon", "Kongo", new Locale("kg")), KOR("kor", "Korean", new Locale("ko")), KOS(
            "kos", "Kosraean", null), KPE("kpe", "Kpelle", null), KRC("krc", "Karachay-Balkar", null), KRL("krl", "Karelian", null), KRO("kro", "Kru languages", null), KRU("kru", "Kurukh", null), KUA(
            "kua", "Kuanyama", new Locale("kj")), KUM("kum", "Kumyk", null), KUR("kur", "Kurdish", new Locale("ku")), KUT("kut", "Kutenai", null), LAD("lad", "Ladino", null), LAH("lah", "Lahnda", null), LAM(
            "lam", "Lamba", null), LAO("lao", "Lao", new Locale("lo")), LEZ("lez", "Lezghian", null), LIM("lim", "Limburgan", new Locale("li")), LIN("lin", "Lingala", new Locale("ln")), LOL("lol", "Mongo",
            null), LOZ("loz", "Lozi", null), LUA("lua", "Luba-Lulua", null), LUB("lub", "Luba-Katanga", new Locale("lu")), LUG("lug", "Ganda", new Locale("lg")), LUI("lui", "Luiseno", null), LUN("lun",
            "Lunda", null), LUO("luo", "Luo ", null), LUS("lus", "Lushai", null), MAD("mad", "Madurese", null), MAG("mag", "Magahi", null), MAH("mah", "Marshallese", new Locale("mh")), MAI("mai",
            "Maithili", null), MAK("mak", "Makasar", null), MAL("mal", "Malayalam", new Locale("ml")), MAN("man", "Mandingo", null), MAP("map", "Austronesian languages", null), MAS("mas", "Masai", null), MDF(
            "mdf", "Moksha", null), MDR("mdr", "Mandar", null), MEN("men", "Mende", null), MGA("mga", "Irish, Middle ", null), MIC("mic", "Mi'kmaq", null), MIN("min", "Minangkabau", null), MIS("mis",
            "Uncoded languages", null), MKH("mkh", "Mon-Khmer languages", null), MLG("mlg", "Malagasy", new Locale("mg")), MNC("mnc", "Manchu", null), MNI("mni", "Manipuri", null), MNO("mno",
            "Manobo languages", null), MOH("moh", "Mohawk", null), MON("mon", "Mongolian", new Locale("mn")), MOS("mos", "Mossi", null), MAO("mao", "Maori", new Locale("mi")), MUN("mun", "Munda languages",
            null), MUS("mus", "Creek", null), MWL("mwl", "Mirandese", null), MWR("mwr", "Marwari", null), BUR("bur", "Burmese", new Locale("my")), MYN("myn", "Mayan languages", null), MYV("myv", "Erzya",
            null), NAH("nah", "Nahuatl languages", null), NAI("nai", "North American Indian languages", null), NAP("nap", "Neapolitan", null), NAU("nau", "Nauru", new Locale("na")), NAV("nav", "Navajo",
            new Locale("nv")), NBL("nbl", "Ndebele, South", new Locale("nr")), NDE("nde", "Ndebele, North", new Locale("nd")), NDO("ndo", "Ndonga", new Locale("ng")), NDS("nds", "Low German", null), NEP(
            "nep", "Nepali", new Locale("ne")), NEW("new", "Nepal Bhasa", null), NIA("nia", "Nias", null), NIC("nic", "Niger-Kordofanian languages", null), NIU("niu", "Niuean", null), NNO("nno",
            "Norwegian Nynorsk", new Locale("nn")), NOB("nob", "Bokmol, Norwegian", new Locale("nb")), NOG("nog", "Nogai", null), NON("non", "Norse, Old", null), NQO("nqo", "N'Ko", null), NSO("nso",
            "Pedi", null), NUB("nub", "Nubian languages", null), NWC("nwc", "Classical Newari", null), NYA("nya", "Chichewa", new Locale("ny")), NYM("nym", "Nyamwezi", null), NYN("nyn", "Nyankole", null), NYO(
            "nyo", "Nyoro", null), NZI("nzi", "Nzima", null), OCI("oci", "Occitan ", new Locale("oc")), OJI("oji", "Ojibwa", new Locale("oj")), ORI("ori", "Oriya", new Locale("or")), ORM("orm", "Oromo",
            new Locale("om")), OSA("osa", "Osage", null), OSS("oss", "Ossetian", new Locale("os")), OTA("ota", "Turkish, Ottoman ", null), OTO("oto", "Otomian languages", null), PAA("paa",
            "Papuan languages", null), PAG("pag", "Pangasinan", null), PAL("pal", "Pahlavi", null), PAM("pam", "Pampanga", null), PAP("pap", "Papiamento", null), PAU("pau", "Palauan", null), PEO("peo",
            "Persian, Old ", null), PHI("phi", "Philippine languages", null), PHN("phn", "Phoenician", null), PLI("pli", "Pali", new Locale("pi")), PON("pon", "Pohnpeian", null), PRA("pra",
            "Prakrit languages", null), PRO("pro", "Provengal, Old ", null), PUS("pus", "Pushto", new Locale("ps")), QAA("qaa", "Reserved for local use", null), QUE("que", "Quechua", new Locale("qu")), RAJ(
            "raj", "Rajasthani", null), RAP("rap", "Rapanui", null), RAR("rar", "Rarotongan", null), ROA("roa", "Romance languages", null), ROM("rom", "Romany", null), RUN("run", "Rundi", new Locale("rn")), RUP(
            "rup", "Aromanian", null), SAD("sad", "Sandawe", null), SAG("sag", "Sango", new Locale("sg")), SAH("sah", "Yakut", null), SAI("sai", "South American Indian languages", null), SAL("sal",
            "Salishan languages", null), SAM("sam", "Samaritan Aramaic", null), SAN("san", "Sanskrit", new Locale("sa")), SAS("sas", "Sasak", null), SAT("sat", "Santali", null), SCN("scn", "Sicilian", null), SCO(
            "sco", "Scots", null), SEL("sel", "Selkup", null), 
            SCR("scr", "Serbo-Croatian", new Locale("sh")),
            SEM("sem", "Semitic languages", null), SGA("sga", "Irish, Old ", null), SGN("sgn", "Sign Languages", null), SHN("shn", "Shan", null), SID(
            "sid", "Sidamo", null), SIN("sin", "Sinhala", new Locale("si")), SIO("sio", "Siouan languages", null), SIT("sit", "Sino-Tibetan languages", null), SLA("sla", "Slavic languages", null), SMA(
            "sma", "Southern Sami", null), SME("sme", "Northern Sami", new Locale("se")), SMI("smi", "Sami languages", null), SMJ("smj", "Lule Sami", null), SMN("smn", "Inari Sami", null), SMO("smo",
            "Samoan", new Locale("sm")), SMS("sms", "Skolt Sami", null), SNA("sna", "Shona", new Locale("sn")), SND("snd", "Sindhi", new Locale("sd")), SNK("snk", "Soninke", null), SOG("sog", "Sogdian",
            null), SOM("som", "Somali", new Locale("so")), SON("son", "Songhai languages", null), SOT("sot", "Sotho, Southern", new Locale("st")), SRD("srd", "Sardinian", new Locale("sc")), SRN("srn",
            "Sranan Tongo", null), SRR("srr", "Serer", null), SSA("ssa", "Nilo-Saharan languages", null), SSW("ssw", "Swati", new Locale("ss")), SUK("suk", "Sukuma", null), SUN("sun", "Sundanese",
            new Locale("su")), SUS("sus", "Susu", null), SUX("sux", "Sumerian", null), SWA("swa", "Swahili", new Locale("sw")), SYC("syc", "Classical Syriac", null), SYR("syr", "Syriac", null), TAH("tah",
            "Tahitian", new Locale("ty")), TAI("tai", "Tai languages", null), TAM("tam", "Tamil", new Locale("ta")), TAT("tat", "Tatar", new Locale("tt")), TEL("tel", "Telugu", new Locale("te")), TEM(
            "tem", "Timne", null), TER("ter", "Tereno", null), TET("tet", "Tetum", null), TGK("tgk", "Tajik", new Locale("tg")), TGL("tgl", "Tagalog", new Locale("tl")), THA("tha", "Thai", new Locale("th")), TIB(
            "tib", "Tibetan", new Locale("bo")), TIG("tig", "Tigre", null), TIR("tir", "Tigrinya", new Locale("ti")), TIV("tiv", "Tiv", null), TKL("tkl", "Tokelau", null), TLH("tlh", "Klingon", null), TLI(
            "tli", "Tlingit", null), TMH("tmh", "Tamashek", null), TOG("tog", "Tonga ", null), TON("ton", "Tonga ", new Locale("to")), TPI("tpi", "Tok Pisin", null), TSI("tsi", "Tsimshian", null), TSN(
            "tsn", "Tswana", new Locale("tn")), TSO("tso", "Tsonga", new Locale("ts")), TUK("tuk", "Turkmen", new Locale("tk")), TUM("tum", "Tumbuka", null), TUP("tup", "Tupi languages", null), TUT("tut",
            "Altaic languages", null), TVL("tvl", "Tuvalu", null), TWI("twi", "Twi", new Locale("tw")), TYV("tyv", "Tuvinian", null), UDM("udm", "Udmurt", null), UGA("uga", "Ugaritic", null), UIG("uig",
            "Uighur", new Locale("ug")), UMB("umb", "Umbundu", null), URD("urd", "Urdu", new Locale("ur")), UZB("uzb", "Uzbek", new Locale("uz")), VAI("vai", "Vai", null), VEN("ven", "Venda", new Locale(
            "ve")), VOL("vol", "Volapak", new Locale("vo")), VOT("vot", "Votic", null), WAK("wak", "Wakashan languages", null), WAL("wal", "Wolaitta", null), WAR("war", "Waray", null), WAS("was", "Washo",
            null), WEN("wen", "Sorbian languages", null), WLN("wln", "Walloon", new Locale("wa")), WOL("wol", "Wolof", new Locale("wo")), XAL("xal", "Kalmyk", null), XHO("xho", "Xhosa", new Locale("xh")), YAO(
            "yao", "Yao", null), YAP("yap", "Yapese", null), YOR("yor", "Yoruba", new Locale("yo")), YPK("ypk", "Yupik languages", null), ZAP("zap", "Zapotec", null), ZBL("zbl", "Blissymbols", null), ZEN(
            "zen", "Zenaga", null), ZHA("zha", "Zhuang", new Locale("za")), ZND("znd", "Zande languages", null), ZUL("zul", "Zulu", new Locale("zu")), ZUN("zun", "Zuni", null), ZZA("zza", "Zaza", null),

    ;
    

    private static final HashMap<String, String>   iso2mapping = new HashMap<String, String>() {{
        put("es", "sp");
    }};

    private static final HashMap<String, Language> iso2Lookup = new HashMap<String, Language>() {{
        for (Language language : Language.values()) {
            if (language.getLocale() != null) {
                put(language.getIso2().toLowerCase(), language);
            }
            
            if (iso2mapping.containsKey(language.getIso2())) {
                put(iso2mapping.get(language.getIso2()), language);
            }
        }   
    }};

    private static final HashMap<String, String[]>   iso3mapping = new HashMap<String, String[]>() {{
       put("alb", new String[] {"sqi"});
       put("arm", new String[] {"hye"});
       put("baq", new String[] {"eus"});
       put("bur", new String[] {"mya"});
       put("chi", new String[] {"zho"});
       put("cze", new String[] {"ces"});
       put("dut", new String[] {"nld"});
       put("fre", new String[] {"fra"});
       put("geo", new String[] {"kat"});
       put("ger", new String[] {"deu", "gzr"});
       put("gre", new String[] {"ell"});
       put("ice", new String[] {"isl"});
       put("mac", new String[] {"mkd"});
       put("mao", new String[] {"mri"});
       put("msa", new String[] {"may"});
       put("per", new String[] {"fas"});
       put("rum", new String[] {"ron"});
       put("slo", new String[] {"slk"});
       put("wel", new String[] {"cym"});
     }};

     private static final HashMap<String, Language> iso3Lookup = new HashMap<String, Language>() {{
      for (Language language : Language.values()) {
        String code = language.getIso3().toLowerCase();

        put(code, language);
        if (iso3mapping.containsKey(code)) {
            String[] maps = iso3mapping.get(code);
            for (String map : maps) {
                put(map, language);   
            }
        }
      }
    }};
    
    private static final HashMap<String, Language> textLookup = new HashMap<String, Language>() {{
      for (Language language : Language.values()) {
        put(language.getName().toLowerCase(), language);
        }
    }};

    private static final HashMap<Locale, Language> localeLookup = new HashMap<Locale, Language>() {{
        for (Language language : Language.values()) {
            Locale locale = language.getLocale();
            if (locale != null) {
                put(locale, language);
            }
        }
    }};
    
    private final String iso3;
    private final String name;
    private final Locale locale;
    private final String translationKey;

    Language(String iso3, String name, Locale locale) {
        this.iso3 = iso3;
        this.name = name;
        this.locale = locale;
        this.translationKey = "language." + iso3;
    }

    /**
     * @return iso3 code for language
     */
    public String getIso3() {
        return iso3;
    }

    /**
     * @return iso3 code for language or first alias
     */
    @Deprecated
    public String getAlias() {
        if (iso3mapping.containsKey(getIso3())) {
            return iso3mapping.get(getIso3())[0];
        }
        return iso3;
    }
    
    /**
     * @return iso3 code for language or all aliases
     */
    public String[] getAliases() {
        if (iso3mapping.containsKey(getIso3())) {
            return iso3mapping.get(getIso3());
        }
        return new String[] { iso3 };
    }

    /**
     * @return iso2 code for language
     */
    public String getIso2() {
        if (locale == null) return "";
        if(this==HEB) //this is a bug only fixed in java 7 - the language code is wrong for HEB
            return "he";
        else
        return locale.getLanguage();
    }

    /**
     * @return natural name of language
     */
    public String getName() {
        return name;
    }

    @Override
    public String translate(Locale locale) {
        return getName(locale);
    }

    /**
     * 
     * @param name
     *            of the language returned in specified language
     * @return
     */
    public String getName(Locale locale) {
        return Translations.getTranslation(translationKey, locale);
    }

    /**
     * @return locale of language
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @param name
     *            natural name of language
     * @return language as enum looked up by name
     */
    public static Language getByName(String name) {
        if (name != null) {
            return textLookup.get(name.toLowerCase());
        }
        return null;
    }

    /**
     * @param iso2
     *            iso2 code of language
     * @return language as enum looked up by iso2
     */
    public static Language getByIso2(String iso2) {
        if (iso2 != null) {
            return iso2Lookup.get(iso2.toLowerCase());
        }
        return null;
    }

    /**
     * @param iso3
     *            iso3 code of language
     * @return language as enum looked up by iso3
     */
    public static Language getByIso3(String iso3) {
        if (iso3 != null) {
            return iso3Lookup.get(iso3.toLowerCase());
        }
        return null;
    }

    /**
     * @param iso3
     *            iso3 code of language
     * @return language as enum looked up by iso3
     */
    public static Language getByLocale(Locale locale) {
        if (locale != null) {
            return localeLookup.get(locale);
        }
        return null;
    }
    
    /**
     * @param languageNameOrCode
     *            iso3, iso2, marc or name
     * @return language as enum looked up implicitly recognized type of given string representation
     */
    public static Language lookupLanguage(String languageNameOrCode) {
        if (languageNameOrCode != null) {
            if (languageNameOrCode.length() == 2) {
                Language language = Language.getByIso2(languageNameOrCode);
                return language;
            } else if (languageNameOrCode.length() == 3) {
                Language language = Language.getByIso3(languageNameOrCode);
                return language;
            } else {
                Language language = Language.getByName(languageNameOrCode);
                return language;
            }
        }
        return null;
    }
}
