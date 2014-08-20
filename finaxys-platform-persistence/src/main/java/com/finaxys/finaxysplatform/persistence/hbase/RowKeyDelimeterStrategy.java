/*
 * 
 */
package com.finaxys.finaxysplatform.persistence.hbase;

import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.hadoop.hbase.util.Bytes;

public class RowKeyDelimeterStrategy extends RowKeyVariableLengthStrategy {

	private static final char DELIMETER = '~';

	@Override
	public byte[] makeRowKey(TreeSet<RowKeyField> keyFields) {
		List<byte[]> rowkeyFields = convertFieldsToBytes(keyFields);
		byte[] rowkey = new byte[bytesSize(rowkeyFields)];
		int offset = 0;
		Iterator<byte[]> it = rowkeyFields.iterator();
		while (it.hasNext()) {
			byte[] field = it.next();
			offset = Bytes.putBytes(rowkey, offset, field, 0, field.length);
			if (it.hasNext())
				offset = Bytes.putByte(rowkey, offset, (byte) DELIMETER);
		}

		return rowkey;

	}

}
