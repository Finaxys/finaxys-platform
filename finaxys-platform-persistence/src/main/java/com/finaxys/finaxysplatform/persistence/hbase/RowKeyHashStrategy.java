/*
 * 
 */
package com.finaxys.finaxysplatform.persistence.hbase;

import java.util.List;
import java.util.TreeSet;

import org.apache.hadoop.hbase.util.Bytes;

public class RowKeyHashStrategy extends RowKeyFixedLengthStrategy {

	@Override
	public byte[] makeRowKey(TreeSet<RowKeyField> keyFields) {
		List<byte[]> rowkeyFields = convertFieldsToBytes(keyFields);
		byte[] rowkey = new byte[bytesSize(rowkeyFields)];
		int offset = 0;
		for (byte[] field : rowkeyFields) {
			if (field == null)
				throw new IllegalArgumentException();
			offset = Bytes.putBytes(rowkey, offset, field, 0, field.length);
		}
		return rowkey;

	}

}