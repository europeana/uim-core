/* PartyObjectTest.java - created on Sep 30, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.party;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.theeuropeanlibrary.model.spatial.SpatialEntity;
import org.theeuropeanlibrary.model.subject.Subject;
import org.theeuropeanlibrary.model.time.Instant;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Sep 30, 2011
 */
public class PartyObjectTest {

    @Test
    public void testParty() {
        Party party = new Party();
        assertNull(party.getPartyName());
        
        party = new Party("test");
        assertNotNull(party.getPartyName());
        assertEquals("test", party.getPartyName());
                
        party = new Party("main");
        assertNotNull(party.getPartyName());
        assertEquals("main", party.getPartyName());
        
        assertEquals(new Party("main"), party);
        assertEquals(new Party("main").hashCode(), party.hashCode());
        
        party.setLocation(new SpatialEntity());
        assertFalse(new Party("main").equals(party));
        
        party.setSubject(new Subject());
        assertFalse(new Party("main").equals(party));

        Party party2 = new Party("main");
        party2.setLocation(new SpatialEntity());
        party2.setSubject(new Subject());
                
        assertEquals(party, party2);
    }
    
    
    @Test
    public void testFamily() {
        Family party = new Family();
        assertNull(party.getPartyName());
        
        party = new Family("test");
        assertNotNull(party.getPartyName());
        assertEquals("test", party.getPartyName());
                
        party = new Family("main");
        assertNotNull(party.getPartyName());
        assertEquals("main", party.getPartyName());
        
        assertEquals(new Family("main"), party);
        assertEquals(new Family("main").hashCode(), party.hashCode());
        
        party.setLocation(new SpatialEntity());
        assertFalse(new Family("main").equals(party));
        
        party.setSubject(new Subject());
        assertFalse(new Family("main").equals(party));

        Family party2 = new Family("main");
        party2.setLocation(new SpatialEntity());
        party2.setSubject(new Subject());
                
        assertEquals(party, party2);
    }
    
    
    @Test
    public void testOrganization() {
        Organization title = new Organization();
        assertNull(title.getPartyName());
        assertNull(title.getSubdivision());
        assertNull(title.getDisplay());
        
        title = new Organization("test");
        assertNotNull(title.getPartyName());
        assertEquals("test", title.getPartyName());
        assertNull(title.getSubdivision());
        assertNotNull(title.getDisplay());
        assertEquals("test", title.getDisplay());
                
        title = new Organization("main", "sub");
        assertNotNull(title.getPartyName());
        assertEquals("main", title.getPartyName());
        assertNotNull(title.getSubdivision());
        assertEquals("sub", title.getSubdivision());
        assertNotNull(title.getDisplay());
        assertEquals("main, sub", title.getDisplay());
        
        assertEquals(new Organization("main", "sub"), title);
        assertEquals(new Organization("main", "sub").hashCode(), title.hashCode());
    }
    
    
    @Test
    public void testMeeting() {
        Meeting title = new Meeting();
        assertNull(title.getPartyName());
        assertNull(title.getSubdivision());
        assertNull(title.getDisplay());
        
        title = new Meeting("test");
        assertNotNull(title.getPartyName());
        assertEquals("test", title.getPartyName());
        assertNull(title.getSubdivision());
        assertNotNull(title.getDisplay());
        assertEquals("test", title.getDisplay());
                
        title = new Meeting("main", new Instant(1234));
        assertNotNull(title.getPartyName());
        assertEquals("main", title.getPartyName());
        
        assertEquals(new Meeting("main", new Instant(1234)), title);
        assertEquals(new Meeting("main", new Instant(1234)).hashCode(), title.hashCode());
    }
    
    

    @Test
    public void testPerson() {
        Person title = new Person();
        assertNull(title.getPartyName());
        assertNull(title.getFullName());
        
        title = new Person("test");
        assertNotNull(title.getPartyName());
        assertEquals("test", title.getPartyName());
        assertNotNull(title.getFullName());
        assertEquals("test", title.getFullName());
                
        title = new Person("main", "sub");
        assertNotNull(title.getPartyName());
        assertEquals("sub main", title.getPartyName());
        assertNotNull(title.getSurname());
        assertEquals("sub", title.getFirstNames());
        assertNotNull(title.getFullName());
        assertEquals("sub main", title.getFullName());
        assertEquals("main sub", title.getFullNameInverted());
        
        assertEquals(new Person("main", "sub"), title);
        assertEquals(new Person("main", "sub").hashCode(), title.hashCode());
    }
    
    

    
}
