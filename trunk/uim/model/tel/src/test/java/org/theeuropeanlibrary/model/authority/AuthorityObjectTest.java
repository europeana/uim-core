/* SubjectObjectTest.java - created on Sep 30, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.authority;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Sep 30, 2011
 */
public class AuthorityObjectTest {

    @Test
    public void testOccurences() {
        Occurrences enc = new Occurrences<String>("a", 4);
        assertEquals(enc.getValue(), "a");
        assertEquals(enc.getCount(), 4);
        
        assertEquals(new Occurrences<String>("a", 4), enc);
    }

}
