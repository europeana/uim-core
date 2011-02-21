package eu.europeana.uim.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import eu.europeana.uim.common.parse.RecordMap;

/** Simple class which provides static date manipulation and parsing
 * functionalities not covered by @see {@link org.apache.commons.lang.time.DateUtils}. 
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @date Feb 16, 2011
 */
public class DateUtils {
    
    private static final Logger log = Logger.getLogger(RecordMap.class.getName());

    private static final DateFormat dfFull = new SimpleDateFormat("yyyy-MM-dd");

    private static final DateFormat dfYear = new SimpleDateFormat("yyyy");
	
	
	/** parses the given date string with the the following patterns: yyyy-MM-dd and yyyy
	 * based on the length of the provided datestring
	 * 
	 * @param datestring
	 * @return the parsed date/year or null if not parsable
	 */
	public static Date parse(String datestring) {
		if (datestring == null) return null;
        try {
            if (datestring.length() <= 4) {
                return (dfYear.parse(datestring));
            } else {
                return (dfFull.parse(datestring));
            }
        } catch (ParseException e) {
        	log.warning("Failed to parse <" + datestring + ">");
        }
        return null;
	}

}
