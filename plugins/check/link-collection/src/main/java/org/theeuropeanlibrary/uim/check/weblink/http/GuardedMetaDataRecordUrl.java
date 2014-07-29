package org.theeuropeanlibrary.uim.check.weblink.http;

import eu.europeana.uim.guarded.GuardedUrl;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.theeuropeanlibrary.model.common.Link;

import eu.europeana.uim.store.Execution;
import eu.europeana.uim.store.MetaDataRecord;

/**
 * This class is used as store for a {@link MetaDataRecord} together with a index of a url, and an
 * actual guarded url.s
 * 
 * @param <I>
 *            the identifier type
 * @author Andreas Juffinger (andreas.juffinger@kb.nl)
 * @since Mar 16, 2011
 */
public abstract class GuardedMetaDataRecordUrl<I> extends GuardedUrl {


    private final Execution<I>      execution;
    private final MetaDataRecord<I> mdr;
    private final Link              link;
    private final int               index;

    private final File              directory;

    /**
     * Creates a new instance of this class.
     * 
     * @param execution
     * @param index
     *            the running index of this url
     * @param url
     *            link to mdr
     * @throws MalformedURLException
     */
    public GuardedMetaDataRecordUrl(Execution<I> execution, int index, String url)
                                                                                  throws MalformedURLException {
        this(execution, index, new URL(url));
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param execution
     * @param index
     *            the running index of this url
     * @param url
     *            link to mdr
     */
    public GuardedMetaDataRecordUrl(Execution<I> execution, int index, URL url) {
        super(url);
        this.execution = execution;
        this.mdr = null;
        this.link = null;
        this.index = index;
        this.directory = null;
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param execution
     * 
     * @param mdr
     *            the meta data record guarded
     * @param link
     *            the current link object
     * @param index
     *            the running index of this url
     * @param url
     *            link to mdr
     */
    public GuardedMetaDataRecordUrl(Execution<I> execution, MetaDataRecord<I> mdr, Link link,
                                    int index, URL url) {
        super(url);
        this.execution = execution;
        this.mdr = mdr;
        this.link = link;
        this.index = index;
        this.directory = null;
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param execution
     * 
     * @param mdr
     *            the meta data record guarded
     * @param link
     *            the current link object
     * @param index
     *            the running index of this url
     * @param url
     *            link to mdr
     * @param directory
     *            the directory to store the downloaded content to.
     */
    public GuardedMetaDataRecordUrl(Execution<I> execution, MetaDataRecord<I> mdr, Link link,
                                    int index, URL url, File directory) {
        super(url);
        this.execution = execution;
        this.mdr = mdr;
        this.link = link;
        this.index = index;
        this.directory = directory;
    }

    /**
     * @return execution
     */
    public Execution<I> getExecution() {
        return execution;
    }

    /**
     * @return the meta data record guarded
     */
    public MetaDataRecord<I> getMetaDataRecord() {
        return mdr;
    }

    /**
     * @return link
     */
    public Link getLink() {
        return link;
    }

    /**
     * @return the running index of this url
     */
    public int getIndex() {
        return index;
    }

    /**
     * @return directory
     */
    public File getDirectory() {
        return directory;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((execution == null) ? 0 : execution.hashCode());
        result = prime * result + ((mdr == null) ? 0 : mdr.hashCode());
        result = prime * result + index;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        GuardedMetaDataRecordUrl<?> other = (GuardedMetaDataRecordUrl<?>)obj;
        if (execution == null) {
            if (other.execution != null) return false;
        } else if (!execution.equals(other.execution)) return false;
        if (mdr == null) {
            if (other.mdr != null) return false;
        } else if (!mdr.equals(other.mdr)) return false;
        if (index != other.index) return false;
        return true;
    }


}
