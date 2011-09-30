/* CombinedObjectTest.java - created on Sep 30, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.junit.Test;
import org.theeuropeanlibrary.qualifier.LinkStatus;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Sep 30, 2011
 */
public class CoreObjectTest {

    
    @Test
    public void testTitle() {
        Title title = new Title();
        assertNull(title.getTitle());
        assertNull(title.getSubTitle());
        assertNull(title.getFullTitle());
        
        title = new Title("test");
        assertNotNull(title.getTitle());
        assertEquals("test", title.getTitle());
        assertNull(title.getSubTitle());
        assertNotNull(title.getFullTitle());
        assertEquals("test", title.getFullTitle());
                
        title = new Title("main", "sub");
        assertNotNull(title.getTitle());
        assertEquals("main", title.getTitle());
        assertNotNull(title.getSubTitle());
        assertEquals("sub", title.getSubTitle());
        assertNotNull(title.getFullTitle());
        assertEquals("main sub", title.getFullTitle());
        assertEquals("main: sub", title.getFullTitleForDisplay());
        
        assertEquals(new Title("main", "sub"), title);
        assertEquals(new Title("main", "sub").hashCode(), title.hashCode());
    }
    
    
    @Test
    public void testText() {
        Text text = new Text();
        assertNull(text.getContent());
        
        text = new Text("test");
        assertNotNull(text.getContent());
        assertEquals("test", text.getContent());
                
        text = new Text("main");
        assertNotNull(text.getContent());
        assertEquals("main", text.getContent());
        
        assertEquals(new Text("main"), text);
        assertEquals(new Text("main").hashCode(), text.hashCode());
    }
    
    @Test
    public void testNumbering() {
        Numbering title = new Numbering();
        assertNotNull(title.getNumber());
        
        title = new Numbering(3);
        assertNotNull(title.getNumber());
        assertEquals(3, title.getNumber());
                
        title = new Numbering(4);
        assertNotNull(title.getNumber());
        assertEquals(4, title.getNumber());
        
        assertEquals(new Numbering(4), title);
        assertEquals(new Numbering(4).hashCode(), title.hashCode());
    }
    

    
    @Test
    public void testLink() {
        Link link = new Link();
        assertNull(link.getUrl());
        
        link = new Link("test");
        assertNotNull(link.getUrl());
        assertEquals(LinkStatus.NOT_CHECKED, link.getLinkStatus());
        assertEquals("test", link.getUrl());
                
        link = new Link("main");
        link.setLinkStatus(LinkStatus.NOT_CHECKED);
        assertNotNull(link.getUrl());
        assertEquals("main", link.getUrl());
        
        assertEquals(new Link("main"), link);
        assertEquals(new Link("main").hashCode(), link.hashCode());

        link.setLastChecked(new Date(0));

        Link link2 = new Link("main");
        link2.setLastChecked(new Date(0));
        assertEquals(link, link2);
    }
    
    

    
    @Test
    public void testIdentifier() {
        Identifier identifier = new Identifier();
        assertNull(identifier.getScope());
        assertNull(identifier.getIdentifier());
        
        identifier = new Identifier("test");
        assertNotNull(identifier.getIdentifier());
        assertEquals("test", identifier.getIdentifier());
                
        identifier = new Identifier("main", "scope");
        assertNotNull(identifier.getIdentifier());
        assertEquals("main", identifier.getIdentifier());
        assertNotNull(identifier.getScope());
        assertEquals("scope", identifier.getScope());
        
        assertEquals(new Identifier("main", "scope"), identifier);
        assertEquals(new Identifier("main", "scope").hashCode(), identifier.hashCode());
    }
    
    
}
