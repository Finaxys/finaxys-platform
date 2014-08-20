package com.finaxys.finaxysplatform.persistence.hbase;

import java.lang.reflect.Field;

import org.apache.hadoop.hbase.util.Bytes;

public abstract class QueuedObject {

	protected Object obj;

	public QueuedObject(Object obj) {
		super();
		this.obj = obj;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public abstract byte[] getQualifier(Field field);

	public static class QueuedEntity extends QueuedObject {

		public QueuedEntity(Object obj) {
			super(obj);
		}

		public byte[] getQualifier(Field field) {
			return (obj.getClass().getSimpleName() + "~" + field.getName()).getBytes();
		}

	}

	public static class QueuedNestedEntity extends QueuedObject {

		byte[] rowkey;
		String attributeName;

		public QueuedNestedEntity(Object obj, byte[] rowkey, String attributeName) {
			super(obj);
			this.rowkey = rowkey;
			this.attributeName = attributeName;
		}

		public byte[] getQualifier(Field field) {
			byte[] entityName = attributeName.getBytes();
			byte[] fieldName = field.getName().getBytes();
			byte[] qualifier = new byte[2 + entityName.length + rowkey.length + fieldName.length];
			int offset = 0;
			offset = Bytes.putBytes(qualifier, offset, entityName, 0, entityName.length);
			offset = Bytes.putByte(qualifier, offset, (byte) '~');
			offset = Bytes.putBytes(qualifier, offset, rowkey, 0, rowkey.length);
			offset = Bytes.putByte(qualifier, offset, (byte) '~');
			offset = Bytes.putBytes(qualifier, offset, fieldName, 0, fieldName.length);
			return qualifier;
		}
	}

}
