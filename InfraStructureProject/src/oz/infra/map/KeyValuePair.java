package oz.infra.map;

import oz.infra.constants.OzConstants;

public class KeyValuePair<K, V> {
	private K key;
	private V value;

	public KeyValuePair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	public V setValue(final V value) {
		V oldValue = this.value;
		this.value = value;
		return oldValue;
	}

	public String toString() {
		return key.toString().concat(OzConstants.EQUAL_SIGN).concat(value.toString());
	}

}
