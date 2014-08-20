/*
 * 
 */
package com.finaxys.finaxysplatform.persistence.hbase;

import java.util.Arrays;

public class RowKey {

	private byte[] key;

	public RowKey() {
		super();
	}

	public RowKey(byte[] key) {
		super();
		setKey(key);
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		if (key == null) {
			this.key = new byte[0];
		} else {
			this.key = Arrays.copyOf(key, key.length);
		}
	}

}
