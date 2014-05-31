package eu.europeana.uim.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple class which provides static date manipulation and parsing
 * functionalities not covered by @see
 * {@link org.apache.commons.lang.time.DateUtils}.
 *
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 16, 2011
 */
public class DateUtils {

    private static final Logger log = Logger.getLogger(DateUtils.class.getName());

    private static final ThreadLocal<DateFormat> dfFull
            = new ThreadLocal<DateFormat>() {
                @Override
                protected DateFormat initialValue() {
                    return new SimpleDateFormat("yyyy-MM-dd");
                }
            };

    private static final ThreadLocal<DateFormat> dfYear
            = new ThreadLocal<DateFormat>() {
                @Override
                protected DateFormat initialValue() {
                    return new SimpleDateFormat("yyyy");
                }
            };

    /**
     * parses the given date string with the the following patterns: yyyy-MM-dd
     * and yyyy based on the length of the provided date string
     *
     * @param datestring
     * @return the parsed date/year or null if it cannot be parsed
     */
    public static Date parse(String datestring) {
        if (datestring == null) {
            return null;
        }
        try {
            if (datestring.length() <= 4) {
                return (dfYear.get().parse(datestring));
            } else {
                return (dfFull.get().parse(datestring));
            }
        } catch (ParseException e) {
            log.log(Level.WARNING, "Failed to parse <{0}>", datestring);
        }
        return null;
    }

}
