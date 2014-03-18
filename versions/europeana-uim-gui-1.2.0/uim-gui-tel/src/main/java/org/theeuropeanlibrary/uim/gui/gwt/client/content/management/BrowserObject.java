/* BrowserObject.java - created on May 20, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.uim.gui.gwt.client.content.management;

/**
 * Wrapped arbitrary object with a name to be shown in browser widget.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @since May 20, 2011
 */
public class BrowserObject {
    private final String name;
    private final Object wrappedObject;

    /**
     * Creates a new instance of this class.
     * 
     * @param name
     * @param wrappedObject
     */
    public BrowserObject(String name, Object wrappedObject) {
        this.name = name;
        this.wrappedObject = wrappedObject;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return wrapped object
     */
    public Object getWrappedObject() {
        return wrappedObject;
    }
}
