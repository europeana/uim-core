/* Title.java - created on Mar 18, 2011, Copyright (c) 2011 The European Library, all rights reserved */
package org.theeuropeanlibrary.model;

import org.theeuropeanlibrary.model.subject.TitleSubject;


/**
 * Base class for titles. Titles have a title and a sub title and a main title.
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @author Nuno Freire <nfreire@gmail.com>
 * @since Mar 18, 2011
 */
public class Title {
    /**
     * main part of this title
     */
    @FieldId(1)
    private String         title;
    /**
     * sub title form
     */
    @FieldId(2)
    private String         subTitle;

    /**
     * If this title is in the role of a subject additional informations can be retrieved throught
     * this subject object.
     */
    @FieldId(3)
    protected TitleSubject titleSubject;

    /**
     * Creates a new instance of this class.
     */
    public Title() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param title
     *            main part of this title
     * @param subTitle
     *            sub title form
     */
    public Title(String title, String subTitle) {
        if (title == null) { throw new IllegalArgumentException(
                "Argument 'title' should not be null!"); }
        this.title = title;
        this.subTitle = subTitle;
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param title
     *            main part of this title
     */
    public Title(String title) {
        this(title, null);
    }

    /**
     * @return main part of this title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return optional sub title
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * @param title
     *            main part of this title
     */
    public void setTitle(String title) {
        if (title == null) { throw new IllegalArgumentException(
                "Argument 'title' should not be null!"); }
        this.title = title;
    }

    /**
     * @param subTitle
     *            optional sub title form
     */
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    /**
     * @return title and subtitle concatenated
     */
    public String getFullTitle() {
        if (subTitle == null) return title;
        return title.trim() + " " + subTitle.trim();
    }

    /**
     * @return title and subtitle concatenated, with a ':' separating them
     */
    public String getFullTitleForDisplay() {
        if (subTitle == null) return title;
        return title.trim() + ": " + subTitle.trim();
    }

    /**
     * @return If this title is in the role of a subject additional informations can be retrieved
     *         throught this subject object.
     */
    public TitleSubject getTitleSubject() {
        return titleSubject;
    }

    /**
     * @param titleSubject
     *            If this title is in the role of a subject additional informations can be retrieved
     *            throught this subject object.
     */
    public void setTitleSubject(TitleSubject titleSubject) {
        this.titleSubject = titleSubject;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((subTitle == null) ? 0 : subTitle.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((titleSubject == null) ? 0 : titleSubject.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Title other = (Title)obj;
        if (subTitle == null) {
            if (other.subTitle != null) return false;
        } else if (!subTitle.equals(other.subTitle)) return false;
        if (title == null) {
            if (other.title != null) return false;
        } else if (!title.equals(other.title)) return false;
        if (titleSubject == null) {
            if (other.titleSubject != null) return false;
        } else if (!titleSubject.equals(other.titleSubject)) return false;
        return true;
    }
    
    @Override
    public String toString() {
    	return getFullTitleForDisplay();
    }
}
