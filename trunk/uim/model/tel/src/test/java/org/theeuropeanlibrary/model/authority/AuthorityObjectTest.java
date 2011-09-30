/* SubjectObjectTest.java - created on Sep 30, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.authority;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.theeuropeanlibrary.model.common.party.Organization;
import org.theeuropeanlibrary.model.common.party.Person;
import org.theeuropeanlibrary.model.tel.authority.Occurrences;
import org.theeuropeanlibrary.model.tel.authority.OrganizationNameForm;
import org.theeuropeanlibrary.model.tel.authority.PersonNameForm;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Sep 30, 2011
 */
public class AuthorityObjectTest {

    /**
     * 
     */
    @Test
    public void testOccurences() {
        Occurrences<String> enc = new Occurrences<String>("a", 4);
        assertEquals(enc.getValue(), "a");
        assertEquals(enc.getCount(), 4);
        
        assertEquals(new Occurrences<String>("a", 4), enc);
        assertEquals(new Occurrences<String>("a", 4).hashCode(), enc.hashCode());
    }

    /**
     * 
     */
    @Test
    public void testOrganizationNameForm() {
        Set<String> sources = new HashSet<String>(){{add("a");add("b");}};
        OrganizationNameForm enc = new OrganizationNameForm(new Organization("test"), sources);
        assertEquals(enc.getNameForm(), new Organization("test"));
        assertEquals(enc.getSources().size(), 2);
        
        assertEquals(new OrganizationNameForm(new Organization("test"), sources), enc);
        assertEquals(new OrganizationNameForm(new Organization("test"), sources).hashCode(), enc.hashCode());
    }

    /**
     * 
     */
    @Test
    public void testPersonNameForm() {
        Set<String> sources = new HashSet<String>(){{add("a");add("b");}};
        PersonNameForm enc = new PersonNameForm(new Person("test"), sources);
        assertEquals(enc.getNameForm(), new Person("test"));
        assertEquals(enc.getSources().size(), 2);
        
        assertEquals(new PersonNameForm(new Person("test"), sources), enc);
        assertEquals(new PersonNameForm(new Person("test"), sources).hashCode(), enc.hashCode());
    }

}
