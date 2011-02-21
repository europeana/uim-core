package eu.europeana.uim.store.memory;


public class AbstractNamedMemoryEntity extends AbstractMemoryEntity {

	private String mnemonic;
	private String name;
	
	public AbstractNamedMemoryEntity(){
	}
	
	public AbstractNamedMemoryEntity(long id) {
		super(id);
	}


	/**
	 * @return the mnemonic
	 */
	public String getMnemonic() {
		return mnemonic;
	}

	/**
	 * @param mnemonic the mnemonic to set
	 */
	public void setMnemonic(String mnemonic) {
		this.mnemonic = mnemonic;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return getMnemonic() + "\t" + getName();
	}
	
}
