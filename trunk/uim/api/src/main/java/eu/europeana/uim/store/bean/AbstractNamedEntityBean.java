package eu.europeana.uim.store.bean;

/**
 * Base class for all mnemonic and name holding data sets.
 * 
 * @param <I>
 *            unique ID
 * 
 * @author Markus Muhr (markus.muhr@kb.nl)
 * @date Mar 22, 2011
 */
public abstract class AbstractNamedEntityBean<I> extends AbstractEntityBean<I> {
    private String mnemonic;
    private String name;

    /**
     * Creates a new instance of this class.
     */
    public AbstractNamedEntityBean() {
        super();
    }

    /**
     * Creates a new instance of this class.
     * 
     * @param id
     *            unique ID
     */
    public AbstractNamedEntityBean(I id) {
        super(id);
    }

    /**
     * @return mnemonic
     */
    public String getMnemonic() {
        return mnemonic;
    }

    /**
     * @param mnemonic
     */
    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getMnemonic() + "\t" + getName();
    }
}
