package eu.europeana.uim.storage.modules.criteria;

import java.io.Serializable;
import java.util.Arrays;

import eu.europeana.uim.common.TKey;

public class KeyCriterium <N, T> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean not = false;

	private TKey<N, T> key; 
	
	private T value; 
	
	private Enum<?>[] qualifiers;
	
	private KeyCriterium(boolean not, TKey<N, T> key, T value, Enum<?> ...qualifiers) {
		super();
		this.not = not;
		this.key = key;
		this.value = value;
		this.qualifiers = qualifiers;
	}
	
	public static <N1, T1> KeyCriterium<N1, T1> buildKeyCriterium(TKey<N1, T1> key, T1 value, Enum<?> ...qualifiers) {
		return new KeyCriterium<N1, T1> (false, key, value, qualifiers);
	}
	
	public static <N1, T1> KeyCriterium<N1, T1> buildNotKeyCriterium(TKey<N1, T1> key, T1 value, Enum<?> ...qualifiers) {
		return new KeyCriterium<N1, T1> (true, key, value, qualifiers);
	}
	
	
	public TKey<N, T> getKey() {
		return key;
	}

	public T getValue() {
		return value;
	}

	public Enum<?>[] getQualifiers() {
		return qualifiers;
	}

	public boolean isNot() {
		return not;
	}

	@Override
	public String toString() {
		return "KeyCriterium [not=" + not + ", key=" + key + ", value=" + value + ", qualifiers="
				+ Arrays.toString(qualifiers) + "]";
	}
}
