/* TestDateUtils.java - created on Feb 16, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package eu.europeana.uim.common;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.text.ParseException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 * Test of functions in {@link FileUtils}.
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Feb 16, 2011
 */
public class TestFileUtils {
    /**
     * test if parsing of 4 and 8 digit dates works correct.
     * 
     * @throws ParseException
     */
    @Test
    public void testTail() throws ParseException {
        File file = new File("src/test/resources/testfile.txt");

        List<String> tail = FileUtils.tail(file, 5);
        assertEquals(5, tail.size());
        String join = StringUtils.join(tail, ",");
        assertEquals("e,f,g,h,i", join);

        tail = FileUtils.tail(file, 3);
        assertEquals(3, tail.size());
        join = StringUtils.join(tail, ",");
        assertEquals("g,h,i", join);

        tail = FileUtils.tail(file, 1000);
        assertEquals(9, tail.size());
        join = StringUtils.join(tail, ",");
        assertEquals("a,b,c,d,e,f,g,h,i", join);
    }
}
