/*
 * 
 */
package com.finaxys.finaxysplatform.persistence.hbase;


public class RowKeyField  implements Comparable<RowKeyField> {

	private byte[] value;
	private int order;

	public RowKeyField( byte[] value, int order) {
		super();
		this.value = value;
		this.order = order;
	}

	public RowKeyField(int order) {
		super();
		this.order = order;
	}


	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int compareTo(RowKeyField field) {
		if (this.order < field.order)
			return -1;
		if (this.order > field.order)
			return 1;
		return 0;
	}

}
