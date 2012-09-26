/* CombinedObjectTest.java - created on Sep 30, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;
import org.theeuropeanlibrary.model.common.qualifier.LinkStatus;

/**
 * 
 * 
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Sep 30, 2011
 */
public class CoreObjectTest {

    
    /**
     * 
     */
    @Test
    public void testTitleSimple() {
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
    
    
    /**
     * Tests the conversion of Title
     * 
     * @throws IOException
     */
    @Test
    public void testTitle() throws IOException {
        Title mainTitle = new Title("title", "subtitle");
        Assert.assertEquals("title", mainTitle.getTitle());
        Assert.assertEquals("subtitle", mainTitle.getSubTitle());

        Title uniformTitle = new Title("title uniform", null);
        Assert.assertEquals("title uniform", uniformTitle.getTitle());
        Assert.assertNull(uniformTitle.getSubTitle());
    }

    
    /**
     * 
     */
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
    
    /**
     * 
     */
    @Test
    public void testNumberingSimple() {
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
    
    /**
     * Tests the conversion of Numbering
     * 
     * @throws IOException
     */
    @Test
    public void testNumbering() throws IOException {
        Numbering enc = new Numbering(10);
        Assert.assertEquals(10, enc.getNumber());
    }

    
    /**
     * 
     */
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
    
    

    
    /**
     * 
     */
    @Test
    public void testIdentifierSimple() {
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
        
        String encodedForm = identifier.getStringEncodedForm();
        assertEquals(identifier, Identifier.decodeIdentifier(encodedForm));
    }
    
    
    /**
     * Tests the conversion of Identifier
     * 
     * @throws IOException
     */
    @Test
    public void testIdentifier() throws IOException {
        Identifier isbn = new Identifier("9828237260", null);
        Assert.assertEquals("9828237260", isbn.getIdentifier());
        Assert.assertNull(isbn.getScope());

        Identifier depositNumber = new Identifier("7260", "NL");
        Assert.assertEquals("7260", depositNumber.getIdentifier());
        Assert.assertEquals("NL", depositNumber.getScope());
    }

    
}
