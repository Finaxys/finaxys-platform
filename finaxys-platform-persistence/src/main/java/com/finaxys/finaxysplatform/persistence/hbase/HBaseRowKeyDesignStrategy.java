/*
 * 
 */
package com.finaxys.finaxysplatform.persistence.hbase;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public abstract class HBaseRowKeyDesignStrategy implements RowKeyDesignStrategy {

	protected List<byte[]> convertFieldsToBytes(TreeSet<RowKeyField> fields) {
		List<byte[]> rowkeyFields = new ArrayList<byte[]>();
		for (RowKeyField field : fields) {
			rowkeyFields.add(field.getValue());
		}
		return rowkeyFields;
	}

	protected int bytesSize(List<byte[]> rowkeyFields) {
		int size = 0;
		for (byte[] field : rowkeyFields)
			size += field.length;
		return size;
	}

}
