package org.theeuropeanlibrary.model.tel.documentation;

import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A class that aims to simplify the java code for building an XML DOM. Supports
 * the building a a DOM by sequencial operations (it is not possible to go back
 * to earlier nodes.
 * 
 * @author Nuno Freire (nfreire@gmail.com)
 * @date 23 de Jan de 2012
 */
public class DomBuilder {
	/**
	 * The Document being built
	 */
	private Document document;
	/**
	 * The stack of Elements that point to the parent nodes of the current node
	 * being edited. At the top of the stack is the current Element being edited
	 */
	private Stack<Element> editingElementStack = new Stack<Element>();

	/**
	 * Creates a new instance of this class, starting to build the DOM from the
	 * given Element.
	 * 
	 * @param fromElement
	 */
	public DomBuilder(Element fromElement) {
		this.document = fromElement.getOwnerDocument();
		editingElementStack.add(fromElement);
	}

	/**
	 * Creates a new instance of this class with a new Document, with the given
	 * top element.
	 * 
	 * @param rootElementName
	 */
	public DomBuilder(String rootElementName) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.newDocument();
			Element rootEl = document.createElement(rootElementName);
			document.appendChild(rootEl);
			editingElementStack.push(rootEl);
		} catch (ParserConfigurationException pce) {
			throw new RuntimeException(pce);
		}
	}

	/**
	 * Goes to the parent of the current Element being edited
	 */
	public void goToParent() {
		editingElementStack.pop();
	}

	/**
	 * Sets an attribute on the current Element being edited
	 * 
	 * @param name
	 * @param value
	 */
	public void setAttribute(String name, String value) {
		editingElementStack.peek().setAttribute(name, value);
	}

	/**
	 * Sets an attribute on the current Element being edited
	 * 
	 * @param name
	 * @param value
	 */
	public void setAttribute(String name, Number value) {
		editingElementStack.peek().setAttribute(name, String.valueOf(value));
	}

	/**
	 * Sets an attribute on the current Element being edited
	 * 
	 * @param name
	 * @param value
	 */
	public void setAttribute(String name, Object value) {
		editingElementStack.peek().setAttribute(name, value.toString());
	}

	/**
	 * Adds a new child Element to the current Element, and sets it as the
	 * current Element being edited
	 * 
	 * @param elementName
	 */
	public void addElement(String elementName) {
	    Element newEl = document.createElement(elementName);
	    editingElementStack.peek().appendChild(newEl);
	    editingElementStack.push(newEl);
	}

	/**
	 * Adds a new child Element to the current Element
	 * 
	 * @param elementName
	 * @param text 
	 */
	public void addElementBellow(String elementName, String text) {
		Element newEl = document.createElement(elementName);
		newEl.setTextContent(text);
		editingElementStack.peek().appendChild(newEl);
	}

	/**
	 * @return the Document being edited
	 */
	public Document getDom() {
		return document;
	}

	/**
	 * @return the current Element being edited
	 */
	public Element getCurrentElement() {
		return editingElementStack.peek();
	}

	/**
	 * Sets the text of the current Element
	 * 
	 * @param text
	 */
	public void setText(String text) {
		editingElementStack.peek().setTextContent(text);
	}

    /**
     * @param text
     */
    public void addTextNode(String text) {
        editingElementStack.peek().appendChild(document.createTextNode(text));
    }

    /**
     * @param elementName 
     */
    public void addEmptyElementBellow(String elementName) {
        Element newEl = document.createElement(elementName);
        editingElementStack.peek().appendChild(newEl);
    }

}
